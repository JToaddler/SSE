package com.madlabs.reactive.client;

import java.util.concurrent.CompletionStage;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class CompletionStageClient {

	public final static void main(String[] args) throws Exception {

		System.out.println("CompletionStageClient started");

		WebTarget target = ClientBuilder.newClient().target("http://localhost:8080/servlet-sse/sse");

		CompletionStage<Void> future = target.request().rx().get(String.class).thenAccept(t -> System.out.println(t));

		future.toCompletableFuture().get();
		
		System.out.println("CompletionStageClient finished");
	}
}