package com.example.diplom.serviceImp;

import com.example.diplom.dao.ProductReviewRepository;
import com.example.diplom.domain.ProductReview;
import com.example.diplom.dto.ProductReviewDTO;
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
}