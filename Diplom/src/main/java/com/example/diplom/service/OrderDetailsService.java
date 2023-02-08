package com.example.diplom.service;

import com.example.diplom.domain.OrderDetails;
import com.example.diplom.dto.OrderDetailsDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderDetailsService {
    List<OrderDetailsDTO> findOrdersDetailsByOrderId(Long orderId);
    boolean save(OrderDetails orderDetails);

}
