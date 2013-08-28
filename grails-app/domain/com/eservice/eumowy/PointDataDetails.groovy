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
		nazwaDoWydrukuZTerminalaPos column: "point_name_for_printing_from_POS_terminal"
		nazwaDoWyszukiwarki column: "point_name_for_search_engine"
		wydrukUlicaTytul column: "dataforprinting_address_street_type"
		wydrukUlica column: "dataforprinting_address_street"
		wydrukNrDomu column: "dataforprinting_address_home_number"
		wydrukNrLokalu column: "dataforprinting_address_flat_number"
		wydrukMiasto column: "dataforprinting_address_city"
		wydrukKodPocztowy column: "dataforprinting_address_postal_code"
		wydrukPoczta column: "dataforprinting_address_post_office"
		wydrukLinia1 column: "other_data_for_printing_from_terminal1"
		wydrukLinia2 column: "other_data_for_printing_from_terminal2"
		contactAddressStreetType column: "contact_address_street_type"
		korespondencjaUlica column: "contact_address_address_street"
		korespondencjaNrDomu column: "contact_address_home_number"
		korespondencjaNrLokalu column: "contact_address_flat_number"
		koresponedencjaMiasto column: "contact_address_city"
		korespondencjaKodPocztowy column: "contact_address_postal_code"
		korespondencjaPoczta column: "contact_address_post_office"
		kontaktWPunkcieImie column: "contact_at_point_first_name"
		kontaktWPunkcieNazwisko column: "contact_at_point_last_name"
		kontaktWPunkcieFax column: "contact_at_point_fax"
		kontaktWPunkcieTelStacjonarny column: "contact_at_point_phone"
		kontaktWPunkcieTelKomorkowy column: "contact_at_point_mobile_phone"
		kontaktWPunkcieEmail column: "contact_at_point_email"

	}
	
	static constraints = {
	}
}

