package com.example.diplom.controllers;

import com.example.diplom.service.UserNotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/notification/")
public class UserNotificationController {

    UserNotificationService userNotificationService;

    public UserNotificationController(UserNotificationService userNotificationService) {
        this.userNotificationService = userNotificationService;
    }

    @GetMapping("/remove/{id}")
    public String removeNotification(@PathVariable Long id,Model model,HttpServletRequest request){
        if (userNotificationService.getNotificationsById(id)==null){
            model.addAttribute("errorMessage", "Notification does not exist");
            return "error";
        }
        userNotificationService.deleteNotificationByUserId(id);
        return "redirect:"+request.getHeader("Referer");
    }

}
