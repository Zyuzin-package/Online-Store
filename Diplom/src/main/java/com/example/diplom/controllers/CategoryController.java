package com.example.diplom.controllers;

import com.example.diplom.dto.CategoryDTO;
import com.example.diplom.dto.ProductDTO;
import com.example.diplom.dto.UserNotificationDTO;
import com.example.diplom.service.CategoryService;
import com.example.diplom.service.ProductService;
import com.example.diplom.service.UserNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final UserNotificationService userNotificationService;

    @Autowired
    public CategoryController(ProductService productService, CategoryService categoryService, UserNotificationService userNotificationService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.userNotificationService = userNotificationService;
    }


    @GetMapping
    public String categoriesList(Model model, Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications", dtos);
        }

        List<CategoryDTO> categoryDTOS = categoryService.getAll();
        model.addAttribute("categories", categoryDTOS);
        return "products";
    }

    @GetMapping("/{category}")
    public String outProductsByCategory(@PathVariable String category, Model model, Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications", dtos);
        }

        List<CategoryDTO> categoryDTOS = categoryService.getAll();
        List<ProductDTO> productDTOList = productService.getProductsByCategory(category);

        model.addAttribute("categories", categoryDTOS);
        model.addAttribute("products", productDTOList);
        return "products";
    }



}
