package com.fintech.wallet.transfer.system.adapter.out.persistence.mapper;

import com.fintech.wallet.transfer.system.adapter.out.persistence.entity.WalletEntity;
import com.fintech.wallet.transfer.system.domain.constant.WalletStatus;
import com.fintech.wallet.transfer.system.domain.model.Money;
import com.fintech.wallet.transfer.system.domain.model.Wallet;
import com.fintech.wallet.transfer.system.domain.model.WalletNumber;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {

    public WalletEntity toEntity(Wallet wallet) {
        return new WalletEntity(
                wallet.getId(),
                wallet.getWalletNumber().getValue(),
                wallet.getOwnerId(),
                wallet.getBalance().getAmount(),
                wallet.getBalance().getCurrency(),
                wallet.getStatus().name(),
                wallet.getCreatedAt(),
                wallet.getVersion()
        );
    }

    public Wallet toDomain(WalletEntity walletEntity) {
        return Wallet.of(
                walletEntity.getId(),
                WalletNumber.of(walletEntity.getWalletNumber()),
                walletEntity.getOwnerId(),
                Money.of(walletEntity.getBalance(), walletEntity.getCurrency()),
                WalletStatus.valueOf(walletEntity.getStatus()),
                walletEntity.getCreatedAt(),
                walletEntity.getVersion()
        );
    }

}
