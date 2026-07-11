package com.fintech.wallet.transfer.system.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fintech.wallet.transfer.system.domain.constant.BusinessError;
import com.fintech.wallet.transfer.system.domain.exception.BusinessException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public final class WalletNumber {

    private final String value;

    /**
     * Private constructor validating the wallet number.
     *
     * @param value wallet number string (must be non-null and non-blank)
     * @throws BusinessException if value is null or blank
     */
    @JsonCreator
    private WalletNumber(String value) {
        /* check null or empty or white space */
        if (value == null || value.isBlank()) {
            throw new BusinessException(BusinessError.WALLET_NUMBER_NULL_OR_EMPTY);
        }

        /* override current value with value + time */
        this.value = value;
    }

    /**
     * Generates a new WalletNumber.
     * Current implementation prefixes a constant and appends the current time.
     *
     * @return generated WalletNumber instance
     */
    public static WalletNumber generate() {
        return new WalletNumber("5751" + System.currentTimeMillis());
    }

    public static WalletNumber of(String value) {
        return new WalletNumber(value);
    }
}
