package com.example.diplom.service;


import com.example.models.domain.statistics.*;
import com.example.models.dto.statistics.*;
import com.example.models.dto.*;
import com.example.models.domain.*;



import java.util.List;

public interface DiscountService {
    List<DiscountDTO> getAll();
    boolean save(double discount,Long productId);
    void remove(Long discount);
    DiscountDTO findDiscountByProductId(Long productId);
    List<DiscountDTO> findDiscountsByProducts(List<ProductDTO> productDTOList);
}
