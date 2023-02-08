package com.example.diplom.dao;

import com.example.diplom.domain.Order;
import com.example.diplom.domain.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {

    List<OrderDetails> findAllByOrderId(Long orderId);
}
