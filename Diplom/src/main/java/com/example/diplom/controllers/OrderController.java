package com.example.diplom.controllers;

import com.example.diplom.domain.Order;
import com.example.diplom.domain.OrderDetails;
import com.example.diplom.dto.BucketDTO;
import com.example.diplom.dto.OrderDTO;
import com.example.diplom.dto.OrderDetailsDTO;
import com.example.diplom.dto.ProductDTO;
import com.example.diplom.service.OrderService;
import com.example.diplom.service.ProductService;
import com.example.diplom.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/order/")
public class OrderController {

    OrderService orderService;
    UserService userService;
    ProductService productService;

    public OrderController(OrderService orderService, UserService userService, ProductService productService) {
        this.orderService = orderService;
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/orders")
    public String getOrders(Model model) {
        List<OrderDTO> orders = orderService.getAll();
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/{id}")
    public String getCurrentOrders(Model model, @PathVariable Long id) {
        OrderDTO order = orderService.findOrderById(id);
        List<OrderDetailsDTO> orderDetailsDTOS = orderService.getDetailsByOrderId(id);
        List<ProductDTO> productDTOList = productService.getProductsByUserIds(id);
        model.addAttribute("order", order);
        model.addAttribute("details", orderDetailsDTOS);
        model.addAttribute("products",productDTOList);
        return "orderDetails";
    }

    @GetMapping("/new")
    public String createOrder(Model model) {
        model.addAttribute("order", new OrderDTO());
        return "createOrder";
    }

    @PostMapping("/new")
    public String saveOrder(OrderDTO orderDTO, Principal principal) {
        orderDTO.setUserId(userService.findByName(principal.getName()).getId());
        orderService.save(orderDTO);
        return "redirect:/order/orders";
    }
}
