package com.example.diplom.service;

import com.example.diplom.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    List<OrderDTO> getAll();
    boolean save(OrderDTO orderDTO);
    OrderDTO findOrderById(Long id);
}
