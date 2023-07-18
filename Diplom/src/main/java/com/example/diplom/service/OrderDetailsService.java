package com.example.diplom.service;


import com.example.models.domain.statistics.*;
import com.example.models.dto.statistics.*;
import com.example.models.dto.*;
import com.example.models.domain.*;


import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderDetailsService {
    List<OrderDetailsDTO> findOrdersDetailsByOrderId(Long orderId);
    boolean save(OrderDetails orderDetails);
    List<OrderDetailsDTO> getAllDetails();

}
