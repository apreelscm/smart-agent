package com.eservice.eumowy.command

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 30.10.13
 * Time: 14:23
 * To change this template use File | Settings | File Templates.
 */
class HirePaymentCommand implements Serializable {

    Integer id
    Integer tpsId
    String posNumber

    Integer cbdId
    String name
    String address
    String type
    Integer termCount
    Integer ppCount
    BigDecimal currentTermPayment
    BigDecimal currentPpPayment
    BigDecimal newTermPayment
    BigDecimal newPpPayment
    Boolean isChoosen

    static constraints = {

    }

}
