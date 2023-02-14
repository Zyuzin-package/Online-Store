package com.example.diplom.controllers;

import com.example.diplom.domain.UserNotification;
import com.example.diplom.service.UserNotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
@RequestMapping("/notification/")
public class UserNotificationController {

    UserNotificationService userNotificationService;

    public UserNotificationController(UserNotificationService userNotificationService) {
        this.userNotificationService = userNotificationService;
    }

    @GetMapping("/remove/{id}")
    public String removeNotification(@PathVariable Long id,Model model, Principal principal,HttpServletRequest request){
        System.out.println(request);
        System.out.println(id);
        userNotificationService.deleteNotificationByUserId(id);
        return "redirect:"+request.getHeader("Referer");
    }

}
