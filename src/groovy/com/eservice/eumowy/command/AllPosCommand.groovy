package com.eservice.eumowy.command

import com.eservice.eumowy.validator.AtLeastValidator
import grails.validation.Validateable;

@Validateable(nullable = true)
class AllPosCommand implements Serializable {

    transient def calculatorService
    transient def calc

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
		wysokoscOplaty(nullable: true, blank: false,  validator: { value, cmd, errors ->
            if (cmd.czyWybrany == null || cmd.czyWybrany == false){
                return true
            } else if (value == null){
                errors.rejectValue("wysokoscOplaty", "default.atLeast.asCalc",["wysokoscOplaty"] as Object[], "")
                return false
            } else {
                return AtLeastValidator.validate(value, cmd, errors, "wysokoscOplaty", "OPLATA_POS_PROM_CENA_NAJMU")
            }
		})
	}
}
