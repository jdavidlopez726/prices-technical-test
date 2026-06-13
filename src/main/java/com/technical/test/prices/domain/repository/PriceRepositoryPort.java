package com.technical.test.prices.domain.repository;

import com.technical.test.prices.domain.model.Price;
import java.time.LocalDateTime;
import java.util.Optional;

public interface PriceRepositoryPort {

    Optional<Price> findApplicablePrice(Long brandId, Long productId, LocalDateTime applicationDate);
}
