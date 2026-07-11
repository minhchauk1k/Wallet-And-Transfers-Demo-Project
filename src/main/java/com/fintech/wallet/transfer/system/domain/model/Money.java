package com.fintech.wallet.transfer.system.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fintech.wallet.transfer.system.domain.constant.BusinessError;
import com.fintech.wallet.transfer.system.domain.exception.BusinessException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

@Getter
@EqualsAndHashCode
public final class Money {

    private final BigDecimal amount;
    private final String currency;

    /**
     * Private constructor that validates and normalizes inputs.
     *
     * @param amount   Monetary amount (must be non-negative). Stored with scale 2.
     * @param currency ISO 4217 currency code (non-null, non-empty). Normalized to upper case.
     * @throws BusinessException if amount is negative, currency is null/empty, or currency code is invalid.
     */
    @JsonCreator
    private Money(BigDecimal amount, String currency) {
        /* check negative */
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(BusinessError.AMOUNT_NEGATIVE);
        }

        /* check null or empty or white space */
        if (currency == null || currency.isBlank()) {
            throw new BusinessException(BusinessError.CURRENCY_NULL_OR_EMPTY);
        }

        /* check the ISO 4217 value of the currency */
        String normalizedCurrency = currency.trim().toUpperCase();
        try {
            Currency.getInstance(normalizedCurrency);
        } catch (IllegalArgumentException exception) {
            throw new BusinessException(BusinessError.CURRENCY_ISO_CODE);
        }

        this.currency = normalizedCurrency;
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Factory method to create a Money instance from the given amount and currency.
     *
     * @param amount   Monetary amount (non-negative)
     * @param currency ISO 4217 currency code
     * @return new Money instance
     */
    public static Money of(BigDecimal amount, String currency) {
        return new Money(amount, currency);
    }

    /**
     * Returns a Money instance representing zero amount for the given currency.
     *
     * @param currency ISO 4217 currency code
     * @return Money with zero amount in the specified currency
     */
    public static Money zero(String currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    /**
     * Adds another Money value to this one and returns the result as a new Money.
     * Both Money instances must use the same currency.
     *
     * @param otherMoney Money to add (must have same currency)
     * @return new Money representing the sum
     * @throws BusinessException if currencies do not match
     */
    public Money add(Money otherMoney) {
        checkSameCurrency(otherMoney);

        return new Money(amount.add(otherMoney.amount), currency);
    }

    /**
     * Subtracts another Money value from this one and returns the result as a new Money.
     * Both Money instances must use the same currency and this amount must be greater than
     * or equal to the other amount.
     *
     * @param otherMoney Money to subtract (must have same currency)
     * @return new Money representing the difference
     * @throws BusinessException if currencies do not match or if there is insufficient balance
     */
    public Money subtract(Money otherMoney) {
        checkSameCurrency(otherMoney);

        if (amount.compareTo(otherMoney.amount) < 0) {
            throw new BusinessException(BusinessError.INSUFFICIENT_BALANCE);
        }

        return new Money(amount.subtract(otherMoney.amount), currency);
    }

    /**
     * Checks whether the provided Money uses the same currency as this instance.
     *
     * @param otherMoney Money to compare
     * @return true if currencies are equal, false otherwise
     */
    public boolean isSameCurrency(Money otherMoney) {
        return currency.equals(otherMoney.currency);
    }

    /**
     * Internal helper that validates both Money instances share the same currency.
     *
     * @param otherMoney Money to compare
     * @throws BusinessException if currencies are different
     */
    private void checkSameCurrency(Money otherMoney) {
        if (!isSameCurrency(otherMoney)) {
            throw new BusinessException(BusinessError.CURRENCY_MISMATCH, currency, otherMoney.currency);
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s", amount, currency);
    }

}
