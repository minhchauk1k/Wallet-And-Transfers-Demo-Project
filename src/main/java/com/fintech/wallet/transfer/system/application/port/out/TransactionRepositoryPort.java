package com.fintech.wallet.transfer.system.application.port.out;

import com.fintech.wallet.transfer.system.domain.model.Transaction;

import java.util.Optional;

public interface TransactionRepositoryPort {

    Transaction save(Transaction transaction);

    Optional<Transaction> findByIdempotencyKey(String idempotencyKey);

}
