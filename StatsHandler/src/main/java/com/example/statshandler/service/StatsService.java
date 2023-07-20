package com.example.statshandler.service;


import com.example.models.dto.*;
import com.example.models.kafka.model.KafkaDTO;
import com.example.statshandler.model.StatsDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class StatsService {
    private List<ProductDTO> productDTOList;

    public List<KafkaDTO> collectStats(List<StatsDTO> visitStats) {
        Map<LocalDate, List<ProductDTO>> localDateToProductListMap = new HashMap<>();

        for (StatsDTO v : visitStats) {
            if (!localDateToProductListMap.containsKey(v.getCreated().toLocalDate())) {
                List<ProductDTO> productDTOList = new ArrayList<>();
                productDTOList.add(v.getProduct());
                localDateToProductListMap.put(LocalDate.from(v.getCreated()), productDTOList);
            } else {
                localDateToProductListMap.get(v.getCreated().toLocalDate()).add(v.getProduct());
            }
        }

        List<KafkaDTO> kafkaDTOS = new ArrayList<>();
        for (LocalDate l : localDateToProductListMap.keySet()) {
            Map<Long, ProductDTO> uniqueProducts = localDateToProductListMap.get(l).stream()
                    .distinct()
                    .collect(Collectors.toMap(ProductDTO::getId, v -> v));
            Map<ProductDTO, Integer> countsMap = new HashMap<>();

            for (Long p : uniqueProducts.keySet()) {
                countsMap.put(uniqueProducts.get(p), 0);
            }
            for (ProductDTO p : localDateToProductListMap.get(l)) {
                for (ProductDTO pd: countsMap.keySet()){
                    if (p.equals(pd)) {
                        countsMap.put(pd, countsMap.get(p) + 1);
                    }
                }
            }

            List<Integer> counts = new ArrayList<>();
            for (Long i : uniqueProducts.keySet()) {
                counts.add(countsMap.get(uniqueProducts.get(i)));
            }
            for (int i = 1 ; i<= productDTOList.size()-uniqueProducts.size();i++){
                counts.add(0);
            }
            kafkaDTOS.add(new KafkaDTO(l, counts));
            System.out.println(kafkaDTOS);
        }

        return kafkaDTOS;
    }

    public void setProductDTOList(List<ProductDTO> productDTOList) {
        this.productDTOList = productDTOList;
    }
}
