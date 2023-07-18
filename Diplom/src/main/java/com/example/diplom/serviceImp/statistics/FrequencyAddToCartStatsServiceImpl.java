package com.example.diplom.serviceImp.statistics;

import com.example.diplom.dao.statistics.FrequencyAddToCartStatsRepository;

import com.example.models.domain.statistics.*;
import com.example.models.dto.statistics.*;
import com.example.models.dto.*;
import com.example.models.domain.*;


import com.example.diplom.mapper.ProductMapper;
import com.example.diplom.service.ProductService;
import com.example.diplom.service.statistics.StatsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class FrequencyAddToCartStatsServiceImpl implements StatsService<FrequencyAddToCartStats, FrequencyAddToCartStatsDTO>{
    private final FrequencyAddToCartStatsRepository repository;
    private final ProductService productService;

    private final ProductMapper mapper = ProductMapper.MAPPER;

    public FrequencyAddToCartStatsServiceImpl(FrequencyAddToCartStatsRepository repository, ProductService productService) {
        this.repository = repository;
        this.productService = productService;
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

    @Override
    public List<String> getUniqueDates() {
        return repository.getUniqueDates();
    }

    @Override
    public Integer getCountByDateAndProductId(LocalDateTime l, Long id) {
        var count = repository.getFrequencyCountByDateAndProductId(l, id);
        if (count == null) {
            return 0;
        }
        return count;
    }

    @Override
    public Map<LocalDateTime, List<Integer>> collectStats() {
            List<ProductDTO> productList = productService.getAll();
            productList.sort(Comparator.comparing(ProductDTO::getId));
            List<LocalDateTime> localDateTimes = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            for (String s : getUniqueDates()) {
                LocalDateTime localDateTime = LocalDateTime.parse(s.substring(0, s.indexOf(".") - 3), formatter);
                localDateTimes.add(localDateTime);
            }
            Map<LocalDateTime, List<Integer>> temp = new HashMap<>();
            for (LocalDateTime l : localDateTimes) {
                List<Integer> counts = new ArrayList<>();
                for (ProductDTO p : productList) {
                    counts.add(getCountByDateAndProductId(l, p.getId()));
                }
                temp.put(l, counts);
            }
            return temp;

    }
}
