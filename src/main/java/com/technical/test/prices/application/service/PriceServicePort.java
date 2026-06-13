package com.technical.test.prices.application.service;

import com.technical.test.prices.domain.model.Price;
import java.time.LocalDateTime;
import java.util.List;

public interface PriceServicePort {

    List<Price> findApplicablePrices(Long brandId, Long productId, LocalDateTime applicationDate);
}
