package com.fintech.wallet.transfer.system.application.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ApplicationError {

    CUSTOMER_NOT_FOUND_BY_ID("A-CUS-001", "Customer with ID %s not found.", HttpStatus.NOT_FOUND),
    WALLET_NOT_FOUND_BY_ID("A-WAL-001", "Wallet with ID %s not found.", HttpStatus.NOT_FOUND),
    WALLET_NOT_FOUND_BY_WALLET_NUMBER("A-WAL-002", "Wallet %s not found.", HttpStatus.NOT_FOUND),
    MISSING_IDEMPOTENCY_KEY("A-TXN-001", "Missing required HTTP header: Idempotency-Key", HttpStatus.BAD_REQUEST),
    TRANSACTION_IN_PROGRESS("A-TXN-002", "Transaction is currently in progress.", HttpStatus.CONFLICT);

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;
}
