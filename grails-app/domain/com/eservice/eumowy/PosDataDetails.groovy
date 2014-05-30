package com.eservice.eumowy

import org.apache.commons.lang.builder.HashCodeBuilder

class PosDataDetails implements Serializable {

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
	
	String tytulInformatykDynamiczna //property removed in eUmowy_ext-605
	String kontaktInformatykDynamiczna //property removed in eUmowy_ext-605
	String imieInformatykDynamiczna //property removed in eUmowy_ext-605
	String nazwiskoInformatykDynamiczna //property removed in eUmowy_ext-605

    //ceny preferencyjne removed in eUmowy_ext-605
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
	
	static belongsTo = [pos: PosData]
	
	static mapping = {
		table name: "POS_DETAILS", schema:DomainConsts.SHEMA_NAME
		id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.POS_DETAILS_SEQ']
		dialupTyp column: "dialup_type"
		dialupIlosc column: "dialup_count"
		dialupPPIlosc column: "dialup_pp_count"
		dialupCena column: "dialup_price"
		dialupPPCena column: "dialup_pp_price"
        dialupCenaPreferencyjna column: "dialup_price_pref"
        dialupPPCenaPreferencyjna column: "dialup_pp_price_pref"
		vpnTyp column: "vpn_type"
		vpnIlosc column: "vpn_count"
		vpnPPIlosc column: "vpn_pp_count"
		vpnCena column: "vpn_price"
		vpnPPCena column: "vpn_pp_price"
        vpnCenaPreferencyjna column: "vpn_price_pref"
        vpnPPCenaPreferencyjna column: "vpn_pp_price_pref"
		sslTyp column: "ssl_type"
		sslIlosc column: "ssl_count"
		sslPPIlosc column: "ssl_pp_count"
		sslCena column: "ssl_price"
		sslPPCena column: "ssl_pp_price"
        sslCenaPreferencyjna column: "ssl_price_pref"
        sslPPCenaPreferencyjna column: "ssl_pp_price_pref"
		wifiTyp column: "wifi_type"
		wifiIlosc column: "wifi_count"
		wifiPPIlosc column: "wifi_pp_count"
		wifiCena column: "wifi_price"
        wifiCenaPreferencyjna column: "wifi_price_pref"
        wifiPPCena column: "wifi_pp_price"
		gprsTyp column: "gprs_type"
		gprsIlosc column: "gprs_count"
		gprsPPIlosc column: "gprs_pp_count"
		gprsCena column: "gprs_price"
		gprsPPCena column: "gprs_pp_price"
        gprsCenaPreferencyjna column: "gprs_price_pref"
        gprsPPCenaPreferencyjna column: "gprs_pp_price_pref"
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
		logowaniePrzedKazdaTransakcja column: "log_beforeeverytransaction"
		logowanieZmianowe column: "log_everychange"
		napiwek1 column: "tip1"
		telePompka column: "tele_pompka"
		teleKodzik column: "tele_kodzik"
		kartaPodarunkowa column: "gift_card"
		pinPadTyp column: "pin_pad_type"
        kartaSimTyp column: "sim_card_type"
        kartaSimIlosc column: "sim_card_count"
		pinPadIlosc column: "pin_pad_count"
		pinPadCena column: "pin_pad_price"
        pinPadCenaPreferencyjna column: "pin_pad_price_pref"
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
		tytulInformatykDynamiczna column: "dynamicdevicesupp_title"
		kontaktInformatykDynamiczna column: "dynamicdevicesupp_contact"
		imieInformatykDynamiczna column: "dynamicdevicesupp_name"
		nazwiskoInformatykDynamiczna column: "dynamicdevicesupp_surname"
	}
	
    static constraints = {
		pos(nullable:true)
		dialupTyp(nullable:true)
		dialupIlosc(nullable:true)
		dialupPPIlosc(nullable:true)
		dialupCena(nullable:true)
		dialupPPCena(nullable:true)
		vpnTyp(nullable:true)
		vpnIlosc(nullable:true)
		vpnPPIlosc(nullable:true)
		vpnCena(nullable:true)
		vpnPPCena(nullable:true)
		sslTyp(nullable:true)
		sslIlosc(nullable:true)
		sslPPIlosc(nullable:true)
		sslCena(nullable:true)
		sslPPCena(nullable:true)
		wifiTyp(nullable:true)
		wifiIlosc(nullable:true)
		wifiPPIlosc(nullable:true)
		wifiCena(nullable:true)
		wifiPPCena(nullable:true)
		gprsTyp(nullable:true)
		gprsIlosc(nullable:true)
		gprsPPIlosc(nullable:true)
		gprsCena(nullable:true)
		gprsPPCena(nullable:true)
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
        tytulInformatykDynamiczna(nullable: true)
		kontaktInformatykDynamiczna(nullable:true)
		imieInformatykDynamiczna(nullable:true)
		nazwiskoInformatykDynamiczna(nullable:true)
        dialupCenaPreferencyjna(nullable:true)
        dialupPPCenaPreferencyjna(nullable:true)
        vpnCenaPreferencyjna(nullable:true)
        vpnPPCenaPreferencyjna(nullable:true)
        sslCenaPreferencyjna(nullable:true)
        sslPPCenaPreferencyjna(nullable:true)
        gprsCenaPreferencyjna(nullable:true)
        gprsPPCenaPreferencyjna(nullable:true)
        pinPadCenaPreferencyjna(nullable:true)
        wifiCenaPreferencyjna(nullable:true)
        kartaSimTyp(nullable:true)
        kartaSimIlosc(nullable:true)
    }
	
}
