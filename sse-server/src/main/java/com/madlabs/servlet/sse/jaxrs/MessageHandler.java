package com.madlabs.servlet.sse.jaxrs;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.SseEventSink;

@ApplicationScoped
public class MessageHandler {

	@Inject
	Logger LOG;

	private final Map<String, SseRequest> requests = new ConcurrentHashMap<>();

	public void register(String uuid, SseRequest request) {
		LOG.log(Level.INFO, "register request:{0}", uuid);
		requests.put(uuid, request);
	}

	public void deregister(String uuid) {
		LOG.log(Level.INFO, "deregister request:{0} received ", uuid);
		if (requests.containsKey(uuid)) {
			SseRequest req = requests.remove(uuid);
			try (SseEventSink eventSink = req.getEventSink()) {
				if (eventSink != null && !eventSink.isClosed()) {
					eventSink.close();
					System.out.println("MessageHandler.deregister  Closing event sink: " + eventSink);
				}
			}
		} else {
			LOG.log(Level.WARNING, "deregister request:{0} not found", uuid);
		}
	}

	public void onMessage(@Observes Message msg) {
		System.out.println("MessageHandler.onMessage :" + msg);
		requests.values().stream()
				.filter(req -> !req.getEventSink().isClosed())
		.forEach(
				req -> req.getEventSink().send(req.getSse().newEventBuilder().mediaType(MediaType.APPLICATION_JSON_TYPE)
						.id(UUID.randomUUID().toString()).name("message from cdi").data(msg).build()));

	}

}
