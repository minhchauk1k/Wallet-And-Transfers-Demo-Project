package com.fintech.wallet.transfer.system.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fintech.wallet.transfer.system.domain.constant.BusinessError;
import com.fintech.wallet.transfer.system.domain.exception.BusinessException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public final class DoubleEntryLedger {

    private final LedgerEntry creditEntry;
    private final LedgerEntry debitEntry;

    @JsonCreator
    private DoubleEntryLedger(LedgerEntry creditEntry, LedgerEntry debitEntry) {
        this.creditEntry = creditEntry;
        this.debitEntry = debitEntry;
    }

    public static DoubleEntryLedger forTransfer(UUID transactionId, UUID sourceWalletId,
                                                UUID targetWalletId, Money amount) {
        if (sourceWalletId.compareTo(targetWalletId) == 0) {
            throw new BusinessException(BusinessError.SELF_TRANSFER);
        }

        LedgerEntry creditEntry = LedgerEntry.credit(transactionId, targetWalletId, amount);
        LedgerEntry debitEntry = LedgerEntry.debit(transactionId, sourceWalletId, amount);

        return new DoubleEntryLedger(creditEntry, debitEntry);
    }

    public List<LedgerEntry> getEntries() {
        if (!creditEntry.getAmount().equals(debitEntry.getAmount())) {
            throw new BusinessException(
                    BusinessError.LEDGER_ENTRY_AMOUNT_MISMATCH,
                    creditEntry.getAmount(),
                    debitEntry.getAmount()
            );
        }

        return List.of(creditEntry, debitEntry);
    }

}
