package com.example.diplom.service.statistics;

import com.example.diplom.domain.statistics.BuyStats;
import com.example.diplom.dto.statistics.BuyStatsDTO;

import java.util.List;

public interface BuyStatsService {
    boolean save(BuyStats buyStats);
    List<BuyStatsDTO> getAll();
}
