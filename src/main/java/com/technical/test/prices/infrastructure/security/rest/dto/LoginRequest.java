package com.technical.test.prices.infrastructure.security.rest.dto;

import org.jspecify.annotations.NonNull;

public record LoginRequest(
        String username,
        String password
) {

    /**
     * Masks the password so it never ends up in logs or error messages.
     */
    @Override
    public @NonNull String toString() {
        return "LoginRequest[username=" + username + ", password=****]";
    }
}
