package com.example.diplom.controllers;

import com.example.diplom.domain.statistics.BuyStats;
import com.example.diplom.domain.statistics.FrequencyAddToCartStats;
import com.example.diplom.domain.statistics.VisitStats;
import com.example.diplom.dto.ProductDTO;
import com.example.diplom.dto.statistics.BuyStatsDTO;
import com.example.diplom.dto.statistics.FrequencyAddToCartStatsDTO;
import com.example.diplom.dto.statistics.VisitStatsDTO;
import com.example.diplom.exception.MicroserviceError;
import com.example.diplom.kafka.service.SyncKafkaService;
import com.example.diplom.service.ProductService;
import com.example.diplom.service.statistics.StatsService;
import org.json.simple.JSONValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.ConnectException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Controller
@RequestMapping("/test")
public class TestController {
    private final StatsService<VisitStats, VisitStatsDTO> visitStatsService;
    private final StatsService<BuyStats, BuyStatsDTO> buyStatsService;
    private final StatsService<FrequencyAddToCartStats, FrequencyAddToCartStatsDTO> frequencyAddToCartStatsService;
    private final SyncKafkaService syncKafkaService;
    private final ProductService productService;

    public TestController(StatsService<VisitStats, VisitStatsDTO> visitStatsService, StatsService<BuyStats, BuyStatsDTO> buyStatsService, StatsService<FrequencyAddToCartStats, FrequencyAddToCartStatsDTO> frequencyAddToCartStatsService, SyncKafkaService syncKafkaService, ProductService productService) {
        this.visitStatsService = visitStatsService;
        this.buyStatsService = buyStatsService;
        this.frequencyAddToCartStatsService = frequencyAddToCartStatsService;
        this.syncKafkaService = syncKafkaService;
        this.productService = productService;
    }

    @GetMapping("/stats")
    public String testStats(Model model, Principal principal) throws MicroserviceError {

        String visitStatsJson = JSONValue.toJSONString(visitStatsService.collectStats());
        String buyStatsJson = JSONValue.toJSONString(buyStatsService.collectStats());
        String frequencyStatsJson = JSONValue.toJSONString(frequencyAddToCartStatsService.collectStats());

        List<ProductDTO> productDTOList = productService.getAll();
        List<String> productsTitle = new ArrayList<>();
        for (ProductDTO p : productDTOList) {
            productsTitle.add(p.getTitle());
        }

        model.addAttribute("productsTitle", JSONValue.toJSONString(productsTitle));

        model.addAttribute("visitStatsJson", visitStatsJson.equals("{}") ? null : visitStatsJson);
        model.addAttribute("buyStatsJson", buyStatsJson.equals("{}") ? null : buyStatsJson);
        model.addAttribute("frequencyStatsJson", frequencyStatsJson.equals("{}") ? null : frequencyStatsJson);

        model.addAttribute("products", productDTOList);
        return "statistics";
    }

    @GetMapping("/test2")
    public ResponseEntity<String> test(String text) {
            System.out.println("GET test2");
            text = "Korka";
            String result = "";
            try {
                result = syncKafkaService.get(text);
                System.out.println("Result " + result);
            } catch (TimeoutException e) {
                System.out.println(e);
                return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
            }

            return ResponseEntity.ok(result);

    }
}
