package com.madlabs.servlet.sse.jaxrs;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;

@Path("events")
@RequestScoped
public class SseResource {

	@GET
	@Produces(MediaType.SERVER_SENT_EVENTS)
	public void eventStream(@Context Sse sse, @Context SseEventSink eventSink) {
		// Resource method is invoked when a client subscribes to an event stream.
		// That implies that sending events will most likely happen from different
		// context - thread / event handler / etc, so common implementation of the
		// resource method will store the eventSink instance and the application
		// logic will retrieve it when an event should be emitted to the client.

		// sending events:
		System.out.println("SseResource.eventStream  Registering event sink: " + eventSink);
		eventSink.send(sse.newEvent("event1"));
	}
}