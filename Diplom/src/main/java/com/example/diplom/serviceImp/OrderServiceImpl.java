package com.example.diplom.serviceImp;

import com.example.diplom.dao.OrderRepository;
import com.example.diplom.domain.*;
import com.example.diplom.domain.statistics.BuyStats;
import com.example.diplom.dto.*;
import com.example.diplom.dto.statistics.BuyStatsDTO;
import com.example.diplom.mapper.OrderMapper;
import com.example.diplom.service.*;
import com.example.diplom.service.statistics.StatsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final UserNotificationService userNotificationService;
    private final StatsService<BuyStats, BuyStatsDTO> buyStatsService;
    private final MailSender mailSender;

    public OrderServiceImpl(OrderRepository orderRepository, ProductService productService, OrderDetailsService orderDetailsService, BucketService bucketService, UserService userService, UserNotificationService userNotificationService,   StatsService<BuyStats, BuyStatsDTO>  buyStatsService, MailSender mailSender) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.orderDetailsService = orderDetailsService;
        this.bucketService = bucketService;
        this.userService = userService;
        this.userNotificationService = userNotificationService;
        this.buyStatsService = buyStatsService;
        this.mailSender = mailSender;
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
                .orderDetailsList(new ArrayList<>())
                .build();
        Order savedOrder = orderRepository.save(order);

        BucketDTO bucketDTO = bucketService.getBucketByUser(userM.getName());
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        double sum = 0;

        for (BucketDetailDTO details : bucketDTO.getBucketDetails()) {
            OrderDetails newOrderDetails = new OrderDetails();
            newOrderDetails.setAmount(details.getAmount());

            if (details.getAmount() > 1) {
                if (details.getDiscountPrice() > 0) {
                    newOrderDetails.setPrice(details.getDiscountPrice() * details.getAmount());
                } else {
                    newOrderDetails.setPrice(details.getPrice() * details.getAmount());
                }
            } else {
                if (details.getDiscountPrice() > 0) {
                    newOrderDetails.setPrice(details.getDiscountPrice());
                } else {
                    newOrderDetails.setPrice(details.getPrice());
                }
            }
            sum = sum + newOrderDetails.getPrice();

            Product product = productService.findProductById(details.getProductId());
            newOrderDetails.setProduct(product);
            orderDetailsService.save(newOrderDetails);
            orderDetailsList.add(newOrderDetails);

            bucketService.removeProductByUsername(product.getId(), userM.getName());
        }

        savedOrder.setOrderDetailsList(orderDetailsList);
        savedOrder.setSum(sum);

        Order doneOrder = orderRepository.save(savedOrder);

        userNotificationService.sendNotificationToUser(UserNotificationDTO.builder()
                .userId(userM.getId())
                .message("Order - " + doneOrder.getId() + " was created")
                .url("")
                .urlText("").build()

        );
        StringBuilder temp = new StringBuilder();

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = localDateTime.format(formatter);
        localDateTime = LocalDateTime.parse(formattedDateTime, formatter);

        for (OrderDetails o : orderDetailsList) {
            temp.append(o.getProduct().getTitle()).append("\n");
            buyStatsService.save(BuyStats.builder()
                    .amount(o.getAmount())
                    .product_id(o.getProduct().getId())
                    .created(localDateTime)
                    .build());
        }


        mailSender.send(userM.getEmail(), "Kork-Market new order", "You create new order with number " + doneOrder.getId() + " :\n" + temp);
        return true;
    }

    @Override
    public OrderDTO findOrderById(Long id) {
        Order order = orderRepository.findFirstById(id);
        OrderDTO dto = mapper.fromOrder(order);
        dto.setUserId(order.getUser().getId());
        return dto;
    }

    @Override
    public List<OrderDetailsDTO> getDetailsByOrderId(Long id) {
        return orderDetailsService.findOrdersDetailsByOrderId(id);
    }

    @Override
    public void updateOrderStatus(String status, Long orderId) {
        Order order = orderRepository.findFirstById(orderId);
        UserM userM = order.getUser();
        userNotificationService.sendNotificationToUser(
                UserNotificationDTO.builder()
                        .userId(userM.getId())
                        .message("Order - " + orderId + " was " + status.toLowerCase())
                        .url("")
                        .urlText("").build());

        if (status.equals("COMPLETED")) {
            StringBuilder stringBuilder = new StringBuilder();

            for (OrderDetails details : order.getOrderDetailsList()) {
                stringBuilder.append(details.getProduct().getTitle()).append(", ");
            }

            userNotificationService.sendNotificationToUser(
                    UserNotificationDTO.builder()
                            .userId(userM.getId())
                            .message("Please review this products: " + stringBuilder)
                            .url("")
                            .urlText("").build());
        }
        mailSender.send(userM.getEmail(), "Kork-Market update order status", "You order with number " + order.getId() + " has changed its status, now the status is " + status);
        orderRepository.updateOrderStatus(orderId, status);
    }

    @Override
    public List<OrderDTO> getOrderByUserName(String username) {
        return mapper.fromOrderList(orderRepository.getOrdersByUserName(username));
    }

    @Override
    public List<OrderDTO> getOrderByStatus(String status) {
        OrderStatus orderStatus = null;
        for (OrderStatus stat : OrderStatus.values()) {
            if (stat.name().equals(status)) {
                orderStatus = stat;
            }
        }
        return mapper.fromOrderList(orderRepository.getOrdersByStatus(orderStatus));
    }
}
