package com.technical.test.prices.infrastructure.rest.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RestErrorDefinitionEnum {

    TYPE_MISMATCH("VALIDATION-001", "Parameter '%s' must be of type '%s', got '%s'"),
    MISSING_PARAMETER("VALIDATION-002", "Required parameter '%s' of type '%s' is missing"),
    MALFORMED_REQUEST_BODY("VALIDATION-003", "Request body is missing or malformed"),
    INVALID_CREDENTIALS("AUTH-001", "Invalid username or password"),
    INVALID_TOKEN("AUTH-002", "Access token is missing, invalid or expired"),
    ACCESS_DENIED("AUTH-003", "You do not have permission to access this resource"),
    INTERNAL_ERROR("INTERNAL-001", "An unexpected error occurred");

    private final String code;
    private final String messageTemplate;

    public String format(Object... args) {
        if (args == null || args.length == 0) {
            return messageTemplate;
        }
        return messageTemplate.formatted(args);
    }
}
