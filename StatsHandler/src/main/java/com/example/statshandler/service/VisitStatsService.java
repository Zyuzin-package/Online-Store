package com.example.statshandler.service;



import com.example.models.domain.statistics.VisitStats;
import com.example.models.dto.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class VisitStatsService {

    public Map<LocalDateTime, List<Integer>> collectStats(List<VisitStats> visitStats, List<ProductDTO> productDTOList) {



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
    return null;
    }

    public Integer getCountByDateAndProductId(LocalDateTime l, Long id) {
        return 0;
    }

}
