package com.madlabs.live.aop;

import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicInteger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.madlabs.live.service.SSEConnectionService;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

@Aspect
@Component
public class SseCounterAspect implements ApplicationListener<ApplicationReadyEvent> {

	Logger logger = LoggerFactory.getLogger(SseCounterAspect.class);

	@Autowired
	MeterRegistry registry;

	private String hostName;

	@Autowired
	private SSEConnectionService sseConnectionService;

	private final AtomicInteger myGauge = new AtomicInteger();

	@After(value = " @annotation(com.madlabs.live.aop.SseMetrics)")
	public void logActiveConnection(JoinPoint joinPoint) {
		logger.info("SSE connection method executed: " + joinPoint.getSignature().getName());
		int activeConnections = sseConnectionService.getActiveConnections().intValue();
		myGauge.set(activeConnections);
	}

	public SseCounterAspect() {
		try {
			this.hostName = InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			logger.error("Error getting host name: {} ", e.getMessage());
		}
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		logger.info("Registering SSE active session gauge with host: {}", hostName);
		Gauge.builder("sse.active_session", myGauge, AtomicInteger::get).description("Totoal active SSE connections")
				.tags("host", hostName).register(registry);
	}

}
