package com.microservice.walletservice.repository;

import com.microservice.walletservice.models.WalletModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<WalletModel, String> {
}
