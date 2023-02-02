package com.example.diplom.controllers;

import com.example.diplom.domain.Bucket;
import com.example.diplom.domain.UserM;
import com.example.diplom.dto.BucketDTO;
import com.example.diplom.service.BucketService;
import com.example.diplom.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class BucketController {
    private final BucketService bucketService;
    private final UserService userService;

    public BucketController(BucketService bucketService, UserService userService) {
        this.bucketService = bucketService;
        this.userService = userService;
    }

    @GetMapping("/bucket")
    public String aboutBucket(Model model, Principal principal) {
        if (principal == null) {
            model.addAttribute("bucket", new BucketDTO());
        } else {
            BucketDTO bucketDTO = bucketService.getBucketByUser(principal.getName());
            model.addAttribute("bucket", bucketDTO);
        }
        return "bucket";
    }

    @GetMapping("/bucket/{id}/remove")
    public String removeFromBucket(Model model, Principal principal, @PathVariable Long id) {
        UserM userM = userService.findByName(principal.getName());
        if (userM == null) {
            throw new RuntimeException("User - " + principal.getName() + " not found");
        }
        bucketService.removeProduct(userM.getBucket(),id);
        BucketDTO bucketDTO = bucketService.getBucketByUser(principal.getName());
        model.addAttribute("bucket", bucketDTO);

        return "redirect:/bucket";
    }

    @GetMapping("/bucket/clear")
    public String cleanBucket(Model model,Principal principal){
        UserM userM = userService.findByName(principal.getName());
        if (userM == null) {
            throw new RuntimeException("User - " + principal.getName() + " not found");
        }
        bucketService.clearBucket(userM.getBucket(),principal.getName());

        BucketDTO bucketDTO = bucketService.getBucketByUser(principal.getName());
        model.addAttribute("bucket", bucketDTO);
        return "redirect:/bucket";
    }
}
