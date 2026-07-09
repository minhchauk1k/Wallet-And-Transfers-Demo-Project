package com.fintech.wallet.transfer.system.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ledger_entries")
public class LedgerEntryEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "transaction_id")
    private UUID transactionId;

    @Column(name = "wallet_id")
    private UUID walletId;

    @Column(name = "amount", precision = 20, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "type")
    private String type;

    @Column(name = "created_at")
    private Instant createdAt;

}
