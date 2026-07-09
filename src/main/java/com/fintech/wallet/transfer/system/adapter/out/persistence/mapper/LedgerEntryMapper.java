package com.fintech.wallet.transfer.system.adapter.out.persistence.mapper;

import com.fintech.wallet.transfer.system.adapter.out.persistence.entity.LedgerEntryEntity;
import com.fintech.wallet.transfer.system.domain.constant.TransferType;
import com.fintech.wallet.transfer.system.domain.model.LedgerEntry;
import com.fintech.wallet.transfer.system.domain.model.Money;
import org.springframework.stereotype.Component;

@Component
public class LedgerEntryMapper {

    public LedgerEntryEntity toEntity(LedgerEntry ledgerEntry) {
        return new LedgerEntryEntity(
                ledgerEntry.getId(),
                ledgerEntry.getTransactionId(),
                ledgerEntry.getWalletId(),
                ledgerEntry.getAmount().getAmount(),
                ledgerEntry.getAmount().getCurrency(),
                ledgerEntry.getType().name(),
                ledgerEntry.getCreatedAt()
        );
    }

    public LedgerEntry toDomain(LedgerEntryEntity ledgerEntryEntity) {
        return LedgerEntry.of(
                ledgerEntryEntity.getId(),
                ledgerEntryEntity.getTransactionId(),
                ledgerEntryEntity.getWalletId(),
                Money.of(ledgerEntryEntity.getAmount(), ledgerEntryEntity.getCurrency()),
                TransferType.valueOf(ledgerEntryEntity.getType()),
                ledgerEntryEntity.getCreatedAt()
        );
    }

}
