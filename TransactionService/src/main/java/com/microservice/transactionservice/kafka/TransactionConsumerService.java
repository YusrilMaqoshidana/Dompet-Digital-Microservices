package com.microservice.transactionservice.kafka;

import com.microservice.transactionservice.DTO.TransferDTO;
import com.microservice.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionConsumerService {

    TransactionPublisherService transactionPublisherService;
    TransactionService transactionService;

    @KafkaListener(topics = "${app.kafka.sender-balance-deducted}", groupId = "${app.kafka.group.transaction}")
    public void listenSenderBalanceDeducted(TransferDTO event) {
        transactionPublisherService.publishAddReceiverBalanceEvent(event);
    }

    @KafkaListener(topics = "${app.kafka.receiver-balance-added}", groupId = "${app.kafka.group.transaction}")
    public void listenReceiverBalanceAdded(TransferDTO event){
        transactionService.update(event.getTransactionId(), "SUCCESS");
        transactionPublisherService.publishTransferCompletedEvent(event);
    }

    @KafkaListener(topics = "${app.kafka.receiver-balance-add-failed}", groupId = "${app.kafka.group.transaction}")
    public void listenReceiverBalanceAddFailed(TransferDTO event) {
        transactionPublisherService.publishRevertSenderBalanceEvent(event);
    }

    @KafkaListener(topics = "${app.kafka.sender-balance-deduction-failed}", groupId = "${app.kafka.group.transaction}")
    public void listenSenderBalanceDeductionFailed(TransferDTO event) {
        transactionService.update(event.getTransactionId(), "FAILED");
        transactionPublisherService.publishTransferFailedEvent(event, "SENDER_BALANCE_DEDUCTION_FAILED");
    }

    @KafkaListener(topics = "${app.kafka.sender-balance-reverted}", groupId = "${app.kafka.group.transaction}")
    public void listenSenderBalanceReverted(TransferDTO event) {
        transactionService.update(event.getTransactionId(), "FAILED");
        transactionPublisherService.publishTransferFailedEvent(event, "SENDER_BALANCE_REVERTED");
    }

}
