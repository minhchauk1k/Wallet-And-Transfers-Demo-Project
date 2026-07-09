package com.fintech.wallet.transfer.system.adapter.out.persistence.mapper;

import com.fintech.wallet.transfer.system.adapter.out.persistence.entity.TransactionEntity;
import com.fintech.wallet.transfer.system.domain.constant.TransactionStatus;
import com.fintech.wallet.transfer.system.domain.model.Money;
import com.fintech.wallet.transfer.system.domain.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionEntity toEntity(Transaction transaction) {
        return new TransactionEntity(
                transaction.getId(),
                transaction.getIdempotencyKey(),
                transaction.getSourceWalletId(),
                transaction.getTargetWalletId(),
                transaction.getAmount().getAmount(),
                transaction.getAmount().getCurrency(),
                transaction.getStatus().name(),
                transaction.getDescription(),
                transaction.getCreatedAt()
        );
    }

    public Transaction toDomain(TransactionEntity transactionEntity) {
        return Transaction.of(
                transactionEntity.getId(),
                transactionEntity.getIdempotencyKey(),
                transactionEntity.getSourceWalletId(),
                transactionEntity.getTargetWalletId(),
                Money.of(transactionEntity.getAmount(),transactionEntity.getCurrency()),
                TransactionStatus.valueOf(transactionEntity.getStatus()),
                transactionEntity.getDescription(),
                transactionEntity.getCreatedAt()
        );
    }

}
