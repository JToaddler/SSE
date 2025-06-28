package com.madlabs.servlet.sse.jaxrs;

import java.util.UUID;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.Produces;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;

@Path("cdievents")
@RequestScoped
public class SseCdiResource {

	@Inject
	MessageHandler handler;

	@GET
	@Produces(MediaType.SERVER_SENT_EVENTS)
	public void eventStreamCdi(@Context Sse sse, @Context SseEventSink eventSink) {
		handler.register(UUID.randomUUID().toString(), new SseRequest(sse, eventSink));
	}

	@DELETE
	@Path("{uuid}")
	public void deregister(@PathParam("uuid") String uuid) {
		handler.deregister(uuid);
	}

}