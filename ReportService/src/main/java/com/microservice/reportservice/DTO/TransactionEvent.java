package com.microservice.reportservice.DTO;

import lombok.Data;

@Data
public class TransactionEvent {
    private String transactionId;
    private String senderUserId;
    private String receiverUserId;
    private float amount;
    private String transactionType;
    private String status;
    private String description;
    private String createdAt;
    private String completedAt;
}
