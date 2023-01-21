package com.example.diplom.service;

import com.example.diplom.dao.UserRepository;
import com.example.diplom.domain.User;
import com.example.diplom.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {

    boolean save(UserDTO userDTO);

}
