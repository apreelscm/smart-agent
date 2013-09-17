package com.eservice.eumowy

import org.apache.commons.lang.builder.HashCodeBuilder

class PosData implements Serializable {
	
	Integer tpsId
	Integer numerZestawuPos
	Date dataOd
	Date dataDo
	BigDecimal wysokoscOplaty
	Boolean czyWybrany
	
	static belongsTo = [point: PointData]
	static hasOne = [posDetails: PosDataDetails]
	
	static mapping = {
		sort "id"
		table name: "POS", schema:DomainConsts.SHEMA_NAME
		id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.POS_SEQ']
		numerZestawuPos column: "pos_set_number"
		dataOd column: "date_from"
		dataDo column: "date_to"
		wysokoscOplaty column: "price_count"
		czyWybrany column: "is_selected"
		tpsId column: "tps_id"
	}
	
	static constraints = {
		point(nullable:true)
		posDetails(nullable:true)
		numerZestawuPos(nullable:true)
		dataOd(nullable:true, blank:false)
		dataDo(nullable:true, blank:false)
		wysokoscOplaty(nullable:true)
		czyWybrany(nullable:true)
		tpsId(nullable:true)
	}
	
}
