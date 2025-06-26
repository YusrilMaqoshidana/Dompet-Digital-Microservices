package com.microservice.walletservice.DTO;

import lombok.Data;

@Data
public class TransactionInitiatedEvent {
    public String transactionId;
    public String senderUserId;
    public String receiverUserId;
    public float amount;
    public String reason;
}
