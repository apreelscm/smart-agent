package com.eservice.eumowy.process

import grails.validation.Validateable

/**
 * Created with IntelliJ IDEA.
 * User: mariusz.kaczkowski
 * Date: 01.08.13
 * Time: 10:05
 * To change this template use File | Settings | File Templates.
 */

@Validateable
class ChooseActivityCommand {

    def documentSignature1
    def documentSignature2

    static constraints = {



    }

}