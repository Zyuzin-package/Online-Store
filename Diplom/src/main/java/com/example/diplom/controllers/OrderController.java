package com.example.diplom.controllers;

import com.example.diplom.dto.OrderDTO;
import com.example.diplom.dto.OrderDetailsDTO;
import com.example.diplom.dto.ProductDTO;
import com.example.diplom.dto.UserNotificationDTO;
import com.example.diplom.service.OrderService;
import com.example.diplom.service.ProductService;
import com.example.diplom.service.UserNotificationService;
import com.example.diplom.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/order/")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final ProductService productService;
    private final UserNotificationService userNotificationService;

    public OrderController(OrderService orderService, UserService userService, ProductService productService, UserNotificationService userNotificationService) {
        this.orderService = orderService;
        this.userService = userService;
        this.productService = productService;
        this.userNotificationService = userNotificationService;
    }

    @GetMapping("/orders")
    public String getOrders(Model model, Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications", dtos);
        }

        List<OrderDTO> orders = orderService.getOrderByUserName(principal.getName());
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/{id}")
    public String getCurrentOrders(Model model, @PathVariable Long id, Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications", dtos);
        }

        OrderDTO order = orderService.findOrderById(id);

        if (order == null || (!Objects.equals(order.getUserId(), userService.findByName(principal.getName()).getId()))) {
            model.addAttribute("errorMessage", "Order not found");
            return "error";
        }

        List<OrderDetailsDTO> orderDetailsDTOS = orderService.getDetailsByOrderId(id);
        List<ProductDTO> productDTOList = productService.getProductsByUserIds(id);
        model.addAttribute("order", order);
        model.addAttribute("details", orderDetailsDTOS);
        model.addAttribute("products", productDTOList);
        return "orderDetails";
    }

    @GetMapping("/new")
    public String createOrder(Model model, Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications", dtos);
        }

        model.addAttribute("order", new OrderDTO());
        return "createOrder";
    }

    @PostMapping("/new")
    public String saveOrder(OrderDTO orderDTO, Principal principal,Model model) {
        orderDTO.setUserId(userService.findByName(principal.getName()).getId());

        if (orderService.save(orderDTO)) {
            return "redirect:/order/orders";

        }else {
            model.addAttribute("errorMessage", "Server Error, order are not save");
            return "error";
        }
    }

}
