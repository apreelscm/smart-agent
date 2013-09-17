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
	
	static constraints = {
		tytulPlatnosci(nullable:true)
		systemKasowy(nullable:true)
		uta(nullable:true)
		czyWybranyAkceptacjaKart(nullable:true)
		czyWybranyZakresUruchomienia(nullable:true)
	}
}
