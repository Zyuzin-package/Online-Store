package com.example.models.domain;

import lombok.*;


@AllArgsConstructor
@Getter
public enum OrderStatus {
    NEW,APPROVED,CANCELED,PAID,CLOSED,COMPLETED;

}
