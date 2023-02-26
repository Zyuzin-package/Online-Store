package com.example.diplom.service.statistics;

import com.example.diplom.domain.statistics.FrequencyAddToCartStats;
import com.example.diplom.domain.statistics.VisitStats;
import com.example.diplom.dto.statistics.VisitStatsDTO;

import java.util.List;


public interface VisitStatsService {
    boolean save(VisitStats visitStats);
    List<VisitStatsDTO> getAll();
}
