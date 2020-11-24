package com.eservice.eumowy

import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
class DomainUserDetailsService {

    def cbdDAO

    private static final String FIND_USER_ROLES = "findUserRole"
    private static final String FIND_AUW_ID = "findUserAuwId"

    def findUserRoles(def userLogin) {
        def roles = cbdDAO.selectMany(FIND_USER_ROLES,[login : userLogin]).collect{it.ROLA}
        return roles
    }

    def findUserAuwId(def userLogin) {
        def result = cbdDAO.selectOne(FIND_AUW_ID,[login : userLogin])
        return result.auwId
    }
}
