package com.example.diplom.controllers;

import com.example.diplom.dto.CategoryDTO;
import com.example.diplom.dto.ProductDTO;
import com.example.diplom.dto.ProductReviewDTO;
import com.example.diplom.dto.UserNotificationDTO;
import com.example.diplom.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final UserNotificationService userNotificationService;
    private final ProductReviewService productReviewService;

    @Autowired
    public ProductController(ProductService productService, UserNotificationService userNotificationService, ProductReviewService productReviewService) {
        this.productService = productService;
        this.userNotificationService = userNotificationService;
        this.productReviewService = productReviewService;
    }


    @GetMapping("/{id}/bucket")
    public String addBucket(@PathVariable Long id, Principal principal, HttpServletRequest request) {
        productService.addToUserBucket(id, principal.getName());
        return "redirect:"+request.getHeader("Referer");
    }

    @GetMapping("/{title}")
    public String productDetails(Model model, @PathVariable String title, Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications", dtos);
        }
        ProductDTO dto = productService.getProductByName(title);
        List<ProductReviewDTO> productReviewDTOS = productReviewService.getReviewsByProductTitle(title);
        model.addAttribute("product", dto);
        model.addAttribute("reviews", productReviewDTOS);
        model.addAttribute("review", new ProductReviewDTO());
        return "productDetails";
    }

}
