package com.eservice.eumowy.auth;

import java.util.ArrayList;
import java.util.List;

public class FakeAuthenticator implements Authenticator {
    private List<AuthUser> users = new ArrayList<AuthUser>();

    @Override
    public AuthUser auth(String username, String password) throws AuthenticationFailed {
        AuthUser user = findUser(username, password);
        if (user == null) {
            throw new AuthenticationFailed("User not found");
        }
        return user;
    }

    public void setUsers(List<AuthUser> users) {
        this.users = users;
    }

    private AuthUser findUser(String username, String password) {
        for (AuthUser user : users) {
            if (user.username.equals(username) && user.password.equals(password)) {
                return user;
            }
        }
        return null;
    }
}
