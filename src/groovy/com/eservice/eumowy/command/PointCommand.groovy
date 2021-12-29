package com.eservice.eumowy.command

import com.eservice.eumowy.PointDataDetails
import com.eservice.eumowy.enums.options.PosSystemSupplier
import com.eservice.eumowy.validator.ConditionValidator
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
	PointDataDetails.Risk ryzyko
	String kodMCC
	String rodzProwadzDzialalWPraktyce
	String numerRachunkuBankowego
	String bank
	Integer bankId

	String nazwaDoWydrukuZTerminalaPos
	String nazwaDoWyszukiwarki
	String idPartnerISV

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
    String dialupCena

    String dialupPPTyp
    Integer dialupPPIlosc
    String dialupPPCena

    String vpnTyp
    Integer vpnIlosc
    String vpnCena

    String vpnPPTyp
    Integer vpnPPIlosc
    String vpnPPCena

    String sslTyp
    Integer sslIlosc
    String sslCena

    String sslPPTyp
    Integer sslPPIlosc
    String sslPPCena

    String gprsTyp
    Integer gprsIlosc
    String gprsCena

    String gprsPPTyp
    Integer gprsPPIlosc
    String gprsPPCena

    String gprsTypPortable
    Integer gprsIloscPortable
    String gprsCenaPortable

    String wifiTypPortable
    Integer wifiIloscPortable
    String wifiCenaPortable

    String pinPadTyp
    Integer pinPadIlosc
    String pinPadCena

    BigDecimal minCenaNajmu //injected during validation

    Integer bazaIlosc

    String zamkniecieDniaOd
    String zamkniecieDniaDo
    Date planowanaDataInstalacji
    String uwagiDodatkowe

    Boolean brakFunkcjiZwrotu
    Boolean zwrotNaHaslo
    Boolean analizaZbioru
    Boolean integracjaZSysKas
    Boolean zwrotyIKO

    Boolean integracjaRs
    Boolean integracjaUsb
    Boolean integracjaEth
    String dostawcaSystemuKasowego

    Boolean logowaniePrzedKazdaTransakcja
    Boolean logowanieZmianowe

    Boolean napiwek1
    Boolean telePompka
    Boolean teleKodzik

    Boolean kartaPodarunkowa

    // polskie nazwy
    String kartaSimTyp
    BigDecimal kartaSimCena
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

    Boolean isAcceptedCardTransactions
    Boolean isPrivateApartment
    Boolean isAcceptedPrepayments
    BigDecimal monthlyCashTurnover
    BigDecimal monthlyTurnoverInInstitution
    BigDecimal averageBill
    BigDecimal highestCashTransaction
    String numberOfDailyTransactions
    Integer percentageOfPrepayments
    Integer averageDeliveryTime
    Integer maximumDeliveryTime

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
		ryzyko(nullable:true)
		kodMCC(nullable:true, shared: "natural")
		rodzProwadzDzialalWPraktyce(nullable:true, blank:false)
		numerRachunkuBankowego(nullable:true, blank:false, validator: {value, cmd, errors ->
            value ? NumberValidator.accountNumber(value, cmd, errors, propertyName) : true
        })
		bank(nullable:true, blank:false)
		nazwaDoWydrukuZTerminalaPos(nullable:true, maxSize: 44)
		nazwaDoWyszukiwarki(nullable:true)
		wydrukUlicaTytul(nullable:true)
		wydrukUlica(nullable:true, blank:false, shared:"alphanumeric")
		wydrukNrDomu(nullable:true, blank:false)
		wydrukNrLokalu(nullable:true, blank:false)
		wydrukMiasto(nullable:true, blank:false, shared: "alphanumericWithBrackets")
		wydrukKodPocztowy(nullable:true, blank:false, shared: "postalCodeValidator")
		wydrukPoczta(nullable:true, blank:true, shared: "alphanumeric")
		wydrukLinia1(nullable:true, blank:true)
		wydrukLinia2(nullable:true, blank:true)
		korespondencjaUlicaTytul(nullable:true)
		korespondencjaUlica(nullable:true, blank:false, shared: "alphanumeric")
		korespondencjaNrDomu(nullable:true, blank:false)
		korespondencjaNrLokalu(nullable:true, blank:false)
		korespondencjaMiasto(nullable:true, blank:false, shared: "alphanumericWithBrackets")
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

        hasTelefonKontaktowy(nullable: true, validator: { value, cmd, errors ->
            if (value && !(cmd.kontaktWPunkcieTelKomorkowy || cmd.kontaktWPunkcieTelStacjonarny)) {
                errors.rejectValue("hasTelefonKontaktowy", "default.atLeastOne.phoneNumber")
                return false
            }
            return true
        })

		bankId(nullable:true)

		dialupTyp(nullable:true)
		dialupIlosc(nullable:true,  shared: "natural")
		dialupCena(nullable:true, shared: "number", validator: { value, cmd, errors ->
            if(!cmd.dialupTyp) return true

            if(cmd.minCenaNajmu) {
                return ConditionValidator.atLeastMinValue(value, cmd, errors, propertyName, cmd.minCenaNajmu)
            }

			return ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "TYP_DIALUP_TERM_CENA")
		})

        dialupPPTyp(nullable:true)
        dialupPPIlosc(nullable:true,  shared: "natural")
        dialupPPCena(nullable:true, shared: "number", validator: { value, cmd, errors ->
            if(!cmd.dialupPPTyp) return true

            if(cmd.minCenaNajmu) {
                return ConditionValidator.atLeastMinValue(value, cmd, errors, propertyName, cmd.minCenaNajmu)
            }

            return ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "TYP_DIALUP_PP_CENA")
        })

		vpnTyp(nullable:true)
		vpnIlosc(nullable:true,  shared: "natural")
		vpnCena(nullable:true, shared: "number", validator: { value, cmd, errors ->
            if(!cmd.vpnTyp) return true

            if(cmd.minCenaNajmu) {
                return ConditionValidator.atLeastMinValue(value, cmd, errors, propertyName, cmd.minCenaNajmu)
            }

			return ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "TYP_VPN_TERM_CENA")
		})

        vpnPPTyp(nullable: true)
        vpnPPIlosc(nullable: true, shared: "natural")
        vpnPPCena(nullable: true, shared: "number", validator: { value, cmd, errors ->
            if (!cmd.vpnPPTyp) return true

            if (cmd.minCenaNajmu) {
                return ConditionValidator.atLeastMinValue(value, cmd, errors, propertyName, cmd.minCenaNajmu)
            }

            return ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "TYP_VPN_PP_CENA")
        })

        sslTyp(nullable: true)
        sslIlosc(nullable: true, shared: "natural")
        sslCena(nullable: true, shared: "number", validator: { value, cmd, errors ->
            if (!cmd.sslTyp) return true

            if (cmd.minCenaNajmu) {
                return ConditionValidator.atLeastMinValue(value, cmd, errors, propertyName, cmd.minCenaNajmu)
            }

            return ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "TYP_SSL_TERM_CENA")
        })

        sslPPTyp(nullable: true)
        sslPPIlosc(nullable: true, shared: "natural")
        sslPPCena(nullable: true, shared: "number", validator: { value, cmd, errors ->
            if (!cmd.sslPPTyp) return true

            if (cmd.minCenaNajmu) {
                return ConditionValidator.atLeastMinValue(value, cmd, errors, propertyName, cmd.minCenaNajmu)
            }

            return ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "TYP_SSL_PP_CENA")
        })

        gprsTyp(nullable: true)
        gprsIlosc(nullable: true, shared: "natural")
        gprsCena(nullable: true, shared: "number", validator: { value, cmd, errors ->
            if (!cmd.gprsTyp) return true

            if (cmd.minCenaNajmu) {
                return ConditionValidator.atLeastMinValue(value, cmd, errors, propertyName, cmd.minCenaNajmu)
            }

            return ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "TYP_GPRS_TERM_CENA")
        })

        gprsPPTyp(nullable: true)
        gprsPPIlosc(nullable: true, shared: "natural")
        gprsPPCena(nullable: true, shared: "number", validator: { value, cmd, errors ->
            if (!cmd.gprsPPTyp) return true

            if (cmd.minCenaNajmu) {
                return ConditionValidator.atLeastMinValue(value, cmd, errors, propertyName, cmd.minCenaNajmu)
            }

            return ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "TYP_GPRS_PP_CENA")
        })

        gprsTypPortable(nullable: true)
        gprsIloscPortable(nullable: true, shared: "natural")
        gprsCenaPortable(nullable: true, shared: "number", validator: { value, cmd, errors ->
            if (!cmd.gprsTypPortable) return true

            if (cmd.minCenaNajmu) {
                return ConditionValidator.atLeastMinValue(value, cmd, errors, propertyName, cmd.minCenaNajmu)
            }

            return ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "TYP_GPRS_TERM_CENA")
        })

        wifiTypPortable(nullable: true)
        wifiIloscPortable(nullable: true, shared: "natural")
        wifiCenaPortable(nullable: true, shared: "number", validator: { value, cmd, errors ->
            if (!cmd.wifiTypPortable) return true

            if (cmd.minCenaNajmu) {
                return ConditionValidator.atLeastMinValue(value, cmd, errors, propertyName, cmd.minCenaNajmu)
            }

            return ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "TYP_WIFI_TERM_CENA")
        })

        pinPadTyp(nullable: true)
        pinPadIlosc(nullable: true, shared: "natural")
        pinPadCena(nullable: true, shared: "number")

        bazaIlosc(nullable: true, shared: "number")
        uwagiDodatkowe(nullable: true)
        brakFunkcjiZwrotu(nullable: true)
        zwrotNaHaslo(nullable: true)
        analizaZbioru(nullable: true)
        integracjaZSysKas(nullable: true)
        zwrotyIKO(nullable: true)

        integracjaRs(nullable: true)
        integracjaUsb(nullable: true)
        integracjaEth(nullable: true)
        dostawcaSystemuKasowego(nullable: true, validator: { value, cmd, errors ->
            if (cmd.integracjaRs || cmd.integracjaUsb || cmd.integracjaEth) {
                return value != null
            }

            return true
        })

        logowaniePrzedKazdaTransakcja(nullable: true)
        logowanieZmianowe(nullable: true)
        napiwek1(nullable: true)
        telePompka(nullable: true)
        teleKodzik(nullable: true)
        kartaPodarunkowa(nullable: true)
        kartaSimTyp(nullable: true)
        kartaSimCena(nullable: true, shared: "number")
        kartaSimIlosc(nullable: true, shared: "natural")
        routerTyp(nullable: true)
        routerIlosc(nullable: true, shared: "natural")
        routerCena(nullable: true, shared: "number")
        czytnikKartTyp(nullable: true)
        czytnikKartCena(nullable: true, shared: "number")
        inneWyposazenie(nullable: true, blank: true)
        inneWyposazenieSsl(nullable: true)
        inneWyposazenieGprs(nullable: true)
        inneWyposazenieTyp(nullable: true, blank: true)
        inneWyposazenieTyp(nullable: true, blank: true)
        inneWyposazenieIlosc(nullable: true, shared: "natural")
        inneWyposazenieCena(nullable: true, shared: "number")
        maska(nullable: true, blank: false)
        bramka(nullable: true, blank: false)
        adresIp(nullable: true, blank: false)
        kontaktInformatykStatyczna(nullable: true, blank: false)
        tytulInformatykStatyczna(nullable: true, blank: false)
        imieInformatykStatyczna(nullable: true, blank: false, shared: "lettersOnly")
        nazwiskoInformatykStatyczna(nullable: true, blank: false, shared: "lettersOnly")
        monthlyCashTurnover(nullable: true, shared: "number")
        monthlyTurnoverInInstitution(nullable: true, shared: "number")
        averageBill(nullable: true, shared: "number")
        highestCashTransaction(nullable: true, shared: "number")
        numberOfDailyTransactions(nullable: true)
        percentageOfPrepayments(nullable: true, shared: "natural")
        averageDeliveryTime(nullable: true, shared: "natural")
        maximumDeliveryTime(nullable: true, shared: "natural")
        isAcceptedCardTransactions(nullable: true, validator: { value, cmd, errors ->
            if (value == null) {
                errors.rejectValue(propertyName, "scoring.atLeastOne.scoring")
                return false
            }
            return true
        })
        isAcceptedPrepayments(nullable: true, validator: { value, cmd, errors ->
            if (value == null) {
                errors.rejectValue(propertyName, "scoring.atLeastOne.scoring")
                return false
            }
            return true
        })
        isPrivateApartment(nullable: true, validator: { value, cmd, errors ->
            if (value == null) {
                errors.rejectValue(propertyName, "scoring.atLeastOne.scoring")
                return false
            }
            return true
        })

        parentPosId(nullable: true)
    }

    def isChildCopy() {
        return parentPosId != null
    }

    boolean hasStationaryTypeChosen() {
        return dialupTyp || dialupPPTyp || vpnTyp || vpnPPTyp || sslTyp || sslPPTyp || gprsTyp || gprsPPTyp
    }
}
