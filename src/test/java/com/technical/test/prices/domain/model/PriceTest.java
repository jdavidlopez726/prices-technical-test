package com.technical.test.prices.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PriceTest {

    private static final LocalDateTime START = LocalDateTime.parse("2020-06-14T15:00:00");
    private static final LocalDateTime END = LocalDateTime.parse("2020-06-14T18:30:00");

    private final Price price = new Price(1L, 35455L, 2L, START, END, 1, new BigDecimal("25.45"), "EUR");

    @ParameterizedTest
    @CsvSource({
            "2020-06-14T15:00:00, true",
            "2020-06-14T16:00:00, true",
            "2020-06-14T18:30:00, true",
            "2020-06-14T14:59:59, false",
            "2020-06-14T18:30:01, false"
    })
    void givenDate_whenIsApplicableAt_thenChecksInclusiveRange(LocalDateTime date, boolean expected) {
        assertThat(price.isApplicableAt(date)).isEqualTo(expected);
    }
}
