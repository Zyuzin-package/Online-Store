package com.example.diplom.serviceImp;

import com.example.diplom.dao.UserRepository;
import com.example.diplom.domain.Role;
import com.example.diplom.domain.UserM;
import com.example.diplom.dto.UserDTO;
import com.example.diplom.mapper.UserMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private MailSender mailSender;
    @MockBean
    private PasswordEncoder passwordEncoder;

    private final UserMapper mapper = UserMapper.MAPPER;

    @Test
    void save() {
        UserDTO newUser = new UserDTO();
        newUser.setUsername("korka");
        newUser.setEmail("korka@mail.com");
        boolean isUserCreated = userService.save(newUser);

        assertTrue(isUserCreated);
//
//        UserM userM = userService.findByName("korka");
//
//        assertNotNull(userM.getActivationCode());
//        assertTrue(CoreMatchers.is(userM.getRole()).matches(Role.CLIENT));
//
//        Mockito.verify(mailSender,Mockito.times(1))
//                .send(
//                        ArgumentMatchers.eq(newUser.getEmail()),
//                        ArgumentMatchers.anyString(),
//                        ArgumentMatchers.anyString());
    }

    @Test
    void addUserFailTest() {
        UserDTO newUser = new UserDTO();
        newUser.setUsername("krok");
        newUser.setEmail("kork@mail.ru");
        Mockito.doReturn(new UserM())
                .when(userRepository)
                .findFirstByName("krok");

        boolean isUserCreated = userService.save(newUser);
        assertFalse(isUserCreated);
    }

    @Test
    void activationUserTest() {
        UserM userM = new UserM();
        userM.setName("KORKA");
        userM.setActivationCode("CODE");
        userM.setPassword("1");
        userM.setRole(Role.CLIENT);

        Mockito.doReturn(userM)
                .when(userRepository)
                .findByActivationCode("activate");

        Mockito.doReturn(userM)
                .when(userRepository)
                .save(ArgumentMatchers.anyObject());

        UserDTO dto = userService.activateUser("activate");
        assertNull(dto.getActivationCode());
    }

}