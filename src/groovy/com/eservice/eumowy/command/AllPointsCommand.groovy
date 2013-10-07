package com.eservice.eumowy.command

class AllPointsCommand implements Serializable {
	Boolean czyCbd
	Integer cbdId
	Integer id
	String nazwa
	String ulica
	String miejscowosc
	String nrBudynku
	String kodPocztowy
    Integer liczbaPos
    Boolean czyWybranyZakresUruchomienia
	Boolean tytulPlatnosci
	Boolean systemKasowy
	Boolean uta
	Boolean czyWybranyAkceptacjaKart
	
	def setNazwa(String value) {
		nazwa = formatProperty(value)
	}
	
	def setUlica(String value) {
		ulica = formatProperty(value)
	}
	
	def setMiejscowosc(String value) {
		miejscowosc = formatProperty(value)
	}
	
	def setNrBudynku(String value) {
		nrBudynku = formatProperty(value)
	}
	
	def setKodPocztowy(String value) {
		kodPocztowy = formatProperty(value)
	}
	
	def formatProperty(String value) {
		if (value?.contains(",")) {
			Set<String> set = new HashSet<String>()
			String[] values = value.split(",")
			set.addAll(values)
			
			if (set.size() == 1) {
				return values[0]
			}
		}
		
		return value
	}
	
	static constraints = {
		tytulPlatnosci(nullable:true)
		systemKasowy(nullable:true)
		uta(nullable:true)
		czyWybranyAkceptacjaKart(nullable:true)
		czyWybranyZakresUruchomienia(nullable:true)
	}
	
	
}
