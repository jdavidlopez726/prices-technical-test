package com.technical.test.prices.application.service;

import com.technical.test.prices.application.service.constant.PriceServiceLogs;
import com.technical.test.prices.domain.exception.DomainErrorDefinitionEnum;
import com.technical.test.prices.domain.exception.NotFoundException;
import com.technical.test.prices.domain.model.Price;
import com.technical.test.prices.domain.repository.PriceRepositoryPort;
import com.technical.test.prices.domain.service.ApplicablePriceSelector;
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
    private final ApplicablePriceSelector applicablePriceSelector;

    @Override
    public Price findApplicablePrice(Long brandId, Long productId, LocalDateTime applicationDate) {
        log.info(PriceServiceLogs.BASE_LOG, PriceServiceLogs.PRICE_SERVICE_CLASS, PriceServiceLogs.RETRIEVE_PRICE_METHOD,
                PriceServiceLogs.RETRIEVE_PRICE_REQUEST.formatted(brandId, productId, applicationDate));

        //Obtain candidate prices
        List<Price> candidates = priceRepository.findCandidatePrices(brandId, productId, applicationDate);

        //Select price from candidate prices
        Price price = applicablePriceSelector.select(candidates, applicationDate)
                .orElseThrow(() -> new NotFoundException(
                        DomainErrorDefinitionEnum.PRICE_NOT_FOUND, brandId, productId, applicationDate));

        log.debug(PriceServiceLogs.BASE_LOG, PriceServiceLogs.PRICE_SERVICE_CLASS, PriceServiceLogs.RETRIEVE_PRICE_METHOD,
                PriceServiceLogs.RETRIEVE_PRICE_FOUND.formatted(brandId, productId, applicationDate));

        log.info(PriceServiceLogs.BASE_LOG, PriceServiceLogs.PRICE_SERVICE_CLASS, PriceServiceLogs.RETRIEVE_PRICE_METHOD,
                PriceServiceLogs.RETRIEVE_PRICE_RESPONSE.formatted(brandId, productId, applicationDate));
        return price;
    }
}
