package com.fintech.wallet.transfer.system.application.port.out;

import com.fintech.wallet.transfer.system.domain.model.Transaction;

import java.util.Optional;

public interface IdempotencyKeyPort {

    Optional<String> getResult(String idempotencyKey);

    void saveResult(String idempotencyKey, Transaction transaction);

}
