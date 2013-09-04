package com.eservice.eumowy

import org.apache.log4j.Logger

class PointData implements Serializable {
	
	private static Logger log = Logger.getLogger(PointData.class)
	
	Integer cbdId
	
	String nip
	String nazwa
	String ulica
	String nrLokalu
	String nrBudynku
	String miejscowosc
	String kodPocztowy
	String poczta
	BigDecimal liczbaPos
	Boolean tytulPlatnosci
	Boolean systemKasowy
	Boolean uta
	Boolean czyWybrany
	
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
		liczbaPos column: "pos_count"
		tytulPlatnosci column: "payment_title"
		systemKasowy column: "cash_system"
		uta column: "uta"
		czyWybrany column: "is_chosen"
		nazwa column: "name"
		cbdId column: "cbd_id"
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
		liczbaPos(nullable:true)
		tytulPlatnosci(nullable:true)
		systemKasowy(nullable:true)
		uta(nullable:true)
		czyWybrany(nullable:true)
		nazwa(nullable:true)
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
