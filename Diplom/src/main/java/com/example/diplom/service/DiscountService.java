package com.example.diplom.service;

import com.example.diplom.domain.Category;
import com.example.diplom.domain.Discount;
import com.example.diplom.domain.Product;
import com.example.diplom.dto.DiscountDTO;
import com.example.diplom.dto.ProductDTO;

import java.math.BigDecimal;
import java.util.List;

public interface DiscountService {
    List<DiscountDTO> getAll();
    boolean save(BigDecimal discount,Long productId);
    void remove(Long discount);
    Discount findDiscountById(Long discountId);
    DiscountDTO findDiscountByProductId(Long productId);
    List<DiscountDTO> findDiscountsByProducts(List<ProductDTO> productDTOList);
}
