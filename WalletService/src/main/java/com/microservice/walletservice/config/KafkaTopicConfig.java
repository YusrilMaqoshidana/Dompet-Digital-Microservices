package com.microservice.walletservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    
    @Value("${topic.kafka.wallet-balance-added}") private String balanceAddedTopic;
    @Value("${topic.kafka.wallet-balance-deducted}") private String balanceDeductedTopic;
    @Value("${topic.kafka.wallet-balance-deduction-failed}") private String deductionFailedTopic;
    @Value("${topic.kafka.wallet-transfer-success}") private String transferSuccessTopic;
    @Value("${topic.kafka.wallet-transfer-failed}") private String transferFailedTopic;
    
    @Bean public NewTopic balanceAddedTopic() { return TopicBuilder.name(balanceAddedTopic).build(); }
    @Bean public NewTopic balanceDeductedTopic() { return TopicBuilder.name(balanceDeductedTopic).build(); }
    @Bean public NewTopic deductionFailedTopic() { return TopicBuilder.name(deductionFailedTopic).build(); }
    @Bean public NewTopic transferSuccessTopic() { return TopicBuilder.name(transferSuccessTopic).build(); }
    @Bean public NewTopic transferFailedTopic() { return TopicBuilder.name(transferFailedTopic).build(); }
}