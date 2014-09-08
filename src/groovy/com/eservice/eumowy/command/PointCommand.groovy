package com.eservice.eumowy.command

import com.eservice.eumowy.validator.AtLeastValidator
import com.eservice.eumowy.validator.NumberValidator
import grails.validation.Validateable

@Validateable(nullable = true)
class PointCommand implements Serializable {

    transient def calculatorService
    transient def calc

	Integer id
	
	Boolean czyLokalny
	
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
	String kontaktWPunkcieTelStacjonarny
	String kontaktWPunkcieTelKomorkowy
	String kontaktWPunkcieEmail
	
	Integer cbdId
	
	String dialupTyp
	Integer dialupIlosc
	BigDecimal dialupCena

    String dialupPPTyp
    Integer dialupPPIlosc
    BigDecimal dialupPPCena

	String vpnTyp
	Integer vpnIlosc
	BigDecimal vpnCena

    String vpnPPTyp
    Integer vpnPPIlosc
    BigDecimal vpnPPCena

	String sslTyp
	Integer sslIlosc
	BigDecimal sslCena

    String sslPPTyp
    Integer sslPPIlosc
    BigDecimal sslPPCena

	String gprsTyp
	Integer gprsIlosc
	BigDecimal gprsCena

    String gprsPPTyp
    Integer gprsPPIlosc
    BigDecimal gprsPPCena

    String gprsTypPortable
    Integer gprsIloscPortable
    BigDecimal gprsCenaPortable

    String pinPadTyp
    Integer pinPadIlosc
    BigDecimal pinPadCena

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
	
	// polskie nazwy
    String kartaSimTyp
    Integer kartaSimIlosc
	String routerTyp
	Integer routerIlosc
	BigDecimal routerCena
	String czytnikKartTyp
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
	
	Boolean takSamoDlaWszystkichPunktow
	Boolean zestawPosTakSamoDlaWszystkichPunktow
	Boolean wydrukJakDlaMerchanta
	Boolean wydrukJakPowyzej
	Boolean dodatkoweWyposazenieTakSamoDlaWszystkichPunktow
	Boolean funkcjeTerminalaTakSamoDlaWszystkichPunktow
	Boolean informacjeTechniczneTakSamoDlaWszystkichPunktow
	Boolean kontaktWPunkcieJakDlaMerchanta
	Boolean korespondencjaJakDlaMerchantaLubWydruku

    Boolean hasDodaniePrepaid
    Boolean hasTelefonKontaktowy

	def isLocal() {
		return czyLokalny
	}
	
	static constraints = {
		phPozysk(nullable:true, blank:false, shared: "alphanumeric")
		opiekaBiznesowa(nullable:true, blank:false, shared: "alphanumeric")
		opiekaSerwisowaI(nullable:true, blank:true, shared: "alphanumeric")
		opiekaSerwisowaII(nullable:true, blank:true, shared: "alphanumeric")
		opiekaSerwisowaIII(nullable:true, blank:true, shared: "alphanumeric")
		nipPunktu(nullable:true, blank:false, shared: "natural")
		kodMCC(nullable:true, shared: "natural")
		rodzProwadzDzialalWPraktyce(nullable:true, blank:false)
		numerRachunkuBankowego(nullable:true, blank:false, validator: {value, cmd, errors ->
            value ? NumberValidator.accountNumber(value, cmd, errors, propertyName) : true
        })
		bank(nullable:true, blank:false)
		nazwaDoWydrukuZTerminalaPos(nullable:true, validator: {value, cmd, errors ->
            if(value && value.length() > 25) {
                errors.rejectValue("nazwaDoWydrukuZTerminalaPos", "nazwaDoWydrukuZTerminalaPos.maxSize.exceeded")
                return false
            }
            return true
        })
		nazwaDoWyszukiwarki(nullable:true)
		wydrukUlicaTytul(nullable:true)
		wydrukUlica(nullable:true, blank:false, shared:"alphanumeric")
		wydrukNrDomu(nullable:true, blank:false, shared: "alphanumeric")
		wydrukNrLokalu(nullable:true, blank:false, shared: "alphanumeric")
		wydrukMiasto(nullable:true, blank:false, shared: "alphanumeric")
		wydrukKodPocztowy(nullable:true, blank:false, shared: "postalCodeValidator")
		wydrukPoczta(nullable:true, blank:true, shared: "alphanumeric")
		wydrukLinia1(nullable:true, blank:true)
		wydrukLinia2(nullable:true, blank:true)
		korespondencjaUlicaTytul(nullable:true)
		korespondencjaUlica(nullable:true, blank:false, shared: "alphanumeric")
		korespondencjaNrDomu(nullable:true, blank:false, shared: "alphanumeric")
		korespondencjaNrLokalu(nullable:true, blank:false, shared: "alphanumeric")
		korespondencjaMiasto(nullable:true, blank:false, shared: "alphanumeric")
		korespondencjaKodPocztowy(nullable:true, blank:false, shared: "postalCodeValidator")
		korespondencjaPoczta(nullable:true, blank:true, shared:"alphanumeric")
		kontaktWPunkcieTytul(nullable:true)
		kontaktWPunkcieImie(nullable:true, blank:false, shared: "lettersOnly")
		kontaktWPunkcieNazwisko(nullable:true, blank:false, shared: "lettersOnly")
		kontaktWPunkcieTelStacjonarny(nullable:true, blank:false)
		kontaktWPunkcieTelKomorkowy(nullable:true, blank:false)

		kontaktWPunkcieEmail(nullable:true, shared: "email")

        hasDodaniePrepaid(nullable: true, validator: {value, cmd, errors ->
            if(value && !(cmd.telePompka || cmd.teleKodzik)){
                errors.rejectValue("hasDodaniePrepaid", "default.atLeastOne.doladowania.funkcjaTerminala")
                return false
            }
            return true
        })

        hasTelefonKontaktowy(nullable: true, validator: {value, cmd, errors ->
            if(value && !(cmd.kontaktWPunkcieTelKomorkowy || cmd.kontaktWPunkcieTelStacjonarny)){
                errors.rejectValue("hasTelefonKontaktowy", "default.atLeastOne.phoneNumber")
                return false
            }
            return true
        })

		bankId(nullable:true)
		
		dialupTyp(nullable:true)
		dialupIlosc(nullable:true,  shared: "natural")
		dialupCena(nullable:true, shared: "number", validator: { value, cmd, errors ->
			cmd.dialupTyp ? AtLeastValidator.validate(value, cmd, errors, "dialupCena", "TYP_DIALUP_TERM_CENA") : true;
		})

        dialupPPTyp(nullable:true)
        dialupPPIlosc(nullable:true,  shared: "natural")
        dialupPPCena(nullable:true, shared: "number", validator: { value, cmd, errors ->
            cmd.dialupPPTyp ? AtLeastValidator.validate(value, cmd, errors, "dialupPPCena", "TYP_DIALUP_PP_CENA") : true;
        })

		vpnTyp(nullable:true)
		vpnIlosc(nullable:true,  shared: "natural")
		vpnCena(nullable:true, shared: "number", validator: { value, cmd, errors ->
			cmd.vpnTyp ? AtLeastValidator.validate(value, cmd, errors, "vpnCena", "TYP_VPN_TERM_CENA") : true;
		})

        vpnPPTyp(nullable:true)
        vpnPPIlosc(nullable:true,  shared: "natural")
        vpnPPCena(nullable:true, shared: "number", validator: { value, cmd, errors ->
            cmd.vpnPPTyp ? AtLeastValidator.validate(value, cmd, errors, "vpnPPCena", "TYP_VPN_PP_CENA") : true;
        })

		sslTyp(nullable:true)
		sslIlosc(nullable:true,  shared: "natural")
		sslCena(nullable:true, shared: "number", validator: { value, cmd, errors ->
			cmd.sslTyp ? AtLeastValidator.validate(value, cmd, errors, "sslCena", "TYP_SSL_TERM_CENA") : true;
		})

        sslPPTyp(nullable:true)
        sslPPIlosc(nullable:true,  shared: "natural")
        sslPPCena(nullable:true, shared: "number", validator: { value, cmd, errors ->
            cmd.sslPPTyp ? AtLeastValidator.validate(value, cmd, errors, "sslPPCena", "TYP_SSL_PP_CENA") : true;
        })

		gprsTyp(nullable:true)
		gprsIlosc(nullable:true,  shared: "natural")
		gprsCena(nullable:true, shared: "number", validator: { value, cmd, errors ->
			cmd.gprsTyp ? AtLeastValidator.validate(value, cmd, errors, "gprsCena", "TYP_GPRS_TERM_CENA") : true;
		})

        gprsPPTyp(nullable:true)
        gprsPPIlosc(nullable:true,  shared: "natural")
        gprsPPCena(nullable:true, shared: "number", validator: { value, cmd, errors ->
            cmd.gprsPPTyp ? AtLeastValidator.validate(value, cmd, errors, "gprsPPCena", "TYP_GPRS_PP_CENA") : true;
        })

        gprsTypPortable(nullable:true)
        gprsIloscPortable(nullable:true,  shared: "natural")
        gprsCenaPortable(nullable:true, shared: "number", validator: { value, cmd, errors ->
            cmd.gprsTypPortable ? AtLeastValidator.validate(value, cmd, errors, "gprsCenaPortable", "TYP_GPRS_TERM_CENA") : true;
        })

		pinPadTyp(nullable:true)
		pinPadIlosc(nullable:true,  shared: "natural")
		pinPadCena(nullable:true,  shared: "number")

		bazaIlosc(nullable:true,  shared: "number")
		uwagiDodatkowe(nullable:true)
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
        kartaSimTyp(nullable:true)
        kartaSimIlosc(nullable: true, shared: "natural")
		routerTyp(nullable:true)
		routerIlosc(nullable:true,  shared: "natural")
		routerCena(nullable:true,  shared: "number")
		czytnikKartTyp(nullable:true)
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
		
		parentPosId(nullable:true)
	}
	
	def isChildCopy() {
		return parentPosId != null
	}

    boolean hasStationaryTypeChoosen() {
        return dialupTyp || dialupPPTyp || vpnTyp || vpnPPTyp || sslTyp || sslPPTyp || gprsTyp || gprsPPTyp
    }
}
