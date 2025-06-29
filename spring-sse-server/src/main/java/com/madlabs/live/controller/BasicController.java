package com.madlabs.live.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
@RequestMapping("/api/sse")
@CrossOrigin(origins = "*")
public class BasicController {

	@Autowired
	TaskExecutor taskExecutor;

	@GetMapping(value = "/basicStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter sseEmitter() {
		SseEmitter emitter = new SseEmitter(0l);
		Runnable runner = () -> {
			try {
				for (int i = 0; i < 10; i++) {
					Thread.sleep(1000); // Simulate delay
					emitter.send("Message " + (i + 1));
				}
				emitter.complete();
			} catch (Exception e) {
				emitter.completeWithError(e);
			}
		};
		taskExecutor.execute(runner);
		return emitter;

	}

}
