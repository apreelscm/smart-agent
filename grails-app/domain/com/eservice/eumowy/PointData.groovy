package com.eservice.eumowy

class PointData implements Serializable {
	
	Integer cbdId
	
	String nip
	String ulica
	Integer nrLokalu
	Integer nrBudynku
	String miejscowosc
	String kodPocztowy
	String poczta
	
	List<PosData> posDatas
	
	static belongsTo = [process: Process]
	
	static hasOne = [pointDetails: PointDataDetails]
	
	static hasMany = {
		posDatas: PosData
	}
	
	static mapping = {
		table name: "POINT", schema:DomainConsts.SHEMA_NAME
		id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.POINT_SEQ']
		nip column: "nip"
		ulica column: "street"
		nrLokalu column: "flat_number"
		nrBudynku column: "home_number"
		miejscowosc column: "city"
		kodPocztowy column: "postal_code"
		poczta column: "post_office"
	}
	
	static constraints = {
		process(nullable:true)
	}
}
