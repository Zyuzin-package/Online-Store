package com.example.statshandler.kafka.model;

import java.io.Serializable;

public class KafkaMessage<T> implements Serializable {
    private T data;

    public KafkaMessage(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
