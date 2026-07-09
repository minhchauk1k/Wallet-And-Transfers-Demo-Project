package com.fintech.wallet.transfer.system.application.port.out;

import com.fintech.wallet.transfer.system.domain.model.Wallet;
import com.fintech.wallet.transfer.system.domain.model.WalletNumber;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepositoryPort {

    Wallet save(Wallet wallet);

    Optional<Wallet> findById(UUID id);

    Optional<Wallet> findByWalletNumber(String walletNumber);

}
