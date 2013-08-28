package com.eservice.eumowy

import java.util.List;

class PointDataDetails implements Serializable {
	List<PosData> posDatas
	
	BigDecimal phPozysk
	BigDecimal opiekaBiznesowa
	BigDecimal opiekaSerwisowaI
	BigDecimal opiekaSerwisowaII
	BigDecimal opiekaSerwisowaIII

	String nipPunktu
	String kodMCC
	String rodzProwadzDzialalWPraktyce
	String kontoBankNumer
	String nazwaBanku
	
	String nazwaDoWydrukuZTerminalaPos
	String nazwaDoWyszukiwarki
	 
	String wydrukUlicaTytul
	String wydrukUlica
	Integer wydrukNrDomu
	Integer wydrukNrLokalu
	String wydrukMiasto
	String wydrukKodPocztowy
	String wydrukPoczta
	 
	String wydrukLinia1
	String wydrukLinia2
	
	String contactAddressStreetType
	String korespondencjaUlica
	Integer korespondencjaNrDomu
	Integer korespondencjaNrLokalu
	String koresponedencjaMiasto
	String korespondencjaKodPocztowy
	String korespondencjaPoczta
	
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
		kontoBankNumer column: "bank_account_number"
		nazwaBanku column: "bank_name"
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
		contactAddressStreetType column: "contact_addr_streettype"
		korespondencjaUlica column: "contact_addr_street"
		korespondencjaNrDomu column: "contact_addr_home_number"
		korespondencjaNrLokalu column: "contact_addr_flat_number"
		koresponedencjaMiasto column: "contact_addr_city"
		korespondencjaKodPocztowy column: "contact_addr_postalcode"
		korespondencjaPoczta column: "contact_addr_post_office"
		kontaktWPunkcieImie column: "contact_at_point_firstname"
		kontaktWPunkcieNazwisko column: "contact_at_point_lastname"
		kontaktWPunkcieFax column: "contact_at_point_fax"
		kontaktWPunkcieTelStacjonarny column: "contact_at_point_phone"
		kontaktWPunkcieTelKomorkowy column: "contact_at_point_mobilephone"
		kontaktWPunkcieEmail column: "contact_at_point_email"

	}
	
	static constraints = {
	}
}

