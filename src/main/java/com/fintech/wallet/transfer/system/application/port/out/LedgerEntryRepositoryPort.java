package com.fintech.wallet.transfer.system.application.port.out;

import com.fintech.wallet.transfer.system.domain.model.LedgerEntry;

import java.util.List;

public interface LedgerEntryRepositoryPort {

    List<LedgerEntry> saveAll(List<LedgerEntry> entries);

}
