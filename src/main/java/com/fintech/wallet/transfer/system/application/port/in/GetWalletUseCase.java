package com.fintech.wallet.transfer.system.application.port.in;

import com.fintech.wallet.transfer.system.domain.model.Money;
import com.fintech.wallet.transfer.system.domain.model.Wallet;
import com.fintech.wallet.transfer.system.domain.model.WalletNumber;

import java.util.UUID;

public interface GetWalletUseCase {

    Wallet getWallet(GetWalletCommand command);

    Wallet getWalletBalanceById(GetWalletBalanceCommand command);

    Wallet getWalletBalanceByWalletNumber(GetWalletBalanceByWalletNumberCommand command);

    record GetWalletCommand(UUID id) {
    }

    record GetWalletBalanceCommand(UUID id) {
    }

    record GetWalletBalanceByWalletNumberCommand(String walletNumber) {
    }

    record GetWalletBalanceResult(UUID id, String walletNumber, Money balance) {
    }

}
