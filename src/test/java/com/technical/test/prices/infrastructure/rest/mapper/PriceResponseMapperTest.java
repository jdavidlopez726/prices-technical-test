package com.technical.test.prices.infrastructure.rest.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.technical.test.prices.domain.model.BrandId;
import com.technical.test.prices.domain.model.Money;
import com.technical.test.prices.domain.model.Price;
import com.technical.test.prices.domain.model.ProductId;
import com.technical.test.prices.infrastructure.rest.dto.MoneyResponse;
import com.technical.test.prices.infrastructure.rest.dto.PriceResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class PriceResponseMapperTest {

    private static final LocalDateTime START = LocalDateTime.parse("2020-06-14T15:00:00");
    private static final LocalDateTime END = LocalDateTime.parse("2020-06-14T18:30:00");

    private final PriceResponseMapper mapper = Mappers.getMapper(PriceResponseMapper.class);

    @Test
    void givenPrice_whenToResponse_thenMapsEveryField() {
        Price price = new Price(new BrandId(1L), new ProductId(35455L), 2L, START, END, 1,
                Money.of(new BigDecimal("25.45"), "EUR"));

        PriceResponse result = mapper.toResponse(price);

        assertThat(result).isEqualTo(new PriceResponse(1L, 35455L, 2L, START, END,
                new MoneyResponse(new BigDecimal("25.45"), "EUR")));
    }

    @Test
    void givenNullPrice_whenToResponse_thenReturnsNull() {
        assertThat(mapper.toResponse(null)).isNull();
    }
}
