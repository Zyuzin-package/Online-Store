package com.example.diplom.service;

import com.example.diplom.domain.Bucket;
import com.example.diplom.domain.UserM;
import com.example.diplom.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    boolean save(UserDTO userDTO);

    void saveBucket(Bucket bucket, UserM userM);

    void save(UserM userM);

    List<UserDTO> getAll();

    UserM findByName(String name);
    UserM findById(Long id);

    void updateProfile(UserDTO userDTO);

    void updateRole(String username, String role);

    UserDTO activateUser(String code);
}
