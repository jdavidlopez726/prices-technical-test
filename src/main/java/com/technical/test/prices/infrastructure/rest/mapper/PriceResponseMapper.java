package com.technical.test.prices.infrastructure.rest.mapper;

import com.technical.test.prices.domain.model.Price;
import com.technical.test.prices.infrastructure.rest.dto.PriceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PriceResponseMapper {

    @Mapping(source = "brandId.value", target = "brandId")
    @Mapping(source = "productId.value", target = "productId")
    PriceResponse toResponse(Price price);
}
