package com.microservice.transactionservice.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${app.kafka.transfer-initiated}")
    private String transferInitiatedTopic;

    @Value(value = "${app.kafka.transfer-completed}")
    private String transferCompletedTopic;

    @Value(value = "${app.kafka.transfer-failed}")
    private String transferFailedTopic;

    @Value(value = "${topic.kafka.wallet-transfer-success}")
    private String walletTransferSuccessTopic;

    @Value(value = "${topic.kafka.wallet-transfer-failed}")
    private String walletTransferFailedTopic;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic transferInitiated() {
        return new NewTopic(transferInitiatedTopic, 2, (short) 1);
    }

    @Bean
    public NewTopic transferCompleted() {
        return new NewTopic(transferCompletedTopic, 2, (short) 1);
    }

    @Bean
    public NewTopic transferFailed() {
        return new NewTopic(transferFailedTopic, 2, (short) 1);
    }

    @Bean
    public NewTopic transactionSuccess() {
        return new NewTopic(walletTransferSuccessTopic, 2, (short) 1);
    }

    @Bean
    public NewTopic transactionFailed() {
        return new NewTopic(walletTransferFailedTopic, 2, (short) 1);
    }

}
