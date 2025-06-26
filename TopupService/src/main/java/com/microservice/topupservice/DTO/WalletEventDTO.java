package com.microservice.topupservice.DTO;

import lombok.Data;

@Data
public class WalletEventDTO {
    private String externalTransactionId;
    private boolean success;
    private String message;
}
