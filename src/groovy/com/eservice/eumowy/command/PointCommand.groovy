package com.eservice.eumowy.command

/**
 * User: Dominik Walczak
 * Date: 20.08.13 Time: 10:28
 *
 */
class PointCommand implements Serializable {
		
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
	String ulica
	Integer nrDomu
	Integer nrLokalu
	String miasto
	String kodPocztowy
	String poczta
	 
	String linia1
	String linia2
 	
	String contactAddressStreetType
	String ulicaDoKorespondencji
	Integer nrDomuDoKorespondencji
	Integer nrLokaluDoKorespondencji
	String miastoDoKorespondencji
	String kodPocztowyDoKorespondencji
	String pocztaDoKorespondencji
		
	String imie
	String nazwisko
	String nrFaksu
	String stacjonarny
	String komorka
	String email
	
	Integer cbdId
	
	String dialupTyp
	Integer dialupIloscc
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
}
