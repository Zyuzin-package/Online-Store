package com.example.diplom.dao;

import com.example.diplom.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long > {
    Order findFirstById(Long id);
}
