package com.technical.test.prices.infrastructure.persistence.repository;

import com.technical.test.prices.domain.model.Price;
import com.technical.test.prices.domain.repository.PriceRepositoryPort;
import com.technical.test.prices.infrastructure.persistence.mapper.PriceEntityMapper;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PriceRepositoryAdapter implements PriceRepositoryPort {

    private final JpaPriceRepository jpaPriceRepository;
    private final PriceEntityMapper priceEntityMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Price> findCandidatePrices(Long brandId, Long productId, LocalDateTime applicationDate) {
        return priceEntityMapper.toDomain(jpaPriceRepository.findCandidatePrices(brandId, productId, applicationDate));
    }
}
