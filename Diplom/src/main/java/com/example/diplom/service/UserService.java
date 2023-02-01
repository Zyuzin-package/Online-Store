package com.example.diplom.service;

import com.example.diplom.domain.UserM;
import com.example.diplom.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    boolean save(UserDTO userDTO);
    List<UserDTO> getAll();

    UserM findByName(String name);
    void updateProfile(UserDTO userDTO);

    void updateProfileName(UserDTO userDTO, String name);

}
