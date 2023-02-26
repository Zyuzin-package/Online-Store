package com.example.diplom.service.statistics;

import com.example.diplom.domain.statistics.BuyStats;
import com.example.diplom.domain.statistics.FrequencyAddToCartStats;
import com.example.diplom.domain.statistics.VisitStats;
import com.example.diplom.dto.statistics.FrequencyAddToCartStatsDTO;

import java.util.List;

public interface FrequencyAddToCartStatsService {
    boolean save(FrequencyAddToCartStats frequencyAddToCartStats);
    List<FrequencyAddToCartStatsDTO> getAll();
}
