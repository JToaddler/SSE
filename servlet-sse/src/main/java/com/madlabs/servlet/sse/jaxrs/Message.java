package com.madlabs.servlet.sse.jaxrs;

import java.io.Serializable;

public class Message implements Serializable {

	private String body;

	public Message() {
	}

	public Message(String body) {
		this.body = body;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "Message [body=" + body + "]";
	}

}