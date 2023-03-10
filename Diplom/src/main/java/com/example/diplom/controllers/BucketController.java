package com.example.diplom.controllers;

import com.example.diplom.domain.UserM;
import com.example.diplom.dto.BucketDTO;
import com.example.diplom.dto.UserNotificationDTO;
import com.example.diplom.service.BucketService;
import com.example.diplom.service.ProductService;
import com.example.diplom.service.UserNotificationService;
import com.example.diplom.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
public class BucketController {
    private final BucketService bucketService;
    private final UserService userService;
    private final ProductService productService;
    private final UserNotificationService userNotificationService;

    public BucketController(BucketService bucketService, UserService userService, ProductService productService, UserNotificationService userNotificationService) {
        this.bucketService = bucketService;
        this.userService = userService;
        this.productService = productService;
        this.userNotificationService = userNotificationService;
    }

    @GetMapping("/my/bucket")
    public String aboutBucket(Model model, Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications",dtos);
        }

        if (principal == null) {
            model.addAttribute("bucket", new BucketDTO());
        } else {
            BucketDTO bucketDTO = bucketService.getBucketByUser(principal.getName());
            System.out.println("\n\n"+bucketDTO);
            model.addAttribute("bucket", bucketDTO);
        }
        return "bucket";
    }

    @GetMapping("/my/bucket/{id}/remove")
    public String removeFromBucket(Model model, Principal principal, @PathVariable Long id) {
        UserM userM = userService.findByName(principal.getName());
        if (userM == null) {
            throw new RuntimeException("User - " + principal.getName() + " not found");
        }

        if(productService.findProductById(id) == null){
            model.addAttribute("errorMessage", "Product not found");
            return "error";
        }

        BucketDTO bucketDTO = bucketService.removeProduct(userM.getBucket(),id,principal.getName());

        model.addAttribute("bucket", bucketDTO);

        return "redirect:/my/bucket";
    }

    @GetMapping("/my/bucket/clear")
    public String cleanBucket(Model model,Principal principal){
        UserM userM = userService.findByName(principal.getName());
        if (userM == null) {
            throw new RuntimeException("User - " + principal.getName() + " not found");
        }
        bucketService.clearBucket(userM.getBucket(),principal.getName());

        BucketDTO bucketDTO = bucketService.getBucketByUser(principal.getName());
        model.addAttribute("bucket", bucketDTO);
        return "redirect:/my/bucket";
    }

    @GetMapping("/my/bucket/{id}/increase")
    public String increase(Model model, Principal principal, @PathVariable Long id){
        if(productService.findProductById(id) == null){
            model.addAttribute("errorMessage", "Product not found");
            return "error";
        }

        if (principal == null) {
            model.addAttribute("bucket", new BucketDTO());
        } else {
            bucketService.amountIncrease(userService.findByName(principal.getName()).getBucket(),id);
            BucketDTO bucketDTO = bucketService.getBucketByUser(principal.getName());
            model.addAttribute("bucket", bucketDTO);
        }
        return "redirect:/my/bucket";
    }
    @GetMapping("/my/bucket/{id}/decrease")
    public String decrease(Model model, Principal principal, @PathVariable Long id){
        if(productService.findProductById(id) == null){
            model.addAttribute("errorMessage", "Product not found");
            return "error";
        }
        bucketService.amountDecrease(userService.findByName(principal.getName()).getBucket(),id);
        BucketDTO bucketDTO = bucketService.getBucketByUser(principal.getName());
        model.addAttribute("bucket", bucketDTO);
        return "redirect:/my/bucket";
    }
}
