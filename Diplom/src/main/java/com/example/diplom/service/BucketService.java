package com.example.diplom.service;

import com.example.diplom.domain.Bucket;
import com.example.diplom.domain.UserM;
import com.example.diplom.dto.BucketDTO;

import java.util.List;

public interface BucketService {
    Bucket createBucket(UserM userM, List<Long> productsIds);
    void addProduct(Bucket bucket, List<Long> productsIds);
    void save(Bucket bucket);
    BucketDTO getBucketByUser(String name);
}
