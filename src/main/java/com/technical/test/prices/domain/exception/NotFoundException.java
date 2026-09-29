package com.technical.test.prices.domain.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    private final DomainErrorDefinitionEnum error;

    public NotFoundException(DomainErrorDefinitionEnum error, Object... args) {
        super(error.format(args));
        this.error = error;
    }
}
