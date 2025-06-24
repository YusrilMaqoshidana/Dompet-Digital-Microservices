package com.microservice.walletservice.services;

import com.microservice.walletservice.DTO.UserCreatedEvent;
import com.microservice.walletservice.models.WalletModel;
import com.microservice.walletservice.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    public WalletModel createWallet(UserCreatedEvent walletResponse) {
        WalletModel walletModel = toWalletModel(walletResponse);
        return walletRepository.save(walletModel);
    }

    public WalletModel getWalletByWalletId() {
        return null;
    }

    public List<WalletModel> getAllWallets() {
        return walletRepository.findAll();
    }

    public WalletModel balanceReducted() {
        return null;
    }

    public WalletModel balanceAdded() {
        return null;
    }

    public WalletModel updateStatus() {
        return null;
    }

    private WalletModel toWalletModel(UserCreatedEvent walletModel) {
        String generatedId = UUID.randomUUID().toString();
        Double balance = 0.0;
        return WalletModel.builder()
                .walletId(generatedId)
                .userId(walletModel.getUserId())
                .balance(balance)
                .status(true)
                .accountNumber(walletModel.getAccountNumber())
                .createdAt(walletModel.getCreatedAt())
                .updatedAt(walletModel.getCreatedAt()).build();
    }
}
