package com.eservice.eumowy.auth

import grails.plugin.springsecurity.userdetails.GrailsUser
import org.springframework.security.core.GrantedAuthority

public class EServiceUserDetails extends GrailsUser {

    final String name;
    final String imie;
    final String nazwisko;
    final String nr;
    final String email;
    final Long auwId

    EServiceUserDetails(String username,
                        String password,
                        boolean enabled,
                        boolean accountNonExpired,
                        boolean credentialsNonExpired,
                        boolean accountNonLocked,
                        Collection<GrantedAuthority> authorities,
                        long id,
                        String imie,
                        String nazwisko,
                        String nr,
                        Long auwId,
                        String email) {

        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities, id);

        this.imie = imie
        this.nazwisko = nazwisko
        this.nr = nr
        this.auwId = auwId
        this.name = imie +" "+nazwisko
        this.email = email
    }

    public transient getFullName() {
        return String.format("%s %s", imie, nazwisko)
    }
}
