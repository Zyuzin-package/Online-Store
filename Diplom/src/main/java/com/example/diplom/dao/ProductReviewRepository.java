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

    ProductReview getProductReviewById(Long id);
    @Modifying
    @Query(value = "Select * from product_review where product_id=(Select id from  products where products.title=:title)", nativeQuery = true)
    @Transactional
    List<ProductReview> getReviewsByProductTitle(@Param("title") String title);
    @Modifying
    @Query(value = "select p from product_review p where p.user_id = (select users.id from users where name=:username)", nativeQuery = true)
    @Transactional
    List<Long> getReviewByUserName(@Param("username") String name);
    @Modifying
    @Query(value = "select * from product_review where product_review.user_id = (select users.id from users where name=:username) " +
            "and product_review.product_id=:productId", nativeQuery = true)
    @Transactional
    List<ProductReview> getReviewByUserNameAndProductId(@Param("username") String name, @Param("productId") Long productId);
}
