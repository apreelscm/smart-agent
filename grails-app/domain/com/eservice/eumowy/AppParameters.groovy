package com.eservice.eumowy

class AppParameters implements Serializable {
	String name
	String value
	
    static constraints = {
		name(unique: true, blank: false)
        value(nullable: true)
    }
	
	static mapping = {
		table name: "APP_PARAMETERS", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:'APP_PARAMETERS_SEQ']
	}
}
