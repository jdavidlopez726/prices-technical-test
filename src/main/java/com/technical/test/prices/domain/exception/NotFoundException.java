package com.technical.test.prices.domain.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    private final ErrorDefinitionEnum error;

    public NotFoundException(ErrorDefinitionEnum error, Object... args) {
        super(error.format(args));
        this.error = error;
    }
}
