package com.madlabs.live.redis;

public interface MessagePublisher {
	void publish(final String message);
}
