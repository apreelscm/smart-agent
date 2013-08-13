package eservice.docx.converter.domain

/**
 * User: user
 * Date: 18.07.13
 * Time: 09:04
 */
class Dokument {

    static Integer forCurrentAndNew = 1;
    static Integer forCurrent = 2;
    static Integer forLocalisation = 3;

    static Integer prestige = 1;
    static Integer comfort = 2;
    static Integer economic = 3;

    //TODO - moze to usunac??
    String dataPodpisaniaAneksuPOZ;
    String dataPodpisaniaAneksuPrepaid;
    String dataPodpisaniaUmowy;
    Acceptor akceptant;
    PH ph;

    // APUNTSZDCCZ1 00112-10-05_Aneks do UN_DCC zmiana warunkow_OUT
    // APUNTSZDCC2.00212-01-16_OUT
    String oplataZaPrzyjmowanieZaplatyKartamiWObcejWalucie;
    String oplataZaUruchomieniePrzyjmowaniaZaplatyKartamiWObcejWalucie;
    String oplataZaTransakcjeInternetowe;

    //APUPZ2ACB1.00013-02-15 - Aneks do Umowy o przyjm po 2013 r (wprow Cashback)_OUT
    // BRAK


//    APUPZIF2.00113-04-05
    String oplataZaKarteVISAPr
    String oplataZaKarteMasterCardPr
    String oplataZaKarteDinersClubPr
    String marzaPKOPBZaFaktureKorygujacaPr

//    APUPZDCC2.00313-02-15 - Aneks do Umowy o przyjm zapł (wprow DCC)
//    APUPZDCCZ1.00213-02-15 - Aneks do Umowy o przyjm zapł. (zm. war. DCC)
    String oplataZaTransakcjeKartaVISAPr
    String oplataZaTransakcjeKartaVISAPLN
    String oplataZaTransakcjeKartaMasterCardPr
    String oplataZaTransakcjeKartaMasterCardPLN
    String oplataZaTransakcjeKartaMaestroPr
    String oplataZaTransakcjeKartaMaestroPLN

    String karencjaOdPodpisaniaAneksu
    String karencjaOdInstalacjiPOZ

    Boolean zgodaNaElektronicznaFaktureVAT
    Boolean zgodaNaOtrzymywanieInformacjiHandlowych

    List<POMPoint> pomPoints
    List<DccPOMPoint> dccPomPoints
    List<CardPOMPoint> cardPomPoints

    AggreementTime czasUmowy

    String dataStartuSerwisuPOS
    String kosztObslugiSerwisuPOS

    Integer obslugaTransakcji

//    APUNTSZAPOO3.00212-01-16
    String dataPoczatkuUzytkowaniaPOS
    String dataKoncaUzytkowaniaPOS
    List<POSPayment> platnosciPOS

//  _APUNTSS1.00312-01-16
    String dataPoczatkuWydrukuGrafiki
    String oplataZaWydrukGrafiki

    String dataPoczatkuDzialanMatematycznych
    String oplataZaDzialaniaMatematyczne

    String dataPoczatkuTytuluPlatnosci
    String oplataZaTytulPlatnosci

    String dataPoczatkuPierwszejSesjiRozliczeniowej
    String oplataZaPierwszaSesjeRozliczeniowa

    String dataPoczatkuGwarantowanegoCzasuObslugi
    Integer opcjaDodatkowegoCzasuObslugi;
    String oplataZaEkonomicznyCzasObslugi;

//    _APUNTSZAPOU3.00212-01-16_TEMPLATE
    String dataIntegracjiZSystememKasowym
    String oplataZaIntegracjeZSystememKasowym

    String dataWeryfikacjiKoduPIN
    String oplataZaWeryfikacjeKoduPIN

    //_APUNTZ2
    //_APUNTSS1
    //_APUNTSZAPOU3
    List<POS> oplatyZaPOS
    List<POS> preferencyjneOplatyZaPOS

    Integer odroczenieOplatyPOS
    Integer okresUmowyOplatyPOS

    //_APUNTZ2
    String oplataZaDzienneZestawienieTransakcji
    String oplataZaMiesieczneZestawienieTransakcji
    String oplataZaPotwierdzenieWykonaniaPrzelewu
    String oplataZaDostarczeniePapieru
    String oplataZaInstalacjePOS
    String oplataZaInstalacjeGPRS
    String oplataZaZmianeGrafiki

    //Poziom opłat i warunki płatności karty
    String card_p_11, card_f_11;
    String card_p_12, card_f_12;
    String card_p_13, card_f_13;

    String card_p_21, card_f_21;
    String card_p_22, card_f_22;
    String card_p_23, card_f_23;

    String card_p_311, card_f_311;
    String card_p_312, card_f_312;
    String card_p_321, card_f_321;
    String card_p_322, card_f_322;

    String card_p_41, card_f_41;
    String card_p_42, card_f_42;
    String card_p_43, card_f_43;
    String card_p_44, card_f_44;

    String card_p_51, card_f_51;
    String card_p_52, card_f_52;
    String card_p_53, card_f_53;
    String card_p_54, card_f_54;

    String card_p_611, card_f_611;
    String card_p_612, card_f_612;
    String card_p_613, card_f_613;
    String card_p_621, card_f_621;
    String card_p_622, card_f_622;
    String card_p_623, card_f_623;
    String card_p_641, card_f_641;
    String card_p_642, card_f_642;
    String card_p_643, card_f_643;

    String card_p_711, card_f_711;
    String card_p_712, card_f_712;
    String card_p_721, card_f_721;
    String card_p_722, card_f_722;
    String card_p_73, card_f_73;

    String card_p_811, card_f_811;
    String card_p_812, card_f_812;
    String card_p_813, card_f_813;
    String card_p_821, card_f_821;
    String card_p_822, card_f_822;
    String card_p_823, card_f_823;
    String card_p_83, card_f_83;
    String card_p_841, card_f_841;
    String card_p_842, card_f_842;
    String card_p_843, card_f_843;

    String card_p_9, card_f_9;

    String card_p_10, card_f_10;

    Boolean telePOMPKA;
    Boolean teleKODZIK;

    String deklarowanaSprzedazDoladowan
    String deklarowanaSprzedazDoladowanSlownie

    //Poziom opłat i warunki płatności PP
    String pp_orange_tk, pp_orange_tp;
    String pp_plus_tk, pp_plus_tp;
    String pp_tmobile_tk, pp_tmobile_tp;
    String pp_heyah_tk, pp_heyah_tp;
    String pp_play_tk, pp_play_tp;
    String pp_telegrosik_tk
    String pp_virginmobile_tk
    String pp_lycamobile_tk
    String pp_gtmobile_tk
    String pp_vectonemobile_tk
    String pp_delightmobile_tk

    String oplataZaOprogramowanieDoDoladowan
}