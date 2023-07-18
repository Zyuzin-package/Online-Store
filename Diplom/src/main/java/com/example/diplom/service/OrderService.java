package com.example.diplom.service;


import com.example.models.domain.statistics.*;
import com.example.models.dto.statistics.*;
import com.example.models.dto.*;
import com.example.models.domain.*;



import java.util.List;

public interface OrderService {
    List<OrderDTO> getAll();
    boolean save(OrderDTO orderDTO);
    OrderDTO findOrderById(Long id);

    void updateOrderStatus(String status, Long orderId);

    List<OrderDTO> getOrderByUserName(String username);

    List<OrderDTO> getOrderByStatus(String status);
}
