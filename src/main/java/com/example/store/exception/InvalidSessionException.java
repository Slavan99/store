package com.example.store.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidSessionException extends AuthenticationException {

    public InvalidSessionException(String message) {
        super(message);
    }
}
