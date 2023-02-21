package com.example.diplom.service;

import com.example.diplom.domain.UserM;
import com.example.diplom.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    boolean save(UserDTO userDTO);

    void save(UserM userM);

    List<UserDTO> getAll();

    UserM findByName(String name);
    UserM findById(Long id);

    void updateProfile(UserDTO userDTO);

    String getRoleByUserId(Long id);
    void updateRole(String username, String role);

    UserDTO activateUser(String code);
}
