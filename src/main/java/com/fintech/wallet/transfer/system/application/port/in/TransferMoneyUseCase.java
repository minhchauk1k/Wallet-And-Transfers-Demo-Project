package com.fintech.wallet.transfer.system.application.port.in;

import com.fintech.wallet.transfer.system.domain.model.Transaction;
import com.fintech.wallet.transfer.system.domain.model.WalletNumber;

import java.math.BigDecimal;

public interface TransferMoneyUseCase {

    Transaction transfer(TransferTransactionCommand command);

    record TransferTransactionCommand(String idempotencyKey, String sourceWalletNumber,
                                      String targetWalletNumber,
                                      BigDecimal amount, String currency, String description) {
    }

}
