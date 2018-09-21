package com.eservice.eumowy

import org.apache.commons.lang.StringUtils

class PosDataDetails implements Serializable {

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

	String wifiTyp
	Integer wifiIlosc
	Integer wifiPPIlosc
	BigDecimal wifiCena
	BigDecimal wifiPPCena

	String gprsTyp
	Integer gprsIlosc
	BigDecimal gprsCena

    String gprsPPTyp
	Integer gprsPPIlosc
	BigDecimal gprsPPCena

    String gprsTypPortable
    Integer gprsIloscPortable
    BigDecimal gprsCenaPortable

	String wifiTypPortable
	Integer wifiIloscPortable
	BigDecimal wifiCenaPortable

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
	BigDecimal pinPadCena
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
		dialupCena column: "dialup_price"

        dialupPPTyp column: "dialup_pp_type"
		dialupPPIlosc column: "dialup_pp_count"
		dialupPPCena column: "dialup_pp_price"

		vpnTyp column: "vpn_type"
		vpnIlosc column: "vpn_count"
		vpnCena column: "vpn_price"

        vpnPPTyp column: "vpn_pp_type"
		vpnPPIlosc column: "vpn_pp_count"
		vpnPPCena column: "vpn_pp_price"

		sslTyp column: "ssl_type"
		sslIlosc column: "ssl_count"
		sslCena column: "ssl_price"

        sslPPTyp column: "ssl_pp_type"
		sslPPIlosc column: "ssl_pp_count"
		sslPPCena column: "ssl_pp_price"

		wifiTyp column: "wifi_type"
		wifiIlosc column: "wifi_count"
		wifiPPIlosc column: "wifi_pp_count"
		wifiCena column: "wifi_price"
        wifiPPCena column: "wifi_pp_price"

		gprsTyp column: "gprs_type"
		gprsIlosc column: "gprs_count"
		gprsCena column: "gprs_price"

        gprsPPTyp column: "gprs_pp_type"
		gprsPPIlosc column: "gprs_pp_count"
		gprsPPCena column: "gprs_pp_price"

        gprsTypPortable column: "gprs_typ_portable"
        gprsIloscPortable column: "gprs_ilosc_portable"
        gprsCenaPortable column: "gprs_cena_portable"

		wifiTypPortable column: "wifi_typ_portable"
		wifiIloscPortable column: "wifi_ilosc_portable"
		wifiCenaPortable column: "wifi_cena_portable"

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
		pinPadCena column: "pin_pad_price"
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
		dialupCena(nullable:true)

        dialupPPTyp(nullable: true)
		dialupPPIlosc(nullable:true)
		dialupPPCena(nullable:true)

		vpnTyp(nullable:true)
		vpnIlosc(nullable:true)
		vpnCena(nullable:true)

        vpnPPTyp(nullable:true)
		vpnPPIlosc(nullable:true)
		vpnPPCena(nullable:true)

		sslTyp(nullable:true)
		sslIlosc(nullable:true)
		sslCena(nullable:true)

        sslPPTyp(nullable:true)
		sslPPIlosc(nullable:true)
		sslPPCena(nullable:true)

		wifiTyp(nullable:true)
		wifiIlosc(nullable:true)
		wifiPPIlosc(nullable:true)
		wifiCena(nullable:true)
		wifiPPCena(nullable:true)

		gprsTyp(nullable:true)
		gprsIlosc(nullable:true)
		gprsCena(nullable:true)

        gprsPPTyp(nullable: true)
		gprsPPIlosc(nullable:true)
		gprsPPCena(nullable:true)

        gprsTypPortable(nullable: true)
        gprsIloscPortable(nullable: true)
        gprsCenaPortable(nullable: true)

		wifiTypPortable(nullable: true)
		wifiIloscPortable(nullable: true)
		wifiCenaPortable(nullable: true)

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
		pinPadCena(nullable:true)
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
