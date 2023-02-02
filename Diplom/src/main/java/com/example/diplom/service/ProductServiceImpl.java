package com.example.diplom.service;

import com.example.diplom.dao.ProductRepository;
import com.example.diplom.domain.Bucket;
import com.example.diplom.domain.UserM;
import com.example.diplom.dto.ProductDTO;
import com.example.diplom.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class ProductServiceImpl  implements ProductService{
    private final ProductMapper mapper = ProductMapper.MAPPER;
    private final  UserService userService;
    private final  BucketService bucketService;
    private final ProductRepository productRepository;

    public ProductServiceImpl(UserService userService, BucketService bucketService, ProductRepository productRepository) {
        this.userService = userService;
        this.bucketService = bucketService;
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDTO> getAll() {
       return mapper.fromProductList(productRepository.findAll());
    }

    @Override
    @Transactional
    public void addToUserBucket(Long productId, String username) {
        UserM userM = userService.findByName(username);
        if(userM == null){
            throw  new RuntimeException("User - "+username+" not found");
        }

        Bucket bucket = userM.getBucket();
        if(bucket == null){
            Bucket newBucket = bucketService.createBucket(userM, Collections.singletonList(productId));
            bucketService.save(newBucket);
            userM.setBucket(newBucket);
            userService.save(userM);
        } else {
            bucketService.addProduct(bucket , Collections.singletonList(productId));
        }
    }


}
