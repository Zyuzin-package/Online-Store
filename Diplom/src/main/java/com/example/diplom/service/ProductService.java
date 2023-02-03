package com.example.diplom.service;

import com.example.diplom.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAll();
    void  addToUserBucket(Long productId, String username);

    boolean save(ProductDTO productDTO);
    void remove(Long productId);
}
