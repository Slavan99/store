package com.example.store.controller;

import com.example.store.controller.request.ChangePasswordRequest;
import com.example.store.entity.User;
import com.example.store.exception.UserDoesNotExistAuthenticationException;
import com.example.store.exception.WrongPasswordException;
import com.example.store.service.LoginService;
import com.example.store.util.ExceptionMessages;
import com.example.store.util.HttpMessage;
import com.example.store.util.Message;
import com.example.store.util.UserSessionMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    @Mock
    private LoginService loginService;

    @Autowired
    @InjectMocks
    private LoginController loginController;

    private User user1;

    private final String exampleSessionId = "EXAMPLESESSID";

    @BeforeEach
    private void prepareUser() {
        user1 = new User("User1@mail", "User1", "password1", true, new HashSet<>());

    }

    @Test
    public void loginTestSuccess() throws Exception {
        Mockito.lenient()
               .doNothing()
               .when(loginService).loginUser(user1, exampleSessionId);
        Message message = new UserSessionMessage(user1.getName(), exampleSessionId);
        assertEquals(message, loginController.login(user1, new MockHttpSession(null, exampleSessionId)));
    }

    @Test
    public void loginTestWrongPassword() throws Exception {
        Mockito.lenient()
               .doThrow(new WrongPasswordException(ExceptionMessages.WRONG_PASSWORD))
               .when(loginService).loginUser(user1, exampleSessionId);
        Message message = new HttpMessage(400, ExceptionMessages.WRONG_PASSWORD);
        assertEquals(message, loginController.login(user1, new MockHttpSession(null, exampleSessionId)));
    }

    @Test
    public void loginTestUserNotExist() throws Exception {
        Mockito.lenient()
               .doThrow(new UserDoesNotExistAuthenticationException(ExceptionMessages.USER_DOES_NOT_EXIST))
               .when(loginService).loginUser(user1, exampleSessionId);
        Message message = new HttpMessage(409, ExceptionMessages.USER_DOES_NOT_EXIST);
        assertEquals(message, loginController.login(user1, new MockHttpSession(null, exampleSessionId)));
    }

    @Test
    public void changePasswordTest() throws Exception {
        ChangePasswordRequest request = new ChangePasswordRequest(user1.getName(), user1.getPassword(), "newpass");
        Mockito.lenient()
               .doNothing()
               .when(loginService).changePassword(request);
        Message message = new HttpMessage(200, ExceptionMessages.PASSWORD_CHANGED);
        assertEquals(message, loginController.changePassword(request));
    }
}