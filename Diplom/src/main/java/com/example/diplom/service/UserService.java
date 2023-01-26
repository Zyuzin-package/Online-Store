package com.example.diplom.service;

import com.example.diplom.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    boolean save(UserDTO userDTO);

}
