package com.example.diplom.serviceImp.statistics;

import com.example.diplom.dao.statistics.FrequencyAddToCartStatsRepository;
import com.example.diplom.domain.statistics.FrequencyAddToCartStats;
import com.example.diplom.dto.statistics.FrequencyAddToCartStatsDTO;
import com.example.diplom.mapper.ProductMapper;
import com.example.diplom.service.statistics.StatsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FrequencyAddToCartStatsServiceImpl implements StatsService<FrequencyAddToCartStats, FrequencyAddToCartStatsDTO>{
    private final FrequencyAddToCartStatsRepository repository;

    private final ProductMapper mapper = ProductMapper.MAPPER;

    public FrequencyAddToCartStatsServiceImpl(FrequencyAddToCartStatsRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean save(FrequencyAddToCartStats frequencyAddToCartStats) {
        try {
            repository.save(frequencyAddToCartStats);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    @Override
    public List<FrequencyAddToCartStatsDTO> getAll() {
        List<FrequencyAddToCartStatsDTO> dtos = new ArrayList<>();
        for (FrequencyAddToCartStats frequencyAddToCartStats : repository.findAll()) {
            dtos.add(FrequencyAddToCartStatsDTO.builder()
                    .product(frequencyAddToCartStats.getProduct())
                    .created(frequencyAddToCartStats.getCreated())
                    .build());
        }
        return dtos;
    }

    @Override
    public List<FrequencyAddToCartStatsDTO> getAllBuyProductName(String productName) {
        List<FrequencyAddToCartStatsDTO> dtos = new ArrayList<>();
        for (FrequencyAddToCartStats frequencyAddToCartStats : repository.getAllBuyProductName(productName)) {
            dtos.add(FrequencyAddToCartStatsDTO.builder()
                    .product(frequencyAddToCartStats.getProduct())
                    .created(frequencyAddToCartStats.getCreated())
                    .build());
        }
        return dtos;
    }
}
