package com.technical.test.prices.domain.model;

import java.util.Objects;

public record BrandId(Long value) {

    public BrandId {
        Objects.requireNonNull(value, "brandId must not be null");
    }
}
