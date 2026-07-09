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
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "idempotency_key")
    private String idempotencyKey;

    @Column(name = "source_wallet_id")
    private UUID sourceWalletId;

    @Column(name = "target_wallet_id")
    private UUID targetWalletId;

    @Column(name = "amount", precision = 20, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "status")
    private String status;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private Instant createdAt;

}
