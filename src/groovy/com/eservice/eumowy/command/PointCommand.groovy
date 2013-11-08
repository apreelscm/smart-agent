package com.eservice.eumowy.command

import com.eservice.eumowy.CalculatorService
import com.eservice.eumowy.annotation.Omit
import grails.validation.Validateable

/**
 * User: Dominik Walczak
 * Date: 20.08.13 Time: 10:28
 *
 */

@Validateable
class PointCommand extends BaseCommand {

	Integer id
	
	Long parentPosId
	
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
    Integer wifiPPIlosc
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
    String kartaSimTyp
	Integer pinPadIlosc
    Integer kartaSimIlosc
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

    BigDecimal dialupCenaPreferencyjna
    BigDecimal dialupPPCenaPreferencyjna
    BigDecimal vpnCenaPreferencyjna
    BigDecimal vpnPPCenaPreferencyjna
    BigDecimal sslCenaPreferencyjna
    BigDecimal sslPPCenaPreferencyjna
    BigDecimal gprsCenaPreferencyjna
    BigDecimal gprsPPCenaPreferencyjna
    BigDecimal pinPadCenaPreferencyjna
    BigDecimal wifiCenaPreferencyjna

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

		kontaktWPunkcieEmail(nullable:true, shared: "email")
		terminalIlosc(nullable:true, shared: "natural", validator: {value, cmd, errors ->
           if(value == null){
               return true
           }
           def terminalsCount = 0
            terminalsCount += cmd?.dialupIlosc != null ? cmd?.dialupIlosc : 0
            terminalsCount += cmd?.vpnIlosc != null ? cmd?.vpnIlosc : 0
            terminalsCount += cmd?.sslIlosc != null ? cmd?.sslIlosc : 0
            terminalsCount += cmd?.gprsIlosc != null ? cmd?.gprsIlosc : 0
            terminalsCount += cmd?.pinPadIlosc != null ? cmd?.pinPadIlosc : 0
            terminalsCount += cmd?.wifiIlosc != null ? cmd?.wifiIlosc : 0

            if(value && value > terminalsCount){
                errors.rejectValue("terminalIlosc", "panel.tooMany.terminalIlosc", [value, terminalsCount] as Object[], "")
                return false
            }
            return true
        })
		bankId(nullable:true)
		
		dialupTyp(nullable:true)
		dialupIlosc(nullable:true,  shared: "natural")
		dialupPPIlosc(nullable:true,  shared: "natural")
		dialupCena(nullable:true, shared: "number", validator: { value, cmd, errors ->
			cmd.dialupTyp ? atLeastClosure.call(value, cmd, errors, "dialupCena", "TYP_DIALUP_TERM_CENA") : true;
		})
		dialupPPCena(nullable:true, shared: "number", validator: { value, cmd, errors ->
			cmd.dialupTyp ? atLeastClosure.call(value, cmd, errors, "dialupPPCena", "TYP_DIALUP_PP_CENA") : true;
		})
		vpnTyp(nullable:true)
		vpnIlosc(nullable:true,  shared: "natural")
		vpnPPIlosc(nullable:true,  shared: "natural")
		vpnCena(nullable:true, shared: "number", validator: { value, cmd, errors ->
			cmd.vpnTyp ? atLeastClosure.call(value, cmd, errors, "vpnCena", "TYP_VPN_TERM_CENA") : true;
		})
		vpnPPCena(nullable:true, shared: "number", validator: { value, cmd, errors ->
			cmd.vpnTyp ? atLeastClosure.call(value, cmd, errors, "vpnPPCena", "TYP_VPN_PP_CENA") : true;
		})
		sslTyp(nullable:true)
		sslIlosc(nullable:true,  shared: "natural")
		sslPPIlosc(nullable:true,  shared: "natural")
		sslCena(nullable:true, shared: "number", validator: { value, cmd, errors ->
			cmd.sslTyp ? atLeastClosure.call(value, cmd, errors, "sslCena", "TYP_SSL_TERM_CENA") : true;
		})
		sslPPCena(nullable:true, shared: "number", validator: { value, cmd, errors ->
			cmd.sslTyp ? atLeastClosure.call(value, cmd, errors, "sslPPCena", "TYP_SSL_PP_CENA") : true;
		})
		wifiTyp(nullable:true)
		wifiIlosc(nullable:true,  shared: "number")
		wifiPPIlosc(nullable:true,  shared: "natural")
		wifiCena(nullable:true, shared: "number", validator: { value, cmd, errors ->
			cmd.wifiTyp ? atLeastClosure.call(value, cmd, errors, "wifiCena", "TYP_WIFI_TERM_CENA") : true;
		})
		wifiPPCena(nullable:true, shared: "number", validator: { value, cmd, errors ->
			cmd.wifiTyp ? atLeastClosure.call(value, cmd, errors, "wifiPPCena", "TYP_WIFI_PP_CENA") : true;
		})
		gprsTyp(nullable:true)
		gprsIlosc(nullable:true,  shared: "natural")
		gprsPPIlosc(nullable:true,  shared: "number")
		gprsCena(nullable:true, shared: "number", validator: { value, cmd, errors ->
			cmd.gprsTyp ? atLeastClosure.call(value, cmd, errors, "gprsCena", "TYP_GPRS_TERM_CENA") : true;
		})
		gprsPPCena(nullable:true, shared: "number", validator: { value, cmd, errors ->
			cmd.gprsTyp ? atLeastClosure.call(value, cmd, errors, "gprsPPCena", "TYP_GPRS_PP_CENA") : true;
		})
		bazaIlosc(nullable:true,  shared: "number")
        dialupCenaPreferencyjna(nullable:true, shared: "number", validator: { value, cmd, errors ->
			cmd.dialupTyp ? atLeastClosure.call(value, cmd, errors, "dialupCenaPreferencyjna", "TYP_DIALUP_CENA") : true;
		})
        dialupPPCenaPreferencyjna(nullable:true, shared: "number", validator: { value, cmd, errors ->
			cmd.dialupTyp ? atLeastClosure.call(value, cmd, errors, "dialupPPCenaPreferencyjna", "TYP_DIALUP_PP_CENA") : true;
		})
        vpnCenaPreferencyjna(nullable:true, shared: "number", validator: { value, cmd, errors ->
			cmd.vpnTyp ? atLeastClosure.call(value, cmd, errors, "vpnCenaPreferencyjna", "TYP_VPN_CENA") : true;
		})
        vpnPPCenaPreferencyjna(nullable:true, shared: "number", validator: { value, cmd, errors ->
			cmd.vpnTyp ? atLeastClosure.call(value, cmd, errors, "vpnPPCenaPreferencyjna", "TYP_VPN_PP_CENA") : true;
		})
        sslCenaPreferencyjna(nullable:true, shared: "number", validator: { value, cmd, errors ->
			cmd.sslTyp ? atLeastClosure.call(value, cmd, errors, "sslCenaPreferencyjna", "TYP_SSL_CENA") : true;
		})
        sslPPCenaPreferencyjna(nullable:true, shared: "number", validator: { value, cmd, errors ->
			cmd.sslTyp ? atLeastClosure.call(value, cmd, errors, "sslPPCenaPreferencyjna", "TYP_SSL_PP_CENA") : true;
		})
        gprsCenaPreferencyjna(nullable:true, shared: "number", validator: { value, cmd, errors ->
			cmd.gprsTyp ? atLeastClosure.call(value, cmd, errors, "gprsCenaPreferencyjna", "TYP_GPRS_CENA") : true;
		})
        gprsPPCenaPreferencyjna(nullable:true, shared: "number", validator: { value, cmd, errors ->
			cmd.gprsTyp ? atLeastClosure.call(value, cmd, errors, "gprsPPCenaPreferencyjna", "TYP_GPRS_PP_CENA") : true;
		})
        pinPadCenaPreferencyjna(nullable:true, shared: "number", validator: { value, cmd, errors ->
			cmd.pinPadTyp ? atLeastClosure.call(value, cmd, errors, "pinPadCenaPreferencyjna", "TYP_PINPAD_CENA") : true;
		})
        wifiCenaPreferencyjna(nullable:true, shared: "number", validator: { value, cmd, errors ->
			cmd.wifiTyp ? atLeastClosure.call(value, cmd, errors, "wifiCenaPreferencyjna", "TYP_WIFI_CENA") : true;
		})
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
        kartaSimTyp(nullable:true)
		pinPadIlosc(nullable:true,  shared: "natural")
        kartaSimIlosc(nullable: true, shared: "natural")
		pinPadCena(nullable:true,  shared: "number")
		routerTyp(nullable:true)
		routerIlosc(nullable:true,  shared: "natural")
		routerCena(nullable:true,  shared: "number")
		czytnikKartTyp(nullable:true)
		czytnikKartIlosc(nullable:true,  shared: "natural")
		czytnikKartCena(nullable:true,  shared: "number")
		inneWyposazenie(nullable:true, blank:true)
		inneWyposazenieSsl(nullable:true)
		inneWyposazenieGprs(nullable:true)
		inneWyposazenieTyp(nullable:true, blank:true)
		inneWyposazenieIlosc(nullable:true, shared: "natural")
		inneWyposazenieCena(nullable:true, shared: "number")
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
		
		parentPosId(nullable:true)
	}
}
