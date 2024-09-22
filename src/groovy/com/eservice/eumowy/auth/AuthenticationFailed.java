package com.eservice.eumowy.auth;

public class AuthenticationFailed extends RuntimeException {
    public AuthenticationFailed(String message) {
        super(message);
    }
}
