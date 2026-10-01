package com.technical.test.prices.infrastructure.config;

import com.technical.test.prices.domain.service.ApplicablePriceSelector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers domain services as Spring beans.
 * <p>
 * Domain classes are plain Java and are intentionally not annotated with {@code @Service}/{@code @Component}:
 * in the onion architecture dependencies point inwards, so the domain must not depend on a framework.
 * Wiring them here keeps Spring in the infrastructure layer, which knows about the domain and not the other way round.
 */
@Configuration
public class DomainConfig {

    @Bean
    public ApplicablePriceSelector applicablePriceSelector() {
        return new ApplicablePriceSelector();
    }
}
