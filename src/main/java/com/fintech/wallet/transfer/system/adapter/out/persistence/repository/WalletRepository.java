package com.fintech.wallet.transfer.system.adapter.out.persistence.repository;

import com.fintech.wallet.transfer.system.adapter.out.persistence.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, UUID> {

    Optional<WalletEntity> findByWalletNumber(String walletNumber);

}
