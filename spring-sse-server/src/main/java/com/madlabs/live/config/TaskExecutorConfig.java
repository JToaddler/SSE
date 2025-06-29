package com.madlabs.live.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class TaskExecutorConfig {

	@Bean(name = "threadPoolTaskExecutor")
	public TaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5); // Minimum number of threads in the pool
		executor.setMaxPoolSize(10); // Maximum number of threads in the pool
		executor.setQueueCapacity(25); // Queue capacity for pending tasks
		executor.setThreadNamePrefix("SSE-"); // Prefix for thread names
		executor.setWaitForTasksToCompleteOnShutdown(true); // Ensures tasks complete on shutdown
		executor.setAwaitTerminationSeconds(60); // Timeout for waiting for tasks to complete
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // Handle rejected tasks
		executor.initialize(); // Initializes the thread pool
		return executor;
	}

}
