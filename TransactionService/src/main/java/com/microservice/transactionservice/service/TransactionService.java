package com.microservice.transactionservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.microservice.transactionservice.DTO.TransactionDTOResponse;
import com.microservice.transactionservice.models.TransactionModel;
import com.microservice.transactionservice.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public List<TransactionModel> getAll() {
        return transactionRepository.findAll();
    }

    public TransactionModel getByTransactionId(String transactionId){
        return transactionRepository.getTransactionModelByTransactionId(transactionId);
    }

    public TransactionModel updateStatus(String transactionId, boolean newStatus){
        TransactionModel transaction = transactionRepository.getTransactionModelByTransactionId(transactionId);
        transaction.setStatus(newStatus);
        return transactionRepository.save(transaction);
    }

    public void delete(String transactionId){
        transactionRepository.deleteById(transactionId);
    }

    public TransactionModel create(TransactionDTOResponse newTransaction) {
        TransactionModel transaction = toTransactionModel(newTransaction);
        return transactionRepository.save(transaction);
    }

    private TransactionModel toTransactionModel(TransactionDTOResponse dto) {
        TransactionModel transaction = new TransactionModel();
//        long generatedId = ThreadLocalRandom.current().nextLong(1_000_000_000L, 9_999_999_999L);
        String generatedId = UUID.randomUUID().toString();
        printHashTransactionId(generatedId, dto.getTransactionName());
        transaction.setTransactionId(generatedId);
        transaction.setNominal(dto.getNominal());
        transaction.setStatus(dto.isStatus());
        return transaction;
    }

    private void printHashTransactionId(String transactionId, String transactionName){
        if (transactionId == null) {
            System.err.println("Error: Transaction ID cannot be null.");
            return; // Exit the method if transactionId is null.
        }
        int transactionIdHash = Math.abs(transactionId.hashCode());
        System.out.println("User ID Hash: " + transactionIdHash);
        if (transactionIdHash % 2 == 0) {
            // If the hash is even
            System.out.println(transactionName + " Masuk ke ds_0 (even hash)");
        } else {
            // If the hash is odd
            System.out.println(transactionName + " Masuk ke ds_1 (odd hash)");
        }
    }

}
