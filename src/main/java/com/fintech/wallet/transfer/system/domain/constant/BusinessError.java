package com.fintech.wallet.transfer.system.domain.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BusinessError {

    /* Common domain errors */
    ID_NULL_OR_EMPTY("BUS-001", "Id must not be null or empty.", HttpStatus.BAD_REQUEST),

    /* Money domain errors */
    CURRENCY_NULL_OR_EMPTY("MNY-001", "Currency must not be null or empty.", HttpStatus.BAD_REQUEST),
    CURRENCY_ISO_CODE("MNY-002", "Currency must be ISO-4217 value.", HttpStatus.BAD_REQUEST),
    CURRENCY_MISMATCH("MNY-003", "Currency mismatch: %s and %s.", HttpStatus.BAD_REQUEST),
    AMOUNT_NEGATIVE("MNY-004", "Amount can not be negative.", HttpStatus.BAD_REQUEST),

    /* Wallet domain errors */
    WALLET_INVALID_STATUS("WAL-001", "Wallet %s can not perform transaction because its status is %s (required: ACTIVE)", HttpStatus.FORBIDDEN),
    WALLET_CONFLICT_STATUS("WAL-002", "Wallet %s has already been %s.", HttpStatus.CONFLICT),
    WALLET_NUMBER_NULL_OR_EMPTY("WAL-003", "Wallet number must not be null or empty.", HttpStatus.BAD_REQUEST),

    /* Transaction/Payment domain errors */
    INSUFFICIENT_BALANCE("TXN-001", "Insufficient balance.", HttpStatus.FORBIDDEN),
    DUPLICATE_TRANSACTION_BY_KEY("TXN-002", "Duplicate transaction with Key %s.", HttpStatus.CONFLICT),
    SELF_TRANSFER("TXN-003", "Self-transfer is not allowed.", HttpStatus.BAD_REQUEST),
    LEDGER_ENTRY_AMOUNT_MISMATCH("TXN-004", "Ledger entry amount mismatch: Total credit (%s) must equal total debit (%s).", HttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;
}