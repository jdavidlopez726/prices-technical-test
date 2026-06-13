package com.technical.test.prices.application.service;

import com.technical.test.prices.application.service.constant.PriceServiceLogs;
import com.technical.test.prices.domain.exception.ErrorDefinitionEnum;
import com.technical.test.prices.domain.exception.NotFoundException;
import com.technical.test.prices.domain.model.Price;
import com.technical.test.prices.domain.repository.PriceRepositoryPort;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceServicePort {

    private final PriceRepositoryPort priceRepository;

    @Override
    public Price findApplicablePrice(Long brandId, Long productId, LocalDateTime applicationDate) {
        log.info(PriceServiceLogs.BASE_LOG, PriceServiceLogs.PRICE_SERVICE_CLASS, PriceServiceLogs.RETRIEVE_PRICE_METHOD,
                PriceServiceLogs.RETRIEVE_PRICE_REQUEST.formatted(brandId, productId, applicationDate));

        Price price = priceRepository.findApplicablePrice(brandId, productId, applicationDate)
                .orElseThrow(() -> new NotFoundException(ErrorDefinitionEnum.PRICE_NOT_FOUND, brandId, productId, applicationDate));

        log.debug(PriceServiceLogs.BASE_LOG, PriceServiceLogs.PRICE_SERVICE_CLASS, PriceServiceLogs.RETRIEVE_PRICE_METHOD,
                PriceServiceLogs.RETRIEVE_PRICE_FOUND_PRICE.formatted(brandId, productId, applicationDate, price));

        log.info(PriceServiceLogs.BASE_LOG, PriceServiceLogs.PRICE_SERVICE_CLASS, PriceServiceLogs.RETRIEVE_PRICE_METHOD,
                PriceServiceLogs.RETRIEVE_PRICE_METHOD_RESPONSE.formatted(brandId, productId, applicationDate));
        return price;
    }
}
