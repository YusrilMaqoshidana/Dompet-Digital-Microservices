package com.microservice.transactionservice.kafka;

import com.microservice.transactionservice.DTO.WalletUpdateResultEvent;
import com.microservice.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionConsumerService {

    private final TransactionService transactionService;

    @KafkaListener(topics = "${topic.kafka.wallet-transfer-success}",
            containerFactory = "walletUpdateSuccessFactory")
    public void listenTransactionSuccess(WalletUpdateResultEvent event) {
        try {
            transactionService.update(event.getExternalTransactionId(), event.isSuccess() ? "SUCCESS" : "FAILED");
        } catch (Exception e) {
            throw new RuntimeException("Error while updating transaction status: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "${topic.kafka.wallet-transfer-failed}",
            containerFactory = "walletUpdateFailedFactory")
    public void listenTransactionFailed(WalletUpdateResultEvent event) {
        try {
            transactionService.update(event.getExternalTransactionId(), "FAILED");
        } catch (Exception e) {
            throw new RuntimeException("Error while updating transaction status: " + e.getMessage());
        }
    }


}
