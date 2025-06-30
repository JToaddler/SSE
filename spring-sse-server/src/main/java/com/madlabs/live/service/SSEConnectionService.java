package com.madlabs.live.service;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter.DataWithMediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madlabs.live.aop.SseMetrics;
import com.madlabs.live.model.SseEvent;
import com.madlabs.live.model.SseEventType;
import com.madlabs.live.redis.RedisMessagePublisher;

@Service
public class SSEConnectionService {

	Logger logger = LoggerFactory.getLogger(SSEConnectionService.class);

	@Autowired
	private RedisMessagePublisher redisMessagePublisher;

	private final ConcurrentHashMap<String, SseEmitter> emitters;

	public SSEConnectionService() {

		emitters = new ConcurrentHashMap<>();

	}

	@SseMetrics
	public SseEmitter createSseEmitter() {

		SseEmitter emitter = new SseEmitter(0L);

		String id = UUID.randomUUID().toString();
		emitters.put(id, emitter);
		SseEventBuilder eventBuilder = SseEmitter.event();
		Set<DataWithMediaType> event = eventBuilder.name("uuid").data(id).build();
		try {
			emitter.send(event);
		} catch (IOException e) {
		}

		emitter.onCompletion(() -> {
			logger.info("SSE connection with ID: {} completed", id);
			emitters.values().remove(emitter);
		});

		emitter.onError((Throwable throwable) -> {
			logger.info("SSE connection with ID: {} encountered an error: {}", id, throwable.getMessage());
			emitters.values().remove(emitter);
		});
		logger.info("New SSE connection created  with ID: {}", id);

		return emitter;
	}

	public Long getActiveConnections() {
		return emitters.values().stream().count();
	}

	@SseMetrics
	public boolean disconnect(String id) {
		SseEmitter emitter = emitters.remove(id);
		if (emitter != null) {
			emitter.complete();
			logger.info("Disconnected emitter with ID: {}", id);
			return true;
		} else {
			return false;
		}
	}

	@SseMetrics
	public void disconnectAll() {
		for (String id : emitters.keySet()) {
			disconnect(id);
		}
		logger.info("All SSE connections have been disconnected.");
	}

	public void sendMessage(String id, Map<String, Object> message) {
		SseEmitter emitter = emitters.get(id);
		if (emitter != null) {
			try {
				emitter.send(message, MediaType.APPLICATION_JSON);
			} catch (IOException e) {
				logger.error("Failed to send message to emitter with ID: {}", id, e);
				emitter.completeWithError(e);
				removeStale(id);
			}
		} else {
			logger.warn("No active emitter found with ID: {}", id);
		}
	}

	@SseMetrics
	public void broadcastMessage(Map<String, Object> message) {
		for (Map.Entry<String, SseEmitter> entry : emitters.entrySet()) {
			String id = entry.getKey();
			SseEmitter emitter = entry.getValue();
			try {
				emitter.send(message, MediaType.APPLICATION_JSON);
			} catch (Exception e) {
				logger.error("Failed to send message to emitter with ID: {}", id);
				emitter.completeWithError(e);
				removeStale(id);
			}
		}
		logger.info("Broadcasted message to all active emitters.");
	}

	private void removeStale(String uuid) {
		if (emitters.containsKey(uuid)) {
			emitters.remove(uuid);
		} else {
			logger.warn("No active emitter found with ID: {}", uuid);
		}
	}

	public boolean hasSubscribers() {
		return !emitters.isEmpty();
	}

	public boolean isConnected(String uuid) {
		return emitters.containsKey(uuid);
	}

	public void publishToDisconnect(String uuid) {
		logger.info("publishing message to redis");
		try {
			ObjectMapper mapper = new ObjectMapper();
			SseEvent eventData = new SseEvent(uuid, SseEventType.DISCONNECT, null);
			String data = mapper.writeValueAsString(eventData);
			redisMessagePublisher.publish(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void publishToSend(String id, Map<String, Object> message) {
		logger.info("publishing message to redis");
		try {
			ObjectMapper mapper = new ObjectMapper();
			SseEvent eventData = new SseEvent(id, SseEventType.MESSAGE, message);
			String data = mapper.writeValueAsString(eventData);
			redisMessagePublisher.publish(data);
			logger.info("Message published to Redis for ID: {}", id);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void publishToBroadcast(Map<String, Object> message) {
		logger.info("broadcasting to all subscribers");
		try {
			ObjectMapper mapper = new ObjectMapper();
			SseEvent eventData = new SseEvent(SseEventType.BROADCAST, message);
			String data = mapper.writeValueAsString(eventData);
			redisMessagePublisher.publish(data);
			logger.info("broadcast published to Redis");
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void publishToDisconnectAll() {
		
	}

}
