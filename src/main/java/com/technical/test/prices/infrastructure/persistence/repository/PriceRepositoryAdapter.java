package com.technical.test.prices.infrastructure.persistence.repository;

import com.technical.test.prices.domain.model.Price;
import com.technical.test.prices.domain.repository.PriceRepository;
import com.technical.test.prices.infrastructure.persistence.mapper.PriceEntityMapper;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PriceRepositoryAdapter implements PriceRepository {

    private final JpaPriceRepository jpaPriceRepository;
    private final PriceEntityMapper priceEntityMapper;

    @Override
    public Optional<Price> findApplicablePrice(Long brandId, Long productId, LocalDateTime applicationDate) {
        return jpaPriceRepository.findApplicablePrice(brandId, productId, applicationDate)
                .map(priceEntityMapper::toDomain);
    }
}
