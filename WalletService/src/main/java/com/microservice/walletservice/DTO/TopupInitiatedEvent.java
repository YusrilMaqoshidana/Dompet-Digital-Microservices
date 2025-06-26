package com.microservice.walletservice.DTO;

import lombok.Data;

@Data
public class TopupInitiatedEvent {
    private String externalTransactionId;
    private String userId;
    private float amount;
    private String type;
}
