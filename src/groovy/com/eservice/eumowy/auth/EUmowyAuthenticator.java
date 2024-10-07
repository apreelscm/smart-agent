package com.eservice.eumowy.auth;

public interface EUmowyAuthenticator {
    AuthUser auth(String username, String password) throws AuthenticationFailed;
}
