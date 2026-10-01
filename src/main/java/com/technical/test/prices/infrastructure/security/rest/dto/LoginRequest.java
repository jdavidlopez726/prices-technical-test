package com.technical.test.prices.infrastructure.security.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jspecify.annotations.NonNull;

public record LoginRequest(
        @Schema(example = "user") String username,
        @Schema(example = "user") String password
) {

    /**
     * Masks the password so it never ends up in logs or error messages.
     */
    @Override
    public @NonNull String toString() {
        return "LoginRequest[username=" + username + ", password=****]";
    }
}
