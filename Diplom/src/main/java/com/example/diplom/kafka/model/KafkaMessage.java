package com.example.diplom.kafka.model;

import java.io.Serializable;

public class KafkaMessage<V> implements Serializable {
    private V data;
    public V getData() {
        return data;
    }
}
