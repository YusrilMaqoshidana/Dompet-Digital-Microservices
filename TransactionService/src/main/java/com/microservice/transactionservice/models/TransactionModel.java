package com.microservice.transactionservice.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "transactions")
public class TransactionModel {
    @Id
    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "transactionName", nullable = false)
    private String transactionName;

    @Column(name = "nominal", nullable = false)
    private float nominal;

    @Column(name = "status", nullable = false)
    private boolean status;
}
