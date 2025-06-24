package com.microservice.userservice.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder // Berguna untuk membuat instance yang mudah
public class UserCreatedEvent {
    private String userId;
    private String createdAt;
}
