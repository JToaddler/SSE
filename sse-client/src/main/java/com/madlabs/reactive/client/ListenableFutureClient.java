package com.madlabs.reactive.client;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.Executors;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.glassfish.jersey.client.rx.guava.RxListenableFutureInvoker;
import org.glassfish.jersey.client.rx.guava.RxListenableFutureInvokerProvider;

public class ListenableFutureClient {

	public final static void main(String[] args) throws Exception {
		Client client = ClientBuilder.newClient();

		client.register(RxListenableFutureInvokerProvider.class);
		WebTarget target = client.target("http://localhost:8080/servlet-sse/sse");

		ListenableFuture<String> future = target.request().rx(RxListenableFutureInvoker.class).get(String.class);

		FutureCallback<String> callback = new FutureCallback<String>() {
			@Override
			public void onSuccess(String result) {
				System.out.println("result :" + result);
			}

			@Override
			public void onFailure(Throwable t) {
				System.out.println("error :" + t.getMessage());
			}
		};

		Futures.addCallback(future, callback, Executors.newFixedThreadPool(10));

		System.out.println("ListenableFuture:" + future.get());

	}
}