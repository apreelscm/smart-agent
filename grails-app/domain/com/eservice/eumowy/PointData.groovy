package com.eservice.eumowy

import org.apache.log4j.Logger

class PointData implements Serializable {
	
	private static Logger log = Logger.getLogger(PointData.class)
	
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
		nip(nullable:true)
		ulica(nullable:true)
		nrLokalu(nullable:true)
		nrBudynku(nullable:true)
		miejscowosc(nullable:true)
		kodPocztowy(nullable:true)
		poczta(nullable:true)
		pointDetails(nullable:true)
		posDatas(nullable:true)
		cbdId(nullable:true)
	}
	
	def beforeInsert() {
		log.info("Tworzenie punktu [id:${id}]")
	}

	def afterInsert() {
		log.info("Utworzono punkt [id:${id}]")
	}

	def afterUpdate() {
		log.info("Aktualizacja punktu [id:${id}]")
	}
}
