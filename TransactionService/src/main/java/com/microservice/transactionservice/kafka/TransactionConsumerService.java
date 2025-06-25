package com.microservice.transactionservice.kafka;

import com.microservice.transactionservice.DTO.WalletUpdateResultEvent;
import com.microservice.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionConsumerService {

    TransactionService transactionService;

    @KafkaListener(topics = "${app.kafka.transfer-completed}",
            containerFactory = "walletUpdateSuccessFactory")
    public void listenTransactionSuccess(WalletUpdateResultEvent event) {
        try {
            transactionService.update(event.getTransactionId(), "SUCCESS");
        } catch (Exception e) {
            throw new RuntimeException("Error while updating transaction status: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "${app.kafka.transfer-failed}",
            containerFactory = "walletUpdateFailedFactory")
    public void listenTransactionFailed(WalletUpdateResultEvent event) {
        try {
            transactionService.update(event.getTransactionId(), "FAILED");
        } catch (Exception e) {
            throw new RuntimeException("Error while updating transaction status: " + e.getMessage());
        }
    }


}
