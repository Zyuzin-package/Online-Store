package com.example.diplom.dao;

import com.example.diplom.domain.Order;
import com.example.diplom.domain.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long > {
    Order findFirstById(Long id);
    @Modifying
    @Query(value = "UPDATE orders set status=:status where orders.id=:id", nativeQuery = true)
    @Transactional
    void updateOrderStatus(@Param("id")Long id, @Param("status")String status);
    @Modifying
    @Query(value = "SELECT * from orders where orders.user_id = (select users.id from users where users.name=:username)", nativeQuery = true)
    @Transactional
    List<Order> getOrdersByUserName(@Param("username")String username);
}
