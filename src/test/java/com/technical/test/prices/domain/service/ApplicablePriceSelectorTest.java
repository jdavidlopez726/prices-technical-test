package com.technical.test.prices.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.technical.test.prices.domain.model.Price;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class ApplicablePriceSelectorTest {

    private static final LocalDateTime DATE = LocalDateTime.parse("2020-06-14T16:00:00");

    private final ApplicablePriceSelector selector = new ApplicablePriceSelector();

    @Test
    void givenOverlappingPrices_whenSelect_thenReturnsHighestPriority() {
        Price basePrice = price(1L, 0, "2020-06-14T00:00:00", "2020-12-31T23:59:59");
        Price promoPrice = price(2L, 1, "2020-06-14T15:00:00", "2020-06-14T18:30:00");

        assertThat(selector.select(List.of(basePrice, promoPrice), DATE)).contains(promoPrice);
    }

    @Test
    void givenHigherPriorityPriceOutOfRange_whenSelect_thenIgnoresIt() {
        Price basePrice = price(1L, 0, "2020-06-14T00:00:00", "2020-12-31T23:59:59");
        Price expiredPromoPrice = price(2L, 1, "2020-06-14T10:00:00", "2020-06-14T11:00:00");

        assertThat(selector.select(List.of(basePrice, expiredPromoPrice), DATE)).contains(basePrice);
    }

    @Test
    void givenNoApplicablePrice_whenSelect_thenReturnsEmpty() {
        Price futurePrice = price(1L, 0, "2020-06-15T00:00:00", "2020-12-31T23:59:59");

        assertThat(selector.select(List.of(futurePrice), DATE)).isEmpty();
    }

    @Test
    void givenNoCandidates_whenSelect_thenReturnsEmpty() {
        assertThat(selector.select(List.of(), DATE)).isEmpty();
    }

    private Price price(Long priceList, int priority, String startDate, String endDate) {
        return new Price(1L, 35455L, priceList, LocalDateTime.parse(startDate), LocalDateTime.parse(endDate),
                priority, BigDecimal.TEN, "EUR");
    }
}
