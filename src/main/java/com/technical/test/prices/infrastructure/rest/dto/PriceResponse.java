package com.technical.test.prices.infrastructure.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceResponse(
        Long brandId,
        Long productId,
        Long priceList,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startDate,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endDate,
        BigDecimal price,
        String curr
) {
}
