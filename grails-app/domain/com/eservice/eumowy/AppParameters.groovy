package com.eservice.eumowy

class AppParameters {
	def name
	def value
	
    static constraints = {
		name(unique: true, blank: false)
    }
	
	static mapping = {
		table name: "APP_PARAMETERS", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:'APP_PARAMETERS_SEQ']
	}
}
