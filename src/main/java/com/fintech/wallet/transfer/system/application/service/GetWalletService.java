package com.fintech.wallet.transfer.system.application.service;

import com.fintech.wallet.transfer.system.application.constant.ApplicationError;
import com.fintech.wallet.transfer.system.application.exception.ApplicationException;
import com.fintech.wallet.transfer.system.application.port.in.GetWalletUseCase;
import com.fintech.wallet.transfer.system.application.port.out.WalletRepositoryPort;
import com.fintech.wallet.transfer.system.domain.model.Wallet;
import org.springframework.stereotype.Service;

@Service
public class GetWalletService implements GetWalletUseCase {

    private final WalletRepositoryPort walletRepositoryPort;

    public GetWalletService(WalletRepositoryPort walletRepositoryPort) {
        this.walletRepositoryPort = walletRepositoryPort;
    }

    @Override
    public Wallet getWallet(GetWalletCommand command) {
        return walletRepositoryPort.findById(command.id())
                .orElseThrow(() -> new ApplicationException(ApplicationError.WALLET_NOT_FOUND_BY_ID, command.id()));
    }

    @Override
    public Wallet getWalletBalanceById(GetWalletBalanceCommand command) {
        return walletRepositoryPort.findById(command.id())
                .orElseThrow(() -> new ApplicationException(ApplicationError.WALLET_NOT_FOUND_BY_ID, command.id()));
    }

    @Override
    public Wallet getWalletBalanceByWalletNumber(GetWalletBalanceByWalletNumberCommand command) {
        return walletRepositoryPort.findByWalletNumber(command.walletNumber())
                .orElseThrow(() -> new ApplicationException(ApplicationError.WALLET_NOT_FOUND_BY_WALLET_NUMBER, command.walletNumber()));
    }

}
