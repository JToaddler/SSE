package com.madlabs.sse.client.wiki;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;

public class WikiMediaChangeProducer {

	private static final Logger log = LoggerFactory.getLogger(WikiMediaChangeProducer.class);

	final static String URL = "https://stream.wikimedia.org/v2/stream/recentchange";

	public static void main(String[] args) throws InterruptedException {

		EventHandler eventHandler = new WikiMediaChangeHandler();
		EventSource.Builder builder = new EventSource.Builder(eventHandler, URI.create(URL));

		EventSource eventSource = builder.build();
		eventSource.start();
		TimeUnit.SECONDS.sleep(10);
		log.info("Main thread execution completed");
	}
}
