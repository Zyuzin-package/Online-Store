package com.example.diplom.controllers;

import com.example.diplom.domain.Role;
import com.example.diplom.domain.UserM;
import com.example.diplom.dto.UserDTO;
import com.example.diplom.dto.UserNotificationDTO;
import com.example.diplom.service.UserNotificationService;
import com.example.diplom.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserNotificationService userNotificationService;

    @Autowired
    public UserController(UserService userService, UserNotificationService userNotificationService) {
        this.userService = userService;
        this.userNotificationService = userNotificationService;
    }

    private String username;

    @GetMapping
    public String userList(Model model, Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications",dtos);
        }

        List<String> roles = Stream.of(Role.values())
                .map(Enum::name).toList();
        model.addAttribute("roles", roles);
        model.addAttribute("users", userService.getAll());
        return "userList";
    }

    @GetMapping("/profile")
    public String profileUser(Model model, Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications",dtos);
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

    @PostMapping("/profile")
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
        return "redirect:/users/profile";
    }

    @GetMapping("/{name}/edit")
    public String updateUsersPage(@PathVariable String name, Model model,Principal principal) {
        if (principal != null) {
            List<UserNotificationDTO> dtos = userNotificationService.getNotificationsByUserName(principal.getName());
            model.addAttribute("notifications",dtos);
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

    @PostMapping("/edit")
    public String updateRole(@RequestParam(name = "roles") String role) {
        userService.updateRole(username,role);
        return "redirect:/users";
    }
}
