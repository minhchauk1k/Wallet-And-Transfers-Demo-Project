package com.fintech.wallet.transfer.system.domain.model;

import com.fintech.wallet.transfer.system.domain.common.UUIDv7;
import com.fintech.wallet.transfer.system.domain.constant.TransactionStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public final class Transaction {

    private final UUID id;
    private final String idempotencyKey;
    private final UUID sourceWalletId;
    private final UUID targetWalletId;
    private final Money amount;
    private TransactionStatus status;
    private final String description;
    private final Instant createdAt;

    private Transaction(
            UUID id,
            String idempotencyKey,
            UUID sourceWalletId,
            UUID targetWalletId,
            Money amount,
            TransactionStatus status,
            String description,
            Instant createdAt
    ) {
        this.id = id;
        this.idempotencyKey = idempotencyKey;
        this.sourceWalletId = sourceWalletId;
        this.targetWalletId = targetWalletId;
        this.amount = amount;
        this.status = status;
        this.description = description;
        this.createdAt = createdAt;
    }

    public static Transaction of(UUID id, String idempotencyKey, UUID sourceWalletId, UUID targetWalletId, Money amount, TransactionStatus status, String description, Instant createdAt) {
        return new Transaction(
                id,
                idempotencyKey,
                sourceWalletId,
                targetWalletId,
                amount,
                status,
                description,
                createdAt
        );
    }

    public static Transaction createNew(String idempotencyKey, Wallet sourceWallet, Wallet targetWallet, Money amount, String description) {
        return new Transaction(
                UUIDv7.randomUUID(),
                idempotencyKey,
                sourceWallet.getId(),
                targetWallet.getId(),
                amount,
                TransactionStatus.NEW,
                description,
                Instant.now()
        );
    }

    public void start() {
        this.status = TransactionStatus.IN_PROGRESS;
    }

    public void completed() {
        this.status = TransactionStatus.COMPLETED;
    }

}
