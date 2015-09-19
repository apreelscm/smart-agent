package com.eservice.eumowy.helpers


class FieldsHelper {

    public static HashMap<String, String[]> uslugiDodatkoweFields() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.put("wydrukGrafikiCena", ["123"] as String[]);
        data.put("dzialaniaMatematyczneCena", ["123"] as String[]);
        data.put("oplataZaPlatnoscWInnejWalucie", ["123"] as String[]);
        data.put("oplataZaUruchomienieDCC", ["123"] as String[]);
        data.put("mudCena", ["123"] as String[]);
        data.put("pierwszaSesjaCena", ["123"] as String[]);
        data.put("obslugaEkonomicznyCena", ["123"] as String[]);

        return data;
    }

    public static HashMap<String, String[]> wykazTerminaliPOSFields() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.put("oplatyPOSIloscA", ["123"] as String[]);
        data.put("oplatyPOSIloscB", ["123"] as String[]);
        data.put("oplatyPOSIloscC", ["123"] as String[]);
        data.put("oplatyPOSIloscD", ["123"] as String[]);
        data.put("oplatyPOSIloscE", ["123"] as String[]);

        data.put("oplatyPOSCenaA", ["123"] as String[]);
        data.put("oplatyPOSCenaB", ["123"] as String[]);
        data.put("oplatyPOSCenaC", ["123"] as String[]);
        data.put("oplatyPOSCenaD", ["123"] as String[]);
        data.put("oplatyPOSCenaE", ["123"] as String[]);

        return data;
    }

    public static HashMap<String, String[]> wykazTerminaliPOSObjetychObnizkaNajmuFields() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();

        data.put("dataPoczatkuUzywaniaPOZ", ["2014-08-01"] as String[]);
        data.put("dataKoncaUzywaniaPOZ", ["2014-08-30"] as String[]);

        data.put("numerPOSA", ["123"] as String[]);
        data.put("numerPOSB", ["123"] as String[]);
        data.put("numerPOSC", ["123"] as String[]);
        data.put("numerPOSD", ["123"] as String[]);
        data.put("numerPOSE", ["123"] as String[]);
        data.put("numerPOSF", ["123"] as String[]);
        data.put("numerPOSG", ["123"] as String[]);
        data.put("numerPOSH", ["123"] as String[]);
        data.put("numerPOSI", ["123"] as String[]);
        data.put("numerPOSJ", ["123"] as String[]);

        data.put("oplataPOSA", ["123"] as String[]);
        data.put("oplataPOSB", ["123"] as String[]);
        data.put("oplataPOSC", ["123"] as String[]);
        data.put("oplataPOSD", ["123"] as String[]);
        data.put("oplataPOSE", ["123"] as String[]);
        data.put("oplataPOSF", ["123"] as String[]);
        data.put("oplataPOSG", ["123"] as String[]);
        data.put("oplataPOSH", ["123"] as String[]);
        data.put("oplataPOSI", ["123"] as String[]);
        data.put("oplataPOSJ", ["123"] as String[]);

        return data;
    }

    public static HashMap<String, String[]> akceptantIReprezentanciFields() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.put("akceptantNazwaOficjalna", ["Jan Kowalski nazwa oficjalna"] as String[]);
        data.put("akceptantNazwaSieciowa", ["Jan Kowalski nazwa sieciowa"] as String[]);
        data.put("siedzibaAkceptanta", ["Siedziba Akceptanta"] as String[]);

        data.put("akceptantNip", ["1234567890"] as String[]);
        data.put("akceptantRegon", ["123456789"] as String[]);

        data.put("reprezentant1", ["Grzegorz Brzęszczyszczykiewicz"] as String[]);
        data.put("reprezentant2", ["Tomek Nowak"] as String[]);
        data.put("reprezentant3", ["Andrzej Jakistam"] as String[]);
        data.put("reprezentant4", ["Zenona Aloska"] as String[]);

        data.put("reprezentant1Full", ["Grzegorz Brzęszczyszczykiewicz - Prezes zarządu"] as String[]);
        data.put("reprezentant2Full", ["Tomek Nowak - Zastępca szeryfa i prezesa"] as String[]);
        data.put("reprezentant3Full", ["Andrzej Jakistam - Szeryf wszechświata i Anglii"] as String[]);
        data.put("reprezentant4Full", ["Zenona Aloska - Konserwatorka powierchni płaskich"] as String[]);

        return data;
    }

    public static HashMap<String, String[]> umowaOznaczonaFields() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.put("umowaOznOd", ["2014-08-01"] as String[]);
        data.put("umowaOznDo", ["2014-08-30"] as String[]);

        return data;
    }

    public static HashMap<String, String[]> okresLojalnosciowyIOplataDeinstalacyjnaFields() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.put("okresLojalnosciowy", ["12"] as String[]);
        data.put("oplataDeinstalacyjna", ["123"] as String[]);

        return data;
    }

    public static HashMap<String, String[]> warunkiHandlowePompkaIKodzikFields() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.put("srednia_sprzedaz_doladowan", ["12345"] as String[]);
        data.put("srednia_sprzedaz_doladowan_slownie", ["To jest srednia sprzedaz pisana slownie"] as String[]);

        return data;
    }

    public static HashMap<String, String[]> upustPompkaIKodzikFields() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();

        data.put("pp_orange_tk", ["12"] as String[]);
        data.put("pp_orange_tp", ["123"] as String[]);

        data.put("pp_plus_tk", ["12"] as String[]);
        data.put("pp_plus_tp", ["123"] as String[]);

        data.put("pp_tmobile_tk", ["12"] as String[]);
        data.put("pp_tmobile_tp", ["123"] as String[]);

        data.put("pp_heyah_tk", ["12"] as String[]);
        data.put("pp_heyah_tp", ["123"] as String[]);

        data.put("pp_play_tk", ["12"] as String[]);
        data.put("pp_play_tp", ["123"] as String[]);

        data.put("pp_telegrosik_tk", ["12"] as String[]);
        data.put("pp_virginmobile_tk", ["123"] as String[]);
        data.put("pp_lycamobile_tk", ["123"] as String[]);
        data.put("pp_gtmobile_tk", ["123"] as String[]);
        data.put("pp_vectonemobile_tk", ["123"] as String[]);
        data.put("pp_delightmobile_tk", ["123"] as String[]);

        return data;
    }

    public static HashMap<String, String[]> listaPlacowekFields() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();

        data.put("punktNazwa1", ["Nazwa placówki"] as String[]);
        data.put("punktAdres1", ["Adres placówki"] as String[]);

        data.put("punktNazwa2", ["Nazwa placówki"] as String[]);
        data.put("punktAdres2", ["Adres placówki"] as String[]);

        data.put("punktNazwa3", ["Nazwa placówki"] as String[]);
        data.put("punktAdres3", ["Adres placówki"] as String[]);

        data.put("punktNazwa4", ["Nazwa placówki"] as String[]);
        data.put("punktAdres4", ["Adres placówki"] as String[]);

        data.put("punktNazwa5", ["Nazwa placówki"] as String[]);
        data.put("punktAdres5", ["Adres placówki"] as String[]);

        return data;
    }

    public static HashMap<String, String[]> listaPlacowekAkceptujacychFields() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();

        data.put("punktAkceptacjaKart1", ["Nazwa placówki"] as String[]);
        data.put("adresAkceptacjaKart1", ["Adres placówki"] as String[]);

        data.put("punktAkceptacjaKart2", ["Nazwa placówki"] as String[]);
        data.put("adresAkceptacjaKart2", ["Adres placówki"] as String[]);

        data.put("punktAkceptacjaKart3", ["Nazwa placówki"] as String[]);
        data.put("adresAkceptacjaKart3", ["Adres placówki"] as String[]);

        data.put("punktAkceptacjaKart4", ["Nazwa placówki"] as String[]);
        data.put("adresAkceptacjaKart4", ["Adres placówki"] as String[]);

        data.put("punktAkceptacjaKart5", ["Nazwa placówki"] as String[]);
        data.put("adresAkceptacjaKart5", ["Adres placówki"] as String[]);

        data.put("punktAkceptacjaKart6", ["Nazwa placówki"] as String[]);
        data.put("adresAkceptacjaKart6", ["Adres placówki"] as String[]);

        data.put("punktAkceptacjaKart7", ["Nazwa placówki"] as String[]);
        data.put("adresAkceptacjaKart7", ["Adres placówki"] as String[]);

        data.put("punktAkceptacjaKart8", ["Nazwa placówki"] as String[]);
        data.put("adresAkceptacjaKart8", ["Adres placówki"] as String[]);

        return data;
    }

    public static HashMap<String, String[]> specyfikacjaPoziomuOplatIWarunkowPlatnosciFields() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();

        data.put("ifOplataVISA", ["5"] as String[]);
        data.put("ifOplataMasterCard", ["5"] as String[]);
        data.put("ifOplataDinersClub", ["5"] as String[]);
        data.put("upustCashback", ["5"] as String[]);
        data.put("dccKartyZagranicznePr", ["5"] as String[]);
        data.put("ifOplataPKOPB", ["5"] as String[]);

        return data
    }

    public static HashMap<String, String[]> poziomOplatIWarunkiPlatnosciFields() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();

        data.put("visaEUKKOPr", ["5%"] as String[]);
        data.put("visaEUKKOSt", ["12zł"] as String[]);
        data.put("visaEUKDPr", ["5%"] as String[]);
        data.put("visaEUKDSt", ["12zł"] as String[]);
        data.put("visaEUKBPr", ["5%"] as String[]);
        data.put("visaEUKBSt", ["12zł"] as String[]);

        data.put("visaOutEUKKOPr", ["5%"] as String[]);
        data.put("visaOutEUKKOSt", ["12zł"] as String[]);
        data.put("visaOutEUKDPr", ["5%"] as String[]);
        data.put("visaOutEUKDSt", ["12zł"] as String[]);
        data.put("visaOutEUKBPr", ["5%"] as String[]);
        data.put("visaOutEUKBSt", ["12zł"] as String[]);

        data.put("visaPolskaKKO1Pr", ["5%"] as String[]);
        data.put("visaPolskaKKO1St", ["12zł"] as String[]);
        data.put("visaPolskaKKO2Pr", ["5%"] as String[]);
        data.put("visaPolskaKKO2St", ["12zł"] as String[]);
        data.put("visaPolskaKD1Pr", ["5%"] as String[]);
        data.put("visaPolskaKD1St", ["12zł"] as String[]);
        data.put("visaPolskaKD2Pr", ["5%"] as String[]);
        data.put("visaPolskaKD2St", ["12zł"] as String[]);
        data.put("visaPolskaKBPr", ["5%"] as String[]);
        data.put("visaPolskaKBSt", ["12zł"] as String[]);

        data.put("mastercardEUKKPr", ["5%"] as String[]);
        data.put("mastercardEUKKSt", ["12zł"] as String[]);
        data.put("mastercardEUKDPr", ["5%"] as String[]);
        data.put("mastercardEUKDSt", ["12zł"] as String[]);
        data.put("mastercardEUKBLPr", ["5%"] as String[]);
        data.put("mastercardEUKBLSt", ["12zł"] as String[]);
        data.put("mastercardEUMPr", ["5%"] as String[]);
        data.put("mastercardEUMSt", ["12zł"] as String[]);

        data.put("mastercardOutEUKKPr", ["5%"] as String[]);
        data.put("mastercardOutEUKKSt", ["12zł"] as String[]);
        data.put("mastercardOutEUKDPr", ["5%"] as String[]);
        data.put("mastercardOutEUKDSt", ["12zł"] as String[]);
        data.put("mastercardOutEUKBPr", ["5%"] as String[]);
        data.put("mastercardOutEUKBSt", ["12zł"] as String[]);
        data.put("mastercardOutEUMPr", ["5%"] as String[]);
        data.put("mastercardOutEUMSt", ["12zł"] as String[]);

        data.put("mastercardPolskaKK1Pr", ["5%"] as String[]);
        data.put("mastercardPolskaKK1St", ["12zł"] as String[]);
        data.put("mastercardPolskaKK2Pr", ["5%"] as String[]);
        data.put("mastercardPolskaKK2St", ["12zł"] as String[]);
        data.put("mastercardPolskaKK3Pr", ["5%"] as String[]);
        data.put("mastercardPolskaKK3St", ["12zł"] as String[]);
        data.put("mastercardPolskaKD1Pr", ["5%"] as String[]);
        data.put("mastercardPolskaKD1St", ["12zł"] as String[]);
        data.put("mastercardPolskaKD2Pr", ["5%"] as String[]);
        data.put("mastercardPolskaKD2St", ["12zł"] as String[]);
        data.put("mastercardPolskaKD3Pr", ["5%"] as String[]);
        data.put("mastercardPolskaKD3St", ["12zł"] as String[]);
        data.put("mastercardPolskaKBPr", ["5%"] as String[]);
        data.put("mastercardPolskaKBSt", ["12zł"] as String[]);
        data.put("mastercardPolskaM1Pr", ["5%"] as String[]);
        data.put("mastercardPolskaM1St", ["12zł"] as String[]);
        data.put("mastercardPolskaM2Pr", ["5%"] as String[]);
        data.put("mastercardPolskaM2St", ["12zł"] as String[]);
        data.put("mastercardPolskaM3Pr", ["5%"] as String[]);
        data.put("mastercardPolskaM3St", ["12zł"] as String[]);

        data.put("visaPKOBPKKO1Pr", ["5%"] as String[]);
        data.put("visaPKOBPKKO1St", ["12zł"] as String[]);
        data.put("visaPKOBPKKO2Pr", ["5%"] as String[]);
        data.put("visaPKOBPKKO2St", ["12zł"] as String[]);
        data.put("visaPKOBPKD1Pr", ["5%"] as String[]);
        data.put("visaPKOBPKD1St", ["12zł"] as String[]);
        data.put("visaPKOBPKD2Pr", ["5%"] as String[]);
        data.put("visaPKOBPKD2St", ["12zł"] as String[]);
        data.put("visaPKOBPKB3Pr", ["5%"] as String[]);
        data.put("visaPKOBPKB3St", ["12zł"] as String[]);

        data.put("mastercardPKOBPKK1Pr", ["5%"] as String[]);
        data.put("mastercardPKOBPKK1St", ["12zł"] as String[]);
        data.put("mastercardPKOBPKK2Pr", ["5%"] as String[]);
        data.put("mastercardPKOBPKK2St", ["12zł"] as String[]);
        data.put("mastercardPKOBPKK3Pr", ["5%"] as String[]);
        data.put("mastercardPKOBPKK3St", ["12zł"] as String[]);
        data.put("mastercardPKOBPKD1Pr", ["5%"] as String[]);
        data.put("mastercardPKOBPKD1St", ["12zł"] as String[]);
        data.put("mastercardPKOBPKD2LPr", ["5%"] as String[]);
        data.put("mastercardPKOBPKD2LSt", ["12zł"] as String[]);
        data.put("mastercardPKOBPKD3Pr", ["5%"] as String[]);
        data.put("mastercardPKOBPKD3St", ["12zł"] as String[]);
        data.put("mastercardPKOBPKBPr", ["5%"] as String[]);
        data.put("mastercardPKOBPKBSt", ["12zł"] as String[]);
        data.put("mastercardPKOBPM1Pr", ["5%"] as String[]);
        data.put("mastercardPKOBPM1St", ["12zł"] as String[]);
        data.put("mastercardPKOBPM2Pr", ["5%"] as String[]);
        data.put("mastercardPKOBPM2St", ["12zł"] as String[]);
        data.put("mastercardPKOBPM3Pr", ["5%"] as String[]);
        data.put("mastercardPKOBPM3St", ["12zł"] as String[]);

        data.put("dinersClubPr", ["5%"] as String[]);

        return data;
    }

    public static HashMap<String, String[]> listaPunktowPlacowekFields() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();

        data.put("punkt1", ["Nazwa placówki"] as String[]);
        data.put("adres1", ["Adres placówki"] as String[]);

        data.put("punkt2", ["Nazwa placówki"] as String[]);
        data.put("adres2", ["Adres placówki"] as String[]);

        data.put("punkt3", ["Nazwa placówki"] as String[]);
        data.put("adres3", ["Adres placówki"] as String[]);

        data.put("punkt4", ["Nazwa placówki"] as String[]);
        data.put("adres4", ["Adres placówki"] as String[]);

        data.put("punkt5", ["Nazwa placówki"] as String[]);
        data.put("adres5", ["Adres placówki"] as String[]);

        return data;
    }

    public static HashMap<String, String[]> wartoscTransackjiPlatniczych() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();

        data.put("oplatyIPlatnosciDo", ["12"] as String[]);
        data.put("oplatyIPlatnosciPowyzej", ["23"] as String[]);

        data.put("oplataPrDo", ["12"] as String[]);
        data.put("oplataPrPowyzej", ["23"] as String[]);

        return data;
    }
}
