package com.microservice.reportservice.DTO;

import lombok.Data;

@Data
public class TopupEvent {
    private String topupId;
    private String externalTransactionId;
    private String userId;
    private float amount;
    private String type;
    private String status;
    private String createdAt;
}
