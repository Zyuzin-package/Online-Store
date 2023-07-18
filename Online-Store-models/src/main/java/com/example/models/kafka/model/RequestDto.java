package com.example.models.kafka.model;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto implements Serializable {

    private UUID requestId;
    private String data;

    public UUID getRequestId() {
        return requestId;
    }

    public String getData() {
        return data;
    }

}
