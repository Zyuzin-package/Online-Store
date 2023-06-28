package com.example.diplom.controllers;

import com.example.diplom.dto.BucketDTO;
import com.example.diplom.dto.OrderDTO;
import com.example.diplom.dto.UserNotificationDTO;
import com.example.diplom.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/order/")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final ProductService productService;
    private final BucketService bucketService;
    private final UserNotificationService userNotificationService;

    public OrderController(OrderService orderService, UserService userService, ProductService productService, BucketService bucketService, UserNotificationService userNotificationService) {
        this.orderService = orderService;
        this.userService = userService;
        this.productService = productService;
        this.bucketService = bucketService;
        this.userNotificationService = userNotificationService;
    }

    @GetMapping("/orders")
    public String getOrders(Model model, Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications", dtos);
            model.addAttribute("notificationsCount",dtos.size());
        }

        List<OrderDTO> orders = orderService.getOrderByUserName(principal.getName());

        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/new")
    public String createOrder(Model model, Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications", dtos);
            model.addAttribute("notificationsCount",dtos.size());

        }

        if (principal == null) {
            model.addAttribute("bucket", new BucketDTO());
        } else {
            BucketDTO bucketDTO = bucketService.getBucketByUser(principal.getName());
            model.addAttribute("bucket", bucketDTO);
        }

        if(!bucketService.checkBucketProducts(userService.findByName(principal.getName()).getId())){
            model.addAttribute("errorMessage", "You bucket is empty");
            return "error";
        }

        model.addAttribute("order", new OrderDTO());
        return "createOrder";
    }

    @PostMapping("/new")
    public String saveOrder(OrderDTO orderDTO, Principal principal, Model model) {
        if (userService.findByName(principal.getName()) == null) {
            model.addAttribute("errorMessage", "Order not found");
            return "error";
        }
        orderDTO.setUserId(userService.findByName(principal.getName()).getId());

        if (orderService.save(orderDTO)) {
            return "redirect:/order/orders";

        } else {
            model.addAttribute("errorMessage", "Server Error, order are not save");
            return "error";
        }
    }

}
