package com.example.diplom.serviceImp;

import com.example.diplom.dao.BucketRepository;
import com.example.diplom.dao.OrderRepository;
import com.example.diplom.dao.ProductRepository;
import com.example.diplom.domain.Order;
import com.example.diplom.domain.OrderDetails;
import com.example.diplom.domain.Product;
import com.example.diplom.dto.OrderDTO;
import com.example.diplom.mapper.OrderMapper;
import com.example.diplom.mapper.ProductMapper;
import com.example.diplom.service.OrderDetailsService;
import com.example.diplom.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderMapper mapper = OrderMapper.MAPPER;

    private final OrderRepository orderRepository ;
    private final ProductRepository productRepository;

    private final OrderDetailsService orderDetailsService;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, OrderDetailsService orderDetailsService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderDetailsService = orderDetailsService;
    }

    @Override
    public List<OrderDTO> getAll() {
        return mapper.fromOrderList(orderRepository.findAll());
    }

    @Override
    public boolean save(OrderDTO orderDTO) {
//
//        List<Product> products = productRepository.getProductIdsByBucketId(orderDTO.get)
//

//        Order order = Order.builder()
//                .address(orderDTO.getAddress())
//                .created(LocalDateTime.now())
//                .sum(orderDTO.getSum())
//                .updated(orderDTO.getUpdated())
//                .details()

        return false;
    }

    @Override
    public OrderDTO findOrderById(Long id) {
        return mapper.fromOrder(orderRepository.findFirstById(id));
    }
}
