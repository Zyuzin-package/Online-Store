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
    private final SyncKafkaService syncKafkaService;
    private final ProductMapper mapper = ProductMapper.MAPPER;

    public VisitStatsServiceImpl(VisitStatsRepository visitStatsRepository, ProductService productService, SyncKafkaService syncKafkaService) {
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

            System.out.println("visitStats: " + visitStats);

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

//            for (VisitStats v: visitStats){
//
//                ProductDTO productDTO = new ProductDTO();
//                for (ProductDTO p: productList){
//                    if (p.getId().equals(v.getProduct_id())){
//                        productDTO = p;
//                    }
//                }
//                visitStatsDTOS.add( VisitStatsDTO.builder()
//                                .id(v.getProduct_id())
//                                .created(v.getCreated())
//                                .product(productDTO)
//                                .build());
//            }
            System.out.println("\nvisitStatsDTOS: " + visitStatsDTOS);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
//            Map<String, List<Integer>> map = objectMapper.readValue(syncKafkaService.get(visitStatsDTOS), new TypeReference<>(){});
//            Map<LocalDate, List<Integer>> result = new HashMap<>();
//
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//            for (String s : map.keySet()){
//                result.put(LocalDate.parse(s, formatter),map.get(s));
//            }
//
//            System.out.println("RESULT: "+ result);
            List<KafkaDTO> kafkaDTOS = objectMapper.readValue(syncKafkaService.get(visitStatsDTOS), new TypeReference<>(){});
            Map<String,List<Integer>> result = new HashMap<>();
            for (KafkaDTO k : kafkaDTOS){
                result.put(k.getLocalDate().toString(),k.getCounts());
            }
            System.out.println(JSONValue.toJSONString(result));
            return JSONValue.toJSONString(result);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return null;
        /*
            try {
            List<ProductDTO> productList = productService.getAll();
            List<LocalDate> localDateTimes = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (String s : getUniqueDates()) {
                LocalDate localDateTime = LocalDate.parse(s.substring(0, s.indexOf(" ")), formatter);
                localDateTimes.add(localDateTime);
            }
            System.out.println(productList+"\n"+localDateTimes);
            syncKafkaService.get(productList,localDateTimes);
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
         */
        /*
        List<ProductDTO> productList = productService.getAll();
        productList.sort(Comparator.comparing(ProductDTO::getId));
        List<LocalDateTime> localDateTimes = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (String s : getUniqueDates()) {
            LocalDateTime localDateTime = LocalDateTime.parse(s.substring(0, s.indexOf(".") - 3), formatter);
            localDateTimes.add(localDateTime);
        }
        Map<LocalDateTime, List<Integer>> temp = new HashMap<>();
        for (LocalDateTime l : localDateTimes) {
            List<Integer> counts = new ArrayList<>();
            for (ProductDTO p : productList) {
                counts.add(getCountByDateAndProductId(l, p.getId()));
            }
            temp.put(l, counts);
        }
        return temp;
         */
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
