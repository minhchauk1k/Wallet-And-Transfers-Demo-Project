package com.fintech.wallet.transfer.system.adapter.in.web.dto;

import com.fintech.wallet.transfer.system.adapter.constant.ValidationMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateWalletRequest(
        @NotNull(message = ValidationMessage.NOT_NULL)
        UUID ownerId,
        BigDecimal balance,
        @NotBlank(message = ValidationMessage.NOT_NULL_OR_BLANK_OR_EMPTY)
        String currency
) {
}
