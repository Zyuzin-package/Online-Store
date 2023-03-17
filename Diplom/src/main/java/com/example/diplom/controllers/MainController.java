package com.example.diplom.controllers;

import com.example.diplom.domain.UserM;
import com.example.diplom.dto.UserNotificationDTO;
import com.example.diplom.service.UserNotificationService;
import com.example.diplom.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
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
            model.addAttribute("notifications", dtos);
            model.addAttribute("notificationsCount",dtos.size());

        }

        return "index";
    }

    @RequestMapping({"/login"})
    public String login(Model model) {
        return "login";
    }

    @RequestMapping({"/authentication"})
    @PostMapping({"/authentication"})
    public String authentication(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password, Model model, HttpServletRequest request) {
        UserM userM  = userService.findByName(username);
        if(userM == null){
            model.addAttribute("errorMessage", "User with name "+ username + " not found");
            return "error";
        }
        if(userM.getActivationCode()==null) {
            try {
                request.login(username, password);
                return "redirect:/";
            } catch (ServletException e) {
                model.addAttribute("errorMessage", "Authorisation error");
                return "error";
            }
        } else {
            model.addAttribute("loginError", true);
            return "login";
        }
    }

    @RequestMapping({"/login-error"})
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }
    @GetMapping({"/error"})
    public String error(Model model){
        model.addAttribute("errorMessage","ERROR!");
        return "error";
    }

    @RequestMapping({"/forbidden"})
    public String forbiddenPage(Model model) {
        model.addAttribute("errorMessage","Forbidden");
        return "error";
    }
}
