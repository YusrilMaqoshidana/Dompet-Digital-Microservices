package com.microservice.walletservice.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.microservice.walletservice.DTO.WalletUpdateResultEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletPublisherService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${topic.kafka.wallet-balance-added}")
    private String balanceAddedTopic;

    @Value("${topic.kafka.wallet-balance-deducted}")
    private String balanceDeductedTopic;

    @Value("${topic.kafka.wallet-balance-deduction-failed}")
    private String deductionFailedTopic;
    
    @Value("${topic.kafka.wallet-transfer-success}")
    private String transferSuccessTopic;

    @Value("${topic.kafka.wallet-transfer-failed}")
    private String transferFailedTopic;

    public void sendTopupSuccessEvent(WalletUpdateResultEvent event) {
        log.info("Sending topup success event (balance added): {}", event);
        kafkaTemplate.send(balanceAddedTopic, event.getExternalTransactionId(), event);
    }
    
    public void sendDebitSuccessEvent(WalletUpdateResultEvent event) {
        log.info("Sending debit success event (balance deducted): {}", event);
        kafkaTemplate.send(balanceDeductedTopic, event.getExternalTransactionId(), event);
    }
    
    public void sendFailureEvent(WalletUpdateResultEvent event) {
        log.info("Sending topup/debit failure event: {}", event);
        kafkaTemplate.send(deductionFailedTopic, event.getExternalTransactionId(), event);
    }
    
    public void sendTransferSuccessEvent(WalletUpdateResultEvent event) {
        log.info("Sending transfer success event: {}", event);
        kafkaTemplate.send(transferSuccessTopic, event.getExternalTransactionId(), event);
    }

    public void sendTransferFailureEvent(WalletUpdateResultEvent event) {
        log.info("Sending transfer failure event: {}", event);
        kafkaTemplate.send(transferFailedTopic, event.getExternalTransactionId(), event);
    }
}
