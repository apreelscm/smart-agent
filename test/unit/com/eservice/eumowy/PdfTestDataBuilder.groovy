package com.eservice.eumowy

import java.text.DecimalFormat

/**
 * User: Dominik Walczak
 * Date: 13.10.13 Time: 15:51
 *
 */
class PdfTestDataBuilder {

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static Random random = new Random();

    public static String randomString( int len )
    {
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( random.nextInt(AB.length()) ) );
        return sb.toString();
    }

    public static HashMap<String, String[]> prepareZestawPosOdplatneUzywanieData(){
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.put("oplatyPOSIloscA", ["10"] as String[]);
        data.put("oplatyPOSIloscB", ["20"] as String[]);
        data.put("oplatyPOSIloscC", ["30"] as String[]);
        data.put("oplatyPOSCenaA", ["40"] as String[]);
        data.put("oplatyPOSCenaB", ["50"] as String[]);
        data.put("oplatyPOSCenaC", ["50"] as String[]);
        data.put("oplatyPOSPrefIloscA", ["50"] as String[]);
        data.put("oplatyPOSPrefIloscB", ["50"] as String[]);
        data.put("oplatyPOSPrefIloscC", ["50"] as String[]);
        data.put("oplatyPOSPrefCenaA", ["11"] as String[]);
        data.put("oplatyPOSPrefCenaB", ["55"] as String[]);
        data.put("oplatyPOSPrefCenaC", ["53"] as String[]);
        data.put("oplatyPOSMiesiacNaliczania", ["1"] as String[]);
        return data;
    }

    public static HashMap<String, String[]> prepareDodatkoweUslugiData(){
        HashMap<String, String[]> result = new HashMap<String, String[]>();
        result.put("oplataZaDzienneZestawienieTransakcji", [getRandomFormattedDouble(20)] as String[]);
        result.put("oplataZaPotwierdzenieWykonaniaPrzelewu", [getRandomFormattedDouble(20)] as String[]);
        result.put("oplataZaDostarczeniePapieru", [getRandomFormattedDouble(20)] as String[]);
        result.put("oplataZaMiesieczneZestawienieTransakcji", [getRandomFormattedDouble(20)] as String[]);
        result.put("oplataZaZmianeGrafiki", [getRandomFormattedDouble(20)] as String[]);
        result.put("oplataZaInstalacjePOS", [getRandomFormattedDouble(20)] as String[]);
        result.put("oplataZaInstalacjeGPRS", [getRandomFormattedDouble(20)] as String[]);
        return result;
    }

    public static String getRandomFormattedDouble(int max){
        return new DecimalFormat("0.00").format(random.nextInt(max) + random.nextDouble());
    }

    public static HashMap<String, String[]> preparePoziomOplatIWarunkiPlatnosciData(){
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.put("pp_orange_tk", ["12"] as String[]);
        data.put("pp_orange_tp", ["22"] as String[]);
        data.put("pp_plus_tk", ["98"] as String[]);
        data.put("pp_plus_tp", ["45"] as String[]);
        data.put("pp_tmobile_tk", ["76"] as String[]);
        data.put("pp_tmobile_tp", ["11"] as String[]);
        data.put("pp_heyah_tk", ["30"] as String[]);
        data.put("pp_heyah_tp", ["49"] as String[]);
        data.put("pp_play_tk", ["20"] as String[]);
        data.put("pp_play_tp", ["72"] as String[]);
        data.put("pp_telegrosik_tk", ["13"] as String[]);
        data.put("pp_virginmobile_tk", ["14"] as String[]);
        data.put("pp_lycamobile_tk", ["15"] as String[]);
        data.put("pp_gtmobile_tk", ["60"] as String[]);
        data.put("pp_vectonemobile_tk", ["71"] as String[]);
        data.put("pp_delightmobile_tk", ["99"] as String[]);
        data.put("oplataZaOprogramowanieDoDoladowan", ["300"] as String[]);
        return data
    }

    public static HashMap<String, String[]> prepareDccData(){
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.put("oplataVISAPr", ["33"] as String[]);
        data.put("oplataVISA", ["34"] as String[]);
        data.put("oplataMasterCardPr", ["90"] as String[]);
        data.put("oplataMasterCard", ["34"] as String[]);
        data.put("oplataMaestroPr", ["56"] as String[]);
        data.put("oplataMaestro", ["83"] as String[]);
        return data
    }

    public static HashMap<String, String[]> preparePozopmOplatIWarunkiPlatnosciKartyData(){
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.put("visaEUKKOPr", ["11"] as String[]);
        data.put("visaEUKDPr", ["22"] as String[]);
        data.put("visaEUKBPr", ["22"] as String[]);
        data.put("visaOutEUKKOPr", ["33"] as String[]);
        data.put("visaOutEUKDPr", ["33"] as String[]);
        data.put("visaOutEUKBPr", ["44"] as String[]);
        data.put("visaPolskaKKO1Pr", ["44"] as String[]);
        data.put("visaPolskaKKO2Pr", ["55"] as String[]);
        data.put("visaPolskaKD1Pr", ["55"] as String[]);
        data.put("visaPolskaKD2Pr", ["55"] as String[]);
        data.put("visaPolskaKBPr", ["66"] as String[]);
        data.put("mastercardEUKKPr", ["55"] as String[]);
        data.put("mastercardEUKDPr", ["5"] as String[]);
        data.put("mastercardEUKBLPr", ["5"] as String[]);
        data.put("mastercardEUMPr", ["5"] as String[]);
        data.put("mastercardOutEUKKPr", ["6"] as String[]);
        data.put("mastercardOutEUKDPr", ["6"] as String[]);
        data.put("mastercardOutEUKBPr", ["6"] as String[]);
        data.put("mastercardOutEUMPr", ["6"] as String[]);
        data.put("mastercardPolskaKK1Pr", ["6"] as String[]);
        data.put("mastercardPolskaKK2Pr", ["6"] as String[]);
        data.put("mastercardPolskaKK3Pr", ["7"] as String[]);
        data.put("mastercardPolskaKD1Pr", ["66"] as String[]);
        data.put("mastercardPolskaKD2Pr", ["77"] as String[]);
        data.put("mastercardPolskaKD3Pr", ["7"] as String[]);
        data.put("mastercardPolskaKBPr", ["7"] as String[]);
        data.put("mastercardPolskaM1Pr", ["7"] as String[]);
        data.put("mastercardPolskaM2Pr", ["7"] as String[]);
        data.put("mastercardPolskaM3Pr", ["7"] as String[]);
        data.put("visaPKOBPKKO1Pr", ["7"] as String[]);
        data.put("visaPKOBPKKO2Pr", ["7"] as String[]);
        data.put("visaPKOBPKD1Pr", ["7"] as String[]);
        data.put("visaPKOBPKD2Pr", ["7"] as String[]);
        data.put("visaPKOBPKB3Pr", ["7"] as String[]);
        data.put("mastercardPKOBPKK1Pr", ["7"] as String[]);
        data.put("mastercardPKOBPKK2Pr", ["66"] as String[]);
        data.put("mastercardPKOBPKK3Pr", ["6"] as String[]);
        data.put("mastercardPKOBPKD1Pr", ["6"] as String[]);
        data.put("mastercardPKOBPKD2LPr", ["6"] as String[]);
        data.put("mastercardPKOBPKD3Pr", ["6"] as String[]);
        data.put("mastercardPKOBPKBPr", ["6"] as String[]);
        data.put("mastercardPKOBPM1Pr", ["6"] as String[]);
        data.put("mastercardPKOBPM2Pr", ["6"] as String[]);
        data.put("mastercardPKOBPM3Pr", ["6"] as String[]);
        data.put("dinersClubPr", ["6"] as String[]);
        data.put("ikoPr", ["6"] as String[]);
        data.put("visaEUKKOSt", ["6"] as String[]);
        data.put("visaEUKDSt", ["6"] as String[]);
        data.put("visaEUKBSt", ["6"] as String[]);
        data.put("visaOutEUKKOSt", ["6"] as String[]);
        data.put("visaOutEUKDSt", ["6"] as String[]);
        data.put("visaOutEUKBSt", ["6"] as String[]);
        data.put("visaPolskaKKO1St", ["6"] as String[]);
        data.put("visaPolskaKKO2St", ["6"] as String[]);
        data.put("visaPolskaKD1St", ["6"] as String[]);
        data.put("visaPolskaKD2St", ["6"] as String[]);
        data.put("visaPolskaKBSt", ["6"] as String[]);
        data.put("mastercardEUKKSt", ["6"] as String[]);
        data.put("mastercardEUKDSt", ["6"] as String[]);
        data.put("mastercardEUKBLSt", ["6"] as String[]);
        data.put("mastercardEUMSt", ["6"] as String[]);
        data.put("mastercardOutEUKKSt", ["6"] as String[]);
        data.put("mastercardOutEUKDSt", ["6"] as String[]);
        data.put("mastercardOutEUKBSt", ["6"] as String[]);
        data.put("mastercardOutEUMSt", ["6"] as String[]);
        data.put("mastercardPolskaKK1St", ["6"] as String[]);
        data.put("mastercardPolskaKK2St", ["6"] as String[]);
        data.put("mastercardPolskaKK3St", ["6"] as String[]);
        data.put("mastercardPolskaKD1St", ["6"] as String[]);
        data.put("mastercardPolskaKD2St", ["6"] as String[]);
        data.put("mastercardPolskaKD3St", ["6"] as String[]);
        data.put("mastercardPolskaKBSt", ["6"] as String[]);
        data.put("mastercardPolskaM1St", ["6"] as String[]);
        data.put("mastercardPolskaM2St", ["6"] as String[]);
        data.put("mastercardPolskaM3St", ["6"] as String[]);
        data.put("visaPKOBPKKO1St", ["6"] as String[]);
        data.put("visaPKOBPKKO2St", ["6"] as String[]);
        data.put("visaPKOBPKD1St", ["6"] as String[]);
        data.put("visaPKOBPKD2St", ["6"] as String[]);
        data.put("visaPKOBPKB3St", ["6"] as String[]);
        data.put("mastercardPKOBPKK1St", ["6"] as String[]);
        data.put("mastercardPKOBPKK2St", ["6"] as String[]);
        data.put("mastercardPKOBPKK3St", ["556"] as String[]);
        data.put("mastercardPKOBPKD1St", ["66"] as String[]);
        data.put("mastercardPKOBPKD2LSt", ["56"] as String[]);
        data.put("mastercardPKOBPKD3St", ["55"] as String[]);
        data.put("mastercardPKOBPKBSt", ["555"] as String[]);
        data.put("mastercardPKOBPM1St", ["55"] as String[]);
        data.put("mastercardPKOBPM2St", ["55"] as String[]);
        data.put("mastercardPKOBPM3St", ["5"] as String[]);
        data.put("dinersClubSt", ["55"] as String[]);
        data.put("ikoPr", ["66"] as String[]);
        return data
    }

    public static HashMap<String, String[]> preparePunktyData(){
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.put("punkt1", ["Kodziki"] as String[]);
        data.put("adres1", ["Wąwozowa 7"] as String[]);
        data.put("punkt2", ["Pompeczki"] as String[]);
        data.put("adres2", ["Kaszubska 2"] as String[]);
        data.put("punkt3", ["Orencz"] as String[]);
        data.put("adres3", ["Pomarańczowa 11"] as String[]);
        data.put("punkt4", ["Timobajl"] as String[]);
        data.put("adres4", ["Rózowa 1"] as String[]);
        data.put("punkt5", ["Minus GSM"] as String[]);
        data.put("adres5", ["Kowalskiego 4"] as String[]);
        return data
    }

    public static HashMap<String, String[]> preparePunktyData(int numOfPoints){
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        for (int i = 1; i <= numOfPoints; i ++){
            data.put("punkt" + i, [randomString(40)] as String[]);
            data.put("adres" + i, [randomString(40)] as String[]);
        }
        return data
    }

    public static HashMap<String, String[]> prepareScoringData(){
        HashMap<String, String[]> result = new HashMap<String, String[]>();

        result.put("scoringMcc", ["4763"] as String[]);
        result.put("phNumer", ["2333"] as String[]);
        result.put("rodzajZezwolenia", ["Od prezydenta USA - VIP"] as String[]);
        result.put("scoringSzczegolyDzialalnosci", ["Pranie pieniedzy w Perwoolu"] as String[]);
        result.put("scoringCharakterystykaInna", ["Tesco Extra"] as String[]);
        result.put("centrumHandlowe", ["true", "", "checkbox"] as String[]);
        result.put("trasaPrzelotowa", ["true", "", "checkbox"] as String[]);
        result.put("dochodowosc", ["10000"] as String[]);
        result.put("dochodowoscRd", ["4500"] as String [])
        result.put("scoringDeklaracjaFinansowa", ["d"] as String[]);
        result.put("scoringDeklaracjaFinansowaObrotOgolem", ["2 złote"] as String[]);
        result.put("scoringDeklaracjaFinansowaObrotNaKarty", ["1 tysiąc euro"] as String[]);
        result.put("scoringDeklaracjaFinansowaSredniObrot", ["50 centów"] as String[]);
        result.put("scoringDeklaracjaFinansowaSredniaTransakcja", ["1 milion złotych"] as String[]);
        result.put("scoringTypPunktuInny", ["Wieża Eiffla"] as String[]);
        result.put("handel", ["true", "", "checkbox"] as String[]);
        result.put("uslugi", ["true", "", "checkbox"] as String[]);
        result.put("wlasnosc", ["true", "", "checkbox"] as String[]);
        result.put("wynajem", ["true", "", "checkbox"] as String[]);
        result.put("powyzej5lat", ["true", "", "checkbox"] as String[]);
        result.put("od1do5lat", ["true", "", "checkbox"] as String[]);
        result.put("ponizejRoku", ["true", "", "checkbox"] as String[]);
        result.put("dzialalnoscWymagaLicencjiTak", ["true", "", "checkbox"] as String[]);
        result.put("dzialalnoscWymagaLicencjiNie", ["true", "", "checkbox"] as String[]);
        result.put("salon", ["true", "", "checkbox"] as String[]);
        result.put("sklep", ["true", "", "checkbox"] as String[]);
        result.put("stoisko", ["true", "", "checkbox"] as String[]);
        result.put("stacjaPaliw", ["true", "", "checkbox"] as String[]);
        result.put("inny", ["true", "", "checkbox"] as String[]);
        result.put("centrumMiasta", ["true", "", "checkbox"] as String[]);
        result.put("peryferiaMiasta", ["true", "", "checkbox"] as String[]);
        result.put("pawilonyHandlowe", ["true", "", "checkbox"] as String[]);
        result.put("budynekWolnoStojacy", ["true", "", "checkbox"] as String[]);
        result.put("osiedleMieszkaniowe", ["true", "", "checkbox"] as String[]);
        result.put("targowisko", ["true", "", "checkbox"] as String[]);
        result.put("inna", ["true", "", "checkbox"] as String[]);
        result.put("miastoPonad500tysChb", ["true", "", "checkbox"] as String[]);
        result.put("miastoOd100Do500tysChb", ["true", "", "checkbox"] as String[]);
        result.put("miastoOd50Do90tysChb", ["true", "", "checkbox"] as String[]);
        result.put("miastoPonizej50tysChb", ["true", "", "checkbox"] as String[]);
        result.put("wies", ["true", "", "checkbox"] as String[]);
        result.put("czynne", ["true", "", "checkbox"] as String[]);
        result.put("nieczynne", ["true", "", "checkbox"] as String[]);
        result.put("stanZadbany", ["true", "", "checkbox"] as String[]);
        result.put("wPunktachMonitoringTak", ["true", "", "checkbox"] as String[]);
        result.put("wPunktachMonitoringNie", ["true", "", "checkbox"] as String[]);
        result.put("powyzej400m2", ["true", "", "checkbox"] as String[]);
        result.put("od50do400m2", ["true", "", "checkbox"] as String[]);
        result.put("do50m2", ["true", "", "checkbox"] as String[]);

        /*
		addCheckboxes(result, ["handel":"handel", "uslugi":"uslugi"], "uslugi")
		addCheckbox(result, "stanZadbany", "true", "true")
		addCheckbox(result, "uslugiPlatneZGory", "true", "true")
		addCheckbox(result, "ruchTurystycznyPrzygraniczny", "true", "false") */

        return result
    }

    public static HashMap<String, String[]> prepareAggrementsFields(){
        HashMap<String, String[]> result = new HashMap<String, String[]>();
        result.put("informacjaHandlowaTak", ["true", "", "checkbox"] as String[]);
        result.put("informacjaHandlowaNie", ["true", "", "checkbox"] as String[]);
        return result;
    }


    public static HashMap<String, String[]> generateFormularzDanychPunktuFields() {
        HashMap<String, String[]> result = new HashMap<String, String[]>();

        result.put("sslTyp", ["Verifone Vx510G GPRS + PINPad Standard"] as String[]);
        result.put("sslIlosc", ["23"] as String[]);
        result.put("sslPPIlosc", ["45"] as String[]);
        result.put("sslCena", ["234"] as String[]);
        result.put("gprsTyp", ["Verifone Vx510G GPRS + PINPad Standard"] as String[]);
        result.put("gprsIlosc", ["23"] as String[]);
        result.put("gprsPPIlosc", ["2"] as String[]);

        result.put("phPozysk", ["12345"] as String[]);
        result.put("opiekaBiznesowa", ["23456"] as String[]);
        result.put("opiekaSerwisowaI", ["34567"] as String[]);
        result.put("opiekaSerwisowaII", ["45678"] as String[]);
        result.put("opiekaSerwisowaIII", ["00000"] as String[]);
        result.put("doladowanieTel", ["true", "", "checkbox"] as String[]);
        result.put("nipPunktu", ["1300000003"] as String[]);
        result.put("kodMCC", ["1453"] as String[]);
        result.put("rodzProwadzDzialalWPraktyce", ["naprawa smartfonow bez klapki"] as String[]);
        result.put("rozszerzenieOPunkt", ["true", "", "checkbox"] as String[]);
        result.put("wydrukUlica", ["Dolnośląska"] as String[]);
        result.put("wydrukNrDomu", ["11A"] as String[]);
        result.put("wydrukNrLokalu", ["13"] as String[]);
        result.put("wydrukMiasto", ["Atomice"] as String[]);
        result.put("wydrukKodPocztowy1", ["20"] as String[]);
        result.put("wydrukKodPocztowy2", ["333"] as String[]);
        result.put("wydrukPoczta", ["Nowa Wieś"] as String[]);
        result.put("wydrukLinia1", ["Linia na uwagi?"] as String[]);
        result.put("wydrukLinia2", ["Linia na uwagiii :)"] as String[]);
        result.put("korespondencjaUlica", ["Dolnośląska"] as String[]);
        result.put("korespondencjaNrDomu", ["11A"] as String[]);
        result.put("korespondencjaNrLokalu", ["13"] as String[]);
        result.put("korespondencjaMiasto", ["Atomice"] as String[]);
        result.put("korespondencjaKodPocztowy1", ["20"] as String[]);
        result.put("korespondencjaKodPocztowy2", ["333"] as String[]);
        result.put("korespondencjaPoczta", ["Nowa Wieś"] as String[]);
        result.put("numerRachunkuBankowego1", ["99"] as String[]);
        result.put("numerRachunkuBankowego2", ["7463"] as String[]);
        result.put("numerRachunkuBankowego3", ["0000"] as String[]);
        result.put("numerRachunkuBankowego4", ["2324"] as String[]);
        result.put("numerRachunkuBankowego5", ["9999"] as String[]);
        result.put("numerRachunkuBankowego6", ["2876"] as String[]);
        result.put("imieINazwisko", ["Jean "] as String[]);
        result.put("pan", ["true", "", "checkbox"] as String[]);
        result.put("pani", ["true", "", "checkbox"] as String[]);
        result.put("email", ["mkniec@apreel.com"] as String[]);
        result.put("kierunkowy1", ["22"] as String[]);
        result.put("stacjonarny1", ["202"] as String[]);
        result.put("stacjonarny2", ["22"] as String[]);
        result.put("stacjonarny3", ["22"] as String[]);
        result.put("kierunkowy2", ["22"] as String[]);
        result.put("nrFaksu1", ["202"] as String[]);
        result.put("nrFaksu2", ["22"] as String[]);
        result.put("nrFaksu3", ["22"] as String[]);
        result.put("komorka1", ["209"] as String[]);
        result.put("komorka2", ["122"] as String[]);
        result.put("komorka3", ["212"] as String[]);
        result.put("nazwaDoWydrukuZTerminalaPos", ["To jest jakaś nazwa trochę inna"] as String[]);
        result.put("wydrukJakWyzej", ["true", "", "checkbox"] as String[]);
        result.put("nazwaDoWyszukiwarki", ["To jest jakaś nazwa"] as String[]);

        return result;
    }

    public static HashMap<String, String[]> generateFormularzAplikacyjnyFields() {
        HashMap<String, String[]> result = new HashMap<String, String[]>();

        result.put("nrMerchanta1", ["98765"] as String[]);
        result.put("nrMerchanta2", ["12345"] as String[]);
        result.put("nrMerchanta3", ["66"] as String[]);
        result.put("nrMerchanta4", ["404"] as String[]);
        result.put("sprawaNr1", ["123"] as String[]);
        result.put("sprawaNr2", ["8906"] as String[]);
        result.put("sprawaNr3", ["0"] as String[]);
        result.put("osobaPozyskalaAkceptanta", ["Klej Zdzisiek"] as String[]);
        result.put("osobaPozyskalaAkceptantaNr", ["12345"] as String[]);
        result.put("osobaPodpisalaUmowe", ["Pucki Zyndram"] as String[]);
        result.put("osobaPodpisalaUmoweNr", ["67890"] as String[]);
        result.put("nrUmowy1", ["7890"] as String[]);
        result.put("nrUmowy2", ["1"] as String[]);
        result.put("akceptantNazwaOficjalna1", ["To jest oficjalna nazwa akceptanta"] as String[]);
        result.put("akceptantNazwaOficjalna2", ["To jest druga linia z nazwą akceptanta"] as String[]);
        result.put("akceptantNazwaSieciowa", ["This is the end"] as String[]);
        result.put("akceptantNip", ["1234567890"] as String[]);
        result.put("akceptantRegon", ["987654321"] as String[]);
        result.put("reprezentant1", ["Wiesiek Gabriel"] as String[]);
        result.put("reprezentant2", ["Waldek Złotopolski"] as String[]);
        result.put("pan1", ["true", "", "checkbox"] as String[]);
        result.put("pan2", ["true", "", "checkbox"] as String[]);
        result.put("pani1", ["true", "", "checkbox"] as String[]);
        result.put("pani2", ["true", "", "checkbox"] as String[]);
        result.put("akceptantUlica", ["Klanowa"] as String[]);
        result.put("akceptantMiasto", ["Warszawa"] as String[]);
        result.put("akceptantNrDomu", ["1"] as String[]);
        result.put("akceptantNrMieszkania", ["2"] as String[]);
        result.put("akceptantPoczta", ["Warszawa"] as String[]);
        result.put("akceptantKodPocztowy1", ["90"] as String[]);
        result.put("akceptantKodPocztowy2", ["310"] as String[]);
        result.put("akceptantKontaktUlica", ["Złotopolska"] as String[]);
        result.put("akceptantKontaktMiasto", ["Złotopolice"] as String[]);
        result.put("akceptantKontaktNrDomu", ["3"] as String[]);
        result.put("akceptantKontaktNrMieszkania", ["4"] as String[]);
        result.put("akceptantKontaktPoczta", ["Wrocław"] as String[]);
        result.put("akceptantKontaktKodPocztowy1", ["02"] as String[]);
        result.put("akceptantKontaktKodPocztowy2", ["200"] as String[]);
        result.put("panDoKontaktu", ["true", "", "checkbox"] as String[]);
        result.put("paniDoKontaktu", ["true", "", "checkbox"] as String[]);
        result.put("imieINazwiskoOsobyDoKontaktu", ["Listonosz Józef"] as String[]);
        result.put("kierunkowyStacjonarnyDoKontaktu", ["01"] as String[]);
        result.put("telStacjonarnyDoKontaktu1", ["801"] as String[]);
        result.put("telStacjonarnyDoKontaktu2", ["01"] as String[]);
        result.put("telStacjonarnyDoKontaktu3", ["07"] as String[]);
        result.put("telKomorkowyDoKontaktu1", ["401"] as String[]);
        result.put("telKomorkowyDoKontaktu2", ["301"] as String[]);
        result.put("telKomorkowyDoKontaktu3", ["901"] as String[]);
        result.put("email", ["zlotopolscy@tvp.pl"] as String[]);
        result.put("osobaFizyczna", ["true", "", "checkbox"] as String[]);
        result.put("spolkaCywilna", ["true", "", "checkbox"] as String[]);
        result.put("spolka", ["true", "", "checkbox"] as String[]);
        result.put("zaswiadczenieZEwidencji", ["true", "", "checkbox"] as String[]);
        result.put("umowaSpolkiCywilnej", ["true", "", "checkbox"] as String[]);
        result.put("odpisZKRS", ["true", "", "checkbox"] as String[]);
        result.put("inne1", ["true", "", "checkbox"] as String[]);
        result.put("inne2", ["true", "", "checkbox"] as String[]);
        result.put("spolkaText", ["zoo"] as String[]);
        result.put("inneText", ["coś"] as String[]);
        result.put("inneText2", ["Tekścior"] as String[]);
        result.put("NrSprzedazowyPH1", ["4561"] as String[]);
        result.put("NrSprzedazowyPH2", ["8"] as String[]);
        result.put("kierunkowyStacjonarny", ["04"] as String[]);
        result.put("telStacjonarny1", ["533"] as String[]);
        result.put("telStacjonarny2", ["33"] as String[]);
        result.put("telStacjonarny3", ["34"] as String[]);
        result.put("telKomorkowy1", ["999"] as String[]);
        result.put("telKomorkowy2", ["451"] as String[]);;
        result.put("telKomorkowy3", ["666"] as String[]);
        result.put("kierunkowyFaks", ["11"] as String[]);
        result.put("faks1", ["343"] as String[]);
        result.put("faks2", ["55"] as String[]);
        result.put("faks3", ["78"] as String[]);

        return result;
    }


}
