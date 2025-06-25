package com.microservice.topupservice.DTO;

import com.microservice.topupservice.models.TransactionType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TopupDTO {
    private String externalTransactionId;
    public String userId;
    public float amount;
    public TransactionType type;
}
