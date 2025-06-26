package com.microservice.notificationservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionModelResponse {
    private String transactionId;
    private String senderUserId;
    private String receiverUserId;
    private float amount;
    private String transactionType;
    private String createdAt;
    private String completedAt;
    private String description;
    private String status;
}
