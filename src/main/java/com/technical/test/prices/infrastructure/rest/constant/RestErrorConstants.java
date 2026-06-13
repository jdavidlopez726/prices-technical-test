package com.technical.test.prices.infrastructure.rest.constant;

public final class RestErrorConstants {

    public static final String CODE_TYPE_MISMATCH = "VALIDATION-001";
    public static final String CODE_MISSING_PARAMETER = "VALIDATION-002";

    public static final String MSG_TYPE_MISMATCH =
            "Parameter '%s' must be of type '%s', got '%s'";
    public static final String MSG_MISSING_PARAMETER =
            "Required parameter '%s' of type '%s' is missing";

    private RestErrorConstants() {}
}
