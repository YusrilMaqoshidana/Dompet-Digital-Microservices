package com.microservice.notificationservice.configs;

import com.microservice.notificationservice.DTO.TopupModelResponse;
import com.microservice.notificationservice.DTO.TransactionModelResponse;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

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
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        return props;
    }

    @Bean
    public ConsumerFactory<String, TopupModelResponse> topupEventConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerProps(),
                new StringDeserializer(),
                new JsonDeserializer<>(TopupModelResponse.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TopupModelResponse> topupListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TopupModelResponse> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(topupEventConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, TransactionModelResponse> transactionEventConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerProps(),
                new StringDeserializer(),
                new JsonDeserializer<>(TransactionModelResponse.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TransactionModelResponse> transactionListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TransactionModelResponse> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(transactionEventConsumerFactory());
        return factory;
    }
}
