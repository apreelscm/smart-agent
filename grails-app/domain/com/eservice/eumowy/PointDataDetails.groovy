package com.eservice.eumowy

import java.util.List;

class PointDataDetails {
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
	 
	String dataforprintingAddressStreetType
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
	
	String imie
	String nazwisko
	String nrFaksu
	String stacjonarny
	String komorka
	String email
	
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
		dataforprintingAddressStreetType column: "dataforprintingAddressStreetType"
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
		imie column: "contactAtPointFirstName"
		nazwisko column: "contactAtPointLastName"
		nrFaksu column: "contactAtPointFax"
		stacjonarny column: "contactAtPointPhone"
		komorka column: "contactAtPointMobilePhone"
		email column: "contactAtPointEmail"
	}
	
	static constraints = {
	}
}
