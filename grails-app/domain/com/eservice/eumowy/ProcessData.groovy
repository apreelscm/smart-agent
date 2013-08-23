package com.eservice.eumowy
/**
 * User: Dominik Walczak
 * Date: 20.08.13 Time: 11:47
 *
 */
class ProcessData implements Serializable {

    String name
    String value

    Process process

    static belongsTo = [process:Process]

    static constraints = {
        name(unique:false,blank:false)
        value(nullable: false,blank:true)
    }

    static mapping = {
        table name: "PROCESS_DATA", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.PROCESS_DATA_SEQ']
    }

    def afterInsert() {
        log.info("Utworzono ProcessData [name:${name}, value:${value}]")
    }

    def afterUpdate() {
        log.info("Zaktualizowano ProcessData [name:${name}, value:${value}]")
    }

}
