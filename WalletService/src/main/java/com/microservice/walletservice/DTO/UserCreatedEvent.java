package com.microservice.walletservice.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCreatedEvent {
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("accountNumber")
    private String accountNumber;
}
