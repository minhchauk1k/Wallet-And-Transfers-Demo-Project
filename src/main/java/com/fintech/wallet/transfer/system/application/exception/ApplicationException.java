package com.fintech.wallet.transfer.system.application.exception;

import com.fintech.wallet.transfer.system.application.constant.ApplicationError;
import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private final ApplicationError error;
    private final String message;

    public ApplicationException(ApplicationError error, Object... args) {
        super(String.format(error.getErrorMessage(), args));
        this.error = error;
        this.message = String.format(error.getErrorMessage(), args);
    }
}
