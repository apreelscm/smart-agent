package com.eservice.eumowy

import org.apache.commons.lang.builder.HashCodeBuilder

class PointDataDetails implements Serializable {

	BigDecimal phPozysk
	BigDecimal opiekaBiznesowa
	BigDecimal opiekaSerwisowaI
	BigDecimal opiekaSerwisowaII
	BigDecimal opiekaSerwisowaIII

	String nipPunktu
	String kodMCC
	String rodzProwadzDzialalWPraktyce
	String numerRachunkuBankowego
    String bank
	Integer bankId
	
	String nazwaDoWydrukuZTerminalaPos
	String nazwaDoWyszukiwarki
	 
	String wydrukUlicaTytul
	String wydrukUlica
	String wydrukNrDomu
	String wydrukNrLokalu
	String wydrukMiasto
	String wydrukKodPocztowy
	String wydrukPoczta
	 
	String wydrukLinia1
	String wydrukLinia2
	
	String korespondencjaUlicaTytul
	String korespondencjaUlica
	String korespondencjaNrDomu
	String korespondencjaNrLokalu
	String korespondencjaMiasto
	String korespondencjaKodPocztowy
	String korespondencjaPoczta
	
	String kontaktWPunkcieTytul
	String kontaktWPunkcieImie
	String kontaktWPunkcieNazwisko
	String kontaktWPunkcieFax
	String kontaktWPunkcieTelStacjonarny
	String kontaktWPunkcieTelKomorkowy
	String kontaktWPunkcieEmail
	
	Integer terminalIlosc
	
	static belongsTo = [point: PointData]
	
	static mapping = {
		table name: "POINT_DETAILS", schema:DomainConsts.SHEMA_NAME
		id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.POINT_DETAILS_SEQ']
		phPozysk column: "ph_gain"
		opiekaBiznesowa column: "business_care"
		opiekaSerwisowaI column: "service_care1"
		opiekaSerwisowaII column: "service_care2"
		opiekaSerwisowaIII column: "service_care3"
		nipPunktu column: "nip"
		kodMCC column: "mcc_code"
		rodzProwadzDzialalWPraktyce column: "bussiness_type_in_practice"
		numerRachunkuBankowego column: "bank_account_number"
		bank column: "bank_name"
		bankId column: "bank_id"
		nazwaDoWydrukuZTerminalaPos column: "name_print_posterminal"
		nazwaDoWyszukiwarki column: "name_search_engine"
		wydrukUlicaTytul column: "print_addressstreet_type"
		wydrukUlica column: "print_addr_street"
		wydrukNrDomu column: "print_addr_home_number"
		wydrukNrLokalu column: "print_addr_flat_number"
		wydrukMiasto column: "print_addr_city"
		wydrukKodPocztowy column: "print_addr_postal_code"
		wydrukPoczta column: "print_addr_post_office"
		wydrukLinia1 column: "print_otherdata_terminal1"
		wydrukLinia2 column: "print_otherdata_terminal2"
		korespondencjaUlicaTytul column: "contact_addr_streettype"
		korespondencjaUlica column: "contact_addr_street"
		korespondencjaNrDomu column: "contact_addr_home_number"
		korespondencjaNrLokalu column: "contact_addr_flat_number"
		korespondencjaMiasto column: "contact_addr_city"
		korespondencjaKodPocztowy column: "contact_addr_postalcode"
		korespondencjaPoczta column: "contact_addr_post_office"
		kontaktWPunkcieTytul column: "contact_at_point_title"
		kontaktWPunkcieImie column: "contact_at_point_firstname"
		kontaktWPunkcieNazwisko column: "contact_at_point_lastname"
		kontaktWPunkcieFax column: "contact_at_point_fax"
		kontaktWPunkcieTelStacjonarny column: "contact_at_point_phone"
		kontaktWPunkcieTelKomorkowy column: "contact_at_point_mobilephone"
		kontaktWPunkcieEmail column: "contact_at_point_email"
		terminalIlosc column: "terminal_count"

	}
	
	static constraints = {
		point(nullable:true)
		phPozysk(nullable:true)
		opiekaBiznesowa(nullable:true)
		opiekaSerwisowaI(nullable:true)
		opiekaSerwisowaII(nullable:true)
		opiekaSerwisowaIII(nullable:true)
		nipPunktu(nullable:true)
		kodMCC(nullable:true)
		rodzProwadzDzialalWPraktyce(nullable:true)
		numerRachunkuBankowego(nullable:true)
		bank(nullable:true)
		nazwaDoWydrukuZTerminalaPos(nullable:true)
		nazwaDoWyszukiwarki(nullable:true)
		wydrukUlicaTytul(nullable:true)
		wydrukUlica(nullable:true)
		wydrukNrDomu(nullable:true)
		wydrukNrLokalu(nullable:true)
		wydrukMiasto(nullable:true)
		wydrukKodPocztowy(nullable:true)
		wydrukPoczta(nullable:true)
		wydrukLinia1(nullable:true)
		wydrukLinia2(nullable:true)
		korespondencjaUlicaTytul(nullable:true)
		korespondencjaUlica(nullable:true)
		korespondencjaNrDomu(nullable:true)
		korespondencjaNrLokalu(nullable:true)
		korespondencjaMiasto(nullable:true)
		korespondencjaKodPocztowy(nullable:true)
		korespondencjaPoczta(nullable:true)
		kontaktWPunkcieTytul(nullable:true)
		kontaktWPunkcieImie(nullable:true)
		kontaktWPunkcieNazwisko(nullable:true)
		kontaktWPunkcieFax(nullable:true)
//		kontaktWPunkcieTelStacjonarny(nullable:true)
//		kontaktWPunkcieTelKomorkowy(nullable:true)
//		TODO - sprawdzic czy to zadziala; mozliwe, ze trzeba nadac nazwy pol pokroju "points[0].kontaktWPunkcieTelStacjonarny
		kontaktWPunkcieTelStacjonarny(nullable:true, validator: { value, pointDetails, errors ->
			if (value == null || value.isEmpty()) {
				if (pointDetails.kontaktWPunkcieTelKomorkowy == null || pointDetails.kontaktWPunkcieTelKomorkowy.isEmpty())
				errors.rejectValue( "kontaktWPunkcieTelStacjonarny", "default.atLeastOne.phoneNumber", "Nale\u017Cy poda\u0107 przynajmniej jeden numer telefonu")
				errors.rejectValue( "kontaktWPunkcieTelKomorkowy", "default.atLeastOne.phoneNumber", "Nale\u017Cy poda\u0107 przynajmniej jeden numer telefonu")
				return false
			}
			 
			return true
		})
		kontaktWPunkcieEmail(nullable:true)
		terminalIlosc(nullable:true)
		bankId(nullable:true)
	}
	
}

