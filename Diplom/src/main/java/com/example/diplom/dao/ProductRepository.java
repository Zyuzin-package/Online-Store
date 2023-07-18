package com.example.diplom.dao;


import com.example.models.domain.statistics.*;
import com.example.models.dto.statistics.*;
import com.example.models.dto.*;
import com.example.models.domain.*;


import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Transactional
    Product findFirstById(Long productId);

    Product findByTitle(String title);

    @Transactional
    List<Product> findProductIdByCategoriesId(Long categories_id);

    @Modifying
    @Query(value = "insert into products_categories (category_id,product_id) VALUES (:category_id,:product_id)", nativeQuery = true)
    @Transactional
    void addCategoryToProduct(@Param("product_id") Long productId, @Param("category_id") Long categoryId);

    @Modifying
    @Query(value = "delete from products_categories where product_id=:product_id", nativeQuery = true)
    @Transactional
    void removeCategoryByProduct(@Param("product_id") Long product_id);

    @Modifying
    @Query(value = "select product_id from buckets_product where bucket_id=:id", nativeQuery = true)
    @Transactional
    List<Long> getProductIdsByBucketId(@Param("id")Long id);

    @Modifying
    @Query(value = "select * from products where products.id IN (select orders_details.product_id from orders_details where orders_details.order_details_id=:id)", nativeQuery = true)
    @Transactional
    List<Product> getProductsByUserIds(@Param("id")Long id);

    @Modifying
    @Query(value = "DELETE from products_categories where category_id=(Select id from categories where title=:name)", nativeQuery = true)
    @Transactional
    void removeFromProductsToCategoryByCategoryName(@Param("name")String name);
    @Modifying
    @Query(value = "DELETE from product_review where product_id=:id", nativeQuery = true)
    @Transactional
    void removeReviewFromProductsByProductId(@Param("id")Long id);
}
