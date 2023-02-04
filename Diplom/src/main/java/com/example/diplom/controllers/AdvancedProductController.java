package com.example.diplom.controllers;

import com.example.diplom.dto.CategoryDTO;
import com.example.diplom.dto.ProductDTO;
import com.example.diplom.service.CategoryService;
import com.example.diplom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/advanced/products")
public class AdvancedProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public AdvancedProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String categoriesList(Model model) {
        List<CategoryDTO> categoryDTOS = categoryService.getAll();
        model.addAttribute("categories", categoryDTOS);
        return "redirect:/advanced/products/cheese";
    }

    @GetMapping("/{categories}")
    public String outProductsByCategory(@PathVariable String categories, Model model) {
        List<CategoryDTO> categoryDTOS = categoryService.getAll();
        List<ProductDTO> productDTOList = productService.getProductsByCategory(categories);
        model.addAttribute("categories", categoryDTOS);
        model.addAttribute("products", productDTOList);
        return "advancedProducts";
    }
}
