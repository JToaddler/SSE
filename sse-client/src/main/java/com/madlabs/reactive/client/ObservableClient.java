package com.madlabs.reactive.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.client.rx.rxjava.RxObservableInvoker;
import org.glassfish.jersey.client.rx.rxjava.RxObservableInvokerProvider;

import rx.Observer;

public class ObservableClient {
	public final static void main(String[] args) throws Exception {
		Client client = ClientBuilder.newClient();

		client.register(RxObservableInvokerProvider.class);
		WebTarget target = client.target("http://localhost:8080/servlet-sse/sse");

		target.request().rx(RxObservableInvoker.class).get(String.class).subscribe(new Observer<String>() {
			@Override
			public void onCompleted() {
				System.out.println("onCompleted");
			}

			@Override
			public void onError(Throwable e) {
				System.out.println("onError:" + e.getMessage());
			}

			@Override
			public void onNext(String t) {
				System.out.println("onNext:" + t + " end of message");
			}
		});

	}
}
