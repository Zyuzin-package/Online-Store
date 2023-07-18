package com.example.diplom.service;


import com.example.models.domain.statistics.*;
import com.example.models.dto.statistics.*;
import com.example.models.dto.*;
import com.example.models.domain.*;



import java.util.List;

public interface ProductReviewService {
    List<ProductReviewDTO> getReviewsByProductTitle(String title);

    void saveReview(ProductReviewDTO productReviewDTO);

    ProductReviewDTO getReviewByUserNameAndProductId(String name, Long productId);
    
}
