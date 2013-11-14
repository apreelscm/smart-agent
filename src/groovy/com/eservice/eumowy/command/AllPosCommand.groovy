package com.eservice.eumowy.command

import grails.validation.Validateable;

import com.eservice.eumowy.annotation.Omit;

@Validateable
class AllPosCommand extends BaseCommand {
	
	Integer id
    Boolean czyCbd
	Integer cbdId
    Integer tpsId
	Integer numerZestawuPos
	Date dataOd
	Date dataDo
	BigDecimal wysokoscOplaty
	Boolean czyWybrany
	
	static constraints = {
		dataOd(nullable:false, blank:false)
		dataDo(nullable:false, blank:false)
		wysokoscOplaty(nullable: false, blank: false,  validator: { value, cmd, errors ->
            if (cmd.czyWybrany == null || cmd.czyWybrany == false)
				return true
			return atLeastClosure.call(value, cmd, errors, "wysokoscOplaty", "OPLATA_POS_PROM_CENA_NAJMU")
		})
	}
}
