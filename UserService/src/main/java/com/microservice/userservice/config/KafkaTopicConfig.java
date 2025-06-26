package com.microservice.userservice.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("${topic.kafka.user-created}")
    private String userCreatedTopic;
    @Value("${topic.kafka.wallet-balance-added}")
    private String balanceAddedTopic;
    @Value("${topic.kafka.wallet-balance-deducted}")
    private String balanceDeductedTopic;
    @Value("${topic.kafka.wallet-balance-deduction-failed}")
    private String deductionFailedTopic;
    @Value("${topic.kafka.wallet-transfer-success}")
    private String transferSuccessTopic;
    @Value("${topic.kafka.wallet-transfer-failed}")
    private String transferFailedTopic;
    @Value("${topic.kafka.topup-initiated}")
    private String topupInitiatedTopic;
    @Value("${topic.kafka.transaction-initiated}")
    private String transferInitiatedTopic;
    @Value("${app.kafka.transfer-completed}")
    private String transactionCompletedTopic;
    @Value("${app.kafka.transfer-failed}")
    private String transactionFailedTopic;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic userCreatedTopic() {
        return TopicBuilder.name(userCreatedTopic).partitions(2).replicas(1).build();
    }

    @Bean
    public NewTopic balanceAddedTopic() {
        return TopicBuilder.name(balanceAddedTopic).partitions(2).replicas(1).build();
    }

    @Bean
    public NewTopic balanceDeductedTopic() {
        return TopicBuilder.name(balanceDeductedTopic).partitions(2).replicas(1).build();
    }

    @Bean
    public NewTopic deductionFailedTopic() {
        return TopicBuilder.name(deductionFailedTopic).partitions(2).replicas(1).build();
    }

    @Bean
    public NewTopic transferSuccessTopic() {
        return TopicBuilder.name(transferSuccessTopic).partitions(2).replicas(1).build();
    }

    @Bean
    public NewTopic transferFailedTopic() {
        return TopicBuilder.name(transferFailedTopic).partitions(2).replicas(1).build();
    }

    @Bean
    public NewTopic topupInitiatedTopic() {
        return TopicBuilder.name(topupInitiatedTopic).partitions(2).replicas(1).build();
    }

    @Bean
    public NewTopic transferInitiatedTopic() {
        return TopicBuilder.name(transferInitiatedTopic).partitions(2).replicas(1).build();
    }

    @Bean
    public NewTopic transactionCompletedTopic() {
        return TopicBuilder.name(transactionCompletedTopic).partitions(2).replicas(1).build();
    }

    @Bean
    public NewTopic transactionFailedTopic() {
        return TopicBuilder.name(transactionFailedTopic).partitions(2).replicas(1).build();
    }

}
