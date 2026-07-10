package com.fintech.wallet.transfer.system.adapter.in.web.dto;

import com.fintech.wallet.transfer.system.adapter.constant.AdapterValidationMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionRequest(
        @NotNull(message = AdapterValidationMessage.NOT_NULL)
        String sourceWalletNumber,
        @NotNull(message = AdapterValidationMessage.NOT_NULL)
        String targetWalletNumber,
        @NotNull(message = AdapterValidationMessage.NOT_NULL)
        @Positive(message = AdapterValidationMessage.MUST_BE_POSITIVE)
        BigDecimal amount,
        @NotBlank(message = AdapterValidationMessage.NOT_NULL_OR_BLANK_OR_EMPTY)
        String type,
        @NotBlank(message = AdapterValidationMessage.NOT_NULL_OR_BLANK_OR_EMPTY)
        String currency,
        @NotNull(message = AdapterValidationMessage.NOT_NULL)
        String description
) {
}

