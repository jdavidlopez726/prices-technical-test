package com.technical.test.prices.application.service;

import com.technical.test.prices.domain.model.Price;
import java.time.LocalDateTime;

public interface PriceServicePort {

    Price findApplicablePrice(Long brandId, Long productId, LocalDateTime applicationDate);
}
