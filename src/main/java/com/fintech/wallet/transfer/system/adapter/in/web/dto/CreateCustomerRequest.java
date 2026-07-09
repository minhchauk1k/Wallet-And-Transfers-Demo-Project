package com.fintech.wallet.transfer.system.adapter.in.web.dto;

import com.fintech.wallet.transfer.system.adapter.constant.ValidationMessage;
import jakarta.validation.constraints.NotBlank;

public record CreateCustomerRequest(
        @NotBlank(message = ValidationMessage.NOT_NULL_OR_BLANK_OR_EMPTY)
        String fullName
) {
}
