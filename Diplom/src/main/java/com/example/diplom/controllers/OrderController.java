package com.example.diplom.controllers;

import com.example.diplom.dto.OrderDTO;
import com.example.diplom.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/order/")
public class OrderController {

    OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public String getOrders(Model model){
        List<OrderDTO> orders = orderService.getAll();
        model.addAttribute("orders",orders);
        return "orders";
    }

    @GetMapping("/{id}")
    public String getCurrentOrders(Model model, @PathVariable Long id){
        return null;
//        return "orderDetail";
    }
    @GetMapping("/new")
    public String createOrder(Model model ){
        model.addAttribute("order",new OrderDTO());
        return "createOrder";
    }

    @PostMapping("/new")
    public String saveOrder(Model model,OrderDTO orderDTO ){
        orderService.save(orderDTO);
        return "redirect:/order/orders";
    }
}
