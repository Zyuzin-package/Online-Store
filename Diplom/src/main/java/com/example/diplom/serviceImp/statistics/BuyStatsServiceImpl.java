package com.example.diplom.serviceImp.statistics;

import com.example.diplom.dao.statistics.BuyStatsRepository;

import com.example.diplom.kafka.service.SyncKafkaService;
import com.example.models.domain.statistics.*;
import com.example.models.dto.statistics.*;
import com.example.models.dto.*;
import com.example.models.domain.*;


import com.example.diplom.mapper.ProductMapper;
import com.example.diplom.service.ProductService;
import com.example.diplom.service.statistics.StatsService;
import com.example.models.kafka.model.KafkaDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeoutException;

@Service
public class BuyStatsServiceImpl implements StatsService<BuyStats, BuyStatsDTO> {

    private final BuyStatsRepository buyStatsRepository;
    private final ProductService productService;
    private final ProductMapper mapper = ProductMapper.MAPPER;
    private final SyncKafkaService<VisitStatsDTO> syncKafkaService;

    public BuyStatsServiceImpl(BuyStatsRepository buyStatsRepository, ProductService productService, SyncKafkaService<VisitStatsDTO> syncKafkaService) {
        this.buyStatsRepository = buyStatsRepository;
        this.productService = productService;
        this.syncKafkaService = syncKafkaService;
    }

    @Override
    public boolean save(BuyStats buyStats) {
        try {
            buyStatsRepository.save(buyStats);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    @Override
    public List<BuyStatsDTO> getAll() {
        List<BuyStatsDTO> dtos = new ArrayList<>();
        for (BuyStats buyStats : buyStatsRepository.findAll()) {
            dtos.add(BuyStatsDTO.builder()
                    .product(mapper.fromProduct(productService.findProductById(buyStats.getProduct_id())))
                    .created(buyStats.getCreated())
                    .amount(buyStats.getAmount())
                    .build());
        }
        return dtos;
    }

    @Override
    public List<BuyStatsDTO> getAllBuyProductName(String productName) {
        List<BuyStatsDTO> dtos = new ArrayList<>();
        for (BuyStats buyStats : buyStatsRepository.getAllBuyProductName(productName)) {
            dtos.add(BuyStatsDTO.builder()
                    .product(mapper.fromProduct(productService.findProductById(buyStats.getProduct_id())))
                    .created(buyStats.getCreated())
                    .amount(buyStats.getAmount())
                    .build());
        }
        return dtos;
    }


    @Override
    public List<String> getUniqueDates() {
        return buyStatsRepository.getUniqueDates();
    }

    @Override
    public Integer getCountByDateAndProductId(LocalDateTime l, Long id) {
        var count = buyStatsRepository.getBuyCountByDateAndProductId(l, id);
        if (count == null) {
            return 0;
        }
        return count;
    }
    @Override
    public String collectStats() {

        try {
            List<ProductDTO> productList = productService.getAll();
            List<BuyStats> visitStats = buyStatsRepository.findAll();

            List<VisitStatsDTO> visitStatsDTOS = new ArrayList<>();
            for (BuyStats v : visitStats) {
                Optional<ProductDTO> product = productList.stream().filter(id -> Objects.equals(v.getProduct_id(), id.getId())).findFirst();
                product.ifPresent(productDTO -> visitStatsDTOS.add(
                        VisitStatsDTO.builder()
                                .id(v.getId())
                                .created(v.getCreated())
                                .product(productDTO)
                                .build()
                ));
            }

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));

            List<KafkaDTO> kafkaDTOS = objectMapper.readValue(syncKafkaService.get(visitStatsDTOS), new TypeReference<>(){});
            Map<String,List<Integer>> result = new HashMap<>();
            for (KafkaDTO k : kafkaDTOS){
                result.put(k.getLocalDate().toString(),k.getCounts());
            }
            return JSONValue.toJSONString(result);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return null;

    }

}
