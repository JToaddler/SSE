package com.madlabs.live.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.madlabs.live.redis.MessagePublisher;
import com.madlabs.live.redis.RedisMessagePublisher;
import com.madlabs.live.redis.RedisMessageSubscriber;

@Configuration
public class RedisConfig {

	@Value("${spring.redis.host}")
	String redisHost;

	@Value("${spring.redis.port}")
	Integer redisPort;

	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		RedisStandaloneConfiguration standAloneConfig = new RedisStandaloneConfiguration();
		standAloneConfig.setHostName(redisHost);
		standAloneConfig.setPort(redisPort);
		JedisConnectionFactory jedisConFactory = new JedisConnectionFactory(standAloneConfig);
		return jedisConFactory;
	}

	@DependsOn(value = "jedisConnectionFactory")
	@Bean(name = "redisTemplate")
	public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(new GenericToStringSerializer<Object>(Object.class));
		template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
		template.setConnectionFactory(jedisConnectionFactory);
		return template;
	}

	@Bean("redisConnectionListener")
	MessageListenerAdapter messageListener(RedisMessageSubscriber redisMessageSubscriber) {
		return new MessageListenerAdapter(redisMessageSubscriber);
	}

	@Bean
	RedisMessageListenerContainer redisContainer(JedisConnectionFactory jedisConnectionFactory,
			@Qualifier("redisConnectionTopic") ChannelTopic redisConnectionTopic,
			@Qualifier("redisConnectionListener") MessageListenerAdapter redisConnectionListener) {

		final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(jedisConnectionFactory);
		container.addMessageListener(redisConnectionListener, redisConnectionTopic);

		return container;
	}

	@Bean
	MessagePublisher redisPublisher(RedisTemplate<String, Object> redisTemplate, ChannelTopic redisConnectionTopic) {
		return new RedisMessagePublisher(redisTemplate, redisConnectionTopic);
	}

	@Bean("redisConnectionTopic")
	ChannelTopic redisConnectionTopic() {
		return new ChannelTopic("pubsub:connection");
	}

}
