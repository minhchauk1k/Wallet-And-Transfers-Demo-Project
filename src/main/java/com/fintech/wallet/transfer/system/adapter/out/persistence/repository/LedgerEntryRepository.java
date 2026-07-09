package com.fintech.wallet.transfer.system.adapter.out.persistence.repository;

import com.fintech.wallet.transfer.system.adapter.out.persistence.entity.LedgerEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LedgerEntryRepository extends JpaRepository<LedgerEntryEntity, UUID> {
}
