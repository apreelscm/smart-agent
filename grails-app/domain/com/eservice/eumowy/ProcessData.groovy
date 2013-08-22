package com.eservice.eumowy

import groovy.transform.ToString

/**
 * User: Dominik Walczak
 * Date: 20.08.13 Time: 11:47
 *
 */
@ToString
class ProcessData implements Serializable{

    String name
    String value

    static belongsTo = [ process : Process ]

    static mapping = {
        table name: "PROCESS_DATA", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.PROCESS_DATA_SEQ']
    }

    def afterInsert() {
        log.info("Utworzono ProcessData [name:${name}]")
    }

}
