package com.technical.test.prices.infrastructure.rest.api;

import com.technical.test.prices.infrastructure.rest.dto.ErrorResponse;
import com.technical.test.prices.infrastructure.rest.dto.PriceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Prices", description = "Price management")
@RequestMapping("/prices")
public interface PriceApi {

    @Operation(summary = "Get applicable price", description = "Returns the applicable price for a product and brand at a given date")
    @ApiResponse(responseCode = "200", description = "Price found",
            content = @Content(schema = @Schema(implementation = PriceResponse.class)))
    @ApiResponse(responseCode = "404", description = "No applicable price found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping
    ResponseEntity<PriceResponse> getApplicablePrice(
            @Parameter(description = "Application date", example = "2020-06-14T10:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @Parameter(description = "Product identifier", example = "35455")
            @RequestParam Long productId,
            @Parameter(description = "Brand identifier", example = "1")
            @RequestParam Long brandId
    );
}
