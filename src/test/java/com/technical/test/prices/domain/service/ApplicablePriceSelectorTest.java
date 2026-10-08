package com.technical.test.prices.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.technical.test.prices.domain.model.BrandId;
import com.technical.test.prices.domain.model.Money;
import com.technical.test.prices.domain.model.Price;
import com.technical.test.prices.domain.model.ProductId;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class ApplicablePriceSelectorTest {

    private final ApplicablePriceSelector selector = new ApplicablePriceSelector();

    @Test
    void givenOverlappingPrices_whenSelect_thenReturnsHighestPriority() {
        Price basePrice = price(1L, 0, "2020-06-14T00:00:00", "2020-12-31T23:59:59");
        Price promoPrice = price(2L, 1, "2020-06-14T15:00:00", "2020-06-14T18:30:00");

        assertThat(selector.select(List.of(basePrice, promoPrice))).contains(promoPrice);
    }

    @Test
    void givenNoCandidates_whenSelect_thenReturnsEmpty() {
        assertThat(selector.select(List.of())).isEmpty();
    }

    private Price price(Long priceList, int priority, String startDate, String endDate) {
        return new Price(new BrandId(1L), new ProductId(35455L), priceList,
                LocalDateTime.parse(startDate), LocalDateTime.parse(endDate), priority, Money.of(BigDecimal.TEN, "EUR"));
    }
}
