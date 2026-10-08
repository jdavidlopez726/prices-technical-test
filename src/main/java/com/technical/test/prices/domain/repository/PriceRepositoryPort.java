package com.technical.test.prices.domain.repository;

import com.technical.test.prices.domain.model.BrandId;
import com.technical.test.prices.domain.model.Price;
import com.technical.test.prices.domain.model.ProductId;
import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepositoryPort {

    List<Price> findPricesApplicableAt(BrandId brandId, ProductId productId, LocalDateTime applicationDate);
}
