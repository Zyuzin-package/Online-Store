package com.example.diplom.serviceImp;

import com.example.diplom.dao.UserRepository;
import com.example.diplom.domain.Bucket;
import com.example.diplom.domain.Role;
import com.example.diplom.domain.UserM;
import com.example.diplom.dto.UserDTO;
import com.example.diplom.mapper.UserMapper;
import com.example.diplom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper = UserMapper.MAPPER;
    private final MailSender mailSender;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, MailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
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
                .activationCode(UUID.randomUUID().toString())
                .build();

        userRepository.save(user);


        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s \n" +
                            "Welcome to Kork-Market. Please visit next link: http://localhost:8080/registration/activate/%s",
                    user.getName(), user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Activation code", message);
        }
        return true;
    }

    @Override
    public void saveBucket(Bucket bucket, UserM userM) {
        userM.setBucket(bucket);
        userRepository.save(userM);
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
    public UserM findById(Long id) {
        return userRepository.findFirstById(id);
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
    public void updateRole(String username, String role) {
        userRepository.updateRole(role, findByName(username).getId());
    }

    @Override
    public UserDTO activateUser(String code) {
        UserM user = userRepository.findByActivationCode(code);

        if (user == null) {
            return null;
        }
        user.setActivationCode(null);
        UserM userM = userRepository.save(user);
        return toDTO(userM);
    }

    private UserDTO toDTO(UserM userM) {
        return UserDTO.builder()
                .username(userM.getName())
                .password(userM.getPassword())
                .email(userM.getEmail())
                .role(userM.getRole().name())
                .activationCode(userM.getActivationCode())
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
