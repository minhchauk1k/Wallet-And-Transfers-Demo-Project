package com.fintech.wallet.transfer.system.adapter.out.persistence.adapter;

import com.fintech.wallet.transfer.system.adapter.out.persistence.entity.WalletEntity;
import com.fintech.wallet.transfer.system.adapter.out.persistence.mapper.WalletMapper;
import com.fintech.wallet.transfer.system.adapter.out.persistence.repository.WalletRepository;
import com.fintech.wallet.transfer.system.application.port.out.WalletRepositoryPort;
import com.fintech.wallet.transfer.system.domain.model.Wallet;
import com.fintech.wallet.transfer.system.domain.model.WalletNumber;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class WalletAdapter implements WalletRepositoryPort {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;

    public WalletAdapter(WalletRepository walletRepository, WalletMapper walletMapper) {
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
    }

    @Override
    public Wallet save(Wallet wallet) {
        WalletEntity saved = walletRepository.save(walletMapper.toEntity(wallet));
        return walletMapper.toDomain(saved);
    }

    @Override
    public Optional<Wallet> findById(UUID id) {
        return walletRepository.findById(id).map(walletMapper::toDomain);
    }

    @Override
    public Optional<Wallet> findByWalletNumber(String walletNumber) {
        return walletRepository.findByWalletNumber(walletNumber).map(walletMapper::toDomain);
    }
}
