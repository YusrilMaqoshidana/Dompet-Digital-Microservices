package com.microservice.transactionservice.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WalletUpdateResultEvent {
    private String transactionId;
    private boolean success;
    private String message;
}

