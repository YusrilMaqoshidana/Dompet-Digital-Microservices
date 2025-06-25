package com.microservice.topupservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Value("${topic.kafka.topup-initiated}")
    private String topupInitiatedTopic;

    @Bean
    public NewTopic topupInitiatedTopicBean() {
        return TopicBuilder.name(topupInitiatedTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
}