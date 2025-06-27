package com.microservice.walletservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "wallets")
@AllArgsConstructor
@NoArgsConstructor
public class WalletModel {
    @Id
    @Column(name = "wallet_id")
    private String walletId;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "account_number")
    private String accountNumber;
    @Column(name = "balance")
    private float balance;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "created_at", updatable = false, insertable = false)
    private String createdAt;
    @Column(name = "updated_at", updatable = false, insertable = false)
    private String updatedAt;
}
