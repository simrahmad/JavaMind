package com.simrahapp.chatboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.simrahapp.chatboot")
public class ChatbootApplication {
	public static void main(String[] args) {
		SpringApplication.run(ChatbootApplication.class, args);
	}
}