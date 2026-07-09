package com.fintech.wallet.transfer.system.application.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ApplicationError {

    CUSTOMER_NOT_FOUND_BY_ID("CUS-001", "Customer with ID %s not found.", HttpStatus.NOT_FOUND),
    WALLET_NOT_FOUND_BY_ID("WAL-001", "Wallet with ID %s not found.", HttpStatus.NOT_FOUND),
    WALLET_NOT_FOUND_BY_WALLET_NUMBER("WAL-002", "Wallet %s not found.", HttpStatus.NOT_FOUND);

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;
}
