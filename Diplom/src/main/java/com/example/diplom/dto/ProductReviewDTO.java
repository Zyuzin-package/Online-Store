package com.example.diplom.dto;

import com.example.diplom.domain.ProductReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReviewDTO {
    private Long id;
    private Long productId;
    private String userName;
    private String review;
    private int stars;

    public ProductReviewDTO(ProductReview productReview) {
        this.id = productReview.getId();
        this.productId = productReview.getProduct().getId();
        this.review = productReview.getReview();
        this.stars = productReview.getStars();
        this.userName = productReview.getUserM().getName();
    }
}
