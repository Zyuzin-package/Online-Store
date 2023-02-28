package com.example.diplom.serviceImp.statistics;

import com.example.diplom.domain.statistics.BuyStats;
import com.example.diplom.domain.statistics.FrequencyAddToCartStats;
import com.example.diplom.domain.statistics.VisitStats;
import com.example.diplom.dto.statistics.BuyStatsDTO;
import com.example.diplom.dto.statistics.FrequencyAddToCartStatsDTO;
import com.example.diplom.dto.statistics.VisitStatsDTO;
import com.example.diplom.service.statistics.StatsService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CollectStatsService {

    private final StatsService<BuyStats, BuyStatsDTO> buyStatsService;
    private final StatsService<FrequencyAddToCartStats, FrequencyAddToCartStatsDTO> frequencyAddToCartStatsService;
    private final StatsService<VisitStats, VisitStatsDTO> visitStatsService;

    public CollectStatsService(StatsService<BuyStats, BuyStatsDTO> buyStatsService, StatsService<FrequencyAddToCartStats, FrequencyAddToCartStatsDTO> frequencyAddToCartStatsService, StatsService<VisitStats, VisitStatsDTO> visitStatsService) {
        this.buyStatsService = buyStatsService;
        this.frequencyAddToCartStatsService = frequencyAddToCartStatsService;
        this.visitStatsService = visitStatsService;
    }

//    public Map<LocalDateTime, Integer[]> collectStats(String title) {
//        Map<LocalDateTime, Integer> visit = visitStatsService.calculateStatsByProductName(title);
//        Map<LocalDateTime, Integer> buyStats = buyStatsService.calculateStatsByProductName(title);
//        Map<LocalDateTime, Integer> frequency = frequencyAddToCartStatsService.calculateStatsByProductName(title);
//        int[] arr = {visit.keySet().size(), buyStats.keySet().size(), frequency.keySet().size()};
//        int max = arr[0];
//        for (int i = 1; i < arr.length; i++) {
//            if (arr[i] > max) {
//                max = i;
//            }
//        }
//        List<LocalDateTime> visitKeys = new ArrayList<LocalDateTime>(visit.keySet());
//        List<LocalDateTime> buyStatsKeys = new ArrayList<LocalDateTime>(buyStats.keySet());
//        List<LocalDateTime> frequencyKeys = new ArrayList<LocalDateTime>(frequency.keySet());
//        for (int i = 0; i < arr[max]; i++) {
//            LocalDateTime temp = visitKeys.get
//        }
//
//        return null;
//    }
}
