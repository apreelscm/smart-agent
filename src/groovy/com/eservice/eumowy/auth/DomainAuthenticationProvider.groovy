package com.eservice.eumowy.auth

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

import static com.eservice.eumowy.auth.AuthRoles.*

/**
 * Based on EServiceAuthenticationProvider with domain authentication changes
 */
class DomainAuthenticationProvider implements AuthenticationProvider {

    private static final log = LogFactory.getLog("audit");

    EUmowyAuthenticator authenticator
    UserDetailsChecker preAuthenticationChecks
    UserDetailsChecker postAuthenticationChecks

    Authentication authenticate(Authentication auth) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authentication = auth as UsernamePasswordAuthenticationToken

        try {
            AuthUser user = authenticator.auth(authentication.name, authentication.credentials as String)

            List<GrantedAuthority> authorities = buildAuthorities(user.roles)

            EServiceUserDetails userDetails = new EServiceUserDetails(user.username, "pass",
                    true, true, true, true, authorities, 1, user.firstName, user.lastName,
                    user.sellerNumber, user.auwId, user.email)

            preAuthenticationChecks.check userDetails
            postAuthenticationChecks.check userDetails

            MDC.clear()
            MDC.put("sessionUserName", userDetails.username);

            log.info("Poprawne logowanie")

            def result = new UsernamePasswordAuthenticationToken(userDetails, authentication.credentials, authorities)
            result.details = authentication.details
            return result
        } catch (AuthenticationFailed e) {
            throw new AuthenticationServiceException(e.getMessage())
        }
    }


    boolean supports(Class<? extends Object> authenticationClass) {
        UsernamePasswordAuthenticationToken.isAssignableFrom authenticationClass
    }

    private static List<GrantedAuthority> buildAuthorities(List<String> roles){
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
