package com.eservice.eumowy

import java.io.Serializable;

class PosData implements Serializable {
	
	//TODO Add things needed for New POS/Point documents, like point name etc.
	Integer numerZestawuPos
	Date dataOd
	Date dataDo
	BigDecimal wysokoscOplaty
	Boolean czyWybrany
	
	static belongsTo = [point: PointData]
	static hasOne = [posDetails: PosDataDetails]
	
	static mapping = {
		table name: "POS", schema:DomainConsts.SHEMA_NAME
		id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.POS_SEQ']
		numerZestawuPos column: "pos_set_number"
		dataOd column: "date_from"
		dataDo column: "date_to"
		wysokoscOplaty column: "price_count"
		czyWybrany column: "is_selected"
	}
	
	static constraints = {
		point(nullable:true)
		posDetails(nullable:true)
		numerZestawuPos(nullable:true)
		dataOd(nullable:true)
		dataDo(nullable:true)
		wysokoscOplaty(nullable:true)
		czyWybrany(nullable:true)
	}
}
