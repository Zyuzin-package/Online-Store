package com.example.diplom.serviceImp;

import com.example.diplom.dao.OrderDetailsRepository;
import com.example.diplom.dao.ProductRepository;
import com.example.diplom.domain.Order;
import com.example.diplom.domain.OrderDetails;
import com.example.diplom.domain.Product;
import com.example.diplom.dto.BucketDetailDTO;
import com.example.diplom.dto.OrderDTO;
import com.example.diplom.dto.OrderDetailsDTO;
import com.example.diplom.mapper.OrderDetailsMapper;
import com.example.diplom.mapper.OrderMapper;
import com.example.diplom.service.OrderDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {

    private final OrderDetailsMapper mapper = OrderDetailsMapper.MAPPER;
    private final OrderDetailsRepository orderDetailsRepository;
    private final ProductRepository productRepository;

    public OrderDetailsServiceImpl(OrderDetailsRepository orderDetailsRepository, ProductRepository productRepository) {
        this.orderDetailsRepository = orderDetailsRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<OrderDetailsDTO> findOrdersDetailsByOrderId(Long orderId) {
        List<OrderDetails> orderDetails = orderDetailsRepository.findAllByOrderId(orderId);
        return mapper.fromOrderDetailsList(orderDetails);
    }

    @Override
    public boolean save(OrderDetails orderDetails) {
        orderDetailsRepository.save(orderDetails);
        return true;
    }


}
