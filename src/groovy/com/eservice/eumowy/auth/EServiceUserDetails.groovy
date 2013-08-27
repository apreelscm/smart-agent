package com.eservice.eumowy.auth

import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUser
import org.springframework.security.core.GrantedAuthority
/**
 * Created with IntelliJ IDEA.
 * User: mariusz.kaczkowski
 * Date: 16.07.13
 * Time: 11:25
 * To change this template use File | Settings | File Templates.
 */
public class EServiceUserDetails extends GrailsUser {

    final String name;
    final String typOsoby;
    final String imie;
    final String nazwisko;
    final Long nr;

    EServiceUserDetails(String username,
                        String password,
                        boolean enabled,
                        boolean accountNonExpired,
                        boolean credentialsNonExpired,
                        boolean accountNonLocked,
                        Collection<GrantedAuthority> authorities,
                        long id,
                        String typOsoby,
                        String imie,
                        String nazwisko,
                        Long nr) {

        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities, id);

        this.typOsoby = typOsoby;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.nr = nr;
        this.name = imie +" "+nazwisko;
    }
}
