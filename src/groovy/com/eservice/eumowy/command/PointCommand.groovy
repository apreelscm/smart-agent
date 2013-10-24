package com.eservice.eumowy.command

import grails.validation.Validateable

/**
 * User: Dominik Walczak
 * Date: 20.08.13 Time: 10:28
 *
 */

@Validateable
class PointCommand implements Serializable {
	
	Integer id
	
	String phPozysk
	String opiekaBiznesowa
	String opiekaSerwisowaI
	String opiekaSerwisowaII
	String opiekaSerwisowaIII

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
	
	Integer cbdId
	
	String dialupTyp
	Integer dialupIlosc
	Integer dialupPPIlosc
	BigDecimal dialupCena
	BigDecimal dialupPPCena
	String vpnTyp
	Integer vpnIlosc
	Integer vpnPPIlosc
	BigDecimal vpnCena
	BigDecimal vpnPPCena
	String sslTyp
	Integer sslIlosc
	Integer sslPPIlosc
	BigDecimal sslCena
	BigDecimal sslPPCena
	String wifiTyp
	Integer wifiIlosc
	Integer wifiPPIlosc
	BigDecimal wifiCena
	BigDecimal wifiPPCena
	String gprsTyp
	Integer gprsIlosc
	Integer gprsPPIlosc
	BigDecimal gprsCena
	BigDecimal gprsPPCena
	Integer bazaIlosc
	
	String zamkniecieDniaOd
	String zamkniecieDniaDo
	Date planowanaDataInstalacji
	String uwagiDodatkowe
	
	Boolean preautoryzacja
	Boolean brakFunkcjiZwrotu
	Boolean zwrotNaHaslo
	Boolean analizaZbioru
	Boolean integracjaZSysKas
	Boolean zwrotyIKO
	
	Boolean logowaniePrzedKazdaTransakcja
	Boolean logowanieZmianowe
	
	Boolean napiwek1
	Boolean telePompka
	Boolean teleKodzik
	
	Boolean kartaPodarunkowa
	Integer terminalIlosc
	
	// polskie nazwy
	String pinPadTyp
	Integer pinPadIlosc
	BigDecimal pinPadCena
	String routerTyp
	Integer routerIlosc
	BigDecimal routerCena
	String czytnikKartTyp
	Integer czytnikKartIlosc
	BigDecimal czytnikKartCena
	
	String inneWyposazenie
	Boolean inneWyposazenieSsl
	Boolean inneWyposazenieGprs
	String inneWyposazenieTyp
	Integer inneWyposazenieIlosc
	BigDecimal inneWyposazenieCena
	
	String maska
	String bramka
	String adresIp
	String tytulInformatykStatyczna
	String kontaktInformatykStatyczna
	String imieInformatykStatyczna
	String nazwiskoInformatykStatyczna
	
	String tytulInformatykDynamiczna
	String kontaktInformatykDynamiczna
	String imieInformatykDynamiczna
	String nazwiskoInformatykDynamiczna	
	
	Boolean takSamoDlaWszystkichPunktow
	Boolean zestawPosTakSamoDlaWszystkichPunktow
	Boolean wydrukJakDlaMerchanta
	Boolean wydrukJakPowyzej
	Boolean dodatkoweWyposazenieTakSamoDlaWszystkichPunktow
	Boolean funkcjeTerminalaTakSamoDlaWszystkichPunktow
	Boolean informacjeTechniczneTakSamoDlaWszystkichPunktow
	Boolean kontaktWPunkcieJakDlaMerchanta
	Boolean korespondencjaJakDlaMerchantaLubWydruku
	
	static constraints = {
		phPozysk(nullable:true, blank:false, shared: "alphanumeric")
		opiekaBiznesowa(nullable:true, blank:false, shared: "alphanumeric")
		opiekaSerwisowaI(nullable:true, blank:true, shared: "alphanumeric")
		opiekaSerwisowaII(nullable:true, blank:true, shared: "alphanumeric")
		opiekaSerwisowaIII(nullable:true, blank:true, shared: "alphanumeric")
		nipPunktu(nullable:true, blank:false, shared: "natural")
		kodMCC(nullable:true, shared: "natural")
		rodzProwadzDzialalWPraktyce(nullable:true, blank:false)
		numerRachunkuBankowego(nullable:true, blank:false, matches: "~|\\d{2}\\s\\d{4}\\s\\d{4}\\s\\d{4}\\s\\d{4}\\s\\d{4}\\s\\d{4}")
		bank(nullable:true, blank:false)
		nazwaDoWydrukuZTerminalaPos(nullable:true)
		nazwaDoWyszukiwarki(nullable:true)
		wydrukUlicaTytul(nullable:true)
		wydrukUlica(nullable:true, blank:false, shared:"alphanumeric")
		wydrukNrDomu(nullable:true, blank:false, shared: "alphanumeric")
		wydrukNrLokalu(nullable:true, blank:false, shared: "alphanumeric")
		wydrukMiasto(nullable:true, blank:false, shared: "alphanumeric")
		wydrukKodPocztowy(nullable:true, blank:false, shared: "postalCodeValidator")
		wydrukPoczta(nullable:true, blank:false, shared: "alphanumeric")
		wydrukLinia1(nullable:true, blank:true)
		wydrukLinia2(nullable:true, blank:true)
		korespondencjaUlicaTytul(nullable:true)
		korespondencjaUlica(nullable:true, blank:false, shared: "alphanumeric")
		korespondencjaNrDomu(nullable:true, blank:false, shared: "alphanumeric")
		korespondencjaNrLokalu(nullable:true, blank:false, shared: "alphanumeric")
		korespondencjaMiasto(nullable:true, blank:false, shared: "alphanumeric")
		korespondencjaKodPocztowy(nullable:true, blank:false, shared: "postalCodeValidator")
		korespondencjaPoczta(nullable:true, blank:false, shared:"alphanumeric")
		kontaktWPunkcieTytul(nullable:true)
		kontaktWPunkcieImie(nullable:true, blank:false, shared: "lettersOnly")
		kontaktWPunkcieNazwisko(nullable:true, blank:false, shared: "lettersOnly")
		kontaktWPunkcieFax(nullable:true, blank:true)
		kontaktWPunkcieTelStacjonarny(nullable:true, blank:false)
		kontaktWPunkcieTelKomorkowy(nullable:true, blank:false)
//		TODO - sprawdzic czy to zadziala; mozliwe, ze trzeba nadac nazwy pol pokroju "points[0].kontaktWPunkcieTelStacjonarny
//		kontaktWPunkcieTelStacjonarny(nullable:true, validator: { value, pointDetails, errors ->
//			if (value == null || value.isEmpty()) {
//				if (pointDetails.kontaktWPunkcieTelKomorkowy == null || pointDetails.kontaktWPunkcieTelKomorkowy.isEmpty()){
//					errors.rejectValue( "kontaktWPunkcieTelStacjonarny", "default.atLeastOne.phoneNumber", "Nale\u017Cy poda\u0107 przynajmniej jeden numer telefonu")
//					errors.rejectValue( "kontaktWPunkcieTelKomorkowy", "default.atLeastOne.phoneNumber", "Nale\u017Cy poda\u0107 przynajmniej jeden numer telefonu")
//					return false
//				}
//			}
//			return true
//		})
		kontaktWPunkcieEmail(nullable:true, shared: "email")
		terminalIlosc(nullable:true, shared: "natural")
		bankId(nullable:true)
		
		dialupTyp(nullable:true)
		dialupIlosc(nullable:true, blank:false, shared: "natural")
		dialupPPIlosc(nullable:true, blank:false, shared: "natural")
		dialupCena(nullable:true, blank:false, shared: "number")
		dialupPPCena(nullable:true, blank:false, shared: "number")
		vpnTyp(nullable:true)
		vpnIlosc(nullable:true, blank:false, shared: "natural")
		vpnPPIlosc(nullable:true, blank:false, shared: "natural")
		vpnCena(nullable:true, blank:false, shared: "number")
		vpnPPCena(nullable:true, blank:false, shared: "number")
		sslTyp(nullable:true)
		sslIlosc(nullable:true, blank:false, shared: "natural")
		sslPPIlosc(nullable:true, blank:false, shared: "natural")
		sslCena(nullable:true, blank:false, shared: "number")
		sslPPCena(nullable:true, blank:false, shared: "number")
		wifiTyp(nullable:true)
		wifiIlosc(nullable:true, blank:false, shared: "number")
		wifiPPIlosc(nullable:true, blank:false, shared: "natural")
		wifiCena(nullable:true, blank:false, shared: "number")
		wifiPPCena(nullable:true, blank:false, shared: "number")
		gprsTyp(nullable:true)
		gprsIlosc(nullable:true, blank:false, shared: "natural")
		gprsPPIlosc(nullable:true, blank:false, shared: "number")
		gprsCena(nullable:true, blank:false, shared: "number")
		gprsPPCena(nullable:true, blank:false, shared: "number")
		bazaIlosc(nullable:true, blank:false, shared: "number")
		//zamkniecieDniaOd(nullable:true, blank:false, shared: "date")
		//zamkniecieDniaDo(nullable:true, blank:false, shared: "date")
		//planowanaDataInstalacji(nullable:true, blank:true, shared: "date")
		uwagiDodatkowe(nullable:true, blank:false, shared: "alphanumeric")
		preautoryzacja(nullable:true)
		brakFunkcjiZwrotu(nullable:true)
		zwrotNaHaslo(nullable:true)
		analizaZbioru(nullable:true)
		integracjaZSysKas(nullable:true)
		zwrotyIKO(nullable:true)
		logowaniePrzedKazdaTransakcja(nullable:true)
		logowanieZmianowe(nullable:true)
		napiwek1(nullable:true)
		telePompka(nullable:true)
		teleKodzik(nullable:true)
		kartaPodarunkowa(nullable:true)
		pinPadTyp(nullable:true)
		pinPadIlosc(nullable:true, blank:false, shared: "natural")
		pinPadCena(nullable:true, blank:false, shared: "number")
		routerTyp(nullable:true)
		routerIlosc(nullable:true, blank:false, shared: "natural")
		routerCena(nullable:true, blank:false, shared: "number")
		czytnikKartTyp(nullable:true)
		czytnikKartIlosc(nullable:true, blank:false, shared: "natural")
		czytnikKartCena(nullable:true, blank:false, shared: "number")
		inneWyposazenie(nullable:true, blank:true)
		inneWyposazenieSsl(nullable:true, blank:true)
		inneWyposazenieGprs(nullable:true, blank:true)
		inneWyposazenieTyp(nullable:true, blank:true)
		inneWyposazenieIlosc(nullable:true, blank:true, shared: "natural")
		inneWyposazenieCena(nullable:true, blank:true, shared: "number")
		maska(nullable:true, blank:false)
		bramka(nullable:true, blank:false)
		adresIp(nullable:true, blank:false)
		kontaktInformatykStatyczna(nullable:true, blank:false)
        tytulInformatykStatyczna(nullable:true, blank:false)
        imieInformatykStatyczna(nullable:true, blank:false, shared: "lettersOnly")
		nazwiskoInformatykStatyczna(nullable:true, blank:false, shared: "lettersOnly")
		kontaktInformatykDynamiczna(nullable:true, blank:false)
        tytulInformatykDynamiczna(nullable:true, blank:false)
		imieInformatykDynamiczna(nullable:true, blank:false, shared: "lettersOnly")
		nazwiskoInformatykDynamiczna(nullable:true, blank:false, shared: "lettersOnly")
		
		
	}
}
