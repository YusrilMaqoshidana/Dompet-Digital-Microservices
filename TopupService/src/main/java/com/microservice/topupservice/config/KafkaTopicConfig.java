package com.microservice.topupservice.config;

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
    private String bootstrapServers;

    @Value("${topic.kafka.topup-initiated}")
    private String topupInitiatedTopic;

    @Value("${topic.kafka.topup-success}")
    private String topupSuccessTopic;

    @Value("${topic.kafka.topup-failed}")
    private String topupFailedTopic;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topicKafkaTopupInitiated() {
        return TopicBuilder.name(topupInitiatedTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topicKafkaTopupSuccess() {
        return TopicBuilder.name(topupSuccessTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topicKafkaTopupFailed() {
        return TopicBuilder.name(topupFailedTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }

}