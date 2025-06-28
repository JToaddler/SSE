package com.madlabs.servlet.sse.jaxrs;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("messages")
@RequestScoped
public class MessageResource {

	@Inject
	Event<Message> messageEvent;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response send(Message message) {
		messageEvent.fire(message);
		return Response.ok().build();
	}
}