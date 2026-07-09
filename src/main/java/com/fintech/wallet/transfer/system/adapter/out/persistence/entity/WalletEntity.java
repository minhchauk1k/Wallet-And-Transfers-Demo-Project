package com.fintech.wallet.transfer.system.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
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
@Table(name = "wallets")
public class WalletEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "wallet_number")
    private String walletNumber;

    @Column(name = "owner_id")
    private UUID ownerId;

    @Column(name = "balance", precision = 20, scale = 2)
    private BigDecimal balance;

    @Column(name = "currency")
    private String currency;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private Instant createdAt;

    @Version
    @Column(name = "version")
    private long version;

}
