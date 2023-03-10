package com.example.diplom.service.statistics;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface StatsService<T,D> {

    boolean save(T visitStats);
    List<D> getAll();

    List<D> getAllBuyProductName(String productName);

    Map<LocalDateTime,Integer> calculateStatsByProductName(String title);

    List<String> getUniqueDates();

    Integer getCountByDateAndProductId(LocalDateTime l, Long id);

    Map<LocalDateTime, List<Integer>> collectStats();
}
