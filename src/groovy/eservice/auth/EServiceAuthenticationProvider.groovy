package eservice.auth

import com.eservice.dto.UserDTO
import com.eservice.service.security.UserService
import org.codehaus.groovy.grails.plugins.springsecurity.GormUserDetailsService
import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetailsChecker
import org.springframework.security.core.userdetails.UsernameNotFoundException

class EServiceAuthenticationProvider implements AuthenticationProvider {

    protected final Logger log = LoggerFactory.getLogger(getClass())

    UserDetailsChecker preAuthenticationChecks
    UserDetailsChecker postAuthenticationChecks
    UserService userService

    Authentication authenticate(Authentication auth) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authentication = auth

        String password = authentication.credentials
        String username = authentication.name

        GrailsUser userDetails
        def authorities

        UserDTO userDTO
        try{
            userDTO = userService.login(username,password);
        }catch(Exception e){}

        if (!userDTO) {
            // TODO customize 'springSecurity.errors.login.fail' i18n message in app's messages.properties with org name
            log.warn "User not found: $username"
            throw new UsernameNotFoundException('User not found', username)
        }

        //TODO pobrac role
        // authorities =
        /*authorities = userDTO.authorities.collect { new GrantedAuthorityImpl(it.authority) }*/

        authorities = authorities ?: GormUserDetailsService.NO_ROLES

        userDetails = new GrailsUser(userDTO.getLogin(), null,
                true, true, true, authorities, userDTO.getUzyId())

        preAuthenticationChecks.check userDetails
        additionalAuthenticationChecks userDetails, authentication
        postAuthenticationChecks.check userDetails

        def result = new UsernamePasswordAuthenticationToken(userDetails, authentication.credentials, authorities)
        result.details = authentication.details
        result
    }

    protected void additionalAuthenticationChecks(GrailsUser userDetails,
                                                  UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        if (authentication.credentials == null) {
            log.debug 'Authentication failed: no credentials provided'
            throw new BadCredentialsException('Bad credentials', userDetails)
        }

        /* String presentedPassword = authentication.credentials
         if (!passwordEncoder.isPasswordValid(userDetails.password, presentedPassword, salt)) {
             log.debug 'Authentication failed: password does not match stored value'

             throw new BadCredentialsException('Bad credentials', userDetails)
         }*/
    }

    boolean supports(Class<? extends Object> authenticationClass) {
        UsernamePasswordAuthenticationToken.isAssignableFrom authenticationClass
    }
}
