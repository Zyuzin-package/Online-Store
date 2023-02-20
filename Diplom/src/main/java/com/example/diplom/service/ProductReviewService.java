package com.example.diplom.service;

import com.example.diplom.dto.ProductReviewDTO;

import java.util.List;

public interface ProductReviewService {
    List<ProductReviewDTO> getReviewsByProductTitle(String title);

    void saveReview(ProductReviewDTO productReviewDTO);

    ProductReviewDTO getReviewByUserNameAndProductId(String name, Long productId);
}
