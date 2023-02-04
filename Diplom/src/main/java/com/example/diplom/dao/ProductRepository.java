package com.example.diplom.dao;

import com.example.diplom.domain.Product;
import com.example.diplom.domain.UserM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    @Transactional
    Product findFirstById(Long productId);
}
