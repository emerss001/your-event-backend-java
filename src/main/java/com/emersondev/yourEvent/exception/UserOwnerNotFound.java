package com.emersondev.yourEvent.exception;

public class UserOwnerNotFound extends RuntimeException {
    public UserOwnerNotFound(String message) {
        super(message);
    }
}
