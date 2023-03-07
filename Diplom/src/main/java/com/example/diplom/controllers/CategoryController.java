package com.example.diplom.controllers;

import com.example.diplom.domain.statistics.FrequencyAddToCartStats;
import com.example.diplom.dto.CategoryDTO;
import com.example.diplom.dto.DiscountDTO;
import com.example.diplom.dto.ProductDTO;
import com.example.diplom.dto.UserNotificationDTO;
import com.example.diplom.dto.statistics.FrequencyAddToCartStatsDTO;
import com.example.diplom.service.CategoryService;
import com.example.diplom.service.DiscountService;
import com.example.diplom.service.ProductService;
import com.example.diplom.service.UserNotificationService;
import com.example.diplom.service.statistics.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/category")
public class CategoryController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final UserNotificationService userNotificationService;
    private final DiscountService discountService;

    @Autowired
    public CategoryController(ProductService productService, CategoryService categoryService, UserNotificationService userNotificationService, DiscountService discountService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.userNotificationService = userNotificationService;
        this.discountService = discountService;
    }

    @GetMapping
    public String categoriesList(Model model, Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications", dtos);
            model.addAttribute("notificationsCount",dtos.size());

        }

        List<CategoryDTO> categoryDTOS = categoryService.getAll();

        if (categoryDTOS != null) {
            model.addAttribute("categories", categoryDTOS);
            return "redirect:/category/" + categoryDTOS.get(0).getTitle();
        } else {
            model.addAttribute("categories", new ArrayList<CategoryDTO>());
            model.addAttribute("products", new ArrayList<ProductDTO>());
            model.addAttribute("discounts", new ArrayList<DiscountDTO>());
            return "products";
        }
    }

    @GetMapping("/{category}")
    public String outProductsByCategory(@PathVariable String category, Model model, Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications", dtos);
            model.addAttribute("notificationsCount",dtos.size());

        }

        if (categoryService.getCategoryByName(category) == null) {
            model.addAttribute("errorMessage", "Category not found");
            return "error";
        }

        List<ProductDTO> productDTOList = productService.getProductsByCategory(category);
        List<DiscountDTO> discountDTOList = discountService.findDiscountsByProducts(productDTOList);

        if (productDTOList != null && discountDTOList != null) {
            productDTOList.sort(Comparator.comparing(ProductDTO::getId));
            discountDTOList.sort(Comparator.comparing(DiscountDTO::getProduct_id));
        }

        model.addAttribute("categories", categoryService.getAll() != null ? categoryService.getAll() : new ArrayList<CategoryDTO>());
        model.addAttribute("products", productDTOList != null ? productDTOList : new ArrayList<ProductDTO>());
        model.addAttribute("discounts", discountDTOList != null ? discountDTOList : new ArrayList<DiscountDTO>());
        return "products";
    }


}
