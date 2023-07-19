package com.example.statshandler.controller;

import com.example.models.domain.statistics.VisitStats;
import com.example.models.dto.ProductDTO;
import com.example.models.dto.statistics.VisitStatsDTO;
import com.example.models.kafka.model.KafkaDTO;
import com.example.models.kafka.model.KafkaMessage;
import com.example.models.kafka.model.RequestDto;
import com.example.statshandler.kafka.KafkaMessageSender;
import com.example.statshandler.service.VisitStatsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("")
public class ServerController {

    private final KafkaMessageSender kafkaMessageSender;
    private VisitStatsService visitStatsService;


    public ServerController(KafkaMessageSender kafkaMessageSender, VisitStatsService visitStatsService) {
        this.kafkaMessageSender = kafkaMessageSender;
        this.visitStatsService = visitStatsService;
    }

    @PostMapping("/test")
    public String test(@RequestBody RequestDto request) {

        Runnable runnable =
                () -> {
                    System.out.println("Start requestId: " + request.getRequestId() + "   text: " + request.getData());
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                        List<VisitStatsDTO> visitStats =objectMapper.readValue(request.getData(), new TypeReference<>(){});

                        List<KafkaDTO> map = visitStatsService.collectStats(visitStats);
                        System.out.println("Result map: " + map);
                        String json =  objectMapper.writeValueAsString(map);
                        kafkaMessageSender.send(request.getRequestId(), new KafkaMessage<>(json));

                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

//                    kafkaMessageSender.send(request.getRequestId(), new KafkaMessage<>(request.getData().toUpperCase()));
                   // kafkaMessageSender.send(request.getRequestId(), new KafkaMessage<>(map));
                    System.out.println("End requestId: " + request.getRequestId());
                };
        Thread thread = new Thread(runnable);
        thread.start();

        return "Ok!";
    }
}
