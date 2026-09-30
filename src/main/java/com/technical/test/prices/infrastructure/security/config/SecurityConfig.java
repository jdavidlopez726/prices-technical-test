package com.technical.test.prices.infrastructure.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * HTTP security rules. Every request goes through this filter chain before reaching any controller.
 * <p>
 * The API is stateless: clients authenticate once against {@code /auth/login} and send the returned JWT on every
 * request, so there is no HTTP session and CSRF protection is not needed.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String LOGIN_PATH = "/auth/login";
    private static final String[] API_DOCS_PATHS = {"/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**"};
    private static final String ERROR_PATH = "/error";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, LOGIN_PATH).permitAll()
                        .requestMatchers(API_DOCS_PATHS).permitAll()
                        .requestMatchers(ERROR_PATH).permitAll()
                        .anyRequest().authenticated())
                .build();
    }
}
