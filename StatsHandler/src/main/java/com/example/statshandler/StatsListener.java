package com.example.statshandler;


import com.example.statshandler.domain.statistics.VisitStats;
import com.example.statshandler.dto.ProductDTO;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StatsListener {

    @KafkaListener(id = "myId1", topics = "stats")
    public void listenVisitStats(String in) {
        if (null != in && !in.isEmpty()) {
            try {
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(in);
                System.out.println(jsonObject.get("visitStats"));
                System.out.println(jsonObject.get("productDTOList"));

//                List<VisitStats> visitStats = jsonObject.get("visitStats");
//                List<ProductDTO> productDTOList = jsonObject.get("productDTOList");


            } catch (ParseException exception) {
                System.out.println(in);
                System.out.println(exception);
            }
        }
    }

}
