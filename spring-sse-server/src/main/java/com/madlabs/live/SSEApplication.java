package com.madlabs.live;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class SSEApplication {

	public static void main(String[] args) {
		SpringApplication.run(SSEApplication.class, args);
	}

}
