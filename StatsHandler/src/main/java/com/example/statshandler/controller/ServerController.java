package com.example.statshandler.controller;

import com.example.statshandler.kafka.KafkaMessageSender;
import com.example.statshandler.kafka.model.KafkaMessage;
import com.example.statshandler.kafka.model.RequestDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("")
public class ServerController {

    private final KafkaMessageSender kafkaMessageSender;

    public ServerController(KafkaMessageSender kafkaMessageSender) {
        this.kafkaMessageSender = kafkaMessageSender;
    }

    @PostMapping("/test")
    public String test(@RequestBody RequestDto request) {

        Runnable runnable =
                () -> {
                    System.out.println("Start requestId: " + request.getRequestId() + "   text: " + request.getData());

                    try {
                        int sleepMs = ThreadLocalRandom.current().nextInt(0, 10000 + 1);
                        System.out.println("RequestId: " + request.getRequestId() + " sleep: " + sleepMs + "ms");
                        Thread.sleep(sleepMs);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    kafkaMessageSender.send(request.getRequestId(), new KafkaMessage<>(request.getData().toUpperCase()));

                    System.out.println("End requestId: " + request.getRequestId());
                };
        Thread thread = new Thread(runnable);
        thread.start();

        return "Ok!";
    }
}
