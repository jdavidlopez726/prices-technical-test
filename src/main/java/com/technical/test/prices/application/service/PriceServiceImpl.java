package com.technical.test.prices.application.service;

import com.technical.test.prices.application.service.constant.PriceServiceLogs;
import com.technical.test.prices.domain.exception.ErrorDefinitionEnum;
import com.technical.test.prices.domain.exception.NotFoundException;
import com.technical.test.prices.domain.model.Price;
import com.technical.test.prices.domain.repository.PriceRepositoryPort;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceServicePort {

    private final PriceRepositoryPort priceRepository;

    @Override
    public List<Price> findApplicablePrices(Long brandId, Long productId, LocalDateTime applicationDate) {
        log.info(PriceServiceLogs.BASE_LOG, PriceServiceLogs.PRICE_SERVICE_CLASS, PriceServiceLogs.LIST_PRICES_METHOD,
                PriceServiceLogs.LIST_PRICES_REQUEST.formatted(brandId, productId, applicationDate));

        List<Price> prices = priceRepository.findApplicablePrices(brandId, productId, applicationDate);
        if (prices.isEmpty()) {
            throw new NotFoundException(ErrorDefinitionEnum.PRICE_NOT_FOUND, brandId, productId, applicationDate);
        }

        log.debug(PriceServiceLogs.BASE_LOG, PriceServiceLogs.PRICE_SERVICE_CLASS, PriceServiceLogs.LIST_PRICES_METHOD,
                PriceServiceLogs.LIST_PRICES_FOUND.formatted(prices.size(), brandId, productId, applicationDate));

        log.info(PriceServiceLogs.BASE_LOG, PriceServiceLogs.PRICE_SERVICE_CLASS, PriceServiceLogs.LIST_PRICES_METHOD,
                PriceServiceLogs.LIST_PRICES_RESPONSE.formatted(brandId, productId, applicationDate));
        return prices;
    }
}
