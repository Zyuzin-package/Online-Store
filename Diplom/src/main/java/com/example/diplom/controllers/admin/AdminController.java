package com.example.diplom.controllers.admin;

import com.example.diplom.domain.OrderStatus;
import com.example.diplom.domain.Role;
import com.example.diplom.domain.UserM;
import com.example.diplom.domain.statistics.BuyStats;
import com.example.diplom.domain.statistics.FrequencyAddToCartStats;
import com.example.diplom.domain.statistics.VisitStats;
import com.example.diplom.dto.*;
import com.example.diplom.dto.statistics.BuyStatsDTO;
import com.example.diplom.dto.statistics.FrequencyAddToCartStatsDTO;
import com.example.diplom.dto.statistics.VisitStatsDTO;
import com.example.diplom.service.*;
import com.example.diplom.service.statistics.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
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
    private final StatsService<BuyStats, BuyStatsDTO> buyStatsService;
    private final StatsService<FrequencyAddToCartStats, FrequencyAddToCartStatsDTO> frequencyAddToCartStatsService;
    private final StatsService<VisitStats, VisitStatsDTO> visitStatsService;
    private String username;
    private Long orderId;

    private String productTitle = "";

    @Autowired
    public AdminController(
            ProductService productService,
            UserNotificationService userNotificationService,
            CategoryService categoryService,
            UserService userService,
            OrderService orderService,
            DiscountService discountService,
            StatsService<BuyStats, BuyStatsDTO> buyStatsService,
            StatsService<FrequencyAddToCartStats, FrequencyAddToCartStatsDTO>frequencyAddToCartStatsService,
            StatsService<VisitStats, VisitStatsDTO> visitStatsService) {
        this.productService = productService;
        this.userNotificationService = userNotificationService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.orderService = orderService;
        this.discountService = discountService;
        this.buyStatsService = buyStatsService;
        this.frequencyAddToCartStatsService = frequencyAddToCartStatsService;
        this.visitStatsService = visitStatsService;
    }

    /**
     * Product
     *
     * @param model
     * @param productDTO
     * @param category
     * @param principal
     * @param request
     * @return
     */
    @PostMapping("/product/new")
    public String createNewProduct(
            Model model,
            ProductDTO productDTO,
            @RequestParam(name = "categories") String category,
            Principal principal,
            HttpServletRequest request,
            @RequestParam(name = "discount") String discount,
            @RequestParam("file") MultipartFile file
    ) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications", dtos);
        }

        if (productDTO.getTitle() == null
                || productDTO.getDescription() == null
                || productDTO.getPrice() <= 0
                || (file.isEmpty() && (productTitle.equals("") || productTitle.isEmpty()))
        ) {
            model.addAttribute("message", "All fields must be filled");
            model.addAttribute("product", productDTO);
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("discount", DiscountDTO.builder().discount_price(Double.parseDouble(discount)).build());
            return "productCreate";
        }

        if (Double.parseDouble(discount) >= productDTO.getPrice()) {
            model.addAttribute("message", "Discount must be less product price");
            model.addAttribute("product", productDTO);
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("discount", DiscountDTO.builder().discount_price(Double.parseDouble(discount)).build());
            return "productCreate";
        }

        if (productService.save(productDTO, file, category, Double.valueOf(discount))) {
            model.addAttribute("product", productDTO);
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("discount", DiscountDTO.builder().discount_price(Double.parseDouble(discount)).build());
            return "redirect:/category";
        } else {
            model.addAttribute("message", "Server error, could not save.");
            model.addAttribute("product", productDTO);
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("discount", DiscountDTO.builder().discount_price(Double.parseDouble(discount)).build());
            return "productCreate";
        }
    }

    @GetMapping("/product/new")
    public String newProduct(Model model, Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications", dtos);
        }

        if (categoryService.getAll() == null) {
            model.addAttribute("errorMessage", "Can't add product because there are no categories");
            return "error";
        }

        model.addAttribute("product", new ProductDTO());
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("discount", new DiscountDTO());
        return "productCreate";
    }

    @GetMapping("/product/{id}/remove")
    public String removeProduct(@PathVariable Long id, HttpServletRequest request, Model model) {
        if (productService.removeWithPhoto(id)) {
            return "redirect:" + request.getHeader("Referer");
        } else {
            model.addAttribute("errorMessage", "Error while deleting");
            return "error";
        }
    }

    @GetMapping("/product/edit/{title}")
    public String editProductPage(Model model, @PathVariable String title, Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications", dtos);
        }
        productTitle = title;
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
    public String createCategory(CategoryDTO categoryDTO, Model model) {
        if (categoryService.saveCategory(categoryDTO)) {
            return "redirect:/category";
        } else {
            model.addAttribute("errorMessage", "Error when save category");
            return "error";
        }
    }

    @GetMapping("category/{title}/remove")
    public String removeCategory(@PathVariable String title, Model model) {
        if (productService.removeProductsByCategoryName(title)
                && categoryService.removeCategoryByName(title)) {
            return "redirect:/category";
        } else {
            model.addAttribute("errorMessage", "Error while deleting");
            return "error";
        }
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
    public String updateRole(@RequestParam(name = "roles") String role, Principal principal, Model model) {
        if (role.equals(Role.ADMIN.name()) && !userService.findByName(principal.getName()).getRole().name().equals(Role.ADMIN.name())) {
            model.addAttribute("errorMessage", "You do not have enough rights to change the user role to administrator");
            return "error";
        }

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

    /**
     * Statistics
     */
    @GetMapping("/stats")
    public String statisticsPage(Model model) {
        model.addAttribute("products", productService.getAll());
        model.addAttribute("visitStats", visitStatsService.getAll());
        model.addAttribute("buyStats", buyStatsService.getAll());
        model.addAttribute("frequencyAddToCartStats", frequencyAddToCartStatsService.getAll());
        return "statistics";
    }

    @GetMapping("/stats/{title}")
    public String statisticsDetails(@PathVariable String title, Model model) {

        List<VisitStatsDTO> visitStatsDTOList = visitStatsService.getAllBuyProductName(title);
        List<BuyStatsDTO> buyStatsDTOS = buyStatsService.getAllBuyProductName(title);
        List<FrequencyAddToCartStatsDTO> frequencyAddToCartStatsDTOS = frequencyAddToCartStatsService.getAllBuyProductName(title);


        model.addAttribute("visitStatsMap",visitStatsService.calculateStatsByProductName(title));
        model.addAttribute("buyStatsMap",buyStatsService.calculateStatsByProductName(title));
        model.addAttribute("frequencyAddToCartStatsMap",frequencyAddToCartStatsService.calculateStatsByProductName(title));



        model.addAttribute("visitStats", visitStatsDTOList);
        model.addAttribute("buyStats", buyStatsDTOS);
        model.addAttribute("frequencyAddToCartStats",frequencyAddToCartStatsDTOS);

        model.addAttribute("products", productService.getAll());
        return "statistics";
    }
}
