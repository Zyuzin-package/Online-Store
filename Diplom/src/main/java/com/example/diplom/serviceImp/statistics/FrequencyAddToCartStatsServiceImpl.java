package com.example.diplom.serviceImp.statistics;

import com.example.diplom.dao.statistics.FrequencyAddToCartStatsRepository;

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
public class FrequencyAddToCartStatsServiceImpl implements StatsService<FrequencyAddToCartStats, FrequencyAddToCartStatsDTO> {
    private final FrequencyAddToCartStatsRepository repository;
    private final ProductService productService;

    private final ProductMapper mapper = ProductMapper.MAPPER;
    private final SyncKafkaService<FrequencyAddToCartStatsDTO> syncKafkaService;


    public FrequencyAddToCartStatsServiceImpl(FrequencyAddToCartStatsRepository repository, ProductService productService, SyncKafkaService<FrequencyAddToCartStatsDTO> syncKafkaService) {
        this.repository = repository;
        this.productService = productService;
        this.syncKafkaService = syncKafkaService;
    }

    @Override
    public boolean save(FrequencyAddToCartStats frequencyAddToCartStats) {
        try {
            repository.save(frequencyAddToCartStats);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    @Override
    public List<FrequencyAddToCartStatsDTO> getAll() {
        List<FrequencyAddToCartStatsDTO> dtos = new ArrayList<>();
        for (FrequencyAddToCartStats frequencyAddToCartStats : repository.findAll()) {
            dtos.add(FrequencyAddToCartStatsDTO.builder()
                    .product(
                            ProductDTO.builder()
                                    .id(frequencyAddToCartStats.getProduct().getId())
                                    .description(frequencyAddToCartStats.getProduct().getDescription())
                                    .image(frequencyAddToCartStats.getProduct().getImage())
                                    .price(frequencyAddToCartStats.getProduct().getPrice())
                                    .title(frequencyAddToCartStats.getProduct().getTitle())
                                    .build()
                    )
                    .created(frequencyAddToCartStats.getCreated())
                    .build());
        }
        return dtos;
    }

    @Override
    public List<FrequencyAddToCartStatsDTO> getAllBuyProductName(String productName) {
        List<FrequencyAddToCartStatsDTO> dtos = new ArrayList<>();
        for (FrequencyAddToCartStats frequencyAddToCartStats : repository.getAllBuyProductName(productName)) {
            dtos.add(FrequencyAddToCartStatsDTO.builder()
                    .product(
                            ProductDTO.builder()
                                    .id(frequencyAddToCartStats.getProduct().getId())
                                    .description(frequencyAddToCartStats.getProduct().getDescription())
                                    .image(frequencyAddToCartStats.getProduct().getImage())
                                    .price(frequencyAddToCartStats.getProduct().getPrice())
                                    .title(frequencyAddToCartStats.getProduct().getTitle())
                                    .build()
                    )
                    .created(frequencyAddToCartStats.getCreated())
                    .build());
        }
        return dtos;
    }

    @Override
    public List<String> getUniqueDates() {
        return repository.getUniqueDates();
    }

    @Override
    public Integer getCountByDateAndProductId(LocalDateTime l, Long id) {
        var count = repository.getFrequencyCountByDateAndProductId(l, id);
        if (count == null) {
            return 0;
        }
        return count;
    }

    @Override
    public String collectStats() {
        try {
            List<ProductDTO> productList = productService.getAll();
            List<FrequencyAddToCartStats> frequencyAddToCartStats = repository.findAll();

            List<FrequencyAddToCartStatsDTO> frequencyAddToCartStatsDTO = new ArrayList<>();
            for (FrequencyAddToCartStats v : frequencyAddToCartStats) {
                Optional<ProductDTO> product = productList.stream().filter(id -> Objects.equals(v.getProduct().getId(), id.getId())).findFirst();
                product.ifPresent(productDTO -> frequencyAddToCartStatsDTO.add(
                        FrequencyAddToCartStatsDTO.builder()
                                .id(v.getId())
                                .created(v.getCreated())
                                .product(ProductDTO.builder()
                                        .id(productDTO.getId())
                                        .description(productDTO.getDescription())
                                        .image(productDTO.getImage())
                                        .price(productDTO.getPrice())
                                        .title(productDTO.getTitle())
                                        .build()
                                )
                                .build()
                ));
            }

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));

            List<KafkaDTO> kafkaDTOS = objectMapper.readValue(syncKafkaService.get(frequencyAddToCartStatsDTO), new TypeReference<>() {
            });
            Map<String, List<Integer>> result = new HashMap<>();
            for (KafkaDTO k : kafkaDTOS) {
                result.put(k.getLocalDate().toString(), k.getCounts());
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
