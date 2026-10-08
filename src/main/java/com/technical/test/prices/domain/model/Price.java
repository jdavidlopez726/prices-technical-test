package com.technical.test.prices.domain.model;

import java.time.LocalDateTime;

public record Price(
        BrandId brandId,
        ProductId productId,
        Long priceList,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Integer priority,
        Money price
) {
}
