package com.fintech.wallet.transfer.system.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fintech.wallet.transfer.system.domain.common.UUIDv7;
import com.fintech.wallet.transfer.system.domain.constant.TransferType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public final class LedgerEntry {

    private final UUID id;
    private final UUID transactionId;
    private final UUID walletId;
    private final Money amount;
    private final TransferType type;
    private final Instant createdAt;

    @JsonCreator
    private LedgerEntry(
            UUID id,
            UUID transactionId,
            UUID walletId,
            Money amount,
            TransferType type,
            Instant createdAt
    ) {
        this.id = id;
        this.transactionId = transactionId;
        this.walletId = walletId;
        this.amount = amount;
        this.type = type;
        this.createdAt = createdAt;
    }

    public static LedgerEntry of(UUID id, UUID transactionId, UUID walletId, Money amount, TransferType type, Instant createdAt) {
        return new LedgerEntry(
                id,
                transactionId,
                walletId,
                amount,
                type,
                createdAt
        );
    }

    /**
     * Create a CREDIT transaction (incoming transfer to the target wallet).
     */
    public static LedgerEntry credit(UUID transactionId, UUID walletId, Money amount) {
        return new LedgerEntry(
                UUIDv7.randomUUID(),
                transactionId,
                walletId,
                amount,
                TransferType.CREDIT,
                Instant.now()
        );
    }

    /**
     * Create a DEBIT transaction (outgoing transfer from the source wallet).
     */
    public static LedgerEntry debit(UUID transactionId, UUID walletId, Money amount) {
        return new LedgerEntry(
                UUIDv7.randomUUID(),
                transactionId,
                walletId,
                amount,
                TransferType.DEBIT,
                Instant.now()
        );
    }

    /**
     * Create a DEPOSIT transaction (cash-in to the provided wallet).
     */
    public static LedgerEntry deposit(UUID transactionId, UUID walletId, Money amount) {
        return new LedgerEntry(
                UUIDv7.randomUUID(),
                transactionId,
                walletId,
                amount,
                TransferType.DEPOSIT,
                Instant.now()
        );
    }

    /**
     * Create a WITHDRAWAL transaction (cash-out from the provided wallet).
     */
    public static LedgerEntry withdrawal(UUID transactionId, UUID walletId, Money amount) {
        return new LedgerEntry(
                UUIDv7.randomUUID(),
                transactionId,
                walletId,
                amount,
                TransferType.WITHDRAWAL,
                Instant.now()
        );
    }


}
