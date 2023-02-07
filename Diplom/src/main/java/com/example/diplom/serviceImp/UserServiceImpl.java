package com.example.diplom.serviceImp;

import com.example.diplom.dao.UserRepository;
import com.example.diplom.domain.Role;
import com.example.diplom.domain.UserM;
import com.example.diplom.dto.UserDTO;
import com.example.diplom.mapper.ProductMapper;
import com.example.diplom.mapper.UserMapper;
import com.example.diplom.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper = UserMapper.MAPPER;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean save(UserDTO userDTO) {
        if (!Objects.equals(userDTO.getPassword(), userDTO.getMatchPassword())) {
            throw new RuntimeException("Password is no equals");
        }
        UserM user = UserM.builder()
                .name(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .email(userDTO.getEmail())
                .role(Role.CLIENT)
                .build();
        userRepository.save(user);
        return true;
    }

    @Override
    public void save(UserM userM) {
        userRepository.save(userM);
    }

    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserM findByName(String name) {
        return userRepository.findFirstByName(name);
    }

    @Override
    public void updateProfile(UserDTO dto) {
        UserM savedUser = userRepository.findFirstByName(dto.getUsername());

        if (savedUser == null) {
            throw new RuntimeException("User with name " + dto.getUsername() + " not found");
        }
        boolean isChanged = false;
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            savedUser.setPassword(passwordEncoder.encode(dto.getPassword()));
            isChanged = true;
        }
        if (!Objects.equals(dto.getEmail(), savedUser.getEmail())) {
            savedUser.setEmail(dto.getEmail());
            isChanged = true;
        }
        if (isChanged) {
            userRepository.save(savedUser);
        }
    }

    @Override
    public String getRoleByUserId(Long id) {
        return userRepository.findFirstById(id).getRole().toString();
    }

    @Override
    public void updateRole(UserDTO userDTO, String role) {
        userRepository.updateRole(role, findByName(userDTO.getUsername()).getId());
    }

    private UserDTO toDTO(UserM userM) {
        return UserDTO.builder()
                .username(userM.getName())
                .email(userM.getEmail())
                .role(userM.getRole().name())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserM user = userRepository.findFirstByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with name: " + username);
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getRole().name()));

        return new org.springframework.security.core.userdetails.User(
                user.getName(), user.getPassword(), roles
        );
    }


}
