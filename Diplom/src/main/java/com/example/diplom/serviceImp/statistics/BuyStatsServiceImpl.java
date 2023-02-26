package com.example.diplom.serviceImp.statistics;

import com.example.diplom.dao.statistics.BuyStatsRepository;
import com.example.diplom.domain.statistics.BuyStats;
import com.example.diplom.domain.statistics.FrequencyAddToCartStats;
import com.example.diplom.dto.statistics.BuyStatsDTO;
import com.example.diplom.dto.statistics.FrequencyAddToCartStatsDTO;
import com.example.diplom.mapper.ProductMapper;
import com.example.diplom.service.ProductService;
import com.example.diplom.service.statistics.BuyStatsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BuyStatsServiceImpl implements BuyStatsService {

    private final BuyStatsRepository buyStatsRepository;
    private final ProductService productService;
    private final ProductMapper mapper = ProductMapper.MAPPER;

    public BuyStatsServiceImpl(BuyStatsRepository buyStatsRepository, ProductService productService) {
        this.buyStatsRepository = buyStatsRepository;
        this.productService = productService;
    }

    @Override
    public boolean save(BuyStats buyStats) {
        try {
            buyStatsRepository.save(buyStats);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    @Override
    public List<BuyStatsDTO> getAll() {
        List<BuyStatsDTO> dtos = new ArrayList<>();
        for (BuyStats buyStats : buyStatsRepository.findAll()) {
            dtos.add(BuyStatsDTO.builder()
                    .product(mapper.fromProduct(productService.findProductById(buyStats.getProduct_id())))
                    .created(buyStats.getCreated())
                    .amount(buyStats.getAmount())
                    .build());
        }
        return dtos;
    }

}
