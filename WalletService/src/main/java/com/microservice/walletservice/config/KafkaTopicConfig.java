package com.microservice.walletservice.config;

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
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;
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
    @Value("${topic.kafka.user-created}")
    private String userCreatedTopic;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }
    @Bean public NewTopic balanceAddedTopic() {
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
    public NewTopic topicKafkaTopupInitiated() {
        return new NewTopic(topupInitiatedTopic, 2, (short) 1);
    }
    @Bean
    public NewTopic topicKafkaTransactionInitiated() {
        return new NewTopic(transferInitiatedTopic, 2, (short) 1);
    }
    @Bean
    public NewTopic topicKafkaUserCreated() {
        return new NewTopic(userCreatedTopic, 2, (short) 1);
    }


}