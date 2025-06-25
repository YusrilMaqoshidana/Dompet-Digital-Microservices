package com.microservice.transactionservice.kafka;

import com.microservice.transactionservice.DTO.TransferInitiatedEvent;
import com.microservice.transactionservice.models.TransactionModel;
import com.microservice.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionPublisherService {
    private final KafkaTemplate<String, TransferInitiatedEvent> kafkaTemplate;

    @Value(value = "${app.kafka.transfer-initiated}")
    private String transferInitiatedTopic;

    @Value(value = "${app.kafka.transfer-completed}")
    private String transferCompletedTopic;

    @Value(value = "${app.kafka.transfer-failed}")
    private String transferFailedTopic;

    public void publishTransferInitiatedEvent(TransferInitiatedEvent dto) {
        kafkaTemplate.send(transferInitiatedTopic, dto.getTransactionId(), dto);
        System.out.println("Transfer initiated event sent to topic: " + transferInitiatedTopic);
    }

}
