package com.example.diplom.service;

import com.example.diplom.domain.Category;
import com.example.diplom.domain.Product;
import com.example.diplom.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAll();

    void addToUserBucket(Long productId, String username);

    boolean save(ProductDTO productDTO);

    Category remove(Long productId);

    Product findProductById(Long productId);

    List<ProductDTO> getProductsByCategory(String category);


}
