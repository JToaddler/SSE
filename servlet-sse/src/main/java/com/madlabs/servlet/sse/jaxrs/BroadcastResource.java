package com.madlabs.servlet.sse.jaxrs;

import java.util.UUID;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.enterprise.event.Observes;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseBroadcaster;
import jakarta.ws.rs.sse.SseEventSink;

@Path("broadcastEvents")
@Singleton
public class BroadcastResource {

	@Context
	private Sse sse;

	private volatile SseBroadcaster sseBroadcaster;

	@PostConstruct
	public void init() {
		System.out.println("BroadcastResource.init  Initializing sseBroadcaster !!! ");
		this.sseBroadcaster = sse.newBroadcaster();
	}

	@GET
	// @Path("register")
	@Produces(MediaType.SERVER_SENT_EVENTS)
	public void register(@Context SseEventSink eventSink) {
		System.out.println("BroadcastResource.register  Registering event sink: " + eventSink);
		eventSink.send(sse.newEvent("welcome!"));
		sseBroadcaster.register(eventSink);
	}

	@POST
	// @Path("broadcast")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void broadcast(@FormParam("event") String event) {
		System.out.println("BroadcastResource.broadcast  Broadcasting event: " + event);
		sseBroadcaster.broadcast(sse.newEvent(event));
	}

	public void eventStreamCdi(@Observes Message msg) {
		if (sseBroadcaster == null) {
			System.out.println(" sseBroadcaster is null, cannot broadcast message: " + msg);
			return;
		}
		System.out.println("BroadcastResource.eventStreamCdi  Broadcasting message: " + msg);
		sseBroadcaster.broadcast(sse.newEventBuilder().mediaType(MediaType.APPLICATION_JSON_TYPE)
				.id(UUID.randomUUID().toString()).name("message from cdi").data(msg).build());
	}
}