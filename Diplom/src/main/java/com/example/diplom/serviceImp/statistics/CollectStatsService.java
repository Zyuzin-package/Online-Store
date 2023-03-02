package com.example.diplom.serviceImp.statistics;

import com.example.diplom.dao.statistics.VisitStatsRepository;
import com.example.diplom.domain.Product;
import com.example.diplom.domain.statistics.BuyStats;
import com.example.diplom.domain.statistics.FrequencyAddToCartStats;
import com.example.diplom.domain.statistics.VisitStats;
import com.example.diplom.dto.ProductDTO;
import com.example.diplom.dto.statistics.BuyStatsDTO;
import com.example.diplom.dto.statistics.FrequencyAddToCartStatsDTO;
import com.example.diplom.dto.statistics.VisitStatsDTO;
import com.example.diplom.service.ProductService;
import com.example.diplom.service.statistics.StatsService;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CollectStatsService {

    //private final StatsService<BuyStats, BuyStatsDTO> buyStatsService;
    //private final StatsService<FrequencyAddToCartStats, FrequencyAddToCartStatsDTO> frequencyAddToCartStatsService;
    //private final StatsService<VisitStats, VisitStatsDTO> visitStatsService;
    private final ProductService productService;
    private final VisitStatsRepository visitStatsRepository;

    public CollectStatsService(ProductService productService, VisitStatsRepository visitStatsRepository) {
        this.productService = productService;
        this.visitStatsRepository = visitStatsRepository;
    }

    //    public Map<String, Map<LocalDateTime, Integer>> collectStats() {
//        List<ProductDTO> productList = productService.getAll();
//        Map<String, Map<LocalDateTime, Integer>> kork = new HashMap<>();
//        //ProductDTO p = productService.getProductByName("Meat1");
//        for (ProductDTO p : productList) {
//            Map<LocalDateTime, Integer> temp = visitStatsService.calculateStatsByProductName(p.getTitle());
//            if (temp == null) {
//                continue;
//            }
//            kork.put(p.getTitle(), temp);
//        }
//
//        return kork;
//    }
    /*
        for (ProductDTO p: productList){
            Map<LocalDateTime, Integer[]> temp = new TreeMap<>();
            for (LocalDateTime l:localDateTimes){
                temp.put(l,getCountByDateAndProductId(l,p.getId()));
            }
            kork.put(p.getTitle(),temp);
        }
     */
//Map<String, Map<LocalDateTime, Integer[]>>
    public Map<LocalDateTime, List<Integer>> collectStats2() {
        List<ProductDTO> productList = productService.getAll();
        productList.sort(Comparator.comparing(ProductDTO::getId));
        List<LocalDateTime> localDateTimes = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (String s : visitStatsRepository.getUniqueDates()) {
            localDateTimes.add(LocalDateTime.parse(s.substring(0, s.length() - 2), dateTimeFormatter));
        }
        Map<LocalDateTime, List<Integer>> temp = new HashMap<>();
        for (LocalDateTime l : localDateTimes) {
            List<Integer> counts = new ArrayList<>();
            for (ProductDTO p : productList) {
                counts.add(getCountByDateAndProductId(l, p.getId()));
            }
            temp.put(l, counts);
        }
        System.out.println(temp);
        return temp;
    }

    public Integer getCountByDateAndProductId(LocalDateTime l, Long productId) {
        var count = visitStatsRepository.getVisitCountByDateAndProductId(l, productId);
        if (count == null) {
            return 0;
        }
        return count;
    }
}
