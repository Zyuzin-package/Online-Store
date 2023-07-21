package com.example.diplom.serviceImp.statistics;

import com.example.diplom.dao.statistics.VisitStatsRepository;

import com.example.diplom.kafka.service.SyncKafkaService;
import com.example.models.domain.statistics.*;
import com.example.models.dto.statistics.*;
import com.example.models.dto.*;
import com.example.models.domain.*;


import com.example.diplom.exception.MicroserviceError;
import com.example.diplom.mapper.ProductMapper;
import com.example.diplom.service.ProductService;
import com.example.diplom.service.statistics.StatsService;
import com.example.models.kafka.model.KafkaDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeoutException;

@Service
public class VisitStatsServiceImpl implements StatsService<VisitStats, VisitStatsDTO> {
    private final VisitStatsRepository visitStatsRepository;
    private final ProductService productService;
    private final SyncKafkaService<VisitStatsDTO> syncKafkaService;
    private final ProductMapper mapper = ProductMapper.MAPPER;


    public VisitStatsServiceImpl(VisitStatsRepository visitStatsRepository, ProductService productService, SyncKafkaService<VisitStatsDTO> syncKafkaService) {
        this.visitStatsRepository = visitStatsRepository;
        this.productService = productService;
        this.syncKafkaService = syncKafkaService;
    }

    @Override
    public boolean save(VisitStats visitStats) {
        try {
            visitStatsRepository.save(visitStats);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    @Override
    public List<VisitStatsDTO> getAll() {
        List<VisitStatsDTO> visitStatsDTOS = new ArrayList<>();
        for (VisitStats vs : visitStatsRepository.findAll()) {
            visitStatsDTOS.add(VisitStatsDTO.builder()
                    .product(mapper.fromProduct(productService.findProductById(vs.getProduct_id())))
                    .created(vs.getCreated())
                    .build());
        }
        return visitStatsDTOS;
    }

    @Override
    public List<VisitStatsDTO> getAllBuyProductName(String productName) {
        List<VisitStatsDTO> visitStatsDTOS = new ArrayList<>();
        List<VisitStats> temp = visitStatsRepository.getAllBuyProductName(productName);

        if (temp != null || !temp.isEmpty()) {
            for (VisitStats vs : temp) {
                visitStatsDTOS.add(VisitStatsDTO.builder()
                        .product(mapper.fromProduct(productService.findProductById(vs.getProduct_id())))
                        .created(vs.getCreated())
                        .build());
            }
            return visitStatsDTOS;
        }
        return null;
    }


    @Override
    public String collectStats() throws MicroserviceError {
        try {
            List<ProductDTO> productList = productService.getAll();
            List<VisitStats> visitStats = visitStatsRepository.findAll();


            List<VisitStatsDTO> visitStatsDTOS = new ArrayList<>();
            for (VisitStats v : visitStats) {
                Optional<ProductDTO> product = productList.stream().filter(id -> Objects.equals(v.getProduct_id(), id.getId())).findFirst();
                product.ifPresent(productDTO -> visitStatsDTOS.add(
                        VisitStatsDTO.builder()
                                .id(v.getId())
                                .created(v.getCreated())
                                .product(productDTO)
                                .build()
                ));
            }
            System.out.println("Updated VSDTOs: "+ visitStatsDTOS);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));

            List<KafkaDTO> kafkaDTOS = objectMapper.readValue(syncKafkaService.get(visitStatsDTOS), new TypeReference<>() {
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



    @Override
    public List<String> getUniqueDates() {
        return visitStatsRepository.getUniqueDates();
    }

    @Override
    public Integer getCountByDateAndProductId(LocalDateTime l, Long id) {
        var count = visitStatsRepository.getVisitCountByDateAndProductId(l, id);
        if (count == null) {
            return 0;
        }
        return count;
    }
}
