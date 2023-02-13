package com.example.diplom.dao;

import com.example.diplom.domain.Order;
import com.example.diplom.domain.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    @Modifying
    @Query(value = "Select * from orders_details where order_details_id=:id", nativeQuery = true)
    @Transactional
    List<OrderDetails> findAllById(@Param("id")Long orderId);
    @Modifying
    @Query(value = "INSERT INTO orders_details (id,amount, price, order_details_id, product_id) VALUES (:orderDetails)", nativeQuery = true)
    @Transactional
    void saveOrderDetails(@Param("orderDetails") OrderDetails orderDetails);
}
