package com.eservice.eumowy.command

import grails.validation.Validateable

/**
 * User: pszkup
 * Date: 30.10.13
 * Time: 14:23
 */
@Validateable
class PosExchangeCommand implements Serializable{

    Long id
    Integer cbdId
    Integer tpsId

    Integer posNumber

    String name
    String address

    String type
    String model

    String newType
    String newModel

    String simType

    Boolean isChoosen

    static constraints = {

    }

}
