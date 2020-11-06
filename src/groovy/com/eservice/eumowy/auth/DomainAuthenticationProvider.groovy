package com.eservice.eumowy.auth

import com.eservice.eumowy.auth.microldap.AuthResponse
import com.eservice.eumowy.auth.microldap.MicroLDAPClient
import com.eservice.eumowy.auth.microldap.User
import org.apache.commons.logging.LogFactory
import org.apache.log4j.MDC
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.GrantedAuthorityImpl
import org.springframework.security.core.userdetails.UserDetailsChecker

class DomainAuthenticationProvider implements AuthenticationProvider {

    private static final log = LogFactory.getLog("audit");

    public static final String EUM_PH_BZOS = "EUM_PH_BZOS";
    public static final String EUM_ZRD = "EUM_ZRD";
    public static final String EUM_ADMINISTRATOR = "EUM_ADMINISTRATOR";

    UserDetailsChecker preAuthenticationChecks
    UserDetailsChecker postAuthenticationChecks
    MicroLDAPClient microLDAPClient
    def cbdService

    Authentication authenticate(Authentication auth) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authentication = auth

        String password = authentication.credentials
        String username = authentication.name

        EServiceUserDetails userDetails

        def User userDTO

        try {
            AuthResponse authResponse = microLDAPClient.authAdUser(username, password)
            if (authResponse.isSuccess()){
                userDTO = authResponse.getUser()
            } else {
                log.error("authentication failed for login " + username)
                throw new AuthenticationServiceException(authResponse.getResponseMsg())
            }
        } catch(Exception e) {
            log.error("authentication error",  e)
            throw new AuthenticationServiceException(e.getMessage())
        }

        def roles = [EUM_ADMINISTRATOR, EUM_ZRD, EUM_PH_BZOS] // TODO
        List<GrantedAuthority> authorities = buildAuthorities(roles)


        if(!authorities.any{ it.getAuthority() in [EUM_PH_BZOS,EUM_ZRD] }) {
            throw new AuthenticationServiceException("Użytkownik nie posiada uprawnień do aplikacji.")
        }

        def sellingNumber = cbdService.getNumerSprzedazowy(userDTO.getAuwId())

        userDetails = new EServiceUserDetails(userDTO.getLogin(), "pass",
                true, true, true, true, authorities, 1, userDTO.getFirstName(), userDTO.getLastName(),
                cbdService.getNumerSprzedazowy(userDTO.getAuwId()),userDTO.getAuwId(), userDTO.getEmail()); //userDTO.getUzyId())
        // TODO pobranie nr sprzedazowego

        preAuthenticationChecks.check userDetails
        postAuthenticationChecks.check userDetails

        MDC.clear()
        MDC.put("sessionUserName", userDetails.username);

        log.info("Poprawne logowanie")

        def result = new UsernamePasswordAuthenticationToken(userDetails, authentication.credentials, authorities)
        result.details = authentication.details
        result
    }


    boolean supports(Class<? extends Object> authenticationClass) {
        UsernamePasswordAuthenticationToken.isAssignableFrom authenticationClass
    }

    private List<GrantedAuthority> buildAuthorities(List<String> roles){
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>()
        if(roles?.any{ it.name == EUM_ADMINISTRATOR }){
            authorities.add(new GrantedAuthorityImpl(EUM_ADMINISTRATOR))
        }
        if(roles?.any{ it.name == EUM_ZRD }){
            authorities.add(new GrantedAuthorityImpl(EUM_ZRD))
        }
        if(roles?.any{ it.name == EUM_PH_BZOS }){
            authorities.add(new GrantedAuthorityImpl(EUM_PH_BZOS))
        }
        return authorities
    }
}
