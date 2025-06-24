package com.microservice.transactionservice.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionDTOResponse {
    public String senderUserId;
    public String receiverUserId;
    public String description;
    public float amount;
}
