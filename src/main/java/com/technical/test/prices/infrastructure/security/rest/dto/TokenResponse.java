package com.technical.test.prices.infrastructure.security.rest.dto;

/**
 * @param accessToken signed JWT to send as {@code Authorization: Bearer <accessToken>}
 * @param tokenType   always {@code Bearer} in this application
 * @param expiresIn   seconds until the token expires
 */
public record TokenResponse(
        String accessToken,
        String tokenType,
        long expiresIn
) {
}
