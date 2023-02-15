package com.example.diplom.dao;

import com.example.diplom.domain.ProductReview;
import com.example.diplom.dto.ProductReviewDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    @Modifying
    @Query(value = "Select * from product_review where product_id=(Select id from  products where products.title=:title)", nativeQuery = true)
    @Transactional
    List<ProductReview> getReviewsByProductTitle(@Param("title") String title);
}
