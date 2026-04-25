package com.simrahapp.chatboot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(
								"/login",
								"/login.html",
								"/api/personas",
								"/api/personas/**",
								"/api/quick-replies/**",
								"/index.html",
								"/error",
								"/api/preferences",
								"/api/preferences/**",
								"/api/notifications",
								"/api/notifications/**",
								"/api/feedback"
						).permitAll()

						.anyRequest().authenticated()
				)
				.oauth2Login(oauth2 -> oauth2
						.loginPage("/login")
						.defaultSuccessUrl("/index.html", true)
				)
				.exceptionHandling(ex -> ex
						.authenticationEntryPoint((request, response, authException) -> {
							String path = request.getRequestURI();
							if (path.startsWith("/api/")) {
								response.setStatus(401);
								response.setContentType("application/json");
								response.getWriter().write("{\"error\":\"Not authenticated\"}");
							} else {
								response.sendRedirect("/login");
							}
						})
				)
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/login")
						.permitAll()
				);
		return http.build();
	}

	@Bean
	public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
		return new OidcUserService();
	}
}