package com.microservice.transactionservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletUpdateResultEvent {
    private String externalTransactionId;
    private boolean success;
    private String message;
}

