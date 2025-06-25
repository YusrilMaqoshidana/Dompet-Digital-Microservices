package com.microservice.reportservice.DTO;

import lombok.Data;

@Data
public class TransactionEvent {
    private String transactionId;
    private String fromUserId;
    private String toUserId;
    private float amount;
    private String type;
    private String status;
    private String description;
}
