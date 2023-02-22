package com.example.diplom.controllers;

import com.example.diplom.dto.*;
import com.example.diplom.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final UserNotificationService userNotificationService;
    private final ProductReviewService productReviewService;
    private final DiscountService discountService;

    @Autowired
    public ProductController(ProductService productService, UserNotificationService userNotificationService, ProductReviewService productReviewService, DiscountService discountService) {
        this.productService = productService;
        this.userNotificationService = userNotificationService;
        this.productReviewService = productReviewService;
        this.discountService = discountService;
    }

    @GetMapping("/{id}/bucket")
    public String addBucket(@PathVariable Long id, Principal principal, HttpServletRequest request) {
        if (principal == null) {
            return "redirect:/login";
        }
        productService.addToUserBucket(id, principal.getName());
        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/{title}")
    public String productDetails(Model model, @PathVariable String title, Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications", dtos);
        }
        ProductDTO dto = productService.getProductByName(title);
        List<ProductReviewDTO> productReviewDTOS = productReviewService.getReviewsByProductTitle(title);
        DiscountDTO discountDTO = discountService.findDiscountByProductId(dto.getId());

//        model.addAttribute("image",new File("C:/DiplomImages/" + title+".jpg"));
        model.addAttribute("product", dto);
        model.addAttribute("reviews", productReviewDTOS);
        model.addAttribute("review", new ProductReviewDTO());
        model.addAttribute("discount", discountDTO);
        return "productDetails";
    }

}
