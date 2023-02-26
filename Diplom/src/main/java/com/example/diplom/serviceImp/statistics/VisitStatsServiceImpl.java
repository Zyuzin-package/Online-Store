package com.example.diplom.serviceImp.statistics;

import com.example.diplom.dao.statistics.VisitStatsRepository;
import com.example.diplom.domain.statistics.VisitStats;
import com.example.diplom.dto.statistics.VisitStatsDTO;
import com.example.diplom.mapper.ProductMapper;
import com.example.diplom.service.ProductService;
import com.example.diplom.service.statistics.VisitStatsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VisitStatsServiceImpl implements VisitStatsService {
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
            System.out.println("\n\n" + visitStatsRepository.save(visitStats));

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
}
