package com.madlabs.live.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import com.madlabs.live.service.SSEConnectionService;

@Component
public class RedisMessageSubscriber implements MessageListener {

	Logger logger = LoggerFactory.getLogger(RedisMessageSubscriber.class);

	private SSEConnectionService sseConnectionService;

	public void onMessage(final Message message, final byte[] pattern) {

		String uuid = new String(message.getBody());		
		logger.info("Message received to disconnect sse connection with uuid {}  ", uuid);
	}

	public RedisMessageSubscriber(SSEConnectionService sseConnectionService) {
		this.sseConnectionService = sseConnectionService;
	}

}