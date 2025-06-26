package com.microservice.userservice.service;

import com.microservice.userservice.DTO.UserCreatedEvent;
import com.microservice.userservice.models.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserPublisherService {

    @Value("${topic.kafka.user-created}")
    private String topicName;

    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    public void publishUserCreatedEvent(UserModel userModel) {
        UserCreatedEvent event = UserCreatedEvent.builder()
                .userId(userModel.getUserId())
                .accountNumber(userModel.getPhoneNumber())
                .createdAt(Instant.now().toString())
                .build();
        kafkaTemplate.send(topicName, event);
    }

}