package com.microservice.notificationservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopupModelResponse {
    private String topupId;
    private String externalTransactionId;
    private String userId;
    private float amount;
    private String type;
    private String status;
    private String createdAt;
}
