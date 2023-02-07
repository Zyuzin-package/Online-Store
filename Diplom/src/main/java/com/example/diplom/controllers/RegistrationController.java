package com.example.diplom.controllers;

import com.example.diplom.domain.Role;
import com.example.diplom.dto.UserDTO;
import com.example.diplom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String outRegisterPage(Model model) {
        model.addAttribute("user", new UserDTO());
        return "registration";
    }

    @PostMapping
    public String userRegister(HttpServletRequest request, UserDTO dto) {
        dto.setRole(Role.CLIENT.name());
        userService.save(dto);
        try {
            request.login(dto.getPassword(), dto.getPassword());
        } catch (ServletException e) {
            System.out.println("Error while login \n" + e);
        }
        return "index";
    }

}
