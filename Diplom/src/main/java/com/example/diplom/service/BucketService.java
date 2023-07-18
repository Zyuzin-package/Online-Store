package com.example.diplom.service;


import com.example.models.domain.statistics.*;
import com.example.models.dto.statistics.*;
import com.example.models.dto.*;
import com.example.models.domain.*;



import java.util.List;

public interface BucketService {
    Bucket createBucket(UserM userM, List<Long> productsIds);
    void addProduct(Bucket bucket, List<Long> productsIds);
    void save(Bucket bucket);
    BucketDTO getBucketByUser(String name);
    BucketDTO removeProduct(Bucket bucket, Long productId,String name);
     void removeProductByUsername(Long productId, String username);
    void clearBucket(Bucket bucket,String name);

    void amountIncrease(Bucket bucket,Long productId);
    void amountDecrease(Bucket bucket,Long productId);

    boolean checkBucketProducts(Long id);

    boolean checkProductInBuckets(Long productId);

}
