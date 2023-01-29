package com.example.diplom.service;

import com.example.diplom.dao.ProductRepository;
import com.example.diplom.dto.ProductDTO;
import com.example.diplom.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl  implements ProductService{
    private final ProductMapper mapper = ProductMapper.MAPPER;
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDTO> getAll() {
       return mapper.fromProductList(productRepository.findAll());
    }
}
