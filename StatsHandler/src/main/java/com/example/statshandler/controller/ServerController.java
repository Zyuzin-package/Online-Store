package com.example.statshandler.controller;

import com.example.models.dto.ProductDTO;
import com.example.models.kafka.model.KafkaDTO;
import com.example.models.kafka.model.KafkaMessage;
import com.example.models.kafka.model.RequestDto;
import com.example.statshandler.kafka.KafkaMessageSender;
import com.example.statshandler.model.StatsDTO;
import com.example.statshandler.service.StatsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("")
public class ServerController {

    private final KafkaMessageSender kafkaMessageSender;
    private StatsService statsService;


    public ServerController(KafkaMessageSender kafkaMessageSender, StatsService statsService) {
        this.kafkaMessageSender = kafkaMessageSender;
        this.statsService = statsService;
    }

    @PostMapping("/test")
    public String test(@RequestBody RequestDto request) {

        Runnable runnable =
                () -> {
                    System.out.println("Start requestId: " + request.getRequestId() + "   text: " + request.getData());
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                        List<StatsDTO> visitStats = new ArrayList<>();
                        try {
                            visitStats = objectMapper.readValue(request.getData(), new TypeReference<>(){});
                            List<KafkaDTO> map = statsService.collectStats(visitStats);
                            System.out.println("Result map: " + map);
                            String json =  objectMapper.writeValueAsString(map);
                            kafkaMessageSender.send(request.getRequestId(), new KafkaMessage<>(json));
                        } catch (Exception e){
                            List<ProductDTO> products = objectMapper.readValue(request.getData(), new TypeReference<>(){});
                            statsService.setProductDTOList(products);

                        }


                        List<KafkaDTO> map = statsService.collectStats(visitStats);
                        System.out.println("Result map: " + map);
                        String json =  objectMapper.writeValueAsString(map);
                        kafkaMessageSender.send(request.getRequestId(), new KafkaMessage<>(json));

                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    System.out.println("End requestId: " + request.getRequestId());
                };
        Thread thread = new Thread(runnable);
        thread.start();

        return "Ok!";
    }
}
