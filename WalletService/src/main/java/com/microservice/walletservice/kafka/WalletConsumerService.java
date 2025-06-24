package com.microservice.walletservice.kafka;

import com.microservice.walletservice.DTO.UserCreatedEvent;
import com.microservice.walletservice.models.WalletModel;
import com.microservice.walletservice.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletConsumerService {


    private final WalletService walletService;

    @KafkaListener(topics = "${topic.kafka.user-created}", groupId = "${spring.application.name}", containerFactory = "factory")
    public void listen(UserCreatedEvent userCreatedEvent) {
        try {

            if (userCreatedEvent == null) {
                System.out.println("Received null UserCreatedEvent");
                return;
            }

            WalletModel wallet = walletService.createWallet(userCreatedEvent);
            System.out.printf("Wallet created successfully: {}", wallet);

        } catch (Exception e) {
            System.err.printf("Error processing UserCreatedEvent: {}", userCreatedEvent, e);
            throw e;
        }
    }
}
