package com.example.diplom.controllers;

import com.example.diplom.dto.ProductReviewDTO;
import com.example.diplom.service.ProductReviewService;
import org.dom4j.rule.Mode;
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

    @Autowired
    public ProductReviewController(ProductReviewService productReviewService) {
        this.productReviewService = productReviewService;
    }

    @GetMapping("/new/{id}")
    public String createNewReview(@RequestParam(name = "review") String review, @RequestParam(name = "stars") int stars, Principal principal, Model model, @PathVariable Long id, HttpServletRequest request) {
        System.out.println("\n\n" + review + "|" + stars + "|" + id);
        productReviewService.saveReview(
                ProductReviewDTO.builder()
                .review(review)
                .productId(id)
                .stars(stars)
                .userName(principal.getName())
                .build());
        return "redirect:"+request.getHeader("Referer");
    }


}
