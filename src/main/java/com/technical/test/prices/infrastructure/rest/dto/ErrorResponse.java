package com.technical.test.prices.infrastructure.rest.dto;

public record ErrorResponse(String detail, int status, String code) {
}
