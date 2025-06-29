package com.madlabs.live.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.madlabs.live.service.SSEConnectionService;

@Controller
@RequestMapping("/api/sse")
public class SSEController {

	Logger logger = LoggerFactory.getLogger(SSEController.class);

	@Autowired
	SSEConnectionService sseConnectionService;

	@Autowired
	TaskExecutor taskExecutor;

	@GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter sseEmitter() {
		SseEmitter emitter = sseConnectionService.createSseEmitter();
		return emitter;
	}

	@PostMapping(value = "/send/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> sendMessage(@PathVariable String id, @RequestBody Map<String, Object> message) {
		logger.info("Creating SSE emitter with ID: {}", id);
		Runnable task = () -> {
			sseConnectionService.sendMessage(id, message);
		};
		taskExecutor.execute(task);
		return ResponseEntity.ok().build();
	}

	@PostMapping(value = "/broadcast", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> broadCaseMessage(@RequestBody Map<String, Object> message) {
		logger.info("Broadcasting message to all emitters: {}");
		Runnable task = () -> {
			sseConnectionService.broadcastMessage(message);
		};
		taskExecutor.execute(task);
		return ResponseEntity.ok().build();
	}

	@PostMapping(value = "/disconnect/{id}")
	public ResponseEntity<Object> disconnect(@PathVariable String id) {
		Runnable task = () -> {
			sseConnectionService.disconnect(id);
		};
		taskExecutor.execute(task);
		return ResponseEntity.ok().build();
	}

	@PostMapping(value = "/disconnectAll")
	public ResponseEntity<Object> disconnect() {
		logger.info("Disconnecting All emitter ");
		Runnable task = () -> {
			sseConnectionService.disconnectAll();
		};
		taskExecutor.execute(task);
		return ResponseEntity.ok().build();
	}

	@GetMapping(value = "/stats", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> getConnectionStats() {
		long count = sseConnectionService.getActiveConnections();
		return Map.of("activeConnections", count);
	}

}
