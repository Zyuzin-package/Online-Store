package com.example.diplom.controllers.admin;

import com.example.diplom.domain.OrderStatus;
import com.example.diplom.domain.Role;
import com.example.diplom.domain.UserM;
import com.example.diplom.dto.*;
import com.example.diplom.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final ProductService productService;
    private final UserNotificationService userNotificationService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final OrderService orderService;
    private final DiscountService discountService;
    private String username;
    private Long orderId;

    @Autowired
    public AdminController(ProductService productService, UserNotificationService userNotificationService, CategoryService categoryService, UserService userService, OrderService orderService, DiscountService discountService) {
        this.productService = productService;
        this.userNotificationService = userNotificationService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.orderService = orderService;
        this.discountService = discountService;
    }

    /**
     * Product
     * @param model
     * @param productDTO
     * @param category
     * @param principal
     * @param request
     * @return
     */
    @PostMapping("/product/new")
    public String createNewProduct(Model model, ProductDTO productDTO,
                                   @RequestParam(name = "categories") String category, Principal principal, HttpServletRequest request,
                                   @RequestParam(name = "discount") String discount,
                                   @RequestParam("file") MultipartFile file
    ) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications", dtos);
        }

        if (productService.save(productDTO,file,category)) {
            productService.addCategoryToProduct(category, productDTO);
            try {
            if (discount.isEmpty() || Double.parseDouble(discount) <= 0) {
                discountService.save(BigDecimal.ZERO, productService.getProductByName(productDTO.getTitle()).getId());
            } else {
                if (BigDecimal.valueOf(Long.parseLong(discount)).compareTo(productDTO.getPrice()) <= 0) {
                    discountService.save(BigDecimal.valueOf(Long.parseLong(discount)), productService.getProductByName(productDTO.getTitle()).getId());
                } else {
                    //TODO: need add catch error
                    model.addAttribute("product", productDTO);
                    model.addAttribute("categories", categoryService.getAll());
                    model.addAttribute("discount", DiscountDTO.builder().discount_price(BigDecimal.valueOf(Long.parseLong(discount))).build());
                    return "productCreate";
                }
            }
            } catch (NumberFormatException e){
                System.out.println("\n\n\nNFE");
            }
            model.addAttribute("product", productDTO);
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("discount", DiscountDTO.builder().discount_price(BigDecimal.valueOf(Long.parseLong(discount))).build());
            return "redirect:/category";
        } else {
            //TODO: need add catch error
            model.addAttribute("product", productDTO);
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("discount", DiscountDTO.builder().discount_price(BigDecimal.valueOf(Long.parseLong(discount))).build());
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
        model.addAttribute("discount", new DiscountDTO());
        return "productCreate";
    }

    @GetMapping("/product/{id}/remove")
    public String removeProduct(@PathVariable Long id, HttpServletRequest request) {
        try {
            productService.remove(id);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {}
        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/product/edit/{title}")
    public String editProductPage(Model model, @PathVariable String title, Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications", dtos);
        }

        ProductDTO dto = productService.getProductByName(title);
        DiscountDTO discountDTO = discountService.findDiscountByProductId(dto.getId());
        model.addAttribute("product", dto);
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("discount", discountDTO);
        return "productCreate";
    }

    /**
     * Category
     *
     * @param model
     * @param principal
     * @return
     */
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
    public String createCategory(CategoryDTO categoryDTO) {
        categoryService.saveCategory(categoryDTO);
        return "redirect:/product";
    }

    @GetMapping("category/{title}/remove")
    public String removeCategory (@PathVariable String title){
        productService.removeProductsByCategoryName(title);
        categoryService.removeCategoryByName(title);
        return "redirect:/category";
    }
    /**
     * User
     *
     * @param model
     * @param principal
     * @return
     */
    @GetMapping("/users")
    public String userList(Model model, Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications", dtos);
        }

        List<String> roles = Stream.of(Role.values())
                .map(Enum::name).toList();
        model.addAttribute("roles", roles);
        model.addAttribute("users", userService.getAll());
        return "userList";
    }

    @PostMapping("/users/edit")
    public String updateRole(@RequestParam(name = "roles") String role) {
        userService.updateRole(username, role);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/{name}/edit")
    public String updateUsersPage(@PathVariable String name, Model model, Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications", dtos);
        }

        UserM userM = userService.findByName(name);
        UserDTO dto = UserDTO.builder()
                .username(userM.getName())
                .email(userM.getEmail())
                .role(userM.getRole().name())
                .build();
        username = name;
        List<String> roles = Stream.of(Role.values())
                .map(Enum::name).toList();
        model.addAttribute("roles", roles);
        model.addAttribute("user", dto);
        return "userEdit";
    }

    /**
     * Order
     */
    @PostMapping("/users/orders")
    public String orderManagementPageEdit(@RequestParam(name = "status") String status, Model model, Principal principal) {
        orderService.updateOrderStatus(status, orderId);
        return "redirect:/admin/users/orders";
    }

    @GetMapping("/users/orders")
    public String orderManagementPage(Model model, Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications", dtos);
        }

        List<String> stats = Stream.of(OrderStatus.values())
                .map(Enum::name).toList();
        List<OrderDTO> orderDTOS = orderService.getAll();

        model.addAttribute("status", stats);
        model.addAttribute("orders", orderDTOS);
        return "orderManagement";
    }

    @GetMapping("/users/orders/{status}")
    public String getOrdersByStatus(@PathVariable String status, Model model, Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications", dtos);
        }

        List<String> stats = Stream.of(OrderStatus.values())
                .map(Enum::name).toList();
        List<OrderDTO> dtos = orderService.getOrderByStatus(status);
        model.addAttribute("orders", dtos);
        model.addAttribute("status", stats);
        return "orderManagement";
    }

    @GetMapping("/users/orders/edit/{id}")
    public String orderEditManagementPage(Model model, Principal principal, @PathVariable Long id) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications", dtos);
        }
        OrderDTO dto = orderService.findOrderById(id);

        List<String> stats = Stream.of(OrderStatus.values())
                .map(Enum::name).toList();
        orderId = id;
        model.addAttribute("orderDTO", dto);
        model.addAttribute("status", stats);
        return "orderEditManagement";
    }


}
