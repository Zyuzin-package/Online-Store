package com.example.diplom.controllers;

import com.example.diplom.domain.Category;
import com.example.diplom.domain.UserM;
import com.example.diplom.dto.CategoryDTO;
import com.example.diplom.dto.ProductDTO;
import com.example.diplom.dto.UserNotificationDTO;
import com.example.diplom.service.CategoryService;
import com.example.diplom.service.ProductService;
import com.example.diplom.service.UserNotificationService;
import com.example.diplom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/advanced/products")
public class AdvancedProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    private final UserNotificationService userNotificationService;
    private final UserService userService;
    private String categoryName;

    @Autowired
    public AdvancedProductController(ProductService productService, CategoryService categoryService, UserNotificationService userNotificationService, UserService userService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.userNotificationService = userNotificationService;
        this.userService = userService;
    }

    @GetMapping
    public String categoriesList(Model model, Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications",dtos);
        }

        List<CategoryDTO> categoryDTOS = categoryService.getAll();
        model.addAttribute("categories", categoryDTOS);
        return "advancedProducts";
    }

    @GetMapping("/{categories}")
    public String outProductsByCategory(@PathVariable String categories, Model model, Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications",dtos);
        }

        List<CategoryDTO> categoryDTOS = categoryService.getAll();
        List<ProductDTO> productDTOList = productService.getProductsByCategory(categories);
        categoryName = categories;
        model.addAttribute("categories", categoryDTOS);
        model.addAttribute("products", productDTOList);
        return "advancedProducts";
    }

    @PostMapping("/new")
    public String createNewProduct(Model model, ProductDTO productDTO,
                                   @RequestParam(name = "categories") String category,Principal principal) {

        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications",dtos);
        }

        if (productService.save(productDTO)) {
            productService.addCategoryToProduct(category, productDTO);
            return "redirect:/advanced/products";
        } else {
            model.addAttribute("product", productDTO);
            return "productCreate";
        }
    }

    @GetMapping("/new")
    public String newProduct(Model model,Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications",dtos);
        }

        model.addAttribute("product", new ProductDTO());
        model.addAttribute("categories", categoryService.getAll());
        return "productCreate";
    }

    @GetMapping("/{id}/remove")
    public String removeProduct(@PathVariable Long id) {
        try {
            productService.remove(id);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {

        }

        return "redirect:/advanced/products/" + categoryName;
    }

    @GetMapping("/{id}/bucket")
    public String addBucket(@PathVariable Long id, Principal principal) {
        productService.addToUserBucket(id, principal.getName());
        return "redirect:/advanced/products/" + categoryName;
    }

    @GetMapping("/category/new")
    public String newCategoryPage(Model model,Principal principal){
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications",dtos);
        }

        model.addAttribute("category",new CategoryDTO());
        return "categoryCreate";
    }

    @PostMapping("/category/new")
    public String createNewCategory(CategoryDTO categoryDTO){
        productService.saveCategory(categoryDTO);
        return "redirect:/advanced/products";
    }
    @GetMapping("/{title}/details")
    public String productDetails(Model model, @PathVariable String title,Principal principal){
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications",dtos);
        }

         ProductDTO dto = productService.getProductByName(title);
        model.addAttribute("product",dto);
        return "productDetails";
    }
    @GetMapping("/{name}/edit")
    public String editProductPage(Model model,@PathVariable String name, Principal principal){
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications",dtos);
        }

        ProductDTO dto = productService.getProductByName(name);
        System.out.println(dto);
        model.addAttribute("product",dto);
        model.addAttribute("categories", categoryService.getAll());
        return "productCreate";
    }
}
