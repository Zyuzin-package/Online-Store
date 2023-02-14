package com.example.diplom.controllers;

import com.example.diplom.domain.Order;
import com.example.diplom.domain.OrderDetails;
import com.example.diplom.domain.OrderStatus;
import com.example.diplom.domain.Role;
import com.example.diplom.dto.BucketDTO;
import com.example.diplom.dto.OrderDTO;
import com.example.diplom.dto.OrderDetailsDTO;
import com.example.diplom.dto.ProductDTO;
import com.example.diplom.service.OrderService;
import com.example.diplom.service.ProductService;
import com.example.diplom.service.UserNotificationService;
import com.example.diplom.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Stream;

@Controller
@RequestMapping("/order/")
public class OrderController {

    OrderService orderService;
    UserService userService;
    ProductService productService;
    private Long orderId;

    public OrderController(OrderService orderService, UserService userService, ProductService productService) {
        this.orderService = orderService;
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/orders")
    public String getOrders(Model model, Principal principal) {
        List<OrderDTO> orders = orderService.getOrderByUserName(principal.getName());
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
        model.addAttribute("products", productDTOList);
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

    @GetMapping("/orders/admin")
    public String orderManagementPage(Model model, Principal principal) {
        List<String> stats = Stream.of(OrderStatus.values())
                .map(Enum::name).toList();
        List<OrderDTO> orderDTOS = orderService.getAll();

        model.addAttribute("status", stats);
        model.addAttribute("orders", orderDTOS);
        return "orderManagement";
    }

    @GetMapping("/orders/admin/edit/{id}")
    public String orderEditManagementPage(Model model, Principal principal, @PathVariable Long id) {
        OrderDTO dto = orderService.findOrderById(id);

        List<String> stats = Stream.of(OrderStatus.values())
                .map(Enum::name).toList();
        orderId = id;
        model.addAttribute("orderDTO", dto);
        model.addAttribute("status", stats);
        return "orderEditManagement";
    }

    @GetMapping("/orders/admin/{status}")
    public String getOrdersByStatus(@PathVariable String status, Model model) {
        List<String> stats = Stream.of(OrderStatus.values())
                .map(Enum::name).toList();
        List<OrderDTO> dtos = orderService.getOrderByStatus(status);
        model.addAttribute("orders", dtos);
        model.addAttribute("status", stats);
        return "orderManagement";
    }

    @PostMapping("/orders/admin")
    public String orderManagementPageEdit(@RequestParam(name = "status") String status, Model model, Principal principal) {
        orderService.updateOrderStatus(status, orderId);
        return "redirect:/order/orders/admin";
    }


}
