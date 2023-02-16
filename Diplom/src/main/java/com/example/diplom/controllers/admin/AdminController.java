package com.example.diplom.controllers.admin;

import com.example.diplom.dto.CategoryDTO;
import com.example.diplom.dto.ProductDTO;
import com.example.diplom.dto.UserNotificationDTO;
import com.example.diplom.service.CategoryService;
import com.example.diplom.service.ProductService;
import com.example.diplom.service.UserNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final ProductService productService;
    private final UserNotificationService userNotificationService;
    private final CategoryService categoryService;

    @Autowired
    public AdminController(ProductService productService, UserNotificationService userNotificationService, CategoryService categoryService) {
        this.productService = productService;
        this.userNotificationService = userNotificationService;
        this.categoryService = categoryService;
    }

    @PostMapping("/product/new")
    public String createNewProduct(Model model, ProductDTO productDTO,
                                   @RequestParam(name = "categories") String category, Principal principal, HttpServletRequest request) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications", dtos);
        }

        if (productService.save(productDTO)) {
            productService.addCategoryToProduct(category, productDTO);
            return "redirect:/category";
        } else {
            model.addAttribute("product", productDTO);
            return "productCreate";
        }
    }

    @GetMapping("/product/new")
    public String newProduct(Model model, Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications", dtos);
        }

        model.addAttribute("product", new ProductDTO());
        model.addAttribute("categories", categoryService.getAll());
        return "productCreate";
    }

    @GetMapping("/product/{id}/remove")
    public String removeProduct(@PathVariable Long id, HttpServletRequest request) {
        try {
            productService.remove(id);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {

        }

        return "redirect:"+request.getHeader("Referer");
    }

    @GetMapping("/product/edit/{title}")
    public String editProductPage(Model model, @PathVariable String title, Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications", dtos);
        }

        ProductDTO dto = productService.getProductByName(title);
        model.addAttribute("product", dto);
        model.addAttribute("categories", categoryService.getAll());
        return "productCreate";
    }
    //Category
    @GetMapping("/category/new")
    public String newCategoryPage(Model model, Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications", dtos);
        }

        model.addAttribute("category", new CategoryDTO());
        return "categoryCreate";
    }

    @PostMapping("/category/new")
    public String createNewCategory(CategoryDTO categoryDTO) {
        productService.saveCategory(categoryDTO);
        return "redirect:/product";
    }
}
