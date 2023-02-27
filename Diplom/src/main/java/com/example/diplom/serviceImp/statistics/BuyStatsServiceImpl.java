package com.example.diplom.serviceImp.statistics;

import com.example.diplom.dao.statistics.BuyStatsRepository;
import com.example.diplom.domain.statistics.BuyStats;
import com.example.diplom.dto.statistics.BuyStatsDTO;
import com.example.diplom.dto.statistics.FrequencyAddToCartStatsDTO;
import com.example.diplom.mapper.ProductMapper;
import com.example.diplom.service.ProductService;
import com.example.diplom.service.statistics.StatsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class BuyStatsServiceImpl implements StatsService<BuyStats, BuyStatsDTO> {

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

    @Override
    public List<BuyStatsDTO> getAllBuyProductName(String productName) {
        List<BuyStatsDTO> dtos = new ArrayList<>();
        for (BuyStats buyStats : buyStatsRepository.getAllBuyProductName(productName)) {
            dtos.add(BuyStatsDTO.builder()
                    .product(mapper.fromProduct(productService.findProductById(buyStats.getProduct_id())))
                    .created(buyStats.getCreated())
                    .amount(buyStats.getAmount())
                    .build());
        }
        return dtos;
    }

    @Override
    public Map<LocalDateTime, Integer> calculateStatsByProductName(String title) {
        List<BuyStatsDTO> buyStatsDTOS = getAllBuyProductName(title);

        buyStatsDTOS.sort(Comparator.comparing(BuyStatsDTO::getCreated));

        LocalDateTime temp = buyStatsDTOS.get(0).getCreated();
        SortedMap<LocalDateTime, Integer> resultMap = new TreeMap<>();

        int amount = 0;
        for (BuyStatsDTO v : buyStatsDTOS) {
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
