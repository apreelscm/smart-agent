package com.eservice.eumowy

class PosData implements Serializable {
	
	Long parentPosId
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
		parentPosId column: "parent_pos_id"
	}
	
	static constraints = {
		point(nullable:true)
		posDetails(nullable:true)
		numerZestawuPos(nullable:true)
		dataOd(nullable:true)
		dataDo(nullable:true)
		wysokoscOplaty(nullable:true)
		czyWybrany(nullable:true)
		tpsId(nullable:true)
		parentPosId(nullable:true)
	}

}
