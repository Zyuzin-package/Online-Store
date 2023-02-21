package com.example.diplom.controllers;

import com.example.diplom.domain.Role;
import com.example.diplom.dto.UserDTO;
import com.example.diplom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private UserDTO userDTO;

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
        userDTO = dto;
        return "login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code,HttpServletRequest request){
        UserDTO dto = userService.activateUser(code);
        System.out.println("\nDTO ACTIVATE: "+dto);
        if(dto != null){
            model.addAttribute("message","User successfully activated");
        } else {
            model.addAttribute("message","Activation code is not found");
            return "registration";
        }

        try {
            request.login(userDTO.getUsername(), userDTO.getPassword());
            userDTO=new UserDTO();
        } catch (ServletException e) {
            System.out.println("Error while login \n" + e);
        }
        return "index";
    }
}
