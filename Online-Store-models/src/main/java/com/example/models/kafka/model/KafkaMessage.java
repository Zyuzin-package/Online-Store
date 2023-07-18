package com.example.models.kafka.model;

import java.io.Serializable;

public class KafkaMessage<V> implements Serializable {
    private V data;
    public V getData() {
        return data;
    }
}
