package com.technical.test.prices.application.service;

import com.technical.test.prices.domain.model.BrandId;
import com.technical.test.prices.domain.model.Price;
import com.technical.test.prices.domain.model.ProductId;
import java.time.LocalDateTime;

public interface FindApplicablePriceUseCase {

    Price findApplicablePrice(BrandId brandId, ProductId productId, LocalDateTime applicationDate);
}
