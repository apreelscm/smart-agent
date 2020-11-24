package com.eservice.eumowy.auth

import com.eservice.eumowy.DomainUserDetailsService
import com.eservice.eumowy.auth.microldap.AuthResponse
import com.eservice.eumowy.auth.microldap.User
import com.eservice.eumowy.auth.microldap.MicroLDAPClient
import org.apache.commons.lang.StringEscapeUtils
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

/**
 * Based on EServiceAuthenticationProvider with domain authentication changes
 */
class DomainAuthenticationProvider implements AuthenticationProvider {

    private static final log = LogFactory.getLog("audit");

    public static final String EUM_PH_BZOS = "EUM_PH_BZOS";
    public static final String EUM_ZRD = "EUM_ZRD";
    public static final String EUM_ADMINISTRATOR = "EUM_ADMINISTRATOR";
    def msgParams = [].toArray()

    UserDetailsChecker preAuthenticationChecks
    UserDetailsChecker postAuthenticationChecks
    MicroLDAPClient microLDAPClient
    DomainUserDetailsService userDetailsService
    def cbdService
    def messagesSource

    Authentication authenticate(Authentication auth) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authentication = auth

        String password = StringEscapeUtils.escapeHtml(authentication.credentials)
        String username = StringEscapeUtils.escapeHtml(authentication.name)

        if(!username || !password){
            throw new AuthenticationServiceException(messagesSource.getMessage("login.fail", msgParams, Locale.default))
        }

        EServiceUserDetails userDetails

        def User user
        AuthResponse authResponse
        try {
            authResponse = microLDAPClient.authAdUser(username, password)
        } catch(Exception e) {
            log.error("authentication error",  e)
            throw new AuthenticationServiceException(messagesSource.getMessage("login.exception", msgParams, Locale.default))
        }
        if (authResponse.isSuccess()){
            user = authResponse.getUser()
        } else {
            log.error("authentication failed [" + authResponse.getResponseCode() + "]" + authResponse.getResponseMsg() + " for login [" + username + "]")
            throw new AuthenticationServiceException(messagesSource.getMessage("login.fail", msgParams, Locale.default))
        }

        List roles = userDetailsService.findUserRoles(username)
        List<GrantedAuthority> authorities = buildAuthorities(roles)

        if(!authorities.any{ it.getAuthority() in [EUM_PH_BZOS,EUM_ZRD] }) {
            throw new AuthenticationServiceException(messagesSource.getMessage("login.notPermitted", msgParams, Locale.default))
        }

        Long auwId = userDetailsService.findUserAuwId(username)
        def sellingNumber = cbdService.getNumerSprzedazowy(auwId)

        userDetails = new EServiceUserDetails(username, "pass",
                true, true, true, true, authorities, 1, user.getFirstName(), user.getLastName(),
                sellingNumber, auwId, user.getEmail())

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
        if(roles?.any{ it == EUM_ADMINISTRATOR }){
            authorities.add(new GrantedAuthorityImpl(EUM_ADMINISTRATOR))
        }
        if(roles?.any{ it == EUM_ZRD }){
            authorities.add(new GrantedAuthorityImpl(EUM_ZRD))
        }
        if(roles?.any{ it == EUM_PH_BZOS }){
            authorities.add(new GrantedAuthorityImpl(EUM_PH_BZOS))
        }
        return authorities
    }
}
