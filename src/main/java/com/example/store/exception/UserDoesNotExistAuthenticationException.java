package com.example.store.exception;

import org.springframework.security.core.AuthenticationException;

public class UserDoesNotExistAuthenticationException extends AuthenticationException {
    public UserDoesNotExistAuthenticationException(String msg) {
        super(msg);
    }
}
