package com.technical.test.prices.infrastructure.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceResponse(
        Long brandId,
        Long productId,
        Long priceList,
        LocalDateTime startDate,
        LocalDateTime endDate,
        BigDecimal price,
        String curr
) {
}
