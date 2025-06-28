package com.microservice.reportservice.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reports")
public class ReportModel {

    @Id
    @Column(name = "report_id")
    private String reportId;

    @Column(name = "transaction_id", nullable = false, updatable = false)
    private String transactionId;

    @Column(name = "user_id", nullable = false, updatable = false)
    private String userId;

    @Column(name = "type", nullable = false, updatable = false)
    private String type;

    @Column(name = "status", nullable = false, updatable = false)
    private String status;

    @Column(name = "amount", nullable = false, updatable = false)
    private float amount;

    @Column(name = "description", updatable = false)
    private String description;

    @Column(name = "transaction_date", updatable = false, insertable = false)
    private String transactionDate;
}
