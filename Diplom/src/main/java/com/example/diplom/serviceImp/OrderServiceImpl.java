package com.example.diplom.serviceImp;

import com.example.diplom.dao.OrderRepository;
import com.example.diplom.domain.*;
import com.example.diplom.dto.*;
import com.example.diplom.mapper.OrderMapper;
import com.example.diplom.service.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
                .sum(BigDecimal.valueOf(orderDTO.getSum()))
                .status(OrderStatus.NEW)
                .orderDetailsList(new ArrayList<>())
                .build();
       Order savedOrder = orderRepository.save(order);

        BucketDTO bucketDTO = bucketService.getBucketByUser(userM.getName());
        System.out.println("\n\nBUCKET DTO: "+bucketDTO);
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        BigDecimal sum = new BigDecimal(0);

        for (BucketDetailDTO details : bucketDTO.getBucketDetails()) {
            OrderDetails newOrderDetails = new OrderDetails();
            newOrderDetails.setAmount(details.getAmount());
            newOrderDetails.setPrice(details.getPrice());
            Product product = productService.findProductById(details.getProductId());
            newOrderDetails.setProduct(product);
            orderDetailsService.save(newOrderDetails);
            orderDetailsList.add(newOrderDetails);
            sum = sum.add(details.getPrice());
            bucketService.removeProductByUsername(product.getId(),userM.getName());
        }

        savedOrder.setOrderDetailsList(orderDetailsList);
        savedOrder.setSum(sum);



        orderRepository.save(savedOrder);

        return true;
    }

    @Override
    public OrderDTO findOrderById(Long id) {
        return mapper.fromOrder(orderRepository.findFirstById(id));
    }

    public List<OrderDetailsDTO> getDetailsByOrderId(Long id) {
        return orderDetailsService.findOrdersDetailsByOrderId(id);
    }

    public List<ProductDTO> getProductsByOrderId(Long id) {
        return productService.getProductsByUserIds(id);
    }
}
