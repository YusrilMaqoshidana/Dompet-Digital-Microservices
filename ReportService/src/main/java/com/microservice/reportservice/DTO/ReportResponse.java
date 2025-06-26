package com.microservice.reportservice.DTO;

import java.time.LocalDateTime;

import com.microservice.reportservice.models.ReportType;
import com.microservice.reportservice.models.TransactionStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportResponse {
    private String reportId;
    private String userId;
    private ReportType type;
    private TransactionStatus status;
    private float amount;
    private String description;
    private LocalDateTime transactionDate;
}
