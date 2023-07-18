package com.example.diplom.controllers;


import com.example.models.domain.statistics.*;
import com.example.models.dto.statistics.*;
import com.example.models.dto.*;
import com.example.models.domain.*;


import com.example.diplom.service.UserNotificationService;
import com.example.diplom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/my")
public class UserController {
    private final UserService userService;
    private final UserNotificationService userNotificationService;

    @Autowired
    public UserController(UserService userService, UserNotificationService userNotificationService) {
        this.userService = userService;
        this.userNotificationService = userNotificationService;
    }

    @GetMapping("/main")
    public String profileUser(Model model, Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications",dtos);
            model.addAttribute("notificationsCount",dtos.size());
        }

        if (principal == null) {
            throw new RuntimeException("You are not authorize");
        }
        UserM userM = userService.findByName(principal.getName());

        UserDTO dto = UserDTO.builder()
                .username(userM.getName())
                .email(userM.getEmail())
                .role(userM.getRole().name())
                .build();
        model.addAttribute("user", dto);
        return "profile";
    }

    @PostMapping("/main")
    public String updateProfileUser(UserDTO dto, Model model, Principal principal) {
        if (dto.getPassword() != null
                && !dto.getPassword().isEmpty()
                && !Objects.equals(dto.getPassword(), dto.getMatchPassword())
        ) {
            model.addAttribute("user", dto);
            return "profile";
        }
        dto.setUsername(principal.getName());
        userService.updateProfile(dto);
        return "redirect:/my/main";
    }



}
