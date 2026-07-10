package com.fintech.wallet.transfer.system.application.service;

import com.fintech.wallet.transfer.system.application.constant.ApplicationError;
import com.fintech.wallet.transfer.system.application.exception.ApplicationException;
import com.fintech.wallet.transfer.system.application.port.in.TransferMoneyUseCase;
import com.fintech.wallet.transfer.system.application.port.out.IdempotencyKeyPort;
import com.fintech.wallet.transfer.system.application.port.out.LedgerEntryRepositoryPort;
import com.fintech.wallet.transfer.system.application.port.out.TransactionRepositoryPort;
import com.fintech.wallet.transfer.system.application.port.out.WalletRepositoryPort;
import com.fintech.wallet.transfer.system.domain.constant.BusinessError;
import com.fintech.wallet.transfer.system.domain.exception.BusinessException;
import com.fintech.wallet.transfer.system.domain.model.DoubleEntryLedger;
import com.fintech.wallet.transfer.system.domain.model.Money;
import com.fintech.wallet.transfer.system.domain.model.Transaction;
import com.fintech.wallet.transfer.system.domain.model.Wallet;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransferMoneyService implements TransferMoneyUseCase {

    private final TransactionRepositoryPort transactionRepositoryPort;
    private final WalletRepositoryPort walletRepositoryPort;
    private final LedgerEntryRepositoryPort ledgerEntryRepositoryPort;
    private final IdempotencyKeyPort idempotencyKeyPort;

    public TransferMoneyService(TransactionRepositoryPort transactionRepositoryPort, WalletRepositoryPort walletRepositoryPort, LedgerEntryRepositoryPort ledgerEntryRepositoryPort, IdempotencyKeyPort idempotencyKeyPort) {
        this.transactionRepositoryPort = transactionRepositoryPort;
        this.walletRepositoryPort = walletRepositoryPort;
        this.ledgerEntryRepositoryPort = ledgerEntryRepositoryPort;
        this.idempotencyKeyPort = idempotencyKeyPort;
    }

    @Override
    @Transactional
    @Retryable(
            includes = {ObjectOptimisticLockingFailureException.class, OptimisticLockingFailureException.class},
            maxRetries = 3,
            multiplier = 2.0
    )
    public Transaction transfer(TransferTransactionCommand command) {
        log.info("START: Money transfer processing ...");

        /* 1. Check duplicate [Transaction] by [IdempotencyKey] */
        transactionRepositoryPort.findByIdempotencyKey(command.idempotencyKey()).ifPresent(transaction -> {
            throw new BusinessException(BusinessError.DUPLICATE_TRANSACTION_BY_KEY, transaction.getIdempotencyKey());
        });

        /* 2. Read [Source Wallet] and [Target Wallet] without lock (optimistic lock) */
        Wallet sourceWallet = walletRepositoryPort.findByWalletNumber(command.sourceWalletNumber())
                .orElseThrow(() -> new ApplicationException(ApplicationError.WALLET_NOT_FOUND_BY_WALLET_NUMBER, command.sourceWalletNumber()));
        Wallet targetWallet = walletRepositoryPort.findByWalletNumber(command.targetWalletNumber())
                .orElseThrow(() -> new ApplicationException(ApplicationError.WALLET_NOT_FOUND_BY_WALLET_NUMBER, command.targetWalletNumber()));

        /* 3.1 Create [Transaction] for business logic (Status=NEW) */
        Transaction newTransaction = Transaction.createNew(
                command.idempotencyKey(),
                sourceWallet,
                targetWallet,
                Money.of(command.amount(), command.currency()),
                command.description()
        );

        /* 3.2 Mark [Transaction] is started (Status=IN_PROGRESS) */
        newTransaction.start();

        /* 4. Create a [Pair] of [LedgerEntry] for transfer money process (credit/IN and debit/OUT) */
        DoubleEntryLedger postingData = DoubleEntryLedger.forTransfer(
                newTransaction.getId(),
                sourceWallet.getId(),
                targetWallet.getId(),
                newTransaction.getAmount()
        );

        /* 5. Do entry for the [Ledger] is first priority (the source of truth) */
        ledgerEntryRepositoryPort.saveAll(postingData.getEntries());

        /* 6. Apply change to [Source Wallet] and [Target Wallet] */
        sourceWallet.debit(newTransaction.getAmount());
        targetWallet.credit(newTransaction.getAmount());
        walletRepositoryPort.save(sourceWallet);
        walletRepositoryPort.save(targetWallet);

        /* 7. Mark [Transaction] is completed (Status=COMPLETED) */
        newTransaction.completed();
        Transaction saved = transactionRepositoryPort.save(newTransaction);

        /* 8. Save result of [Transaction] as [Cache] */
        idempotencyKeyPort.saveResult(command.idempotencyKey(), saved);

        log.info("END: Money transfer processing!");
        return saved;
    }
}
