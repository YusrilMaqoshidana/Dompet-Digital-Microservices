package com.microservice.topupservice.kafka;

import com.microservice.topupservice.DTO.TopupDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TopupPublisherService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${topic.kafka.topup-initiated}")
    private String topupInitiatedTopic;

    public void publishTopupInitiatedEvent(TopupDTO dto) {
        try {
            kafkaTemplate.send(topupInitiatedTopic, dto.getExternalTransactionId(), dto);
            log.info("Topup initiated event sent to topic '{}' for transaction ID: {}",
                topupInitiatedTopic, dto.getExternalTransactionId());
        } catch (Exception e) {
            log.error("Failed to send Topup initiated event for transaction ID: {}",
                dto.getExternalTransactionId(), e);
        }
    }
}