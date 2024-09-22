package com.eservice.eumowy.auth

import com.eservice.eumowy.CbdService
import com.eservice.eumowy.DomainUserDetailsService
import com.eservice.eumowy.auth.microldap.AuthResponse
import com.eservice.eumowy.auth.microldap.MicroLDAPClient
import com.eservice.eumowy.auth.microldap.User
import org.apache.commons.lang.StringUtils
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.context.MessageSource

import static com.eservice.eumowy.auth.AuthRoles.EUM_PH_BZOS
import static com.eservice.eumowy.auth.AuthRoles.EUM_ZRD

public class LdapAuthenticator implements Authenticator {
    private static final Log log = LogFactory.getLog("audit");
    private static final Object[] EMPTY_ARRAY = new Object[0];
    MicroLDAPClient ldapClient;
    DomainUserDetailsService userDetailsService;
    CbdService cbd;
    MessageSource messageSource;

    @Override
    public AuthUser auth(String username, String password) throws AuthenticationFailed {
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            throw new AuthenticationFailed(messageSource.getMessage("login.fail", EMPTY_ARRAY, Locale.getDefault()));
        }

        User user = tryLdapAuth(username, password);

        List<String> roles = userDetailsService.findUserRoles(username) as List<String>;

        if (!roles.any { it in [EUM_PH_BZOS, EUM_ZRD] }) {
            throw new AuthenticationFailed(messageSource.getMessage("login.notPermitted", EMPTY_ARRAY, Locale.getDefault()))
        }

        Long auwId = userDetailsService.findUserAuwId(username) as Long
        String sellingNumber = cbd.getNumerSprzedazowy(auwId)

        return new AuthUser(username, password, user.firstName, user.lastName, user.email, auwId, sellingNumber, roles);
    }

    private User tryLdapAuth(String username, String password) {
        AuthResponse auth;
        try {
            auth = ldapClient.authAdUser(username, password);
        } catch (Exception e) {
            log.error("authentication error",  e);
            throw new AuthenticationFailed(messageSource.getMessage("login.exception", EMPTY_ARRAY, Locale.getDefault()));
        }

        if (auth.isSuccess()){
            return auth.getUser();
        } else {
            log.error("authentication failed [" + auth.getResponseCode() + "]" + auth.getResponseMsg() + " for login [" + username + "]");
            throw new AuthenticationFailed(messageSource.getMessage("login.fail", EMPTY_ARRAY, Locale.getDefault()));
        }
    }
}
