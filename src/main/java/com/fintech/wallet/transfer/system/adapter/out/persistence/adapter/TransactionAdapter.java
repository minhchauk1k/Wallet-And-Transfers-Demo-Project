package com.fintech.wallet.transfer.system.adapter.out.persistence.adapter;

import com.fintech.wallet.transfer.system.adapter.out.persistence.entity.TransactionEntity;
import com.fintech.wallet.transfer.system.adapter.out.persistence.mapper.TransactionMapper;
import com.fintech.wallet.transfer.system.adapter.out.persistence.repository.TransactionRepository;
import com.fintech.wallet.transfer.system.application.port.out.TransactionRepositoryPort;
import com.fintech.wallet.transfer.system.domain.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TransactionAdapter implements TransactionRepositoryPort {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionAdapter(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public Transaction save(Transaction transaction) {
        TransactionEntity saved = transactionRepository.save(transactionMapper.toEntity(transaction));
        return transactionMapper.toDomain(saved);
    }

    @Override
    public Optional<Transaction> findByIdempotencyKey(String idempotencyKey) {
        return transactionRepository.findByIdempotencyKey(idempotencyKey).map(transactionMapper::toDomain);
    }

}
