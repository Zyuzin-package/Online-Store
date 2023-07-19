package com.example.diplom.kafka.service;


import com.example.diplom.kafka.senderreceiver.SenderReceiverMap;
import com.example.models.domain.statistics.VisitStats;
import com.example.models.dto.ProductDTO;
import com.example.models.dto.statistics.VisitStatsDTO;
import com.example.models.kafka.model.RequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeoutException;

@Service
public class SyncKafkaServiceImp implements SyncKafkaService {

    @Autowired
    private SenderReceiverMap<UUID, String> senderReceiverMap;

    @Value("http://localhost:8888/test")
    private String endpointServer;

    @Value("${timeout:0}")
    private Long timeout;

    public String get(List<VisitStatsDTO> visitStatsDTOS) throws TimeoutException {
        UUID requestId = UUID.randomUUID();
        while (senderReceiverMap.containsKey(requestId)) {
            requestId = UUID.randomUUID();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String json;

        try {

            json = objectMapper.writeValueAsString(visitStatsDTOS);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String responseFromServer = this.sendText(requestId, json);

        System.out.println("REST response1 from server: " + responseFromServer);
        Thread thread = senderReceiverMap.add(requestId, timeout);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String responseKafka;
        try {
            responseKafka = senderReceiverMap.get(requestId).getData();
        } catch (TimeoutException e) {
            throw e;
        } finally {
            senderReceiverMap.remove(requestId);
        }

        System.out.println("responseKafka: " + responseKafka);
        return responseKafka;
    }

    private String sendText(final UUID requestId, final String text) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestJsonStr = null;
        try {
            requestJsonStr = new ObjectMapper().writeValueAsString(new RequestDto(requestId, text));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        HttpEntity<String> request = new HttpEntity<>(requestJsonStr, headers);
        return restTemplate.postForObject(endpointServer, request, String.class);
    }
}
