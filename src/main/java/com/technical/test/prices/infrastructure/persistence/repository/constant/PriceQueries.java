package com.technical.test.prices.infrastructure.persistence.repository.constant;

public final class PriceQueries {

    public static final String FIND_APPLICABLE_PRICE = """
            SELECT * FROM prices
            WHERE brand_id = :brandId
              AND product_id = :productId
              AND :applicationDate BETWEEN start_date AND end_date
            ORDER BY priority DESC
            LIMIT 1
            """;

    private PriceQueries() {
    }
}
