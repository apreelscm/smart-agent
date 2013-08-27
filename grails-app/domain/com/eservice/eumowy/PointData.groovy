package com.eservice.eumowy

class PointData {
	
	Integer cbdId
	
	String nip
	String ulica
	Integer nrLokalu
	Integer nrBudynku
	String miejscowosc
	String kodPocztowy
	String poczta
	
	static belongsTo = [process: Process]
	
	static hasOne = [pointDetails: PointDataDetails]
	
	static hasMany = {
		posDatas: PosData
	}
	
	static mapping = {
		table name: "POINT", schema:DomainConsts.SHEMA_NAME
		id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.POINT_SEQ']
	}
	
	static constraints = {
		process(nullable:true)
	}
}
