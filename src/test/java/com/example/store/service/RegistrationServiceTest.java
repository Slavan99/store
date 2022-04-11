package com.example.store.service;

import com.example.store.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @Autowired
    @InjectMocks
    private RegistrationService registrationService;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void registerUserTest() throws Exception {
        User newUser = new User("new@mail", "new", "pass", true, new HashSet<>());
        Mockito.lenient()
               .when(userService.getUserByName(newUser.getName()))
               .thenReturn(null);
        User registerUser = registrationService.registerUser(newUser);
        assertEquals(userService.getUserByName(newUser.getName()), registerUser);
    }
}