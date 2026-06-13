package com.technical.test.prices.application.service.constant;

public final class PriceServiceLogs {

    public static final String BASE_LOG = "[{}][{}] {}";

    public static final String PRICE_SERVICE_CLASS = "PriceServiceImpl";
    public static final String LIST_PRICES_METHOD = "findApplicablePrices";

    public static final String LIST_PRICES_REQUEST =
            "Received list prices request for brandId=%s, productId=%s, date=%s";
    public static final String LIST_PRICES_FOUND =
            "Found %d prices for brandId=%s, productId=%s, date=%s";
    public static final String LIST_PRICES_RESPONSE =
            "Returning list prices response for brandId=%s, productId=%s, date=%s";

    private PriceServiceLogs() {}
}
