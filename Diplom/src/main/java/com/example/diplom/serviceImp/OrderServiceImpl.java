package com.example.diplom.serviceImp;

import com.example.diplom.dao.BucketRepository;
import com.example.diplom.dao.OrderRepository;
import com.example.diplom.dao.ProductRepository;
import com.example.diplom.domain.*;
import com.example.diplom.dto.BucketDTO;
import com.example.diplom.dto.BucketDetailDTO;
import com.example.diplom.dto.OrderDTO;
import com.example.diplom.dto.OrderDetailsDTO;
import com.example.diplom.mapper.OrderMapper;
import com.example.diplom.mapper.ProductMapper;
import com.example.diplom.service.BucketService;
import com.example.diplom.service.OrderDetailsService;
import com.example.diplom.service.OrderService;
import com.example.diplom.service.UserService;
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
    private final ProductRepository productRepository;
    private final OrderDetailsService orderDetailsService;
    private final BucketService bucketService;
    private final UserService userService;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, OrderDetailsService orderDetailsService, BucketService bucketService, UserService userService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderDetailsService = orderDetailsService;
        this.bucketService = bucketService;
        this.userService = userService;
    }

    @Override
    public List<OrderDTO> getAll() {
        return mapper.fromOrderList(orderRepository.findAll());
    }

    // @Override
    public boolean save(OrderDTO orderDTO) {
        UserM userM = userService.findById(orderDTO.getUserId());
        Order order = Order.builder()
                .user(userM)
                .address(orderDTO.getAddress())
                .created(LocalDateTime.now())
                .sum(orderDTO.getSum())
                .updated(orderDTO.getUpdated())
                .status(OrderStatus.NEW)
                .details(new ArrayList<>())
                .build();
        Order savedOrder = orderRepository.save(order);

        BucketDTO bucketDTO = bucketService.getBucketByUser(userM.getName());

        Long a =new Random().nextLong();


        for (BucketDetailDTO details : bucketDTO.getBucketDetails()) {
            OrderDetails newOrderDetails = new OrderDetails();
            newOrderDetails.setId(a);
            newOrderDetails.setDetails_id(a);
            newOrderDetails.setOrder(savedOrder);
            newOrderDetails.setAmount(details.getAmount());
            newOrderDetails.setPrice(details.getPrice());
            Product product = productRepository.findFirstById(details.getProductId());
            newOrderDetails.setProduct(product);

            orderDetailsService.save(newOrderDetails,details);
        }

        return false;
    }

    @Override
    public OrderDTO findOrderById(Long id) {
        return mapper.fromOrder(orderRepository.findFirstById(id));
    }
}
