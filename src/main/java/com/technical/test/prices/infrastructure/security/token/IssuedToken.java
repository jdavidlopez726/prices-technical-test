package com.technical.test.prices.infrastructure.security.token;

import java.time.Duration;

public record IssuedToken(
        String value,
        Duration expiresIn
) {
}
