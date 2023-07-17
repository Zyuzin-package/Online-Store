package com.example.diplom.config;

import com.example.diplom.kafka.senderreceiver.SenderReceiverMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.UUID;

@Configuration
public class SenderReceiverConfig {
    @Bean
    @Scope("singleton")
    public SenderReceiverMap<UUID, String> senderReceiverMap(){
        return new SenderReceiverMap<>();
    }
}
