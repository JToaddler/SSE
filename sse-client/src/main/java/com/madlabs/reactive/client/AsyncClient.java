package com.madlabs.reactive.client;

import java.util.concurrent.Future;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncClient {

	private static final Logger logger = LoggerFactory.getLogger(AsyncClient.class);

	public final static void main(String[] args) throws Exception {

		WebTarget target = ClientBuilder.newClient().target("http://localhost:8080/servlet-sse/sse");

		Future<String> future = target.request().async().get(String.class);

		System.out.println("ejb resource future:" + future.get());

		target.request().async().get(AsyncClient.responseInvocationCallback());
	}

	private static InvocationCallback<Response> responseInvocationCallback() {
		return new InvocationCallback<Response>() {
			@Override
			public void completed(Response res) {
				System.out.println("Status:" + res.getStatusInfo());
				System.out.println("Entity:" + res.getEntity());
				System.out.println("Request success!");
			}

			@Override
			public void failed(Throwable e) {
				System.out.println("Request failed!");
			}

		};
	}
}
