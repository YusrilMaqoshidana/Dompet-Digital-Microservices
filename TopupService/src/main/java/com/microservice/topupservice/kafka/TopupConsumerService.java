package com.microservice.topupservice.kafka;

import com.microservice.topupservice.DTO.WalletEventDTO;
import com.microservice.topupservice.service.TopupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TopupConsumerService {
    private final TopupService topupService;

    @KafkaListener(topics = {
        "${topic.kafka.wallet-balance-added}",
        "${topic.kafka.wallet-balance-deducted}",
        "${topic.kafka.wallet-balance-deduction-failed}"
    },
    groupId = "${spring.kafka.consumer.group-id}",
    containerFactory = "kafkaListenerContainerFactory")
    public void consumeWalletEvent(WalletEventDTO event) {
        log.info("Received wallet event: {}", event);
        topupService.finalizeTopup(event);
    }
}