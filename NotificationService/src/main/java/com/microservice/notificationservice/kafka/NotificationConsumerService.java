package com.microservice.notificationservice.kafka;

import com.microservice.notificationservice.DTO.TopupModelResponse;
import com.microservice.notificationservice.DTO.TransactionModelResponse;
import com.microservice.notificationservice.services.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumerService {
    private final NotificationService notificationService;

    @KafkaListener(topics = {"${topic.kafka.topup-success}", "${topic.kafka.topup-failed}"},
            containerFactory = "topupListenerContainerFactory")
    public void consumeTopupEvents(TopupModelResponse event) {
        log.info("Consumed topup event -> {}", event);
        notificationService.createNotificationFromTopup(event);
    }

    @KafkaListener(topics = {"${topic.kafka.transaction-success}", "${topic.kafka.transaction-failed}"},
            containerFactory = "transactionListenerContainerFactory")
    public void consumeTransactionEvents(TransactionModelResponse event) {
        log.info("Consumed transaction event -> {}", event);
        if (event.getStatus().equals("SUCCESS")){
            notificationService.createNotificationFromSenderTransaction(event);
            notificationService.createNotificationFromReceiverTransaction(event);
        } else {
            notificationService.createNotificationFromSenderTransaction(event);
        }
    }
}

