package com.example.diplom.dao;

import com.example.diplom.domain.Category;
import com.example.diplom.domain.Product;
import com.example.diplom.domain.UserM;
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
    @Query(value = "SELECT product_id from products_categories where category_id=:categoryId", nativeQuery = true)
    @Transactional
    Category test(@Param("categoryId") Long categoryId);

    @Transactional
    void deleteProductIdById(Long productId);

    @Modifying
    @Query(value = "insert into products_categories (category_id,product_id) VALUES (:category_id,:product_id)", nativeQuery = true)
    @Transactional
    void addCategoryToProduct(@Param("product_id") Long productId, @Param("category_id") Long categoryId);

    @Modifying
    @Query(value = "delete from products_categories where product_id=:product_id", nativeQuery = true)
    @Transactional
    void removeCategoryByProduct(@Param("product_id") Long product_id);

    @Modifying
    @Query(value = "delete from products_categories where category_id = :id", nativeQuery = true)
    @Transactional
    void removeProductByCategory(@Param("id") Long category_id);

    @Modifying
    @Query(value = "select product_id from buckets_product where bucket_id=:id", nativeQuery = true)
    @Transactional
    List<Long> getProductIdsByBucketId(@Param("id")Long id);
}
