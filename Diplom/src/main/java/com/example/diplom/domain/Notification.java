package com.example.diplom.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum Notification {
    NOTING("No notifications"),
    NEW("Your order has been created"),
    APPROVED("Your order has been approved"),
    CANCELED("Your order has been cancelled"),
    PAID("Your order has been paid"),
    COMPLETED("Your order has been completed");

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
