package com.eservice.eumowy.auth.microldap

interface MicroLDAPClient {

    AuthResponse authAdUser(String login, String password)

}