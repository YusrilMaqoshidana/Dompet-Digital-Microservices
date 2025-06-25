package com.microservice.reportservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.microservice.reportservice.DTO.ReportResponse;
import com.microservice.reportservice.DTO.TopupEvent;
import com.microservice.reportservice.DTO.TransactionEvent;
import com.microservice.reportservice.models.ReportType;
import com.microservice.reportservice.models.ReportModel;
import com.microservice.reportservice.models.TransactionStatus;
import com.microservice.reportservice.repository.ReportRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {
    private final ReportRepository repository;

    public void createReportFromTopup(TopupEvent event) {
        ReportModel report = new ReportModel();
        report.setTransactionId(event.getExternalTransactionId());
        report.setUserId(event.getUserId());
        report.setAmount(event.getAmount());

        if ("CREDIT".equalsIgnoreCase(event.getType())) {
            report.setType(ReportType.TOPUP_CREDIT);
            report.setDescription("Top up saldo");
        } else {
            report.setType(ReportType.PAYMENT_BILL);
            report.setDescription("Pembayaran via saldo");
        }

        report.setStatus("SUCCESS".equalsIgnoreCase(event.getStatus()) ? TransactionStatus.SUCCESS : TransactionStatus.FAILED);
        
        repository.save(report);
        log.info("Saved history from topup event for user {}", event.getUserId());
    }

    public void createReportFromTransaction(TransactionEvent event) {
        ReportModel senderReport = new ReportModel();
        senderReport.setTransactionId(event.getTransactionId());
        senderReport.setUserId(event.getFromUserId());
        senderReport.setAmount(event.getAmount());
        senderReport.setType(ReportType.TRANSFER_OUT);
        senderReport.setDescription("Transfer ke " + event.getToUserId());
        senderReport.setStatus("SUCCESS".equalsIgnoreCase(event.getStatus()) ? TransactionStatus.SUCCESS : TransactionStatus.FAILED);
        repository.save(senderReport);
        log.info("Saved TRANSFER_OUT history for user {}", event.getFromUserId());

        if (TransactionStatus.SUCCESS.equals(senderReport.getStatus()) && event.getToUserId() != null) {
            ReportModel receiverReport = new ReportModel();
            receiverReport.setTransactionId(event.getTransactionId());
            receiverReport.setUserId(event.getToUserId());
            receiverReport.setAmount(event.getAmount());
            receiverReport.setType(ReportType.TRANSFER_IN);
            receiverReport.setDescription("Transfer dari " + event.getFromUserId());
            receiverReport.setStatus(TransactionStatus.SUCCESS);
            repository.save(receiverReport);
            log.info("Saved TRANSFER_IN history for user {}", event.getToUserId());
        }
    }

    public Page<ReportResponse> getUserReport(String userId, Pageable pageable) {
        Page<ReportModel> reportPage = repository.findByUserId(userId, pageable);
        return reportPage.map(this::toReportResponse);
    }
    
    private ReportResponse toReportResponse(ReportModel report) {
        return ReportResponse.builder()
                .reportId(report.getReportId())
                .userId(report.getUserId())
                .type(report.getType())
                .status(report.getStatus())
                .amount(report.getAmount())
                .description(report.getDescription())
                .transactionDate(report.getTransactionDate())
                .build();
    }
}
