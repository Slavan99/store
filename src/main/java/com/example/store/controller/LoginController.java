package com.example.store.controller;

import com.example.store.controller.request.ChangePasswordRequest;
import com.example.store.entity.User;
import com.example.store.exception.IncorrectDataException;
import com.example.store.exception.UserDoesNotExistAuthenticationException;
import com.example.store.exception.WrongPasswordException;
import com.example.store.service.LoginService;
import com.example.store.util.ExceptionMessages;
import com.example.store.util.HttpMessage;
import com.example.store.util.Message;
import com.example.store.util.UserSessionMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Message login(@RequestBody User user, HttpSession session) {
        String sessionId = session.getId();
        try {
            loginService.loginUser(user, sessionId);
        } catch (IncorrectDataException | WrongPasswordException e) {
            return new HttpMessage(400, e.getMessage());
        } catch (UserDoesNotExistAuthenticationException e) {
            return new HttpMessage(409, e.getMessage());
        } catch (Exception e) {
            return new HttpMessage(500, ExceptionMessages.INTERNAL_SERVER_ERROR);
        }
        return new UserSessionMessage(user.getUsername(), sessionId);
    }

    @PatchMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Message changePassword(@RequestBody ChangePasswordRequest request) {

        try {
            loginService.changePassword(request);
        } catch (IncorrectDataException | WrongPasswordException e) {
            return new HttpMessage(400, e.getMessage());
        } catch (UserDoesNotExistAuthenticationException e) {
            return new HttpMessage(409, e.getMessage());
        } catch (Exception e) {
            return new HttpMessage(500, ExceptionMessages.INTERNAL_SERVER_ERROR);
        }
        return new HttpMessage(200, ExceptionMessages.PASSWORD_CHANGED);
    }

}
