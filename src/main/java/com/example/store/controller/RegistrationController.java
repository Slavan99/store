package com.example.store.controller;


import com.example.store.entity.User;
import com.example.store.exception.IncorrectDataException;
import com.example.store.exception.UserAlreadyExistAuthenticationException;
import com.example.store.service.RegistrationService;
import com.example.store.util.ExceptionMessages;
import com.example.store.util.HttpMessage;
import com.example.store.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping(
            path = "/registration",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Message addUser(@RequestBody User user) {
        try {
            registrationService.registerUser(user);
        } catch (IncorrectDataException e) {
            return new HttpMessage(400, e.getMessage());
        } catch (UserAlreadyExistAuthenticationException e) {
            return new HttpMessage(409, e.getMessage());
        } catch (Exception e) {
            return new HttpMessage(500, ExceptionMessages.INTERNAL_SERVER_ERROR);
        }
        return new HttpMessage(200, ExceptionMessages.USER_REGISTERED);
    }

}