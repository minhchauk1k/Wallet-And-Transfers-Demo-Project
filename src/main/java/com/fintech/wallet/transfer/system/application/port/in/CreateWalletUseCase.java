package com.fintech.wallet.transfer.system.application.port.in;

import com.fintech.wallet.transfer.system.domain.model.Wallet;

import java.math.BigDecimal;
import java.util.UUID;

public interface CreateWalletUseCase {

    Wallet createWallet(CreateWalletCommand command);

    record CreateWalletCommand(UUID ownerId, BigDecimal amount, String currency) {
    }

}
