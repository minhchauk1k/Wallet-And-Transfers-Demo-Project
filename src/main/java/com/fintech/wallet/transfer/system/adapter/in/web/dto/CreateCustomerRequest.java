package com.fintech.wallet.transfer.system.adapter.in.web.dto;

import com.fintech.wallet.transfer.system.adapter.constant.AdapterValidationMessage;
import jakarta.validation.constraints.NotBlank;

public record CreateCustomerRequest(
        @NotBlank(message = AdapterValidationMessage.NOT_NULL_OR_BLANK_OR_EMPTY)
        String fullName
) {
}
