package com.microservice.transactionservice.kafka;

import com.microservice.transactionservice.DTO.WalletUpdateResultEvent;
import com.microservice.transactionservice.models.TransactionModel;
import com.microservice.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionConsumerService {

    private final TransactionService transactionService;
    private final TransactionPublisherService transactionPublisherService;

    @KafkaListener(topics = "${topic.kafka.wallet-transfer-success}",
            containerFactory = "walletUpdateSuccessFactory")
    public void listenTransactionSuccess(WalletUpdateResultEvent event) {
        try {
            transactionService.update(event.getExternalTransactionId(), "SUCCESS");
            TransactionModel transactionRecord = transactionService.getByTransactionId(event.getExternalTransactionId());
            transactionRecord.setStatus("SUCCESS");
            transactionPublisherService.publishTransferCompleted(transactionRecord);
        } catch (Exception e) {
            throw new RuntimeException("Error while updating transaction status: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "${topic.kafka.wallet-transfer-failed}",
            containerFactory = "walletUpdateFailedFactory")
    public void listenTransactionFailed(WalletUpdateResultEvent event) {
        try {
            transactionService.update(event.getExternalTransactionId(), "FAILED");
            TransactionModel transactionModel = transactionService.getByTransactionId(event.getExternalTransactionId());
            transactionModel.setStatus("FAILED");
            transactionPublisherService.publishTransferFailed(transactionModel);
        } catch (Exception e) {
            throw new RuntimeException("Error while updating transaction status: " + e.getMessage());
        }
    }


}
