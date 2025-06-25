package com.microservice.transactionservice.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransferInitiatedEvent {
    public String transactionId;
    public String senderUserId;
    public String receiverUserId;
    public float amount;
    public String reason;
}
