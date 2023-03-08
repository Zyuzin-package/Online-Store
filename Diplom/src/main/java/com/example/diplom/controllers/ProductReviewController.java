package com.example.diplom.controllers;

import com.example.diplom.dto.ProductReviewDTO;
import com.example.diplom.service.ProductReviewService;
import com.example.diplom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
@RequestMapping("/review/")
public class ProductReviewController {

    private final ProductReviewService productReviewService;
    private final ProductService productService;

    @Autowired
    public ProductReviewController(ProductReviewService productReviewService, ProductService productService) {
        this.productReviewService = productReviewService;
        this.productService = productService;
    }

    @GetMapping("/new/{id}")
    public String createNewReview(@RequestParam(name = "review") String review, @RequestParam(name = "stars") int stars, Principal principal, Model model, @PathVariable Long id, HttpServletRequest request) {
        if(productService.findProductById(id)==null){
            model.addAttribute("errorMessage", "Product not found");
            return "error";
        }
        if(productReviewService.getReviewByUserNameAndProductId(principal.getName(),id)!=null){
            model.addAttribute("isReviewCreated", true);
            return "redirect:"+request.getHeader("Referer");
        }

        productReviewService.saveReview(
                ProductReviewDTO.builder()
                .review(review)
                .productId(id)
                .stars(stars)
                .userName(principal.getName())
                .build());
        model.addAttribute("isReviewCreated", false);
        return "redirect:"+request.getHeader("Referer");
    }


}
