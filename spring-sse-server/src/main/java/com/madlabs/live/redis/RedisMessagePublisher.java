package com.madlabs.live.redis;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisMessagePublisher implements MessagePublisher {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(RedisMessagePublisher.class);

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private ChannelTopic topic;

	public RedisMessagePublisher() {
	}

	public RedisMessagePublisher(final RedisTemplate<String, Object> redisTemplate, final ChannelTopic topic) {
		this.redisTemplate = redisTemplate;
		this.topic = topic;
	}

	public void publish(final String message) {
		try {
			redisTemplate.convertAndSend(topic.getTopic(), message);
		} catch (Exception e) {
			logger.error("Error publishing message to Redis topic: {}", topic.getTopic(), e);
		}

	}
}
