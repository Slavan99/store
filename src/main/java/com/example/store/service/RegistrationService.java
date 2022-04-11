package com.example.store.service;

import com.example.store.entity.Role;
import com.example.store.entity.User;
import com.example.store.exception.IncorrectDataException;
import com.example.store.exception.UserAlreadyExistAuthenticationException;
import com.example.store.util.ExceptionMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UserService userService,
                               PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) throws Exception {
        checkCorrectRegistrationDataInput(user);

        User byName = userService.getUserByName(user.getName());
        User byEmail = userService.getUserByEmail(user.getEmail());
        if (byName != null || byEmail != null) {
            throw new UserAlreadyExistAuthenticationException(ExceptionMessages.USER_EXISTS);
        }

        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.addRole(Role.USER);
        return userService.saveUser(user);
    }

    private void checkCorrectRegistrationDataInput(User user) throws IncorrectDataException {
        if (user.getName().equals("") || user.getPassword().equals("") || user.getEmail().equals("")) {
            throw new IncorrectDataException(ExceptionMessages.INCORRECT_DATA_INPUT);
        }
    }

}
