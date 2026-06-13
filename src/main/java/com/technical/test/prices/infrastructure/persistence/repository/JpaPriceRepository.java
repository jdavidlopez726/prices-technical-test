package com.technical.test.prices.infrastructure.persistence.repository;

import com.technical.test.prices.infrastructure.persistence.entity.PriceEntity;
import java.time.LocalDateTime;
import java.util.Optional;

import com.technical.test.prices.infrastructure.persistence.repository.constant.PriceQueries;
import com.technical.test.prices.infrastructure.persistence.repository.constant.PriceQueryParams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaPriceRepository extends JpaRepository<PriceEntity, Long> {

    @Query(value = PriceQueries.FIND_APPLICABLE_PRICE, nativeQuery = true)
    Optional<PriceEntity> findApplicablePrice(@Param(PriceQueryParams.BRAND_ID) Long brandId,
                                              @Param(PriceQueryParams.PRODUCT_ID) Long productId,
                                              @Param(PriceQueryParams.APPLICATION_DATE) LocalDateTime applicationDate);
}
