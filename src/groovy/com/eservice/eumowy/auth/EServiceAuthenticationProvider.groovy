package com.eservice.eumowy.auth
import com.eservice.dto.UserDTO
import org.apache.commons.logging.LogFactory
import org.apache.log4j.MDC
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.authority.GrantedAuthorityImpl
import org.springframework.security.core.userdetails.UserDetailsChecker
import org.springframework.security.core.userdetails.UsernameNotFoundException

class EServiceAuthenticationProvider implements AuthenticationProvider {

    private static final auditLogger = LogFactory.getLog("audit");

    public static final String PH_ROLE = "PH_ROLE";
    public static final String ADM_ROLE = "ADM_ROLE";

    UserDetailsChecker preAuthenticationChecks
    UserDetailsChecker postAuthenticationChecks
    def userService

    Authentication authenticate(Authentication auth) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authentication = auth

        String password = authentication.credentials
        String username = authentication.name

        EServiceUserDetails userDetails
        List<GrantedAuthorityImpl> authorities

        UserDTO userDTO
        try{
            userDTO = userService.login(username,password);
        }catch(Exception e)
        {
            e.printStackTrace();
        }

        if (!userDTO) {
            // TODO customize 'springSecurity.errors.login.fail' i18n message in app's messages.properties with org name
            auditLogger.info("Nie znaleziono użytkownika [login:${username}]")
            throw new UsernameNotFoundException('User not found', username)
        }

        authorities = new ArrayList<GrantedAuthorityImpl>()
        if(userDTO.przId){
            authorities.add(new GrantedAuthorityImpl(PH_ROLE))
        }
        else if (userDTO.uzyId){
            authorities.add(new GrantedAuthorityImpl(ADM_ROLE))
            /*authorities = userDTO.authorities.collect { new GrantedAuthorityImpl(it.authority) }*/
        }
        //authorities = authorities ?: GormUserDetailsService.NO_ROLES

        userDetails = new EServiceUserDetails(userDTO.getLogin(), "pass",
                true, true, true, true, authorities, 1,
                userDTO.getFirstName() + ' ' +userDTO.getLastName()); //userDTO.getUzyId())


        preAuthenticationChecks.check userDetails
        postAuthenticationChecks.check userDetails

        MDC.clear()
        MDC.put("sessionUserName", userDetails.username);

        auditLogger.info("Poprawne logowanie")

        def result = new UsernamePasswordAuthenticationToken(userDetails, authentication.credentials, authorities)
        result.details = authentication.details
        result
    }


    boolean supports(Class<? extends Object> authenticationClass) {
        UsernamePasswordAuthenticationToken.isAssignableFrom authenticationClass
    }
}
