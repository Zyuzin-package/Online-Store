package com.example.diplom.controllers;


import com.example.diplom.domain.Notification;
import com.example.diplom.domain.UserM;
import com.example.diplom.domain.UserNotification;
import com.example.diplom.dto.UserNotificationDTO;
import com.example.diplom.service.UserNotificationService;
import com.example.diplom.service.UserService;
import com.example.diplom.serviceImp.SessionObjectHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
public class MainController {

    private final SessionObjectHolder sessionObjectHolder;

    UserNotificationService userNotificationService;
    UserService userService;

    public MainController(SessionObjectHolder sessionObjectHolder, UserNotificationService userNotificationService, UserService userService) {
        this.sessionObjectHolder = sessionObjectHolder;
        this.userNotificationService = userNotificationService;
        this.userService = userService;
    }

    @RequestMapping({"", "/"})
    public String index(Model model, HttpSession session, Principal principal) {
        if (principal != null) {
            UserM userM = userService.findByName(principal.getName());
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserId(userM.getId());
            model.addAttribute("notifications",dtos);
        }
        if (session.getAttribute("myID") == null) {
            String uuid = UUID.randomUUID().toString();
            session.setAttribute("myID", uuid);
            System.out.println("Generated UUID: " + uuid);
        }
        model.addAttribute("uuid", session.getAttribute("myID"));
        model.addAttribute("amountClicks", sessionObjectHolder.getAmountClicks());

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
