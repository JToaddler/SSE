package com.madlabs.live.service;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter.DataWithMediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import com.madlabs.live.aop.SseMetrics;
import com.madlabs.live.controller.SSEController;

@Service
public class SSEConnectionService {

	Logger logger = LoggerFactory.getLogger(SSEConnectionService.class);

	private final ConcurrentHashMap<String, SseEmitter> emitters;

	public SSEConnectionService() {

		emitters = new ConcurrentHashMap<>();

	}

	@SseMetrics
	public SseEmitter createSseEmitter() {

		SseEmitter emitter = new SseEmitter(0L); // 0L means no timeout

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
		if (emitters.containsKey(id)) {
			SseEmitter emitter = emitters.remove(id);
			if (emitter != null) {
				emitter.complete();
				logger.info("Disconnected emitter with ID: {}", id);
				return true;
			} else {
				return false;
			}
		} else {
			logger.warn("No active emitter found with ID: {}", id);
			return false;
		}
	}

	@SseMetrics
	public void disconnectAll() {
		for (String id : emitters.keySet()) {
			SseEmitter emitter = emitters.remove(id);
			if (emitter != null) {
				emitter.complete();
				logger.info("Disconnected emitter with ID: {}", id);
			}
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
				remove(id);
			}
		} else {
			logger.warn("No active emitter found with ID: {}", id);
		}
	}

	@SseMetrics
	public void broadcastMessage(Map<String, Object> message) {
		if (emitters.size() == 0) {
			logger.warn("No active emitters to broadcast the message.");
			return;
		} else {
			for (Map.Entry<String, SseEmitter> entry : emitters.entrySet()) {
				String id = entry.getKey();
				SseEmitter emitter = entry.getValue();
				try {
					emitter.send(message, MediaType.APPLICATION_JSON);
				} catch (IOException e) {
					logger.error("Failed to send message to emitter with ID: {}", id);
					emitter.completeWithError(e);
					remove(id);
				} catch (Exception e) {
					logger.error("Error while broadcasting message to emitter with ID: {}", id);
					emitter.completeWithError(e);
					remove(id);
				}
			}
			logger.info("Broadcasted message to all active emitters.");
		}
	}

	private void remove(String uuid) {
		if (emitters.containsKey(uuid)) {
			emitters.remove(uuid);
		} else {
			logger.warn("No active emitter found with ID: {}", uuid);
		}
	}

}
