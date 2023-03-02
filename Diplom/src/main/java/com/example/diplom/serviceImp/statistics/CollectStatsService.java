package com.example.diplom.serviceImp.statistics;

import com.example.diplom.dao.statistics.BuyStatsRepository;
import com.example.diplom.dao.statistics.FrequencyAddToCartStatsRepository;
import com.example.diplom.dao.statistics.VisitStatsRepository;
import com.example.diplom.dto.ProductDTO;
import com.example.diplom.service.ProductService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CollectStatsService {
    private final ProductService productService;
    private final VisitStatsRepository visitStatsRepository;
    private final BuyStatsRepository buyStatsRepository;
    private final FrequencyAddToCartStatsRepository frequencyAddToCartStatsRepository;

    public CollectStatsService(ProductService productService, VisitStatsRepository visitStatsRepository, BuyStatsRepository buyStatsRepository, FrequencyAddToCartStatsRepository frequencyAddToCartStatsRepository) {
        this.productService = productService;
        this.visitStatsRepository = visitStatsRepository;
        this.buyStatsRepository = buyStatsRepository;
        this.frequencyAddToCartStatsRepository = frequencyAddToCartStatsRepository;
    }

    public Map<LocalDateTime, List<Integer>> collectVisitStats() {
        List<ProductDTO> productList = productService.getAll();
        productList.sort(Comparator.comparing(ProductDTO::getId));
        List<LocalDateTime> localDateTimes = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (String s : visitStatsRepository.getUniqueDates()) {
            LocalDateTime localDateTime = LocalDateTime.parse(s.substring(0, s.indexOf(".") - 3), formatter);
            localDateTimes.add(localDateTime);
        }
        Map<LocalDateTime, List<Integer>> temp = new HashMap<>();
        for (LocalDateTime l : localDateTimes) {
            List<Integer> counts = new ArrayList<>();
            for (ProductDTO p : productList) {
                counts.add(getCountByDateAndProductIdFromVisitStats(l, p.getId()));
            }
            temp.put(l, counts);
        }
        return temp;
    }

    public Integer getCountByDateAndProductIdFromVisitStats(LocalDateTime l, Long productId) {
        var count = visitStatsRepository.getVisitCountByDateAndProductId(l, productId);
        if (count == null) {
            return 0;
        }
        return count;
    }

    public Map<LocalDateTime, List<Integer>> collectBuyStats() {
        List<ProductDTO> productList = productService.getAll();
        productList.sort(Comparator.comparing(ProductDTO::getId));
        List<LocalDateTime> localDateTimes = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (String s : buyStatsRepository.getUniqueDates()) {
            LocalDateTime localDateTime = LocalDateTime.parse(s.substring(0, s.indexOf(".") - 3), formatter);
            localDateTimes.add(localDateTime);
        }
        Map<LocalDateTime, List<Integer>> temp = new HashMap<>();
        for (LocalDateTime l : localDateTimes) {
            List<Integer> counts = new ArrayList<>();
            for (ProductDTO p : productList) {
                counts.add(getCountByDateAndProductIdFromBuyStats(l, p.getId()));
            }
            temp.put(l, counts);
        }
        return temp;
    }

    public Integer getCountByDateAndProductIdFromBuyStats(LocalDateTime l, Long productId) {
        var count = buyStatsRepository.getBuyCountByDateAndProductId(l, productId);
        if (count == null) {
            return 0;
        }
        return count;
    }


    public Map<LocalDateTime, List<Integer>> collectFrequencyStats() {
        List<ProductDTO> productList = productService.getAll();
        productList.sort(Comparator.comparing(ProductDTO::getId));
        List<LocalDateTime> localDateTimes = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (String s : frequencyAddToCartStatsRepository.getUniqueDates()) {
            LocalDateTime localDateTime = LocalDateTime.parse(s.substring(0, s.indexOf(".") - 3), formatter);
            localDateTimes.add(localDateTime);
        }
        Map<LocalDateTime, List<Integer>> temp = new HashMap<>();
        for (LocalDateTime l : localDateTimes) {
            List<Integer> counts = new ArrayList<>();
            for (ProductDTO p : productList) {
                counts.add(getCountByDateAndProductIdFromFrequencyStats(l, p.getId()));
            }
            temp.put(l, counts);
        }
        System.out.println("\n\nTEMP: "+temp);
        return temp;
    }

    public Integer getCountByDateAndProductIdFromFrequencyStats(LocalDateTime l, Long productId) {
        var count = frequencyAddToCartStatsRepository.getFrequencyCountByDateAndProductId(l, productId);
        if (count == null) {
            return 0;
        }
        return count;
    }
}
