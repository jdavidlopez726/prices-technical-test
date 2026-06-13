package com.technical.test.prices.infrastructure.persistence.repository;

import com.technical.test.prices.domain.model.Price;
import com.technical.test.prices.domain.repository.PriceRepositoryPort;
import com.technical.test.prices.infrastructure.persistence.mapper.PriceEntityMapper;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PriceRepositoryAdapter implements PriceRepositoryPort {

    private final JpaPriceRepository jpaPriceRepository;
    private final PriceEntityMapper priceEntityMapper;

    @Override
    public List<Price> findApplicablePrices(Long brandId, Long productId, LocalDateTime applicationDate) {
        return jpaPriceRepository.findApplicablePrices(brandId, productId, applicationDate)
                .stream()
                .map(priceEntityMapper::toDomain)
                .toList();
    }
}
