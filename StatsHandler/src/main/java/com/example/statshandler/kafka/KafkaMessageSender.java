package com.example.statshandler.kafka;

import com.example.statshandler.kafka.model.KafkaMessage;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class KafkaMessageSender {
    private final KafkaTemplate<String, KafkaMessage<String>> kafkaTemplate;

    @Value("stats")
    private String topic;

    private static final String RQ_ID = "RQ_ID";

    public KafkaMessageSender(KafkaTemplate<String, KafkaMessage<String>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void send(final UUID requestId, final KafkaMessage<String> message) {
        ProducerRecord<String, KafkaMessage<String>> record = new ProducerRecord<>(topic, message);
        record.headers().add(new RecordHeader(RQ_ID, requestId.toString().getBytes()));
        kafkaTemplate.send(record);
        kafkaTemplate.flush();
    }
}
