package com.microservice.topupservice.DTO;

import com.microservice.topupservice.models.TransactionType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TopupRequestDTO {
    private String externalTransactionId;
    public String userId;
    public float amount;
    private TransactionType type;
}
