package com.eservice.eumowy

import org.apache.commons.lang.StringUtils

class PosDataDetails implements Serializable {

	String dialupTyp
	Integer dialupIlosc
	BigDecimal dialupCenaPosData

    String dialupPPTyp
	Integer dialupPPIlosc
	BigDecimal dialupPPCenaPosData

	String vpnTyp
	Integer vpnIlosc
	BigDecimal vpnCenaPosData

    String vpnPPTyp
	Integer vpnPPIlosc
	BigDecimal vpnPPCenaPosData

	String sslTyp
	Integer sslIlosc
	BigDecimal sslCenaPosData

    String sslPPTyp
	Integer sslPPIlosc
	BigDecimal sslPPCenaPosData

	String wifiTyp
	Integer wifiIlosc
	Integer wifiPPIlosc
	BigDecimal wifiCenaPosData
	BigDecimal wifiPPCenaPosData

	String gprsTyp
	Integer gprsIlosc
	BigDecimal gprsCenaPosData

    String gprsPPTyp
	Integer gprsPPIlosc
	BigDecimal gprsPPCenaPosData

    String gprsTypPortable
    Integer gprsIloscPortable
    BigDecimal gprsCenaPortablePosData

	String wifiTypPortable
	Integer wifiIloscPortable
	BigDecimal wifiCenaPortablePosData

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

	String pinPadTyp
	Integer pinPadIlosc
	BigDecimal pinPadCenaPosData
	String routerTyp
	Integer routerIlosc
	BigDecimal routerCena
	String czytnikKartTyp
	Integer czytnikKartIlosc
	BigDecimal czytnikKartCena
    String kartaSimTyp
    BigDecimal kartaSimCena
    Integer kartaSimIlosc

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

	static belongsTo = [pos: PosData]

	static mapping = {
		table name: "POS_DETAILS", schema:DomainConsts.SHEMA_NAME
		id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.POS_DETAILS_SEQ']

		dialupTyp column: "dialup_type"
		dialupIlosc column: "dialup_count"
		dialupCenaPosData column: "dialup_price"

        dialupPPTyp column: "dialup_pp_type"
		dialupPPIlosc column: "dialup_pp_count"
		dialupPPCenaPosData column: "dialup_pp_price"

		vpnTyp column: "vpn_type"
		vpnIlosc column: "vpn_count"
		vpnCenaPosData column: "vpn_price"

        vpnPPTyp column: "vpn_pp_type"
		vpnPPIlosc column: "vpn_pp_count"
		vpnPPCenaPosData column: "vpn_pp_price"

		sslTyp column: "ssl_type"
		sslIlosc column: "ssl_count"
		sslCenaPosData column: "ssl_price"

        sslPPTyp column: "ssl_pp_type"
		sslPPIlosc column: "ssl_pp_count"
		sslPPCenaPosData column: "ssl_pp_price"

		wifiTyp column: "wifi_type"
		wifiIlosc column: "wifi_count"
		wifiPPIlosc column: "wifi_pp_count"
		wifiCenaPosData column: "wifi_price"
        wifiPPCenaPosData column: "wifi_pp_price"

		gprsTyp column: "gprs_type"
		gprsIlosc column: "gprs_count"
		gprsCenaPosData column: "gprs_price"

        gprsPPTyp column: "gprs_pp_type"
		gprsPPIlosc column: "gprs_pp_count"
		gprsPPCenaPosData column: "gprs_pp_price"

        gprsTypPortable column: "gprs_typ_portable"
        gprsIloscPortable column: "gprs_ilosc_portable"
        gprsCenaPortablePosData column: "gprs_cena_portable"

		wifiTypPortable column: "wifi_typ_portable"
		wifiIloscPortable column: "wifi_ilosc_portable"
		wifiCenaPortablePosData column: "wifi_cena_portable"

		bazaIlosc column: "base_count"
		zamkniecieDniaOd column: "day_close_from"
		zamkniecieDniaDo column: "day_close_to"
		planowanaDataInstalacji column: "planned_installation_date"
		uwagiDodatkowe column: "additional_notes"
		preautoryzacja column: "preauthorization"
		brakFunkcjiZwrotu column: "noreturnfunction"
		zwrotNaHaslo column: "return_with_password"
		analizaZbioru column: "set_analysis"
		integracjaZSysKas column: "cashmachine_systemintegration"
		zwrotyIKO column: "returnIKO"
		integracjaRs column: "rs_integration"
		integracjaUsb column: "usb_integration"
		integracjaEth column: "eth_integration"
		dostawcaSystemuKasowego column: "pos_system_supplier"
		logowaniePrzedKazdaTransakcja column: "log_beforeeverytransaction"
		logowanieZmianowe column: "log_everychange"
		napiwek1 column: "tip1"
		telePompka column: "tele_pompka"
		teleKodzik column: "tele_kodzik"
		kartaPodarunkowa column: "gift_card"
		pinPadTyp column: "pin_pad_type"
        kartaSimTyp column: "sim_card_type"
        kartaSimCena column: "sim_card_price"
        kartaSimIlosc column: "sim_card_count"
		pinPadIlosc column: "pin_pad_count"
		pinPadCenaPosData column: "pin_pad_price"
        routerTyp column: "router_type"
		routerIlosc column: "router_count"
		routerCena column: "router_price"
		czytnikKartTyp column: "card_reader_type"
		czytnikKartIlosc column: "card_reader_count"
		czytnikKartCena column: "card_reader_price"
		inneWyposazenie column: "other_additional_device"
		inneWyposazenieSsl column: "otheradditionaldevice_ssl"
		inneWyposazenieGprs column: "otheradditionaldevice_gprs"
		inneWyposazenieTyp column: "otheradditionaldevice_type"
		inneWyposazenieIlosc column: "otheradditionaldevice_count"
		inneWyposazenieCena column: "otheradditionaldevice_price"
		maska column: "statdevice_mask"
		bramka column: "statdevice_gateway"
		adresIp column: "statdevice_ip"
		tytulInformatykStatyczna column: "statdevicesupp_title"
		kontaktInformatykStatyczna column: "statdevicesupp_contact"
		imieInformatykStatyczna column: "statdevicesupp_contactname"
		nazwiskoInformatykStatyczna column: "statdevicesupp_contactsurname"
	}

    static constraints = {
		pos(nullable:true)

        dialupTyp(nullable:true)
		dialupIlosc(nullable:true)
		dialupCenaPosData(nullable:true)

        dialupPPTyp(nullable: true)
		dialupPPIlosc(nullable:true)
		dialupPPCenaPosData(nullable:true)

		vpnTyp(nullable:true)
		vpnIlosc(nullable:true)
		vpnCenaPosData(nullable:true)

        vpnPPTyp(nullable:true)
		vpnPPIlosc(nullable:true)
		vpnPPCenaPosData(nullable:true)

		sslTyp(nullable:true)
		sslIlosc(nullable:true)
		sslCenaPosData(nullable:true)

        sslPPTyp(nullable:true)
		sslPPIlosc(nullable:true)
		sslPPCenaPosData(nullable:true)

		wifiTyp(nullable:true)
		wifiIlosc(nullable:true)
		wifiPPIlosc(nullable:true)
		wifiCenaPosData(nullable:true)
		wifiPPCenaPosData(nullable:true)

		gprsTyp(nullable:true)
		gprsIlosc(nullable:true)
		gprsCenaPosData(nullable:true)

        gprsPPTyp(nullable: true)
		gprsPPIlosc(nullable:true)
		gprsPPCenaPosData(nullable:true)

        gprsTypPortable(nullable: true)
        gprsIloscPortable(nullable: true)
        gprsCenaPortablePosData(nullable: true)

		wifiTypPortable(nullable: true)
		wifiIloscPortable(nullable: true)
		wifiCenaPortablePosData(nullable: true)

		bazaIlosc(nullable:true)
		zamkniecieDniaOd(nullable:true)
		zamkniecieDniaDo(nullable:true)
		planowanaDataInstalacji(nullable:true)
		uwagiDodatkowe(nullable:true)
		preautoryzacja(nullable:true)
		brakFunkcjiZwrotu(nullable:true)
		zwrotNaHaslo(nullable:true)
		analizaZbioru(nullable:true)
		integracjaZSysKas(nullable:true)
		zwrotyIKO(nullable:true)
		integracjaRs(nullable: true)
		integracjaUsb(nullable: true)
		integracjaEth(nullable: true)
		dostawcaSystemuKasowego(nullable: true)
		logowaniePrzedKazdaTransakcja(nullable:true)
		logowanieZmianowe(nullable:true)
		napiwek1(nullable:true)
		telePompka(nullable:true)
		teleKodzik(nullable:true)
		kartaPodarunkowa(nullable:true)
		pinPadTyp(nullable:true)
		pinPadIlosc(nullable:true)
		pinPadCenaPosData(nullable:true)
		routerTyp(nullable:true)
		routerIlosc(nullable:true)
		routerCena(nullable:true)
		czytnikKartTyp(nullable:true)
		czytnikKartIlosc(nullable:true)
		czytnikKartCena(nullable:true)
		inneWyposazenie(nullable:true)
		inneWyposazenieSsl(nullable:true)
		inneWyposazenieGprs(nullable:true)
		inneWyposazenieTyp(nullable:true)
		inneWyposazenieIlosc(nullable:true)
		inneWyposazenieCena(nullable:true)
		maska(nullable:true)
		bramka(nullable:true)
		adresIp(nullable:true)
        tytulInformatykStatyczna(nullable: true)
		kontaktInformatykStatyczna(nullable:true)
		imieInformatykStatyczna(nullable:true)
		nazwiskoInformatykStatyczna(nullable:true)
        kartaSimTyp(nullable:true)
        kartaSimCena(nullable:true)
        kartaSimIlosc(nullable:true)
    }

    public boolean isPortable() {
        return StringUtils.isNotEmpty(gprsTypPortable)
    }

    public boolean isPinpad() {
        return StringUtils.isNotEmpty(pinPadTyp)
    }

    public boolean isStationary() {
        return StringUtils.isNotEmpty(dialupTyp) || StringUtils.isNotEmpty(dialupPPTyp) || StringUtils.isNotEmpty(vpnTyp) ||
                StringUtils.isNotEmpty(vpnPPTyp) || StringUtils.isNotEmpty(sslTyp) || StringUtils.isNotEmpty(sslPPTyp) ||
                StringUtils.isNotEmpty(gprsTyp) || StringUtils.isNotEmpty(gprsPPTyp)
    }
}
