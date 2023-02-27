package com.example.diplom.serviceImp.statistics;

import com.example.diplom.dao.statistics.VisitStatsRepository;
import com.example.diplom.domain.statistics.VisitStats;
import com.example.diplom.dto.statistics.VisitStatsDTO;
import com.example.diplom.mapper.ProductMapper;
import com.example.diplom.service.ProductService;
import com.example.diplom.service.statistics.StatsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class VisitStatsServiceImpl implements StatsService<VisitStats, VisitStatsDTO> {
    private final VisitStatsRepository visitStatsRepository;
    private final ProductService productService;
    private final ProductMapper mapper = ProductMapper.MAPPER;

    public VisitStatsServiceImpl(VisitStatsRepository visitStatsRepository, ProductService productService) {
        this.visitStatsRepository = visitStatsRepository;
        this.productService = productService;
    }

    @Override
    public boolean save(VisitStats visitStats) {
        try {
            visitStatsRepository.save(visitStats);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    @Override
    public List<VisitStatsDTO> getAll() {
        List<VisitStatsDTO> visitStatsDTOS = new ArrayList<>();
        for (VisitStats vs : visitStatsRepository.findAll()) {
            visitStatsDTOS.add(VisitStatsDTO.builder()
                    .product(mapper.fromProduct(productService.findProductById(vs.getProduct_id())))
                    .created(vs.getCreated())
                    .build());
        }
        return visitStatsDTOS;
    }

    @Override
    public List<VisitStatsDTO> getAllBuyProductName(String productName) {
        List<VisitStatsDTO> visitStatsDTOS = new ArrayList<>();
        for (VisitStats vs : visitStatsRepository.getAllBuyProductName(productName)) {
            visitStatsDTOS.add(VisitStatsDTO.builder()
                    .product(mapper.fromProduct(productService.findProductById(vs.getProduct_id())))
                    .created(vs.getCreated())
                    .build());
        }
        return visitStatsDTOS;
    }

    @Override
    public Map<LocalDateTime, Integer> calculateStatsByProductName(String title) {
        List<VisitStatsDTO> visitStatsDTOS = getAllBuyProductName(title);

        visitStatsDTOS.sort(Comparator.comparing(VisitStatsDTO::getCreated));

        LocalDateTime temp = visitStatsDTOS.get(0).getCreated();
        SortedMap<LocalDateTime, Integer> resultMap = new TreeMap<>();

        int amount = 0;
        for (VisitStatsDTO v : visitStatsDTOS) {
            if (temp.getDayOfYear() == (v.getCreated().getDayOfYear())) {
                amount+=1;
            } else {
                amount=1;
                temp=v.getCreated();
            }
            resultMap.put(v.getCreated(), amount);
        }
        resultMap.put(temp, amount);
        return resultMap;
    }
}
