package com.example.diplom.dao;

import com.example.diplom.domain.Bucket;
import com.example.diplom.domain.Product;
import com.example.diplom.domain.UserM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BucketRepository extends JpaRepository<Bucket, Long> {

    @Modifying
    @Query(value = "DELETE from buckets_product where  buckets_product.product_id=:productId and buckets_product.bucket_id=(Select id from buckets where buckets.user_id=(Select users.id from users where name=:username))", nativeQuery = true)
    @Transactional
    void removeProductByUsername(@Param("productId") Long productId,@Param("username") String username);

    Bucket getBucketByUserId(Long id);

    @Modifying
    @Query(value = "Select from buckets_product where product_id=:id", nativeQuery = true)
    @Transactional
    Product getProductFromBucketById(@Param("id") Long id);

    @Modifying
    @Query(value = "Select * from buckets_product where product_id=:productId", nativeQuery = true)
    @Transactional
    List<Long> getProductAtUserCart(@Param("productId")Long productId);
}
