package com.eservice.eumowy

import java.io.Serializable;
import java.util.Date;

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
	
	Date zamkniecieDniaOd
	Date zamkniecieDniaDo
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
	
	String inneWyposazenie
	Boolean inneWyposazenieSsl
	Boolean inneWyposazenieGprs
	String inneWyposazenieTyp
	Integer inneWyposazenieIlosc
	BigDecimal inneWyposazenieCena
	
	String maska
	String bramka
	String adresIp
	String kontaktInformatykStatyczna
	String imieInformatykStatyczna
	String nazwiskoInformatykStatyczna
	
	String kontaktInformatykDynamiczna
	String imieInformatykDynamiczna
	String nazwiskoInformatykDynamiczna	
	
	static belongsTo = [pos: PosData]
	
	static mapping = {
		table name: "POS_DETAILS", schema:DomainConsts.SHEMA_NAME
		id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.POS_DETAILS_SEQ']
		dialupTyp column: "dialup_type"
		dialupIlosc column: "dialup_count"
		dialupPPIlosc column: "dialup_pp_count"
		dialupCena column: "dialup_price"
		dialupPPCena column: "dialup_pp_price"
		vpnTyp column: "vpn_type"
		vpnIlosc column: "vpn_count"
		vpnPPIlosc column: "vpn_pp_count"
		vpnCena column: "vpn_price"
		vpnPPCena column: "vpn_pp_price"
		sslTyp column: "ssl_type"
		sslIlosc column: "ssl_count"
		sslPPIlosc column: "ssl_pp_count"
		sslCena column: "ssl_price"
		sslPPCena column: "ssl_pp_price"
		wifiTyp column: "wifi_type"
		wifiIlosc column: "wifi_count"
		wifiPPIlosc column: "wifi_pp_count"
		wifiCena column: "wifi_price"
		wifiPPCena column: "wifi_pp_price"
		gprsTyp column: "gprs_type"
		gprsIlosc column: "gprs_count"
		gprsPPIlosc column: "gprs_pp_count"
		gprsCena column: "gprs_price"
		gprsPPCena column: "gprs_pp_price"
		bazaIlosc column: "base_count"
		zamkniecieDniaOd column: "day_close_from"
		zamkniecieDniaDo column: "day_close_to"
		planowanaDataInstalacji column: "planned_installation_date"
		uwagiDodatkowe column: "additional_notes"
		preautoryzacja column: "preauthorization"
		brakFunkcjiZwrotu column: "noreturnfunction"
		zwrotNaHaslo column: "return_with_password"
		analizaZbioru column: "set_analysis"
		integracjaZSysKas column: "cash_machine_system_integration"
		zwrotyIKO column: "returnIKO"
		logowaniePrzedKazdaTransakcja column: "logging_before_every_transaction"
		logowanieZmianowe column: "loggin_every_change"
		napiwek1 column: "tip1"
		telePompka column: "telePompka"
		teleKodzik column: "teleKodzik"
		kartaPodarunkowa column: "gift_card"
		pinPadTyp column: "pin_pad_type"
		pinPadIlosc column: "pin_pad_count"
		pinPadCena column: "pin_pad_price"
		routerTyp column: "router_type"
		routerIlosc column: "router_count"
		routerCena column: "router_price"
		czytnikKartTyp column: "card_reader_type"
		czytnikKartIlosc column: "card_reader_count"
		czytnikKartCena column: "card_reader_price"
		inneWyposazenie column: "other_additional_device"
		inneWyposazenieSsl column: "other_additional_device_ssl"
		inneWyposazenieGprs column: "other_additional_device_gprs"
		inneWyposazenieTyp column: "other_additional_device_type"
		inneWyposazenieIlosc column: "other_additional_device_count"
		inneWyposazenieCena column: "other_additional_device_price"
		maska column: "static_device_mask"
		bramka column: "static_device_gateway"
		adresIp column: "static_device_ip"
		kontaktInformatykStatyczna column: "static_device_support_contact"
		imieInformatykStatyczna column: "static_device_support_contact_name"
		nazwiskoInformatykStatyczna column: "static_device_support_contact_surname"
		kontaktInformatykDynamiczna column: "dynamic_device_support_contact"
		imieInformatykDynamiczna column: "dynamic_device_support_name"
		nazwiskoInformatykDynamiczna column: "dynamic_device_support_surname"
	}
	
    static constraints = {
    }
	
}
