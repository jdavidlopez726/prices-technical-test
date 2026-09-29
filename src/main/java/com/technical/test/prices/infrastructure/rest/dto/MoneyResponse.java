package com.technical.test.prices.infrastructure.rest.dto;

import java.math.BigDecimal;

public record MoneyResponse(
        BigDecimal amount,
        String currency
) {
}
