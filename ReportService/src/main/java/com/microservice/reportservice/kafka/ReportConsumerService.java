package com.microservice.reportservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.microservice.reportservice.DTO.TopupEvent;
import com.microservice.reportservice.DTO.TransactionEvent;
import com.microservice.reportservice.service.ReportService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportConsumerService {
    private final ReportService reportService;

    @KafkaListener(topics = {"${topic.kafka.topup-success}", "${topic.kafka.topup-failed}"},
                   containerFactory = "topupListenerContainerFactory") // <-- Gunakan factory yang tepat
    public void consumeTopupEvents(TopupEvent event) {
        log.info("Consumed topup event -> {}", event);
        reportService.createReportFromTopup(event);
    }

    @KafkaListener(topics = {"${topic.kafka.transaction-success}", "${topic.kafka.transaction-failed}"},
                   containerFactory = "transactionListenerContainerFactory") // <-- Gunakan factory yang tepat
    public void consumeTransactionEvents(TransactionEvent event) {
        log.info("Consumed transaction event -> {}", event);
        reportService.createReportFromTransaction(event);
    }
}
