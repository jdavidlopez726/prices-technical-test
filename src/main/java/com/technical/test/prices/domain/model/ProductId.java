package com.technical.test.prices.domain.model;

import java.util.Objects;

public record ProductId(Long value) {

    public ProductId {
        Objects.requireNonNull(value, "productId must not be null");
    }
}
