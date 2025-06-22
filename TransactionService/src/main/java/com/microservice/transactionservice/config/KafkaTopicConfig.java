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

    @Value(value = "${app.kafka.add-receiver-balance}")
    private String addReceiverBalanceTopic;

    @Value(value = "${app.kafka.revert-sender-balance}")
    private String revertSenderBalanceTopic;

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
    public NewTopic addReceiverBalance() {
        return new NewTopic(addReceiverBalanceTopic, 2, (short) 1);
    }

    @Bean
    public NewTopic revertSenderBalance() {
        return new NewTopic(revertSenderBalanceTopic, 2, (short) 1);
    }
}
