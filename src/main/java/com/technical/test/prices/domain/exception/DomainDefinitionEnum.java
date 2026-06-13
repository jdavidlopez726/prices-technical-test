package com.technical.test.prices.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DomainDefinitionEnum {

    PRICE_NOT_FOUND("PRICE-001", "No applicable price found for brandId=%d, productId=%d, date=%s");

    private final String code;
    private final String messageTemplate;

    public String format(Object... args) {
        if (args == null || args.length == 0) {
            return messageTemplate;
        }
        return messageTemplate.formatted(args);
    }
}
