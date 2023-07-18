package com.example.diplom.service;


import com.example.models.domain.statistics.*;
import com.example.models.dto.statistics.*;
import com.example.models.dto.*;
import com.example.models.domain.*;


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
