package com.example.diplom.serviceImp;

import com.example.diplom.dao.OrderDetailsRepository;
import com.example.diplom.domain.OrderDetails;
import com.example.diplom.dto.OrderDetailsDTO;
import com.example.diplom.mapper.OrderDetailsMapper;
import com.example.diplom.mapper.OrderMapper;
import com.example.diplom.service.OrderDetailsService;

import java.util.List;

public class OrderDetailsServiceImpl implements OrderDetailsService {

    private final OrderDetailsMapper mapper = OrderDetailsMapper.MAPPER;
    private final OrderDetailsRepository orderDetailsRepository;

    public OrderDetailsServiceImpl(OrderDetailsRepository orderDetailsRepository) {
        this.orderDetailsRepository = orderDetailsRepository;
    }

    @Override
    public List<OrderDetailsDTO> findOrdersDetailsByOrderId(Long orderId) {
        List<OrderDetails> orderDetails = orderDetailsRepository.findAllByOrderId(orderId);
        return mapper.fromOrderDetailsList(orderDetails);
    }

    public void save(OrderDetailsDTO orderDetailsDTO){
        orderDetailsRepository.save(mapper.toOrderDetails(orderDetailsDTO));
    }
}
