package com.example.diplom.dao;

import com.example.diplom.domain.Category;
import com.example.diplom.domain.Product;
import com.example.diplom.domain.UserM;
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
    @Modifying
    @Query(value = "select product_id from buckets_product where bucket_id = (select bucket_id from buckets where user_id=:id)", nativeQuery = true)
    @Transactional
    List<Long> getProductIdsByUserId(@Param("id")Long id);

    @Modifying
    @Query(value = "select * from products where products.id IN (select orders_details.product_id from orders_details where orders_details.order_details_id=:id)", nativeQuery = true)
    @Transactional
    List<Product> getProductsByUserIds(@Param("id")Long id);

    @Modifying
    @Query(value = "DELETE from products where products.id=(Select products_categories.product_id from products_categories where products_categories.category_id= (Select categories.id from categories where categories.title=:name))", nativeQuery = true)
    @Cascade(value = CascadeType.ALL)
    @Transactional
    void removeProductsByCategoryName(@Param("name")String name);
    @Modifying
    @Query(value = "DELETE from products_categories where category_id=(Select id from categories where title=:name)", nativeQuery = true)
    @Transactional
    void removeFromProductsToCategoryByCategoryName(@Param("name")String name);

}
