package com.example.diplom.serviceImp.statistics;

import com.example.diplom.dao.statistics.VisitStatsRepository;

import com.example.models.domain.statistics.*;
import com.example.models.dto.statistics.*;
import com.example.models.dto.*;
import com.example.models.domain.*;


import com.example.diplom.exception.MicroserviceError;
import com.example.diplom.mapper.ProductMapper;
import com.example.diplom.service.ProductService;
import com.example.diplom.service.statistics.StatsService;
import com.example.diplom.serviceHandler.MessageHandler;
import org.json.simple.parser.ParseException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Semaphore;

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
        List<VisitStats> temp = visitStatsRepository.getAllBuyProductName(productName);

        if (temp != null || !temp.isEmpty()) {
            for (VisitStats vs : temp) {
                visitStatsDTOS.add(VisitStatsDTO.builder()
                        .product(mapper.fromProduct(productService.findProductById(vs.getProduct_id())))
                        .created(vs.getCreated())
                        .build());
            }
            return visitStatsDTOS;
        }
        return null;
    }




    @Override
    public Map<LocalDateTime, List<Integer>> collectStats() throws MicroserviceError {

        return null;
//        productList.sort(Comparator.comparing(ProductDTO::getId));
//        List<LocalDateTime> localDateTimes = new ArrayList<>();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//        for (String s : getUniqueDates()) {
//            LocalDateTime localDateTime = LocalDateTime.parse(s.substring(0, s.indexOf(".") - 3), formatter);
//            localDateTimes.add(localDateTime);
//        }
//        Map<LocalDateTime, List<Integer>> temp = new HashMap<>();
//        for (LocalDateTime l : localDateTimes) {
//            List<Integer> counts = new ArrayList<>();
//            for (ProductDTO p : productList) {
//                counts.add(getCountByDateAndProductId(l, p.getId()));
//            }
//            temp.put(l, counts);
//        }
//        return temp;
    }

    @Override
    public List<String> getUniqueDates() {
        return visitStatsRepository.getUniqueDates();
    }

    @Override
    public Integer getCountByDateAndProductId(LocalDateTime l, Long id) {
        var count = visitStatsRepository.getVisitCountByDateAndProductId(l, id);
        if (count == null) {
            return 0;
        }
        return count;
    }
}
