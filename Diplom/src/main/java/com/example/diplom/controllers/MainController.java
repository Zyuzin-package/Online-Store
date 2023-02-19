package com.example.diplom.controllers;

import com.example.diplom.dto.UserNotificationDTO;
import com.example.diplom.service.UserNotificationService;
import com.example.diplom.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
public class MainController {

    UserNotificationService userNotificationService;
    UserService userService;
    public MainController(UserNotificationService userNotificationService, UserService userService) {

        this.userNotificationService = userNotificationService;
        this.userService = userService;
    }

    @RequestMapping({"", "/"})
    public String index(Model model, Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications",dtos);
        }

        return "index";
    }

    @RequestMapping({"/login"})
    public String login() {
        return "login";
    }

    @RequestMapping({"/login-error"})
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }
}
