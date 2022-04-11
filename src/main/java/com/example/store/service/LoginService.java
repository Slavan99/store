package com.example.store.service;

import com.example.store.controller.request.ChangePasswordRequest;
import com.example.store.entity.User;
import com.example.store.exception.IncorrectDataException;
import com.example.store.exception.UserDoesNotExistAuthenticationException;
import com.example.store.exception.WrongPasswordException;
import com.example.store.repository.AuthenticatedUsersRepository;
import com.example.store.util.ExceptionMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final CartService cartService;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticatedUsersRepository authenticatedUsersRepository;

    @Autowired
    public LoginService(CartService cartService,
                        UserService userService,
                        PasswordEncoder passwordEncoder,
                        AuthenticatedUsersRepository authenticatedUsersRepository) {
        this.cartService = cartService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticatedUsersRepository = authenticatedUsersRepository;
    }

    public void loginUser(User user, String sessionId) throws Exception {
        checkCorrectLoginDataInput(user);
        authenticateUser(user);

        String username = user.getUsername();
        cartService.deleteActiveCartsByUserName(username);

        if (authenticatedUsersRepository.containsUser(username)) {
            authenticatedUsersRepository.removeUser(username);
        }

        authenticatedUsersRepository.addUser(username, sessionId);
    }

    public void changePassword(ChangePasswordRequest request) throws Exception {
        checkCorrectLoginDataInput(request);
        authenticateUser(request);
        checkCorrectPasswordChangeDataInput(request);
        User user = userService.getUserByName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userService.saveUser(user);

    }

    private void checkCorrectLoginDataInput(User user) throws IncorrectDataException {
        if (user.getName().equals("") || user.getPassword().equals("")) {
            throw new IncorrectDataException(ExceptionMessages.INCORRECT_DATA_INPUT);
        }
    }

    private void checkCorrectPasswordChangeDataInput(ChangePasswordRequest request) throws IncorrectDataException {
        if (request.getNewPassword() == null
                || request.getNewPassword().equals("")
                || request.getNewPassword().equals(request.getPassword())) {
            throw new IncorrectDataException(ExceptionMessages.INCORRECT_NEW_PASSWORD_DATA_INPUT);
        }
    }

    private void authenticateUser(User user) throws WrongPasswordException {
        User byName = userService.getUserByName(user.getName());
        if (byName == null) {
            throw new UserDoesNotExistAuthenticationException(ExceptionMessages.USER_DOES_NOT_EXIST);
        }
        String password = byName.getPassword();
        String currentPassword = user.getPassword();
        if (!passwordEncoder.matches(currentPassword, password)) {
            throw new WrongPasswordException(ExceptionMessages.WRONG_PASSWORD);
        }
    }


}
