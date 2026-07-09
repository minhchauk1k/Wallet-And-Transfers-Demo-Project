package com.fintech.wallet.transfer.system.application.service;

import com.fintech.wallet.transfer.system.application.port.out.LedgerEntryRepositoryPort;
import com.fintech.wallet.transfer.system.application.port.out.TransactionRepositoryPort;
import com.fintech.wallet.transfer.system.application.port.out.WalletRepositoryPort;
import org.mockito.Mockito;

public class TransferMoneyServiceTest {

    private final TransactionRepositoryPort transactionRepositoryPort = Mockito.mock(TransactionRepositoryPort.class);
    private final WalletRepositoryPort walletRepositoryPort = Mockito.mock(WalletRepositoryPort.class);
    private final LedgerEntryRepositoryPort ledgerEntryRepositoryPort = Mockito.mock(LedgerEntryRepositoryPort.class);

    private final TransferMoneyService transferMoneyService = new TransferMoneyService(
            transactionRepositoryPort,
            walletRepositoryPort,
            ledgerEntryRepositoryPort
    );

}
