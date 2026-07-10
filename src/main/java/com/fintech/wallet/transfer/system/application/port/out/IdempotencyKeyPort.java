package com.fintech.wallet.transfer.system.application.port.out;

import com.fintech.wallet.transfer.system.domain.model.Transaction;

import java.util.Optional;

public interface IdempotencyKeyPort {

    boolean tryLock(String idempotencyKey);

    void releaseLock(String idempotencyKey);

    Optional<String> getResult(String idempotencyKey);

    void saveResult(String idempotencyKey, Transaction transaction);
}
