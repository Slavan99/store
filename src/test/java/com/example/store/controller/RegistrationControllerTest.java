package com.example.store.controller;

import com.example.store.entity.User;
import com.example.store.exception.IncorrectDataException;
import com.example.store.exception.UserAlreadyExistAuthenticationException;
import com.example.store.service.RegistrationService;
import com.example.store.util.ExceptionMessages;
import com.example.store.util.HttpMessage;
import com.example.store.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RegistrationControllerTest {

    @Mock
    private RegistrationService registrationService;

    @Autowired
    @InjectMocks
    private RegistrationController registrationController;

    private User user1;

    @BeforeEach
    private void prepareUser() {
        MockitoAnnotations.openMocks(this);
        user1 = new User("User1@mail", "User1", "password1", true, new HashSet<>());
    }

    @Test
    public void addUserTestSuccess() throws Exception {

        Mockito.lenient()
               .when(registrationService.registerUser(user1))
               .thenReturn(user1);
        Message message = new HttpMessage(200, ExceptionMessages.USER_REGISTERED);
        assertEquals(message, registrationController.addUser(user1));
    }

    @Test
    public void addUserTestUserExists() throws Exception {
        Mockito.lenient()
               .when(registrationService.registerUser(user1))
               .thenThrow(new UserAlreadyExistAuthenticationException(ExceptionMessages.USER_EXISTS));
        Message message = new HttpMessage(409, ExceptionMessages.USER_EXISTS);
        assertEquals(message, registrationController.addUser(user1));
    }

    @Test
    public void addUserTestIncorrectData() throws Exception {
        Mockito.lenient()
               .when(registrationService.registerUser(user1))
               .thenThrow(new IncorrectDataException(ExceptionMessages.INCORRECT_DATA_INPUT));
        Message message = new HttpMessage(400, ExceptionMessages.INCORRECT_DATA_INPUT);
        assertEquals(message, registrationController.addUser(user1));
    }

    @Test
    public void addUserTestInternalError() throws Exception {
        Mockito.lenient()
               .when(registrationService.registerUser(user1))
               .thenThrow(new Exception());
        Message message = new HttpMessage(500, ExceptionMessages.INTERNAL_SERVER_ERROR);
        assertEquals(message, registrationController.addUser(user1));
    }

}