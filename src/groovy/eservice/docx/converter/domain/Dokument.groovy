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

    String dataPodpisaniaAneksu;
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

}