package com.microservice.walletservice.services;

import com.microservice.walletservice.DTO.TopupInitiatedEvent;
import com.microservice.walletservice.DTO.TransactionInitiatedEvent;
import com.microservice.walletservice.DTO.UserCreatedEvent;
import com.microservice.walletservice.DTO.WalletUpdateResultEvent;
import com.microservice.walletservice.kafka.WalletPublisherService;
import com.microservice.walletservice.models.WalletModel;
import com.microservice.walletservice.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        String generateId = UUID.randomUUID().toString();
        WalletModel wallet = WalletModel.builder()
                .walletId(generateId)
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
        WalletModel wallet = walletRepository.findWalletModelByUserId(event.getUserId());
        if (wallet == null) {
            throw new RuntimeException("Wallet not found for userId: " + event.getUserId());
        }
        if (!wallet.getStatus()) {
            throw new RuntimeException("Wallet is frozen for userId: " + event.getUserId());
        }

        float newBalance;

        if ("CREDIT".equalsIgnoreCase(event.getType())) {
            newBalance = wallet.getBalance() + event.getAmount();
        } else if ("DEBIT".equalsIgnoreCase(event.getType())) {
            if (wallet.getBalance() < event.getAmount()) {
                throw new RuntimeException("Insufficient balance for userId: " + event.getUserId());
            }
            newBalance = wallet.getBalance() - event.getAmount();
        } else {
            throw new RuntimeException("Unknown topup type: " + event.getType());
        }
        walletRepository.updateBalanceByUserId(event.getUserId(), newBalance);
        log.info("Balance for userId {} updated successfully. New balance: {}", event.getUserId(), newBalance);
    }

    @Transactional
    public void processTransfer(TransactionInitiatedEvent event) {
        WalletModel senderWallet = walletRepository.findWalletModelByUserId(event.getSenderUserId());
        WalletModel receiverWallet = walletRepository.findWalletModelByUserId(event.getReceiverUserId());

        if (senderWallet == null) {
            throw new RuntimeException("Sender wallet not found for userId: " + event.getSenderUserId());
        }
        if (receiverWallet == null) {
            throw new RuntimeException("Receiver wallet not found for userId: " + event.getReceiverUserId());
        }
        if (!senderWallet.getStatus()) {
            throw new RuntimeException("Sender wallet is frozen for userId: " + event.getSenderUserId());
        }
        if (!receiverWallet.getStatus()) {
            throw new RuntimeException("Receiver wallet is frozen for userId: " + event.getReceiverUserId());
        }
        if (senderWallet.getBalance() < event.getAmount()) {
            throw new RuntimeException("Insufficient balance for sender userId: " + event.getSenderUserId());
        }

        float newSenderBalance = senderWallet.getBalance() - event.getAmount();
        float newReceiverBalance = receiverWallet.getBalance() + event.getAmount();

        walletRepository.updateBalanceByUserId(event.getSenderUserId(), newSenderBalance);
        log.info("Deducted {} from sender {}. New balance: {}", event.getAmount(), event.getSenderUserId(), newSenderBalance);

        walletRepository.updateBalanceByUserId(event.getReceiverUserId(), newReceiverBalance);
        log.info("Added {} to receiver {}. New balance: {}", event.getAmount(), event.getReceiverUserId(), newReceiverBalance);

    }

    public WalletModel getWalletByUserId(String userId) {
        return walletRepository.findWalletModelByUserId(userId);
    }

    public List<WalletModel> getAllWallets() {
        return walletRepository.findAll();
    }
}