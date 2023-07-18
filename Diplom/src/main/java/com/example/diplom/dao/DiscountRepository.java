package com.example.diplom.dao;


import com.example.models.domain.statistics.*;
import com.example.models.dto.statistics.*;
import com.example.models.dto.*;
import com.example.models.domain.*;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    @Transactional
    Discount findFirstById(Long discountId);
    Discount findFirstByProductId(Long productId);

}
