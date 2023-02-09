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

    List<OrderDetails> findAllByOrderId(Long orderId);
    @Modifying
    @Query(value = "INSERT INTO orders_details (id,amount, price, order_id, product_id, details_id) VALUES (:orderDetails,:details_id)", nativeQuery = true)
    @Transactional
    void saveOrderDetails(
            @Param("orderDetails") OrderDetails orderDetails,
            @Param("details_id")Long details_id
            );
}
