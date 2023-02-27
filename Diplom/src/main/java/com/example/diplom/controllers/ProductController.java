package com.example.diplom.controllers;

import com.example.diplom.domain.statistics.VisitStats;
import com.example.diplom.dto.*;
import com.example.diplom.mapper.ProductMapper;
import com.example.diplom.service.*;
import com.example.diplom.service.statistics.VisitStatsService;
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
    private final DiscountService discountService;
    private final VisitStatsService visitStatsService;
    private final ProductMapper mapper = ProductMapper.MAPPER;
    @Autowired
    public ProductController(ProductService productService, UserNotificationService userNotificationService, ProductReviewService productReviewService, DiscountService discountService, VisitStatsService visitStatsService) {
        this.productService = productService;
        this.userNotificationService = userNotificationService;
        this.productReviewService = productReviewService;
        this.discountService = discountService;
        this.visitStatsService = visitStatsService;
    }

    @GetMapping("/{id}/bucket")
    public String addBucket(@PathVariable Long id, Principal principal, HttpServletRequest request) {
        if (principal == null) {
            return "redirect:/login";
        }
        productService.addToUserBucket(id, principal.getName());
        productService.findProductById(id);
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

        visitStatsService.save(VisitStats.builder()
                .product_id(dto.getId())
                .build());

        if(productReviewService.getReviewByUserNameAndProductId(principal.getName(),dto.getId())!=null){
            model.addAttribute("isReviewCreated", true);
        } else {
            model.addAttribute("isReviewCreated", false);
        }

        model.addAttribute("product", dto);
        model.addAttribute("reviews", productReviewDTOS);
        model.addAttribute("review", new ProductReviewDTO());
        model.addAttribute("discount", discountDTO);

        return "productDetails";
    }

}
