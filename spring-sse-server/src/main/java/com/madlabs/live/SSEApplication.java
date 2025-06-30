package com.madlabs.live;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableAspectJAutoProxy
@EnableRedisRepositories(basePackages = "com.madlabs.live.repo")
public class SSEApplication {

	public static void main(String[] args) {
		SpringApplication.run(SSEApplication.class, args);
	}

}
