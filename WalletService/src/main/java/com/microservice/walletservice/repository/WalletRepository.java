package com.microservice.walletservice.repository;

import com.microservice.walletservice.models.WalletModel;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WalletRepository extends JpaRepository<WalletModel, String> {
    WalletModel findWalletModelByUserId(String userId);

    boolean existsByUserId(String userId);

    @Modifying // Wajib ada untuk query yang memodifikasi data (UPDATE/DELETE)
    @Query("UPDATE WalletModel w SET w.balance = :newBalance WHERE w.userId = :userId")
    void updateBalanceByUserId(@Param("userId") String userId, @Param("newBalance") float newBalance);
}
