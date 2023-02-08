package com.example.diplom.dao;

import com.example.diplom.domain.Bucket;
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
}
