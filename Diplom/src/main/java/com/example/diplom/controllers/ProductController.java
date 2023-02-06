package com.example.diplom.controllers;


import com.example.diplom.dto.ProductDTO;
import com.example.diplom.service.ProductService;
import com.example.diplom.serviceImp.SessionObjectHolder;
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
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final SessionObjectHolder sessionObjectHolder;

    @Autowired
    public ProductController(ProductService productService, SessionObjectHolder sessionObjectHolder) {
        this.productService = productService;
        this.sessionObjectHolder = sessionObjectHolder;
    }

    @GetMapping
    public String list(Model model) {
        sessionObjectHolder.addClick();
        List<ProductDTO> list = productService.getAll();
        model.addAttribute("products", list);
        return "products";
    }

    @GetMapping("/{id}/bucket")
    public String addBucket(@PathVariable Long id, Principal principal) {
        sessionObjectHolder.addClick();
        if (principal == null) {
            return "redirect:/products";
        }
        productService.addToUserBucket(id, principal.getName());
        return "redirect:/products";
    }

    @GetMapping("/new")
    public String newProduct(Model model) {
        model.addAttribute("product", new ProductDTO());
        return "productCreate";
    }

    @PostMapping("/new")
    public String createNewProduct(Model model, ProductDTO productDTO) {
        if (productService.save(productDTO)) {
            return "redirect:/products";
        } else {
            model.addAttribute("product", productDTO);
            return "productCreate";
        }
    }
    @GetMapping("/{id}/remove")
    public String removeProduct(@PathVariable Long id){
        productService.remove(id);
        return "redirect:/products";
    }
}
