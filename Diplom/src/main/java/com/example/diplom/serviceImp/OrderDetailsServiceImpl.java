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
import com.example.diplom.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {

    private final OrderDetailsMapper mapper = OrderDetailsMapper.MAPPER;
    private final OrderDetailsRepository orderDetailsRepository;
    private final ProductService productService;

    public OrderDetailsServiceImpl(OrderDetailsRepository orderDetailsRepository, ProductService productService) {
        this.orderDetailsRepository = orderDetailsRepository;
        this.productService = productService;
    }

    @Override
    public List<OrderDetailsDTO> findOrdersDetailsByOrderId(Long orderId) {
        List<OrderDetails> details = orderDetailsRepository.findAllById(orderId);
        List<OrderDetailsDTO> orderDetails = mapper.fromOrderDetailsList(details);
        for (int i = 0; i<=details.size()-1;i++) {
        orderDetails.get(i).setOrderId(orderId);
        orderDetails.get(i).setProductName(details.get(i).getProduct().getTitle());
        orderDetails.get(i).setProductId(details.get(i).getProduct().getId());
        }

        return orderDetails;
    }

    @Override
    public boolean save(OrderDetails orderDetails) {
        orderDetailsRepository.save(orderDetails);
        return true;
    }

    @Override
    public List<OrderDetailsDTO> getAllDetails() {
        return mapper.fromOrderDetailsList(orderDetailsRepository.findAll());
    }


}
