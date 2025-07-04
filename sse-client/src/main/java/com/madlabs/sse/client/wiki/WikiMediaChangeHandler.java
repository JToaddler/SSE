package com.madlabs.sse.client.wiki;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;

public class WikiMediaChangeHandler implements EventHandler {

	private static final Logger log = LoggerFactory.getLogger(WikiMediaChangeHandler.class);

	public WikiMediaChangeHandler() {
	}

	@Override
	public void onOpen() throws Exception {

	}

	@Override
	public void onClosed() throws Exception {
	}

	@Override
	public void onMessage(String event, MessageEvent messageEvent) throws Exception {

		System.out.println("Data received :" + messageEvent.getData());
	}

	@Override
	public void onComment(String comment) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(Throwable t) {
		log.error("Error in the stream :" + t);

	}

}
