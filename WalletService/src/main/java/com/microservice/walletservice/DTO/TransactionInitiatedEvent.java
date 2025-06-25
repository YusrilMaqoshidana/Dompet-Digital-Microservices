package com.microservice.walletservice.DTO;

import lombok.Data;

@Data
public class TransactionInitiatedEvent {
    private String transactionId;
    private String senderId;
    private String recheiverId;
    private float amount;
}
