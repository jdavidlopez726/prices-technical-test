package com.technical.test.prices.application.service;

import com.technical.test.prices.application.service.constant.FindApplicablePriceServiceLogs;
import com.technical.test.prices.domain.exception.DomainErrorDefinitionEnum;
import com.technical.test.prices.domain.exception.NotFoundException;
import com.technical.test.prices.domain.model.BrandId;
import com.technical.test.prices.domain.model.Price;
import com.technical.test.prices.domain.model.ProductId;
import com.technical.test.prices.domain.repository.PriceRepositoryPort;
import com.technical.test.prices.domain.service.ApplicablePriceSelector;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class FindApplicablePriceService implements FindApplicablePriceUseCase {

    private final PriceRepositoryPort priceRepository;
    private final ApplicablePriceSelector applicablePriceSelector;

    @Override
    public Price findApplicablePrice(BrandId brandId, ProductId productId, LocalDateTime applicationDate) {
        log.info(FindApplicablePriceServiceLogs.BASE_LOG,
                FindApplicablePriceServiceLogs.SERVICE_CLASS,
                FindApplicablePriceServiceLogs.RETRIEVE_PRICE_METHOD,
                FindApplicablePriceServiceLogs.RETRIEVE_PRICE_REQUEST.formatted(brandId.value(), productId.value(), applicationDate));

        //Obtain candidate prices
        List<Price> candidates = priceRepository.findPricesApplicableAt(brandId, productId, applicationDate);

        //Select price from candidate prices
        Price price = applicablePriceSelector.select(candidates)
                .orElseThrow(() -> new NotFoundException(
                        DomainErrorDefinitionEnum.PRICE_NOT_FOUND, brandId.value(), productId.value(), applicationDate));

        log.debug(FindApplicablePriceServiceLogs.BASE_LOG,
                FindApplicablePriceServiceLogs.SERVICE_CLASS,
                FindApplicablePriceServiceLogs.RETRIEVE_PRICE_METHOD,
                FindApplicablePriceServiceLogs.RETRIEVE_PRICE_FOUND.formatted(brandId.value(), productId.value(), applicationDate));

        log.info(FindApplicablePriceServiceLogs.BASE_LOG,
                FindApplicablePriceServiceLogs.SERVICE_CLASS,
                FindApplicablePriceServiceLogs.RETRIEVE_PRICE_METHOD,
                FindApplicablePriceServiceLogs.RETRIEVE_PRICE_RESPONSE.formatted(brandId.value(), productId.value(), applicationDate));
        return price;
    }
}


