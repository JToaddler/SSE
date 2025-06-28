package com.madlabs.servlet.sse.jaxrs;

import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;

public class SseRequest {
	private Sse sse;
	private SseEventSink eventSink;

	public SseRequest(Sse sse, SseEventSink eventSink) {
		this.sse = sse;
		this.eventSink = eventSink;
	}

	public Sse getSse() {
		return sse;
	}

	public void setSse(Sse sse) {
		this.sse = sse;
	}

	public SseEventSink getEventSink() {
		return eventSink;
	}

	public void setEventSink(SseEventSink eventSink) {
		this.eventSink = eventSink;
	}

}
