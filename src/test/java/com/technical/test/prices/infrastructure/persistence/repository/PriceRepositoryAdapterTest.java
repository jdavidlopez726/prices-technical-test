package com.technical.test.prices.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.technical.test.prices.domain.model.BrandId;
import com.technical.test.prices.domain.model.Money;
import com.technical.test.prices.domain.model.Price;
import com.technical.test.prices.domain.model.ProductId;
import com.technical.test.prices.infrastructure.persistence.mapper.PriceEntityMapperImpl;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

/**
 * Runs the adapter against the real query, on the sample data that Liquibase loads into H2.
 */
@DataJpaTest
@Import({PriceRepositoryAdapter.class, PriceEntityMapperImpl.class})
class PriceRepositoryAdapterTest {

    private static final BrandId BRAND_ID = new BrandId(1L);
    private static final ProductId PRODUCT_ID = new ProductId(35455L);

    @Autowired
    private PriceRepositoryAdapter priceRepositoryAdapter;

    @Test
    void givenDateWithOverlappingPrices_whenFindPricesApplicableAt_thenReturnsEveryPriceInForce() {
        List<Price> result = priceRepositoryAdapter.findPricesApplicableAt(BRAND_ID, PRODUCT_ID,
                LocalDateTime.parse("2020-06-14T16:00:00"));

        assertThat(result).extracting(Price::priceList).containsExactlyInAnyOrder(1L, 2L);
        assertThat(result).filteredOn(price -> price.priceList() == 2L).singleElement()
                .isEqualTo(new Price(BRAND_ID, PRODUCT_ID, 2L,
                        LocalDateTime.parse("2020-06-14T15:00:00"), LocalDateTime.parse("2020-06-14T18:30:00"), 1,
                        Money.of(new BigDecimal("25.45"), "EUR")));
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', textBlock = """
            2020-06-14T14:59:59 | 1
            2020-06-14T15:00:00 | 1, 2
            2020-06-14T18:30:00 | 1, 2
            2020-06-14T18:30:01 | 1
            """)
    void givenDateOnRangeBoundary_whenFindPricesApplicableAt_thenRangeIsInclusive(LocalDateTime date,
                                                                                  String expectedPriceLists) {
        List<Price> result = priceRepositoryAdapter.findPricesApplicableAt(BRAND_ID, PRODUCT_ID, date);

        assertThat(result).extracting(Price::priceList).containsExactlyInAnyOrderElementsOf(
                Arrays.stream(expectedPriceLists.split(",")).map(String::trim).map(Long::valueOf).toList());
    }

    @Test
    void givenDateWithoutPrices_whenFindPricesApplicableAt_thenReturnsEmpty() {
        assertThat(priceRepositoryAdapter.findPricesApplicableAt(BRAND_ID, PRODUCT_ID,
                LocalDateTime.parse("2000-01-01T00:00:00"))).isEmpty();
    }

    @Test
    void givenOtherBrandOrProduct_whenFindPricesApplicableAt_thenReturnsEmpty() {
        LocalDateTime date = LocalDateTime.parse("2020-06-14T16:00:00");

        assertThat(priceRepositoryAdapter.findPricesApplicableAt(new BrandId(2L), PRODUCT_ID, date)).isEmpty();
        assertThat(priceRepositoryAdapter.findPricesApplicableAt(BRAND_ID, new ProductId(99999L), date)).isEmpty();
    }
}
