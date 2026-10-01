package com.technical.test.prices.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Currency;
import org.junit.jupiter.api.Test;

class MoneyTest {

    private static final Currency EUR = Currency.getInstance("EUR");

    @Test
    void givenValidAmountAndCurrencyCode_whenOf_thenCreatesMoney() {
        Money money = Money.of(new BigDecimal("35.50"), "EUR");

        assertThat(money.amount()).isEqualByComparingTo("35.50");
        assertThat(money.currency()).isEqualTo(EUR);
    }

    @Test
    void givenSameAmountAndCurrency_whenCompared_thenAreEqual() {
        assertThat(Money.of(new BigDecimal("35.50"), "EUR")).isEqualTo(new Money(new BigDecimal("35.50"), EUR));
    }

    @Test
    void givenZeroAmount_whenCreated_thenIsAllowed() {
        assertThat(new Money(BigDecimal.ZERO, EUR).amount()).isZero();
    }

    @Test
    void givenNegativeAmount_whenCreated_thenThrowsException() {
        BigDecimal negativeAmount = new BigDecimal("-0.01");

        assertThatThrownBy(() -> new Money(negativeAmount, EUR))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenNullAmount_whenCreated_thenThrowsException() {
        assertThatThrownBy(() -> new Money(null, EUR))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullCurrency_whenCreated_thenThrowsException() {
        assertThatThrownBy(() -> new Money(BigDecimal.ONE, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenInvalidCurrencyCode_whenOf_thenThrowsException() {
        assertThatThrownBy(() -> Money.of(BigDecimal.ONE, "XXXX"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
