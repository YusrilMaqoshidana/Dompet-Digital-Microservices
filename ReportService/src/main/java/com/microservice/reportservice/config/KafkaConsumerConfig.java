package com.microservice.reportservice.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.microservice.reportservice.DTO.TopupEvent;
import com.microservice.reportservice.DTO.TransactionEvent;

import org.springframework.beans.factory.annotation.Value;

@Configuration
public class KafkaConsumerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    private Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*"); // Izinkan semua package
        return props;
    }

    // ============== FACTORY UNTUK TOPUP EVENT ==============
    @Bean
    public ConsumerFactory<String, TopupEvent> topupEventConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerProps(),
                new StringDeserializer(),
                new JsonDeserializer<>(TopupEvent.class) // Eksplisit untuk TopupEvent
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TopupEvent> topupListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TopupEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(topupEventConsumerFactory());
        return factory;
    }

    // ============== FACTORY UNTUK TRANSACTION EVENT ==============
    @Bean
    public ConsumerFactory<String, TransactionEvent> transactionEventConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerProps(),
                new StringDeserializer(),
                new JsonDeserializer<>(TransactionEvent.class) // Eksplisit untuk TransactionEvent
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TransactionEvent> transactionListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TransactionEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(transactionEventConsumerFactory());
        return factory;
    }
}
