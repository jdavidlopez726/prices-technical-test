package com.technical.test.prices.infrastructure.security.rest;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * Authentication errors are raised in the filter chain, before any controller, so {@code GlobalExceptionHandler}
 * never sees them. This handler forwards them to Spring MVC's exception resolver, so they are rendered by
 * {@code GlobalExceptionHandler}, in the same format as every other error of the API.
 */
@Component
public class SecurityErrorHandler implements AuthenticationEntryPoint {

    private final AuthenticationEntryPoint bearerTokenEntryPoint = new BearerTokenAuthenticationEntryPoint();
    private final HandlerExceptionResolver handlerExceptionResolver;

    public SecurityErrorHandler(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    /**
     * 401: the request has no token, or the token is not valid or has expired.
     */
    @Override
    public void commence(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull AuthenticationException ex)
            throws IOException, ServletException {
        bearerTokenEntryPoint.commence(request, response, ex);
        handlerExceptionResolver.resolveException(request, response, null, ex);
    }
}
