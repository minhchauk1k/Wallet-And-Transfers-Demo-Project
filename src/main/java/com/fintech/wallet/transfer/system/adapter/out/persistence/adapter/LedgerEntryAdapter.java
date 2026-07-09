package com.fintech.wallet.transfer.system.adapter.out.persistence.adapter;

import com.fintech.wallet.transfer.system.adapter.out.persistence.entity.LedgerEntryEntity;
import com.fintech.wallet.transfer.system.adapter.out.persistence.mapper.LedgerEntryMapper;
import com.fintech.wallet.transfer.system.adapter.out.persistence.repository.LedgerEntryRepository;
import com.fintech.wallet.transfer.system.application.port.out.LedgerEntryRepositoryPort;
import com.fintech.wallet.transfer.system.domain.model.LedgerEntry;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LedgerEntryAdapter implements LedgerEntryRepositoryPort {

    private final LedgerEntryRepository ledgerEntryRepository;
    private final LedgerEntryMapper ledgerEntryMapper;

    public LedgerEntryAdapter(LedgerEntryRepository ledgerEntryRepository, LedgerEntryMapper ledgerEntryMapper) {
        this.ledgerEntryRepository = ledgerEntryRepository;
        this.ledgerEntryMapper = ledgerEntryMapper;
    }

    @Override
    public List<LedgerEntry> saveAll(List<LedgerEntry> ledgerEntries) {
        List<LedgerEntryEntity> savedAll = ledgerEntryRepository.saveAll(
                ledgerEntries.stream().map(ledgerEntryMapper::toEntity).toList()
        );

        return savedAll.stream().map(ledgerEntryMapper::toDomain).toList();
    }

}
