package com.example.diplom.service.statistics;


import java.util.List;

public interface StatsService<T,D> {

    boolean save(T visitStats);
    List<D> getAll();

    List<D> getAllBuyProductName(String productName);
}
