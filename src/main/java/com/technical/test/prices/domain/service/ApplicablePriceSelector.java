package com.technical.test.prices.domain.service;

import com.technical.test.prices.domain.model.Price;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

public class ApplicablePriceSelector {

    public Optional<Price> select(Collection<Price> candidates, LocalDateTime applicationDate) {
        return candidates.stream()
                .filter(price -> price.isApplicableAt(applicationDate))
                .max(Comparator.comparing(Price::priority));
    }
}
