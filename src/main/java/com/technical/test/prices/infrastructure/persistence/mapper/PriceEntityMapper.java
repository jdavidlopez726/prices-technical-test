package com.technical.test.prices.infrastructure.persistence.mapper;

import com.technical.test.prices.domain.model.Price;
import com.technical.test.prices.infrastructure.persistence.entity.PriceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PriceEntityMapper {

    @Mapping(source = "brand.brandId", target = "brandId")
    @Mapping(source = "product.productId", target = "productId")
    Price toDomain(PriceEntity entity);
}
