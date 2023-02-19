package com.example.diplom.dao;

import com.example.diplom.domain.Discount;
import com.example.diplom.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    @Transactional
    Discount findFirstById(Long discountId);
    Discount findFirstByProductId(Long productId);

}
