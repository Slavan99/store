package com.example.store.exception;

public class IncorrectDataException extends Exception {
    public IncorrectDataException() {
    }

    public IncorrectDataException(String message) {
        super(message);
    }
}