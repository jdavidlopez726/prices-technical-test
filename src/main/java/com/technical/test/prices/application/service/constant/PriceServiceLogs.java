package com.technical.test.prices.application.service.constant;

public final class PriceServiceLogs {

    public static final String BASE_LOG = "[{}][{}] {}";

    public static final String PRICE_SERVICE_CLASS = "PriceServiceImpl";
    public static final String RETRIEVE_PRICE_METHOD = "findApplicablePrice";

    public static final String RETRIEVE_PRICE_REQUEST =
            "Received retrieve price request for brandId=%s, productId=%s, date=%s";
    public static final String RETRIEVE_PRICE_FOUND =
            "Found applicable price for brandId=%s, productId=%s, date=%s";
    public static final String RETRIEVE_PRICE_RESPONSE =
            "Returning price response for brandId=%s, productId=%s, date=%s";

    private PriceServiceLogs() {}
}
