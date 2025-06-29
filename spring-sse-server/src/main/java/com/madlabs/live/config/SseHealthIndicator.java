package com.madlabs.live.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.madlabs.live.service.SSEConnectionService;

@Component("sse")
@Configuration(value = "sse")
public class SseHealthIndicator implements HealthIndicator {

	@Autowired
	private SSEConnectionService sseService;

	@Override
	public Health health() {
		long activeConnections = sseService.getActiveConnections();
		return Health.up().withDetail("activeConnections", activeConnections).withDetail("status", "Healthy").build();
	}
}