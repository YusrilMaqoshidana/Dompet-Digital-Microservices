package com.microservice.walletservice.services;

import com.microservice.walletservice.DTO.TopupInitiatedEvent;
import com.microservice.walletservice.DTO.TransactionInitiatedEvent;
import com.microservice.walletservice.DTO.UserCreatedEvent;
import com.microservice.walletservice.models.WalletModel;
import com.microservice.walletservice.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletService {
    private final WalletRepository walletRepository;

    @Transactional
    public void createWallet(UserCreatedEvent event) {
        if (walletRepository.existsByUserId(event.getUserId())) {
            log.warn("Wallet for userId {} already exists. Ignoring event.", event.getUserId());
            return;
        }
        WalletModel wallet = WalletModel.builder()
                .userId(event.getUserId())
                .accountNumber(event.getAccountNumber())
                .balance(0.0f)
                .status(true)
                .build();
        walletRepository.save(wallet);
        log.info("Wallet created successfully for userId {}", event.getUserId());
    }

    @Transactional
    public void processTopup(TopupInitiatedEvent event) {
        WalletModel wallet = walletRepository.findByUserId(event.getUserId())
                .orElseThrow(() -> new RuntimeException("Wallet not found for userId: " + event.getUserId()));

        if (!wallet.getStatus()) {
            throw new RuntimeException("Wallet is frozen for userId: " + event.getUserId());
        }

        if ("CREDIT".equalsIgnoreCase(event.getType())) {
            wallet.setBalance(wallet.getBalance() + event.getAmount());
        } else if ("DEBIT".equalsIgnoreCase(event.getType())) {
            if (wallet.getBalance() < event.getAmount()) {
                throw new RuntimeException("Insufficient balance for userId: " + event.getUserId());
            }
            wallet.setBalance(wallet.getBalance() - event.getAmount());
        } else {
            throw new RuntimeException("Unknown topup type: " + event.getType());
        }

        walletRepository.save(wallet);
        log.info("Balance for userId {} updated successfully. New balance: {}", wallet.getUserId(), wallet.getBalance());
    }

    @Transactional
    public void processTransfer(TransactionInitiatedEvent event) {
        WalletModel senderWallet = walletRepository.findByUserId(event.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender wallet not found for userId: " + event.getSenderId()));

        if (!senderWallet.getStatus()) {
            throw new RuntimeException("Sender wallet is frozen for userId: " + event.getSenderId());
        }

        if (senderWallet.getBalance() < event.getAmount()) {
            throw new RuntimeException("Insufficient balance for sender userId: " + event.getSenderId());
        }
        senderWallet.setBalance(senderWallet.getBalance() - event.getAmount());
        walletRepository.save(senderWallet);
        log.info("Deducted {} from sender {}", event.getAmount(), event.getSenderId());

        WalletModel receiverWallet = walletRepository.findByUserId(event.getRecheiverId())
                .orElseThrow(() -> new RuntimeException("Receiver wallet not found for userId: " + event.getRecheiverId()));

        if (!receiverWallet.getStatus()) {
            throw new RuntimeException("Receiver wallet is frozen for userId: " + event.getRecheiverId());
        }
        receiverWallet.setBalance(receiverWallet.getBalance() + event.getAmount());
        walletRepository.save(receiverWallet);
        log.info("Added {} to receiver {}", event.getAmount(), event.getRecheiverId());
    }

    public Optional<WalletModel> getWalletByUserId(String userId) {
        return walletRepository.findByUserId(userId);
    }

    public List<WalletModel> getAllWallets() {
        return walletRepository.findAll();
    }
}