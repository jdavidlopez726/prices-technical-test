package com.technical.test.prices.infrastructure.persistence.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.technical.test.prices.domain.model.BrandId;
import com.technical.test.prices.domain.model.Money;
import com.technical.test.prices.domain.model.Price;
import com.technical.test.prices.domain.model.ProductId;
import com.technical.test.prices.infrastructure.persistence.entity.BrandEntity;
import com.technical.test.prices.infrastructure.persistence.entity.PriceEntity;
import com.technical.test.prices.infrastructure.persistence.entity.ProductEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class PriceEntityMapperTest {

    private static final LocalDateTime START = LocalDateTime.parse("2020-06-14T15:00:00");
    private static final LocalDateTime END = LocalDateTime.parse("2020-06-14T18:30:00");

    private final PriceEntityMapper mapper = Mappers.getMapper(PriceEntityMapper.class);

    @Test
    void givenEntity_whenToDomain_thenMapsEveryField() {
        Price result = mapper.toDomain(priceEntity(2L, "25.45"));

        assertThat(result).isEqualTo(new Price(new BrandId(1L), new ProductId(35455L), 2L, START, END, 1,
                Money.of(new BigDecimal("25.45"), "EUR")));
    }

    @Test
    void givenEntities_whenToDomain_thenMapsEachOneInOrder() {
        List<Price> result = mapper.toDomain(List.of(priceEntity(1L, "35.50"), priceEntity(2L, "25.45")));

        assertThat(result).extracting(Price::priceList).containsExactly(1L, 2L);
    }

    @Test
    void givenNullEntity_whenToDomain_thenReturnsNull() {
        assertThat(mapper.toDomain((PriceEntity) null)).isNull();
        assertThat(mapper.toDomain((List<PriceEntity>) null)).isNull();
    }

    private static PriceEntity priceEntity(Long priceList, String amount) {
        BrandEntity brand = new BrandEntity();
        brand.setBrandId(1L);
        ProductEntity product = new ProductEntity();
        product.setProductId(35455L);

        PriceEntity entity = new PriceEntity();
        entity.setBrand(brand);
        entity.setProduct(product);
        entity.setPriceList(priceList);
        entity.setStartDate(START);
        entity.setEndDate(END);
        entity.setPriority(1);
        entity.setPrice(new BigDecimal(amount));
        entity.setCurr("EUR");
        return entity;
    }
}
