package com.technical.test.prices.infrastructure.config;

import com.technical.test.prices.application.service.FindApplicablePriceService;
import com.technical.test.prices.application.service.FindApplicablePriceUseCase;
import com.technical.test.prices.domain.repository.PriceRepositoryPort;
import com.technical.test.prices.domain.service.ApplicablePriceSelector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers application services as Spring beans.
 * <p>
 * The application layer is plain Java and is intentionally not annotated with
 * {@code @Service}/{@code @Component}, so neither ring of the core depends on a framework.
 * Beans are exposed through their port interface, so adapters depend on the use case contract, not on its implementation.
 */
@Configuration
public class ApplicationConfig {

    @Bean
    public FindApplicablePriceUseCase findApplicablePriceUseCase(PriceRepositoryPort priceRepository,
                                                                 ApplicablePriceSelector applicablePriceSelector) {
        return new FindApplicablePriceService(priceRepository, applicablePriceSelector);
    }
}
