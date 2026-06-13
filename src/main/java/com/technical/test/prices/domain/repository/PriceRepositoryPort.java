package com.technical.test.prices.domain.repository;

import com.technical.test.prices.domain.model.Price;
import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepositoryPort {

    List<Price> findApplicablePrices(Long brandId, Long productId, LocalDateTime applicationDate);
}
