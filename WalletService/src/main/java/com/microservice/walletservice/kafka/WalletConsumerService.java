package com.microservice.walletservice.kafka;

import com.microservice.walletservice.DTO.TopupInitiatedEvent;
import com.microservice.walletservice.DTO.TransactionInitiatedEvent;
import com.microservice.walletservice.DTO.UserCreatedEvent;
import com.microservice.walletservice.DTO.WalletUpdateResultEvent;
import com.microservice.walletservice.services.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletConsumerService {
    
    private final WalletService walletService;
    private final WalletPublisherService walletProducer;

    @KafkaListener(topics = "${topic.kafka.user-created}", 
                   containerFactory = "userCreatedListenerFactory")
    public void listenUserCreated(UserCreatedEvent event) {
        log.info("Received UserCreatedEvent for userId: {}", event.getUserId());
        try {
            walletService.createWallet(event);
        } catch (Exception e) {
            log.error("Error creating wallet for userId {}: {}", event.getUserId(), e.getMessage());
        }
    }
    
    @KafkaListener(topics = "${topic.kafka.topup-initiated}", 
                   containerFactory = "topupInitiatedListenerFactory")
    public void listenTopupInitiated(TopupInitiatedEvent event) {
        log.info("Received TopupInitiatedEvent: {}", event);
        try {
            walletService.processTopup(event);
            
            WalletUpdateResultEvent resultEvent = WalletUpdateResultEvent.builder()
                    .externalTransactionId(event.getExternalTransactionId())
                    .success(true)
                    .message("Balance updated successfully from topup")
                    .build();
            
            if("CREDIT".equalsIgnoreCase(event.getType())) {
                walletProducer.sendTopupSuccessEvent(resultEvent);
            } else {
                walletProducer.sendDebitSuccessEvent(resultEvent);
            }

        } catch (Exception e) {
            log.error("Error processing topup for txId {}: {}", event.getExternalTransactionId(), e.getMessage());
            WalletUpdateResultEvent resultEvent = WalletUpdateResultEvent.builder()
                    .externalTransactionId(event.getExternalTransactionId())
                    .success(false)
                    .message(e.getMessage())
                    .build();
            walletProducer.sendFailureEvent(resultEvent);
        }
    }
    
    @KafkaListener(topics = "${topic.kafka.transaction-initiated}", 
                   containerFactory = "transactionInitiatedListenerFactory")
    public void listenTransactionInitiated(TransactionInitiatedEvent event) {
        log.info("Received TransactionInitiatedEvent: {}", event);
        try {
            walletService.processTransfer(event);

            WalletUpdateResultEvent resultEvent = WalletUpdateResultEvent.builder()
                    .externalTransactionId(event.getTransactionId())
                    .success(true)
                    .message("Transfer completed successfully")
                    .build();
            walletProducer.sendTransferSuccessEvent(resultEvent);

        } catch (Exception e) {
            log.error("Error processing transfer for txId {}: {}", event.getTransactionId(), e.getMessage());
            WalletUpdateResultEvent resultEvent = WalletUpdateResultEvent.builder()
                    .externalTransactionId(event.getTransactionId())
                    .success(false)
                    .message(e.getMessage())
                    .build();
            walletProducer.sendTransferFailureEvent(resultEvent);
        }
    }
}