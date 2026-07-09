package com.fintech.wallet.transfer.system.adapter.in.web.dto;

import com.fintech.wallet.transfer.system.adapter.constant.ValidationMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionRequest(
        @NotNull(message = ValidationMessage.NOT_NULL)
        String sourceWalletNumber,
        @NotNull(message = ValidationMessage.NOT_NULL)
        String targetWalletNumber,
        @NotNull(message = ValidationMessage.NOT_NULL)
        @Positive(message = ValidationMessage.MUST_BE_POSITIVE)
        BigDecimal amount,
        @NotBlank(message = ValidationMessage.NOT_NULL_OR_BLANK_OR_EMPTY)
        String type,
        @NotBlank(message = ValidationMessage.NOT_NULL_OR_BLANK_OR_EMPTY)
        String currency,
        @NotNull(message = ValidationMessage.NOT_NULL)
        String description
) {
}

