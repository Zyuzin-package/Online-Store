package com.example.diplom.serviceImp.statistics;

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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
@Service
public class CollectStatsService {

    private final StatsService<BuyStats, BuyStatsDTO> buyStatsService;
    private final StatsService<FrequencyAddToCartStats, FrequencyAddToCartStatsDTO> frequencyAddToCartStatsService;
    private final StatsService<VisitStats, VisitStatsDTO> visitStatsService;
    private final ProductService productService;

    public CollectStatsService(StatsService<BuyStats, BuyStatsDTO> buyStatsService, StatsService<FrequencyAddToCartStats, FrequencyAddToCartStatsDTO> frequencyAddToCartStatsService, StatsService<VisitStats, VisitStatsDTO> visitStatsService, ProductService productService) {
        this.buyStatsService = buyStatsService;
        this.frequencyAddToCartStatsService = frequencyAddToCartStatsService;
        this.visitStatsService = visitStatsService;
        this.productService = productService;
    }

    public Map<String,Map<LocalDateTime,Integer>> collectStats() {
        List<ProductDTO> productList = productService.getAll();
        Map<String,Map<LocalDateTime,Integer>> kork = new HashMap<>();
        ProductDTO p = productService.getProductByName("Meat1");
//        for (ProductDTO p:productList){
            if(visitStatsService.calculateStatsByProductName(p.getTitle()) != null) {
            Map<LocalDateTime,Integer> temp = visitStatsService.calculateStatsByProductName(p.getTitle());
                kork.put(p.getTitle(), temp);
            }
//        }

        return kork;
    }
}
