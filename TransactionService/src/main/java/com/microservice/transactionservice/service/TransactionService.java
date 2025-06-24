package com.microservice.transactionservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.microservice.transactionservice.DTO.TransferDTO;
import com.microservice.transactionservice.kafka.TransactionPublisherService;
import org.springframework.stereotype.Service;

import com.microservice.transactionservice.DTO.TransactionDTOResponse;
import com.microservice.transactionservice.models.TransactionModel;
import com.microservice.transactionservice.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionPublisherService transactionPublisherService;

    public List<TransactionModel> getAll() {
        return transactionRepository.findAll();
    }

    public TransactionModel getByTransactionId(String transactionId){
        return transactionRepository.getTransactionModelByTransactionId(transactionId);
    }

    public void update(String transactionId, String newStatus) {
        TransactionModel transaction = transactionRepository.getTransactionModelByTransactionId(transactionId);
        if (transaction != null) {
            transaction.setStatus(newStatus);
            transaction.setCompletedAt(LocalDateTime.now().toString());
            transactionRepository.save(transaction);
            System.out.println("Transaction updated successfully.");
        } else {
            System.err.println("Error: Transaction ID not found.");
        }
    }

    public TransactionModel create(TransactionDTOResponse newTransaction) {
        String generatedId = UUID.randomUUID().toString();
        TransactionModel transaction = toTransactionModel(newTransaction, generatedId);
        TransferDTO dataTransfer = TransferDTO.builder()
                .transactionId(generatedId)
                .senderUserId(newTransaction.getSenderUserId())
                .receiverUserId(newTransaction.getReceiverUserId())
                .amount(newTransaction.getAmount())
                .reason("Initial Transaction")
                .build();
        transactionPublisherService.publishTransferInitiatedEvent(dataTransfer);
        return transactionRepository.save(transaction);
    }

    private TransactionModel toTransactionModel(TransactionDTOResponse dto, String transactionId) {
        TransactionModel transaction = new TransactionModel();

        String createdAt = LocalDateTime.now().toString();
        transaction.setTransactionId(transactionId);
        transaction.setSenderUserId(dto.getSenderUserId());
        transaction.setReceiverUserId(dto.getReceiverUserId());
        transaction.setAmount(dto.getAmount());
        transaction.setTransactionType("USER_TRANSFER");
        transaction.setDescription(dto.getDescription());
        transaction.setStatus("PENDING");
        transaction.setCreatedAt(createdAt);
        return transaction;
    }

//    private void printHashTransactionId(String transactionId, String transactionName){
//        if (transactionId == null) {
//            System.err.println("Error: Transaction ID cannot be null.");
//            return; // Exit the method if transactionId is null.
//        }
//        int transactionIdHash = Math.abs(transactionId.hashCode());
//        System.out.println("User ID Hash: " + transactionIdHash);
//        if (transactionIdHash % 2 == 0) {
//            // If the hash is even
//            System.out.println(transactionName + " Masuk ke ds_0 (even hash)");
//        } else {
//            // If the hash is odd
//            System.out.println(transactionName + " Masuk ke ds_1 (odd hash)");
//        }
//    }

}
