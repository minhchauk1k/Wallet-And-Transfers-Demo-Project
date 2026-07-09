package com.fintech.wallet.transfer.system.domain.model;

import com.fintech.wallet.transfer.system.domain.common.UUIDv7;
import com.fintech.wallet.transfer.system.domain.constant.BusinessError;
import com.fintech.wallet.transfer.system.domain.constant.WalletStatus;
import com.fintech.wallet.transfer.system.domain.exception.BusinessException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public class Wallet {

    private final UUID id;
    /* walletNumber or accountNumber */
    private final WalletNumber walletNumber;
    private final UUID ownerId;
    private Money balance;
    private WalletStatus status;
    private final Instant createdAt;
    private final long version;

    private Wallet(
            UUID id,
            WalletNumber walletNumber,
            UUID customerId,
            Money balance,
            WalletStatus status,
            Instant createdAt,
            long version
    ) {
        this.id = id;
        this.walletNumber = walletNumber;
        this.ownerId = customerId;
        this.balance = balance;
        this.status = status;
        this.createdAt = createdAt;
        this.version = version;
    }

    /**
     * Recreates a Wallet instance from the provided fields. This factory is intended for
     * rehydration (e.g., mapping database rows to domain objects) and preserves the exact
     * identity, balance, status and audit fields supplied by the caller.
     *
     * @param id           wallet UUID identifier
     * @param walletNumber wallet number value object
     * @param customerId   owner's UUID
     * @param balance      current balance held by the wallet
     * @param status       current wallet status
     * @param createdAt    creation timestamp
     * @param version      version number for optimistic locking / concurrency control
     * @return Wallet instance populated with provided values
     */
    public static Wallet of(
            UUID id,
            WalletNumber walletNumber,
            UUID customerId,
            Money balance,
            WalletStatus status,
            Instant createdAt,
            long version
    ) {
        return new Wallet(id, walletNumber, customerId, balance, status, createdAt, version);
    }

    /**
     * Opens a new wallet for the given customer with zero balance in the specified currency.
     *
     * @param customer the wallet owner
     * @param currency ISO 4217 currency code for the wallet balance
     * @return newly created Wallet with zero balance and ACTIVE status
     */
    public static Wallet openWalletWithoutBalance(Customer customer, String currency) {
        return new Wallet(
                UUIDv7.randomUUID(),
                WalletNumber.generate(),
                customer.getId(),
                Money.zero(currency),
                WalletStatus.ACTIVE,
                Instant.now(),
                0L
        );
    }

    /**
     * Opens a new wallet for the given customer initialized with the provided amount.
     *
     * @param customer the wallet owner
     * @param amount   initial balance amount
     * @param currency ISO 4217 currency code for the wallet balance
     * @return newly created Wallet with the provided initial balance and ACTIVE status
     */
    public static Wallet openWalletWithBalance(Customer customer, BigDecimal amount, String currency) {
        return new Wallet(
                UUIDv7.randomUUID(),
                WalletNumber.generate(),
                customer.getId(),
                Money.of(amount, currency),
                WalletStatus.ACTIVE,
                Instant.now(),
                0L
        );
    }

    /**
     * Credits (adds) the specified Money amount to this wallet's balance.
     * Wallet must be ACTIVE.
     *
     * @param money Money to credit to the wallet (must use same currency)
     * @throws BusinessException if wallet is not ACTIVE or currency mismatch occurs
     */
    public void credit(Money money) {
        checkActiveStatus();

        this.balance = this.balance.add(money);
    }

    /**
     * Debits (subtracts) the specified Money amount from this wallet's balance.
     * Wallet must be ACTIVE and have sufficient balance.
     *
     * @param money Money to debit from the wallet (must use same currency)
     * @throws BusinessException if wallet is not ACTIVE, currency mismatch, or insufficient balance
     */
    public void debit(Money money) {
        checkActiveStatus();

        this.balance = this.balance.subtract(money);
    }

    /**
     * Activates the wallet. Throws BusinessException if the wallet is already ACTIVE.
     *
     * @throws BusinessException when the wallet is already in ACTIVE status
     */
    public void activate() {
        checkConflictStatus(WalletStatus.ACTIVE);

        this.status = WalletStatus.ACTIVE;
    }


    /**
     * Deactivates the wallet. Throws BusinessException if the wallet is already INACTIVE.
     *
     * @throws BusinessException when the wallet is already in INACTIVE status
     */
    public void deactivate() {
        checkConflictStatus(WalletStatus.INACTIVE);

        this.status = WalletStatus.INACTIVE;
    }


    private void checkConflictStatus(WalletStatus newStatus) {
        if (this.status.equals(newStatus)) {
            throw new BusinessException(
                    BusinessError.WALLET_CONFLICT_STATUS, this.walletNumber, this.status
            );
        }
    }

    /**
     * Ensures the wallet is ACTIVE before performing balance operations.
     *
     * @throws BusinessException if wallet is not ACTIVE
     */
    private void checkActiveStatus() {
        if (!this.status.equals(WalletStatus.ACTIVE)) {
            throw new BusinessException(
                    BusinessError.WALLET_INVALID_STATUS, this.walletNumber, this.status
            );
        }
    }
}
