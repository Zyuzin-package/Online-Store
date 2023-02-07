package com.example.diplom.controllers;

import com.example.diplom.domain.Role;
import com.example.diplom.domain.UserM;
import com.example.diplom.dto.UserDTO;
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

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String userList(Model model) {
        List<String> roles = Stream.of(Role.values())
                .map(Enum::name).toList();
        model.addAttribute("roles", roles);
        model.addAttribute("users", userService.getAll());
        return "userList";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new UserDTO());
        return "user";
    }

    @PostMapping("/new")
    public String saveUser(UserDTO userDTO, Model model) {
        if (userService.save(userDTO)) {
            return "redirect:/users";
        } else {
            model.addAttribute("user", userDTO);
            return "user";
        }
    }

    @GetMapping("/profile")
    public String profileUser(Model model, Principal principal) {
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
    public String updateUsersPage(@PathVariable String name, Model model) {
        UserM userM = userService.findByName(name);
        UserDTO dto = UserDTO.builder()
                .username(userM.getName())
                .email(userM.getEmail())
                .role(userM.getRole().name())
                .build();

        List<String> roles = Stream.of(Role.values())
                .map(Enum::name).toList();
        model.addAttribute("roles", roles);
        model.addAttribute("user", dto);
        return "userEdit";
    }

    @PostMapping("/edit")
    public String updateRole(UserDTO userDTO,
                             @RequestParam(name = "roles") String role) {
        userService.updateRole(userDTO,role);
        return "redirect:/users";
    }
}
