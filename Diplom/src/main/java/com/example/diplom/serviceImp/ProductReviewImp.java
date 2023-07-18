package com.example.diplom.serviceImp;

import com.example.diplom.dao.ProductReviewRepository;

import com.example.models.domain.statistics.*;
import com.example.models.dto.statistics.*;
import com.example.models.dto.*;
import com.example.models.domain.*;


import com.example.diplom.service.ProductReviewService;
import com.example.diplom.service.ProductService;
import com.example.diplom.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductReviewImp implements ProductReviewService {
    private final ProductReviewRepository productReviewRepository;
    private final UserService userService;
    private final ProductService productService;

    public ProductReviewImp(ProductReviewRepository productReviewRepository, UserService userService, ProductService productService) {
        this.productReviewRepository = productReviewRepository;
        this.userService = userService;
        this.productService = productService;
    }

    @Override
    public List<ProductReviewDTO> getReviewsByProductTitle(String title) {
        List<ProductReview> reviewList = productReviewRepository.getReviewsByProductTitle(title);
        List<ProductReviewDTO> dtos = new ArrayList<>();
        for (ProductReview productReview : reviewList) {
            dtos.add(
                    ProductReviewDTO.builder()
                            .productId(productReview.getProduct().getId())
                            .id(productReview.getId())
                            .stars(productReview.getStars())
                            .review(productReview.getReview())
                            .userName(productReview.getUserM().getName())
                            .build());
        }
        return dtos;
    }

    @Override
    public void saveReview(ProductReviewDTO productReviewDTO) {
        ProductReview productReview = ProductReview.builder()
                .review(productReviewDTO.getReview())
                .stars(productReviewDTO.getStars())
                .product(productService.findProductById(productReviewDTO.getProductId()))
                .userM(userService.findByName(productReviewDTO.getUserName())).build();
        productReviewRepository.save(productReview);

    }

    @Override
    public ProductReviewDTO getReviewByUserNameAndProductId(String name, Long productId) {

        List<ProductReview> productReviews = productReviewRepository.getReviewByUserNameAndProductId(name, productId);
        if(productReviews == null || productReviews.isEmpty()){
            return null;
        }
        ProductReview productReview = productReviews.get(0);

        return ProductReviewDTO.builder()
                .productId(productReview.getProduct().getId())
                .review(productReview.getReview())
                .stars(productReview.getStars())
                .userName(productReview.getUserM().getName())
                .id(productReview.getId()).build();
    }

}
