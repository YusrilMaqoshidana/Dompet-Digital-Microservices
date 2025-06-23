package com.microservice.transactionservice.repository;

import com.microservice.transactionservice.models.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionModel, String> {
    TransactionModel getTransactionModelByTransactionId(String transactionId);
}
