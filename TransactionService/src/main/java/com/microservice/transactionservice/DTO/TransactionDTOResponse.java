package com.microservice.transactionservice.DTO;

import lombok.Data;

@Data
public class TransactionDTOResponse {
    public String transactionName;
    public float nominal;
    public boolean status;
}
