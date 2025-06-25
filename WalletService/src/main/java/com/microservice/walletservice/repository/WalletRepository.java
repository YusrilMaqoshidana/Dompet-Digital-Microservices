package com.microservice.walletservice.repository;

import com.microservice.walletservice.models.WalletModel;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<WalletModel, String> {
    Optional<WalletModel> findByUserId(String userId);

    boolean existsByUserId(String userId);
}
