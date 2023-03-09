package com.example.diplom.dao;

import com.example.diplom.domain.Bucket;
import com.example.diplom.domain.Product;
import com.example.diplom.domain.UserM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BucketRepository extends JpaRepository<Bucket, Long> {

    @Modifying
    @Query(value = "Select from users left join buckets b on users.id = b.user_id where b.id =:id", nativeQuery = true)
    @Transactional
    UserM getBucketId(@Param("id") Long bucketId);
    @Modifying
    @Query(value = "DELETE from buckets_product where  buckets_product.product_id=:productId and buckets_product.bucket_id=(Select id from buckets where buckets.user_id=(Select users.id from users where name=:username))", nativeQuery = true)
    @Transactional
    void removeProductByUsername(@Param("productId") Long productId,@Param("username") String username);

    Bucket getBucketByUserId(Long id);

    @Modifying
    @Query(value = "Select from buckets_product where product_id=:id", nativeQuery = true)
    @Transactional
    Product getProductFromBucketById(@Param("id") Long id);
}
