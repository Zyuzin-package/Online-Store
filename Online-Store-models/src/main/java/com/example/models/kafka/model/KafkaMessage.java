package com.example.models.kafka.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaMessage<V> implements Serializable {

    private V data;

    public V getData() {
        return data;
    }
}
