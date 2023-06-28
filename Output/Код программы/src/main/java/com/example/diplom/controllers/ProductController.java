package com.example.diplom.controllers;

import com.example.diplom.domain.Product;
import com.example.diplom.domain.statistics.FrequencyAddToCartStats;
import com.example.diplom.domain.statistics.VisitStats;
import com.example.diplom.dto.DiscountDTO;
import com.example.diplom.dto.ProductDTO;
import com.example.diplom.dto.ProductReviewDTO;
import com.example.diplom.dto.UserNotificationDTO;
import com.example.diplom.dto.statistics.FrequencyAddToCartStatsDTO;
import com.example.diplom.dto.statistics.VisitStatsDTO;
import com.example.diplom.mapper.ProductMapper;
import com.example.diplom.service.DiscountService;
import com.example.diplom.service.ProductReviewService;
import com.example.diplom.service.ProductService;
import com.example.diplom.service.UserNotificationService;
import com.example.diplom.service.statistics.StatsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Controller
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final UserNotificationService userNotificationService;
    private final ProductReviewService productReviewService;
    private final StatsService<FrequencyAddToCartStats, FrequencyAddToCartStatsDTO> frequencyAddToCartStatsService;
    private final DiscountService discountService;
    private final StatsService<VisitStats, VisitStatsDTO> visitStatsService;
    private final ProductMapper mapper = ProductMapper.MAPPER;

    public ProductController(ProductService productService, UserNotificationService userNotificationService, ProductReviewService productReviewService, StatsService<FrequencyAddToCartStats, FrequencyAddToCartStatsDTO> frequencyAddToCartStatsService, DiscountService discountService, StatsService<VisitStats, VisitStatsDTO> visitStatsService) {
        this.productService = productService;
        this.userNotificationService = userNotificationService;
        this.productReviewService = productReviewService;
        this.frequencyAddToCartStatsService = frequencyAddToCartStatsService;
        this.discountService = discountService;
        this.visitStatsService = visitStatsService;
    }

    @GetMapping("/{id}/bucket")
    public String addBucket(@PathVariable Long id, Principal principal, HttpServletRequest request, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        Product p = productService.findProductById(id);

        if (p == null) {
            model.addAttribute("errorMessage", "Product not found");
            return "error";
        }
        productService.addToUserBucket(id, principal.getName());

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = localDateTime.format(formatter);
        localDateTime = LocalDateTime.parse(formattedDateTime, formatter);
        frequencyAddToCartStatsService.save(FrequencyAddToCartStats.builder()
                .product(p)
                .created(localDateTime)
                .build());

        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/{title}")
    public String productDetails(Model model, @PathVariable String title, Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications", dtos);
            model.addAttribute("notificationsCount", dtos.size());

        }
        ProductDTO dto = productService.getProductByName(title);

        if (dto == null) {
            model.addAttribute("errorMessage", "Product not found");
            return "error";
        }

        List<ProductReviewDTO> productReviewDTOS = productReviewService.getReviewsByProductTitle(title);
        DiscountDTO discountDTO = discountService.findDiscountByProductId(dto.getId());

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = localDateTime.format(formatter);
        localDateTime = LocalDateTime.parse(formattedDateTime, formatter);

        visitStatsService.save(VisitStats.builder()
                .product_id(dto.getId())
                .created(localDateTime)
                .build());

        if (principal != null) {
            if (productReviewService.getReviewByUserNameAndProductId(principal.getName(), dto.getId()) != null) {
                model.addAttribute("isReviewCreated", true);
            } else {
                model.addAttribute("isReviewCreated", false);
            }
        }

        model.addAttribute("product", dto);
        model.addAttribute("reviews", productReviewDTOS);
        model.addAttribute("review", new ProductReviewDTO());
        model.addAttribute("discount", discountDTO);

        return "productDetails";
    }

}
