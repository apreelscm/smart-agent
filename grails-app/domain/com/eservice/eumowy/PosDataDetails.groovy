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
		dialupTyp column: "dialupType"
		dialupIlosc column: "dialupCount"
		dialupPPIlosc column: "dialupPPCount"
		dialupCena column: "dialupPrice"
		dialupPPCena column: "dialupPPPrice"
		vpnTyp column: "vpnType"
		vpnIlosc column: "vpnCount"
		vpnPPIlosc column: "vpnPPCount"
		vpnCena column: "vpnPrice"
		vpnPPCena column: "vpnPPPrice"
		sslTyp column: "sslType"
		sslIlosc column: "sslCount"
		sslPPIlosc column: "sslPPCount"
		sslCena column: "sslPrice"
		sslPPCena column: "sslPPPrice"
		wifiTyp column: "wifiType"
		wifiIlosc column: "wifiCount"
		wifiPPIlosc column: "wifiPPCount"
		wifiCena column: "wifiPrice"
		wifiPPCena column: "wifiPPPrice"
		gprsTyp column: "gprsType"
		gprsIlosc column: "gprsCount"
		gprsPPIlosc column: "gprsPPCount"
		gprsCena column: "gprsPrice"
		gprsPPCena column: "gprsPPPrice"
		bazaIlosc column: "baseCount"
		zamkniecieDniaOd column: "dayCloseFrom"
		zamkniecieDniaDo column: "dayCloseTo"
		planowanaDataInstalacji column: "plannedInstallationDate"
		uwagiDodatkowe column: "additionalNotes"
		preautoryzacja column: "preauthorization"
		brakFunkcjiZwrotu column: "noreturnfunction"
		zwrotNaHaslo column: "returnWithPassword"
		analizaZbioru column: "setAnalysis"
		integracjaZSysKas column: "cashMachineSystemIntegration"
		zwrotyIKO column: "returnIKO"
		logowaniePrzedKazdaTransakcja column: "loggingBeforeEveryTransaction"
		logowanieZmianowe column: "logginEveryChange"
		napiwek1 column: "tip1"
		telePompka column: "telePompka"
		teleKodzik column: "teleKodzik"
		kartaPodarunkowa column: "giftCard"
		pinPadTyp column: "pinPadType"
		pinPadIlosc column: "pinPadCount"
		pinPadCena column: "pinPadPrice"
		routerTyp column: "routerType"
		routerIlosc column: "routerCount"
		routerCena column: "routerPrice"
		czytnikKartTyp column: "cardReaderType"
		czytnikKartIlosc column: "cardReaderCount"
		czytnikKartCena column: "cardReaderPrice"
		inneWyposazenie column: "otherAdditionalDevice"
		inneWyposazenieSsl column: "otherAdditionalDeviceSsl"
		inneWyposazenieGprs column: "otherAdditionalDeviceGprs"
		inneWyposazenieTyp column: "otherAdditionalDeviceType"
		inneWyposazenieIlosc column: "otherAdditionalDeviceCount"
		inneWyposazenieCena column: "otherAdditionalDevicePrice"
		maska column: "staticDeviceMask"
		bramka column: "staticDeviceGateway"
		adresIp column: "staticDeviceIp"
		kontaktInformatykStatyczna column: "staticDeviceSupportContact"
		imieInformatykStatyczna column: "staticDeviceSupportContactName"
		nazwiskoInformatykStatyczna column: "staticDeviceSupportContactSurname"
		kontaktInformatykDynamiczna column: "dynamicDeviceSupportContact"
		imieInformatykDynamiczna column: "dynamicDeviceSupportName"
		nazwiskoInformatykDynamiczna column: "dynamicDeviceSupportSurname"
	}
	
    static constraints = {
    }
	
}
