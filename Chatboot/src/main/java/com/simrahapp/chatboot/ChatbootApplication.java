package com.simrahapp.chatboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication
public class ChatbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatbootApplication.class, args);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth
						.anyRequest().permitAll()
				)
				.oauth2Login(oauth -> oauth
						.defaultSuccessUrl("/", true)
						.failureUrl("/")
						.loginPage("/")
				);
		return http.build();
	}
}