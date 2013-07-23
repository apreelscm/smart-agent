package eumowy

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class HomeController {

    def index() {

        boolean hasPH = SpringSecurityUtils.ifAllGranted("PH_ROLE");
        boolean hasAdmin = SpringSecurityUtils.ifAllGranted("ADM_ROLE");

        if(hasAdmin){
            redirect(controller: "process")
        }else if (hasPH){
            redirect(controller: "activity")
        }

    }
}
