package com.microservice.transactionservice.kafka;

import com.microservice.transactionservice.DTO.TransactionDTOResponse;
import com.microservice.transactionservice.DTO.TransferDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionPublisherService {
    private final KafkaTemplate<String, TransferDTO> kafkaTemplate;

    @Value(value = "${app.kafka.transfer-initiated}")
    private String transferInitiatedTopic;

    @Value(value = "${app.kafka.transfer-completed}")
    private String transferCompletedTopic;

    @Value(value = "${app.kafka.transfer-failed}")
    private String transferFailedTopic;

    @Value(value = "${app.kafka.add-receiver-balance}")
    private String addReceiverBalanceTopic;

    @Value(value = "${app.kafka.revert-sender-balance}")
    private String revertSenderBalanceTopic;

    public void publishTransferInitiatedEvent(TransferDTO dto) {
        kafkaTemplate.send(transferInitiatedTopic, dto.getTransactionId(), dto);
        System.out.println("Transfer initiated event sent to topic: " + transferInitiatedTopic);
    }

    public void publishAddReceiverBalanceEvent(TransferDTO dto) {
        kafkaTemplate.send(addReceiverBalanceTopic, dto.getTransactionId(), dto);
        System.out.println("Add receiver balance event sent to topic: " + addReceiverBalanceTopic);
    }

    public void publishRevertSenderBalanceEvent(TransferDTO dto) {
        kafkaTemplate.send(revertSenderBalanceTopic, dto.getTransactionId(), dto);
        System.out.println("Revert sender balance event sent to topic: " + revertSenderBalanceTopic);
    }

    public void publishTransferCompletedEvent(TransferDTO dto) {
        dto.setReason("SUCCESS");
        kafkaTemplate.send(transferCompletedTopic, dto.getTransactionId(), dto);
        System.out.println("Transfer completed event sent to topic: " + transferCompletedTopic);
    }

    public void publishTransferFailedEvent(TransferDTO dto, String reason) {
        dto.setReason(reason);
        kafkaTemplate.send(transferFailedTopic, dto.getTransactionId(), dto);
        System.out.println("Transfer failed event sent to topic: " + transferFailedTopic);
    }

}
