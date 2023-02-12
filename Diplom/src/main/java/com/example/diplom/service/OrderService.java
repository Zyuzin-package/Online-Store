package com.example.diplom.service;

import com.example.diplom.dto.BucketDetailDTO;
import com.example.diplom.dto.OrderDTO;
import com.example.diplom.dto.OrderDetailsDTO;

import java.util.List;

public interface OrderService {
    List<OrderDTO> getAll();
    List<OrderDetailsDTO> getAllDetails();
    boolean save(OrderDTO orderDTO);
    OrderDTO findOrderById(Long id);

    List<OrderDetailsDTO> getDetailsByOrderId(Long id);
}
