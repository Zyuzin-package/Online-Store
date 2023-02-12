package com.example.diplom.serviceImp;

import com.example.diplom.dao.BucketRepository;
import com.example.diplom.dao.OrderRepository;
import com.example.diplom.dao.ProductRepository;
import com.example.diplom.domain.*;
import com.example.diplom.dto.*;
import com.example.diplom.mapper.OrderMapper;
import com.example.diplom.mapper.ProductMapper;
import com.example.diplom.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderMapper mapper = OrderMapper.MAPPER;
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final OrderDetailsService orderDetailsService;
    private final BucketService bucketService;
    private final UserService userService;

    public OrderServiceImpl(OrderRepository orderRepository, ProductService productService, OrderDetailsService orderDetailsService, BucketService bucketService, UserService userService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.orderDetailsService = orderDetailsService;
        this.bucketService = bucketService;
        this.userService = userService;
    }

    @Override
    public List<OrderDTO> getAll() {
        return mapper.fromOrderList(orderRepository.findAll());
    }

    @Override
    public List<OrderDetailsDTO> getAllDetails() {
        return orderDetailsService.getAllDetails();
    }
    public boolean save(OrderDTO orderDTO) {
        UserM userM = userService.findById(orderDTO.getUserId());
        Order order = Order.builder()
                .user(userM)
                .address(orderDTO.getAddress())
                .sum(orderDTO.getSum())
                .status(OrderStatus.NEW)
                .build();
        Order savedOrder = orderRepository.save(order);

        BucketDTO bucketDTO = bucketService.getBucketByUser(userM.getName());

        for (BucketDetailDTO details : bucketDTO.getBucketDetails()) {
            OrderDetails newOrderDetails = new OrderDetails();
            newOrderDetails.setOrder(savedOrder);
            newOrderDetails.setAmount(details.getAmount());
            newOrderDetails.setPrice(details.getPrice());
            Product product = productService.findProductById(details.getProductId());
            newOrderDetails.setProduct(product);
            orderDetailsService.save(newOrderDetails);
        }

        return true;
    }

    @Override
    public OrderDTO findOrderById(Long id) {
        return mapper.fromOrder(orderRepository.findFirstById(id));
    }

    public List<OrderDetailsDTO> getDetailsByOrderId(Long id){
        return orderDetailsService.findOrdersDetailsByOrderId(id);
    }
    public List<ProductDTO> getProductsByOrderId(Long id){
        return productService.getProductsByUserIds(id);
    }
}
