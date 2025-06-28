package com.microservice.reportservice.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportResponse {
    private String reportId;
    private String userId;
    private String type;
    private String status;
    private float amount;
    private String description;
}
