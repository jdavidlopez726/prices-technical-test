package com.technical.test.prices.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.technical.test.prices.domain.exception.DomainErrorDefinitionEnum;
import com.technical.test.prices.domain.exception.NotFoundException;
import com.technical.test.prices.domain.model.Price;
import com.technical.test.prices.domain.repository.PriceRepositoryPort;
import com.technical.test.prices.domain.service.ApplicablePriceSelector;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PriceServiceImplTest {

    private static final Long BRAND_ID = 1L;
    private static final Long PRODUCT_ID = 35455L;
    private static final LocalDateTime APPLICATION_DATE = LocalDateTime.parse("2020-06-14T16:00:00");

    @Mock
    private PriceRepositoryPort priceRepository;

    private PriceServiceImpl priceService;

    @BeforeEach
    void setup() {
        priceService = new PriceServiceImpl(priceRepository, new ApplicablePriceSelector());
    }

    @Test
    void givenCandidatePrices_whenFindApplicablePrice_thenReturnsSelectedPrice() {
        Price basePrice = price(1L, 0, "2020-06-14T00:00:00", "2020-12-31T23:59:59");
        Price promoPrice = price(2L, 1, "2020-06-14T15:00:00", "2020-06-14T18:30:00");
        when(priceRepository.findCandidatePrices(BRAND_ID, PRODUCT_ID, APPLICATION_DATE))
                .thenReturn(List.of(basePrice, promoPrice));

        Price result = priceService.findApplicablePrice(BRAND_ID, PRODUCT_ID, APPLICATION_DATE);

        assertThat(result).isEqualTo(promoPrice);
        verify(priceRepository).findCandidatePrices(BRAND_ID, PRODUCT_ID, APPLICATION_DATE);
    }

    @Test
    void givenNoCandidatePrices_whenFindApplicablePrice_thenThrowsNotFound() {
        when(priceRepository.findCandidatePrices(BRAND_ID, PRODUCT_ID, APPLICATION_DATE))
                .thenReturn(List.of());

        assertThatThrownBy(() -> priceService.findApplicablePrice(BRAND_ID, PRODUCT_ID, APPLICATION_DATE))
                .isInstanceOf(NotFoundException.class)
                .extracting(ex -> ((NotFoundException) ex).getError())
                .isEqualTo(DomainErrorDefinitionEnum.PRICE_NOT_FOUND);
    }

    @Test
    void givenOnlyNonApplicableCandidates_whenFindApplicablePrice_thenThrowsNotFound() {
        Price futurePrice = price(1L, 0, "2020-06-15T00:00:00", "2020-12-31T23:59:59");
        when(priceRepository.findCandidatePrices(BRAND_ID, PRODUCT_ID, APPLICATION_DATE))
                .thenReturn(List.of(futurePrice));

        assertThatThrownBy(() -> priceService.findApplicablePrice(BRAND_ID, PRODUCT_ID, APPLICATION_DATE))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("No applicable price found for brandId=1, productId=35455, date=2020-06-14T16:00");
    }

    private Price price(Long priceList, int priority, String startDate, String endDate) {
        return new Price(BRAND_ID, PRODUCT_ID, priceList, LocalDateTime.parse(startDate), LocalDateTime.parse(endDate),
                priority, BigDecimal.TEN, "EUR");
    }
}
