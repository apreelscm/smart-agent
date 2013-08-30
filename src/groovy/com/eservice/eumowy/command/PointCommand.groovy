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
	String numerRachunkuBankowego
	String bank
				 
	String nazwaDoWydrukuZTerminalaPos
	String nazwaDoWyszukiwarki
	 
	String wydrukUlicaTytul
	String wydrukUlica
	Integer wydrukNrDomu
	Integer wydrukNrLokalu
	String wydrukMiasto
	String wydrukKodPocztowy
	String WydrukPoczta
	 
	String wydrukLinia1
	String wydrukLinia2
	
	String korespondencjaJakDlaMerchantaLubWydruku
	String korespondencjaUlicaTytul
	String korespondencjaUlica
	Integer korespondencjaNrDomu
	Integer korespondencjaNrLokalu
	String korespondencjaMiasto
	String korespondencjaKodPocztowy
	String korespondencjaPoczta
		
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
	
	/* Not Saved To DB - only GUI */
	Boolean takSamoDlaWszystkichPunktow
	Boolean zestawPosTakSamoDlaWszystkichPunktow
	Boolean wydrukJakDlaMerchanta
	Boolean wydrukJakPowyzej
	Boolean dodatkoweWyposazenieTakSamoDlaWszystkichPunktow
	Boolean funkcjeTerminalaTakSamoDlaWszystkichPunktow
	Boolean informacjeTechniczneTakSamoDlaWszystkichPunktow
	Boolean kontaktWPunkcieJakDlaMerchanta
}
