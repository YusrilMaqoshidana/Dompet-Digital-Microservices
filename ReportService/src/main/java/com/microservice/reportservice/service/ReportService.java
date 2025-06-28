package com.microservice.reportservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.microservice.reportservice.DTO.ReportResponse;
import com.microservice.reportservice.DTO.TopupEvent;
import com.microservice.reportservice.DTO.TransactionEvent;
import com.microservice.reportservice.models.ReportModel;
import com.microservice.reportservice.repository.ReportRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {
    private final ReportRepository repository;

    public void createReportFromTopup(TopupEvent event) {
        ReportModel report = toReportModel(event);
        repository.save(report);
        log.info("Saved history from topup event for user {}", event.getUserId());
    }

    @Transactional
    public void createReportFromTransaction(TransactionEvent event) {
        ReportModel senderReport = toSenderReportModel(event);
        repository.save(senderReport);
        log.info("Saved TRANSFER_OUT history for user {}", event.getSenderUserId());
        ReportModel receiverReport = toReceiverReportModel(event);
        repository.save(receiverReport);
        log.info("Saved TRANSFER_IN history for user {}", event.getReceiverUserId());
    }

    public Page<ReportResponse> getUserReport(String userId, Pageable pageable) {
        Page<ReportModel> reportPage = repository.findByUserId(userId, pageable);
        return reportPage.map(this::toReportResponse);
    }

    private ReportModel toReportModel(TopupEvent event) {
        ReportModel report = new ReportModel();
        String generateUUID = UUID.randomUUID().toString();
        report.setReportId(generateUUID);
        report.setTransactionId(event.getExternalTransactionId());
        report.setUserId(event.getUserId());
        report.setAmount(event.getAmount());
        report.setType(event.getType());
        report.setDescription("Top up saldo");
        report.setStatus(event.getStatus());
        return report;
    }

    private ReportModel toSenderReportModel(TransactionEvent event) {
        ReportModel senderReport = new ReportModel();
        String generateUUID = java.util.UUID.randomUUID().toString();
        senderReport.setReportId(generateUUID);
        senderReport.setTransactionId(event.getTransactionId());
        senderReport.setUserId(event.getSenderUserId());
        senderReport.setAmount(event.getAmount());
        senderReport.setType(event.getTransactionType());
        senderReport.setDescription("Transfer ke " + event.getReceiverUserId());
        senderReport.setStatus(event.getStatus());
        return senderReport;
    }

    private ReportModel toReceiverReportModel(TransactionEvent event) {
        ReportModel receiverReport = new ReportModel();
        String generateUUID = java.util.UUID.randomUUID().toString();
        receiverReport.setReportId(generateUUID);
        receiverReport.setTransactionId(event.getTransactionId());
        receiverReport.setUserId(event.getReceiverUserId());
        receiverReport.setAmount(event.getAmount());
        receiverReport.setType(event.getTransactionType());
        receiverReport.setDescription("Transfer dari " + event.getSenderUserId());
        receiverReport.setStatus(event.getStatus());
        return receiverReport;
    }

    private ReportResponse toReportResponse(ReportModel report) {
        return ReportResponse.builder()
                .reportId(report.getReportId())
                .userId(report.getUserId())
                .type(report.getType())
                .status(report.getStatus())
                .amount(report.getAmount())
                .description(report.getDescription())
                .build();
    }

}