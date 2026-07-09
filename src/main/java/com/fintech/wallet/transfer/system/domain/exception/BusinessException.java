package com.fintech.wallet.transfer.system.domain.exception;

import com.fintech.wallet.transfer.system.domain.constant.BusinessError;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final BusinessError error;
    private final String message;

    public BusinessException(BusinessError error, Object... args) {
        super(String.format(error.getErrorMessage(), args));
        this.error = error;
        this.message = String.format(error.getErrorMessage(), args);
    }
}
