package com.eservice.eumowy
import groovy.transform.ToString

import org.apache.commons.lang.builder.HashCodeBuilder

@ToString
class Client implements Serializable{

    String name
    Long cbdId
    String nip
    String mid

    static constraints = {
        name(nullable:true,blank:false)
        nip(blank:false)
        cbdId(nullable: true)
        mid(nullable: true, blank:true)
    }

    static mapping = {
        table name: "CLIENT", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.CLIENT_SEQ']
    }

    def beforeInsert() {
        log.info("Tworzenie klienta [id:${id}, cbdId:${cbdId}, name:${name}, nip:${nip}, mid:${mid}]")
    }

    def afterInsert() {
        log.info("Utworzono klienta [id:${id}, cbdId:${cbdId}, name:${name}, nip:${nip}, mid:${mid}]")
    }

    def afterUpdate() {
        log.info("Aktualizacja klienta [id:${id}, cbdId:${cbdId}, name:${name}, nip:${nip}, mid:${mid}]")
    }
	
}
