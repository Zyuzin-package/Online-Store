package com.example.diplom.serviceImp.statistics;

import com.example.diplom.dao.statistics.BuyStatsRepository;

import com.example.models.domain.statistics.*;
import com.example.models.dto.statistics.*;
import com.example.models.dto.*;
import com.example.models.domain.*;


import com.example.diplom.mapper.ProductMapper;
import com.example.diplom.service.ProductService;
import com.example.diplom.service.statistics.StatsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public List<String> getUniqueDates() {
        return buyStatsRepository.getUniqueDates();
    }

    @Override
    public Integer getCountByDateAndProductId(LocalDateTime l, Long id) {
        var count = buyStatsRepository.getBuyCountByDateAndProductId(l, id);
        if (count == null) {
            return 0;
        }
        return count;
    }
    @Override
    public String collectStats() {
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
//        return temp;
        return null;
    }


}
