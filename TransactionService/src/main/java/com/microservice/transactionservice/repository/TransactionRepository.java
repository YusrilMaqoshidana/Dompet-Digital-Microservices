package com.microservice.transactionservice.repository;

import com.microservice.transactionservice.models.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionModel, String> {
    TransactionModel getTransactionModelByTransactionId(String transactionId);
    List<TransactionModel> getTransactionModelsBySenderUserId(String senderUserId);
    List<TransactionModel> getTransactionModelsByReceiverUserId(String senderUserId);
}
