package com.eservice.eumowy.auth

import com.eservice.eumowy.util.EumowyCustomEnvironment
import grails.util.Environment
import org.apache.commons.logging.LogFactory
import org.apache.log4j.MDC
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.authority.GrantedAuthorityImpl
import org.springframework.security.core.userdetails.UserDetailsChecker
import org.springframework.security.core.userdetails.UsernameNotFoundException

class EServiceAuthenticationProvider implements AuthenticationProvider {

    private static final auditLogger = LogFactory.getLog("audit");

    public static final String EUM_PH_BZOS = "EUM_PH_BZOS";
    public static final String EUM_ZRD = "EUM_ZRD";
    public static final String EUM_ADMINISTRATOR = "EUM_ADMINISTRATOR";

    UserDetailsChecker preAuthenticationChecks
    UserDetailsChecker postAuthenticationChecks
    def userService

    Authentication authenticate(Authentication auth) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authentication = auth
        Exception exception = null

        String password = authentication.credentials
        String username = authentication.name

        EServiceUserDetails userDetails
        List<GrantedAuthorityImpl> authorities

        def userDTO
        try{
            userDTO = userService.loginToEUmowy(username,password);
			println(userDTO.getRoles())
        }catch(Exception e){
            exception = e
            log.error(e.message)
            e.printStackTrace();
        }

        authorities = new ArrayList<GrantedAuthorityImpl>()

        if (!userDTO) {
            throw new AuthenticationServiceException(exception.getMessage())
        }

        switch (Environment.getCurrent().getName()) {
            case EumowyCustomEnvironment.MOCK.getName():
				userDTO.setEmail("pszkup@apreel.com")
                if(username == "ph"){
                    authorities.add(new GrantedAuthorityImpl(EUM_PH_BZOS))
                }

                if(username == "admin"){
                    authorities.add(new GrantedAuthorityImpl(EUM_ZRD))
                }

                if(username == "admin"){
                    authorities.add(new GrantedAuthorityImpl(EUM_ADMINISTRATOR))
                }
                break;
           default:
                if(userDTO.roles.any{ it.name == EUM_ADMINISTRATOR }){
                    authorities.add(new GrantedAuthorityImpl(EUM_ADMINISTRATOR))
                }
                if(userDTO.roles.any{ it.name == EUM_ZRD }){
                    authorities.add(new GrantedAuthorityImpl(EUM_ZRD))
                }
                if(userDTO.roles.any{ it.name == EUM_PH_BZOS }){
                    authorities.add(new GrantedAuthorityImpl(EUM_PH_BZOS))
                }
                break;
        }

        if(!authorities.any{ it.getAuthority() in [EUM_PH_BZOS,EUM_ZRD] }) {
            throw new AuthenticationServiceException("Użytkownik nie posiada uprawnień do aplikacji.")
        }

        userDetails = new EServiceUserDetails(userDTO.getLogin(), "pass",
                true, true, true, true, authorities, 1, userDTO.getFirstName(), userDTO.getLastName(),userDTO.getAuwId(),userDTO.getEmail()); //userDTO.getUzyId())

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
