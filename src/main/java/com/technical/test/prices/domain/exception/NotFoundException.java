package com.technical.test.prices.domain.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    private final DomainDefinitionEnum error;

    public NotFoundException(DomainDefinitionEnum error, Object... args) {
        super(error.format(args));
        this.error = error;
    }
}
