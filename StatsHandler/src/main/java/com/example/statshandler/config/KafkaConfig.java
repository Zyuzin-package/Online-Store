package com.example.statshandler.config;

import com.example.statshandler.kafka.model.KafkaMessage;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    private final String kafkaServer="localhost:9092";

    @Bean
    public ProducerFactory<String, KafkaMessage<String>> producerFactory() {
        Map<String, Object> producerConfigProperties = new HashMap<>();
        producerConfigProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        producerConfigProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerConfigProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        producerConfigProperties.put(JsonDeserializer.TRUSTED_PACKAGES, "com.example.statshandler.model.KafkaMessage");
        return new DefaultKafkaProducerFactory<>(producerConfigProperties);
    }

    @Bean
    public KafkaTemplate<String, KafkaMessage<String>> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
//
//    @Bean
//    public NewTopic newTopic(){
//        return new NewTopic("topic1", 56, (short) 1);
//    }
}
