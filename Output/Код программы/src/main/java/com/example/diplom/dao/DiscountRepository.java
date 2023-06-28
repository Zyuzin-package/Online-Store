package com.example.diplom.dao;

import com.example.diplom.domain.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    @Transactional
    Discount findFirstById(Long discountId);
    Discount findFirstByProductId(Long productId);

}
