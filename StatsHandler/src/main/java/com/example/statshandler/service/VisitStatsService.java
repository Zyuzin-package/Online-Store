package com.example.statshandler.service;


import com.example.models.domain.statistics.VisitStats;
import com.example.models.dto.*;
import com.example.models.dto.statistics.VisitStatsDTO;
import com.example.models.kafka.model.KafkaDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class VisitStatsService {

    public List<KafkaDTO> collectStats(List<VisitStatsDTO> visitStats) {
        System.out.println(visitStats);
        Map<LocalDate, List<Integer>> map = new HashMap<>();
        Map<LocalDate, List<ProductDTO>> localDateToProductListMap = new HashMap<>();

        for (VisitStatsDTO v : visitStats) {

            if (!localDateToProductListMap.containsKey(v.getCreated().toLocalDate())) {
                List<ProductDTO> productDTOList = new ArrayList<>();
                productDTOList.add(v.getProduct());
                localDateToProductListMap.put(LocalDate.from(v.getCreated()), productDTOList);
            } else {
                localDateToProductListMap.get(v.getCreated().toLocalDate()).add(v.getProduct());
            }
        }
//        for (LocalDate l : localDateToProductListMap.keySet()) {
//            System.out.println(localDateToProductListMap.get(l) + "\n");
//        }
        System.out.println("localDateToProductListMap: \n" + localDateToProductListMap);

        System.out.println("\n\n\n");
        List<KafkaDTO> kafkaDTOS = new ArrayList<>();
        for (LocalDate l : localDateToProductListMap.keySet()) {
            Map<Long, ProductDTO> uniqueProducts = localDateToProductListMap.get(l).stream()
                    .distinct()
                    .collect(Collectors.toMap(ProductDTO::getId, v -> v));

            Map<ProductDTO, Integer> countsMap = new HashMap<>();
            for (ProductDTO p : localDateToProductListMap.get(l)) {
                countsMap.put(p, countsMap.containsKey(p) ? countsMap.get(p) + 1 : 1);
            }

            System.out.println("countsMap: " + countsMap+" ||||| "+l);
            List<Integer> counts = new ArrayList<>();
            for (Long i : uniqueProducts.keySet()){
                counts.add(countsMap.get(uniqueProducts.get(i)));
            }
            kafkaDTOS.add(new KafkaDTO(l,counts));
        }
//        for (LocalDate l : localDateToProductListMap.keySet()) {
//            Map<Long, ProductDTO> uniqueProducts = localDateToProductListMap.get(l).stream()
//                    .distinct()
//                    .collect(Collectors.toMap(ProductDTO::getId, v -> v));
//
//            Map<ProductDTO, Integer> countsMap = new HashMap<>();
//            for (ProductDTO p : localDateToProductListMap.get(l)) {
//                countsMap.put(p, countsMap.containsKey(p) ? countsMap.get(p) + 1 : 1);
//            }
//            System.out.println("countsMap: " + countsMap+" ||||| "+l);
//            List<Integer> counts = new ArrayList<>();
//            for (Long i : uniqueProducts.keySet()){
//                counts.add(countsMap.get(uniqueProducts.get(i)));
//            }
//            map.put(l,counts);
//        }


        /*
               for (LocalDate l : localDateToProductListMap.keySet()) {
            List<Integer> integerList = new ArrayList<>();
            ProductDTO temp = new ProductDTO();
            for (ProductDTO p : localDateToProductListMap.get(l)) {
                System.out.println(temp.equals(p) + "_" + count);
                if (!p.equals(temp)) {
                    System.out.println(p.getId() + " | " + temp.getId());
                    integerList.add(count);
                    count = 0;
                } else {
                    System.out.println("KORKA");
                    count++;
                }
                temp = p;
            }
            System.out.println("integerList" + integerList);
            map.put(l, integerList);
        }
         */
//        System.out.println("\nlocalDateToProductListMap: " + localDateToProductListMap);
//        System.out.println(localDateToProductListMap.keySet());
//        System.out.println(localDateToProductListMap.values());
        System.out.println("\n\nMAP : " + map);
//        visitStats.sort(Comparator.comparing(VisitStatsDTO::getCreated));
//        System.out.println("Sorted");
//        System.out.println(visitStats);

//        Map<LocalDate, List<Integer>> temp = new HashMap<>();
//        for (LocalDate l : dateList) {
//            List<Integer> counts = new ArrayList<>();
//            for (ProductDTO p : productDTOList) {
//                counts.add(getCountByDateAndProductId(l, p.getId()));
//            }
//            temp.put(l, counts);
//        }
        return kafkaDTOS;


//        productList.sort(Comparator.comparing(ProductDTO::getId));
//        List<LocalDateTime> localDateTimes = new ArrayList<>();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//        for (String s : data) {
//            LocalDateTime localDateTime = LocalDateTime.parse(s.substring(0, s.indexOf(".") - 3), formatter);
//            localDateTimes.add(localDateTime);
//        }
//        Map<LocalDateTime, List<Integer>> temp = new HashMap<>();
//        for (LocalDateTime l : localDateTimes) {
//            List<Integer> counts = new ArrayList<>();
//            for (ProductDTO p : productList) {
//                counts.add(getCountByDateAndProductId(l, p.getId()));
//            }
//            temp.put(l, counts);
//        }
//        return temp;

    }

    public Integer getCountByDateAndProductId(LocalDate l, Long id) {
        return 0;
    }

}
