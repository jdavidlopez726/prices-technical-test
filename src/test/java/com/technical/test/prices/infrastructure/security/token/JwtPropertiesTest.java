package com.technical.test.prices.infrastructure.security.token;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class JwtPropertiesTest {

    @Test
    void givenPositiveExpiration_whenCreated_thenIsAllowed() {
        assertThat(new JwtProperties(Duration.ofHours(1)).expiration()).isEqualTo(Duration.ofHours(1));
    }

    @Test
    void givenMissingExpiration_whenCreated_thenThrowsException() {
        assertThatThrownBy(() -> new JwtProperties(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("app.security.jwt.expiration");
    }

    @Test
    void givenZeroExpiration_whenCreated_thenThrowsException() {
        assertThatThrownBy(() -> new JwtProperties(Duration.ZERO))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenNegativeExpiration_whenCreated_thenThrowsException() {
        Duration negative = Duration.ofMinutes(-5);

        assertThatThrownBy(() -> new JwtProperties(negative))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
