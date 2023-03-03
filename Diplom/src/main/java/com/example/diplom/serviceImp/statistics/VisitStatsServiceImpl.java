package com.example.diplom.serviceImp.statistics;

import com.example.diplom.dao.statistics.VisitStatsRepository;
import com.example.diplom.domain.statistics.VisitStats;
import com.example.diplom.dto.ProductDTO;
import com.example.diplom.dto.statistics.VisitStatsDTO;
import com.example.diplom.mapper.ProductMapper;
import com.example.diplom.service.ProductService;
import com.example.diplom.service.statistics.StatsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public Map<LocalDateTime, Integer> calculateStatsByProductName(String title) {
        List<VisitStatsDTO> visitStatsDTOS = getAllBuyProductName(title);
        if (visitStatsDTOS.isEmpty() || visitStatsDTOS == null) {
            return null;
        }

        visitStatsDTOS.sort(Comparator.comparing(VisitStatsDTO::getCreated));
        LocalDateTime temp = visitStatsDTOS.get(0).getCreated();
        SortedMap<LocalDateTime, Integer> resultMap = new TreeMap<>();

        int amount = 0;
        for (VisitStatsDTO v : visitStatsDTOS) {
            if (temp.getDayOfYear() == (v.getCreated().getDayOfYear())) {
                amount += 1;
            } else {
                amount = 1;
                temp = v.getCreated();
            }
            resultMap.put(v.getCreated(), amount);
        }
        resultMap.put(temp, amount);
        return resultMap;

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
    @Override
    public List<String> getUniqueDates(){
        return visitStatsRepository.getUniqueDates();
    }
    @Override
    public Integer getCountByDateAndProductId(LocalDateTime l, Long id){
        var count = visitStatsRepository.getVisitCountByDateAndProductId(l, id);
        if (count == null) {
            return 0;
        }
        return count;
    }
}
