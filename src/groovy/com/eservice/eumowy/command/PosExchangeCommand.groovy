package com.eservice.eumowy.command

import com.google.common.base.Strings
import grails.validation.Validateable

/**
 * User: pszkup
 * Date: 30.10.13
 * Time: 14:23
 */
@Validateable(nullable = true)
class PosExchangeCommand implements Serializable{

    Long id
    Integer cbdId
    Integer tpsId

    Integer posNumber

    String name
    String address

    String model

    String newType
    String newModel

    String simType

    BigDecimal currentPrice

    Boolean isChoosen

    Boolean integrationWithCashSystem
    String integrationType
    String integrationSystemSupplier

    static constraints = {
        integrationType(nullable:true, validator: { value, cmd, errors ->
            if(!cmd.integrationWithCashSystem) return true

            return !Strings.isNullOrEmpty(value)
        })

        integrationSystemSupplier(nullable:true, validator: { value, cmd, errors ->
            if(!cmd.integrationWithCashSystem) return true

            return !Strings.isNullOrEmpty(value)
        })
    }

}
