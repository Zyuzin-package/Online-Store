package com.example.diplom.kafka.service;

import java.util.concurrent.TimeoutException;

public interface SyncKafkaService {
    String get(String text) throws TimeoutException;
}
