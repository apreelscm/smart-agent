package eservice.auth

import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUserDetailsService
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.springframework.security.core.authority.GrantedAuthorityImpl
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException

/**
 * Created with IntelliJ IDEA.
 * User: mariusz.kaczkowski
 * Date: 16.07.13
 * Time: 11:26
 * To change this template use File | Settings | File Templates.
 */
public class EServiceUserDetailsService implements GrailsUserDetailsService {

    /**
     * Some Spring Security classes (e.g. RoleHierarchyVoter) expect at least one role, so
     * we give a user with no granted roles this one which gets past that restriction but
     * doesn't grant anything.
     */
    static final List NO_ROLES = [new GrantedAuthorityImpl(SpringSecurityUtils.NO_ROLE)]

    UserDetails loadUserByUsername(String username, boolean loadRoles) {
        return loadUserByUsername(username)
    }

    UserDetails loadUserByUsername(String username) {
        User.withTransaction { status ->

            User user = User.findByUsername(username);
            if (!user) {
                throw new UsernameNotFoundException('User not found', username)
            }

            def authorities = user.authorities.collect {new GrantedAuthorityImpl(it.authority)}
            return new EServiceUserDetails(user.username,
                    user.password,
                    user.enabled,
                    !user.accountExpired,
                    !user.passwordExpired,
                    !user.accountLocked,
                    authorities ?: NO_ROLES,
                    user.id,
                    user.name);
        }
    }
}