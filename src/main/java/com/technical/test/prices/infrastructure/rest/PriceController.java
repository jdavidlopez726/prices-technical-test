package com.technical.test.prices.infrastructure.rest;

import com.technical.test.prices.application.service.PriceServicePort;
import com.technical.test.prices.infrastructure.rest.api.PriceApi;
import com.technical.test.prices.infrastructure.rest.dto.PriceResponse;
import com.technical.test.prices.infrastructure.rest.mapper.PriceResponseMapper;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PriceController implements PriceApi {

    private final PriceServicePort priceService;
    private final PriceResponseMapper priceResponseMapper;

    @Override
    public ResponseEntity<List<PriceResponse>> getApplicablePrices(LocalDateTime date, Long productId, Long brandId) {
        return ResponseEntity.ok(priceResponseMapper.toResponseList(priceService.findApplicablePrices(brandId, productId, date)));
    }
}
