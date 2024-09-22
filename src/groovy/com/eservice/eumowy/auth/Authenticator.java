package com.eservice.eumowy.auth;

public interface Authenticator {
    AuthUser auth(String username, String password) throws AuthenticationFailed;
}
