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
	
	static belongsTo = [point: PointData]
	
	static mapping = {
		table name: "POINT_DETAILS", schema:DomainConsts.SHEMA_NAME
		id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.POINT_DETAILS_SEQ']
		phPozysk column: "phGain"
		opiekaBiznesowa column: "businessCare"
		opiekaSerwisowaI column: "serviceCare1"
		opiekaSerwisowaII column: "serviceCare2"
		opiekaSerwisowaIII column: "serviceCare3"
		nipPunktu column: "nip"
		kodMCC column: "mccCode"
		rodzProwadzDzialalWPraktyce column: "bussinessTypeInPractice"
		kontoBankNumer column: "bankAccountNumber"
		nazwaBanku column: "bankName"
		nazwaDoWydrukuZTerminalaPos column: "pointNameForPrintingFromPOSTerminal"
		nazwaDoWyszukiwarki column: "pointNameForSearchEngine"
		wydrukUlicaTytul column: "dataforprintingAddressStreetType"
		wydrukUlica column: "dataforprintingAddressStreet"
		wydrukNrDomu column: "dataforprintingAddressHomeNumber"
		wydrukNrLokalu column: "dataforprintingAddressFlatNumber"
		wydrukMiasto column: "dataforprintingAddressCity"
		wydrukKodPocztowy column: "dataforprintingAddressPostalCode"
		wydrukPoczta column: "dataforprintingAddressPostOffice"
		wydrukLinia1 column: "otherDataForPrintingFromTerminal1"
		wydrukLinia2 column: "otherDataForPrintingFromTerminal2"
		contactAddressStreetType column: "contactAddressStreetType"
		korespondencjaUlica column: "contactAddressAddressStreet"
		korespondencjaNrDomu column: "contactAddressAddressHomeNumber"
		korespondencjaNrLokalu column: "contactAddressAddressFlatNumber"
		koresponedencjaMiasto column: "contactAddressAddressCity"
		korespondencjaKodPocztowy column: "contactAddressAddressPostalCode"
		korespondencjaPoczta column: "contactAddressAddressPostOffice"
		kontaktWPunkcieImie column: "contactAtPointFirstName"
		kontaktWPunkcieNazwisko column: "contactAtPointLastName"
		kontaktWPunkcieFax column: "contactAtPointFax"
		kontaktWPunkcieTelStacjonarny column: "contactAtPointPhone"
		kontaktWPunkcieTelKomorkowy column: "contactAtPointMobilePhone"
		kontaktWPunkcieEmail column: "contactAtPointEmail"
	}
	
	static constraints = {
	}
}

