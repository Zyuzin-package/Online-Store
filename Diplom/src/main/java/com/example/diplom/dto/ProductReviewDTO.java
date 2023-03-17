package com.example.diplom.dto;

import com.example.diplom.domain.ProductReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }
}
