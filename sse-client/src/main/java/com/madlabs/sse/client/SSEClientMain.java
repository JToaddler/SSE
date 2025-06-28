package com.madlabs.sse.client;

import com.cisco.commons.networking.EventHandler;
import com.cisco.commons.networking.SSEClient;

public class SSEClientMain {

	public static void main(String[] args) {
		EventHandler eventHandler = eventText -> {
			process(eventText);
		};
		SSEClient sseClient = SSEClient.builder().url("http://localhost:8080/servlet-sse/rest/broadcastEvents")
				.eventHandler(eventHandler).build();
		sseClient.start();
	}

	public static void process(String data) {
		System.out.println("data received " + data);
	}

}
