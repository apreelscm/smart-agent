package com.eservice.eumowy

import grails.plugin.springsecurity.SpringSecurityUtils

class HomeController {

    def index() {

        boolean hasPH = SpringSecurityUtils.ifAllGranted("EUM_PH_BZOS");
        boolean hasAdmin = SpringSecurityUtils.ifAllGranted("EUM_ZRD");

        if(hasAdmin){
            redirect(controller: "process")
        }else if (hasPH){
            redirect(controller: "activity")
        }

    }
}
