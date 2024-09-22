package com.eservice.eumowy.auth;

import java.util.Arrays;
import java.util.List;

public class AuthUser {
    public final String username;
    public final String password;
    public final String firstName;
    public final String lastName;
    public final String email;
    public final Long auwId;
    public final String sellerNumber;
    public final List<String> roles;

    AuthUser(
            String username,
            String password,
            String firstName,
            String lastName,
            String email,
            Long auwId,
            String sellerNumber,
            List<String> roles
    ) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.auwId = auwId;
        this.sellerNumber = sellerNumber;
        this.roles = roles;
    }

    public static AuthUser user(
            String username,
            String password,
            String firstName,
            String lastName,
            String email,
            Long auwId,
            String sellerNumber,
            String... roles
    ) {
        return new AuthUser(
                username,
                password,
                firstName,
                lastName,
                email,
                auwId,
                sellerNumber,
                Arrays.asList(roles)
        );
    }
}
