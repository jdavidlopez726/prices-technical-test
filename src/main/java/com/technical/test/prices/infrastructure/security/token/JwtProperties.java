package com.technical.test.prices.infrastructure.security.token;

import java.time.Duration;
import java.util.Objects;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @param expiration how long an issued token is valid
 */
@ConfigurationProperties("app.security.jwt")
public record JwtProperties(Duration expiration) {

    /**
     * Fails at startup, instead of on the first login, when the configuration is missing or not valid.
     */
    public JwtProperties {
        Objects.requireNonNull(expiration, "app.security.jwt.expiration must be set");
        if (expiration.isNegative() || expiration.isZero()) {
            throw new IllegalArgumentException("app.security.jwt.expiration must be positive: " + expiration);
        }
    }
}
