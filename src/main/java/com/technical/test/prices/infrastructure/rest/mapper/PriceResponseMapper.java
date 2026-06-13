package com.technical.test.prices.infrastructure.rest.mapper;

import com.technical.test.prices.domain.model.Price;
import com.technical.test.prices.infrastructure.rest.dto.PriceResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceResponseMapper {

    PriceResponse toResponse(Price price);

    List<PriceResponse> toResponseList(List<Price> prices);
}
