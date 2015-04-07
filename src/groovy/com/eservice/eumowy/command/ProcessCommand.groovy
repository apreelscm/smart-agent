package com.eservice.eumowy.command

import com.eservice.eumowy.Process
import com.eservice.eumowy.annotation.DateField
import com.eservice.eumowy.annotation.Omit
import com.eservice.eumowy.enums.options.AcceptorLocation
import com.eservice.eumowy.enums.options.LegalForm
import com.eservice.eumowy.validator.*
import grails.util.Holders
import grails.validation.Validateable
import org.apache.commons.collections.FactoryUtils
import org.apache.commons.collections.ListUtils
import org.apache.commons.lang.StringUtils

@Validateable(nullable = true)
class ProcessCommand implements Serializable {

    @Omit()
    transient def calculatorService
    @Omit
    transient def calc

    @Omit
    String calcId

    @Omit()
    transient def messageSource = Holders.applicationContext.messageSource

    @Omit
    static int MAX_PRICE_GROUP_SIZE = 3

    @Omit
    static def nullableTrueBlankFalse = {/** puste ale potrzebne */}

    @Omit
    static def DEFAULT_VALUE = "~"

    //adresDoKorespondencjizAkecptantem - 
    String akceptantKontaktUlicaTytul = DEFAULT_VALUE
    String akceptantKontaktUlica = DEFAULT_VALUE
    String akceptantKontaktNrDomu = DEFAULT_VALUE
    String akceptantKontaktNrMieszkania = DEFAULT_VALUE

    String akceptantKontaktMiasto = DEFAULT_VALUE
    String akceptantKontaktKodPocztowy = DEFAULT_VALUE
    String akceptantKontaktPoczta = DEFAULT_VALUE

//    aneksDoUmowyNajmuZestawuPos - 
    @DateField
    String dataAneksowanejUmowyPos = DEFAULT_VALUE

//    aneksDoUmowyPrepaid - 
    @DateField
    String dataAneksowanejUmowyPrepaid = DEFAULT_VALUE

//    czasObowiazywaniaUmowy - 
    String umowaCzas = DEFAULT_VALUE
    @DateField
    String umowaOznOd = DEFAULT_VALUE
    @DateField
    String umowaOznDo = DEFAULT_VALUE

    //cenaPakietu
    String cenaPakietu = DEFAULT_VALUE

    //oplataDeinstalacyjna
    String oplataDeinstalacyjna = DEFAULT_VALUE

    //cashbackInfo
    String cashbackUpust = DEFAULT_VALUE
    String cashbackAbonament = DEFAULT_VALUE
    Boolean isCashbackUpustEditable

    //poziomOplatIWarunkiPlatnosci
    String oplatyIPlatnosciDo = DEFAULT_VALUE
    String oplatyIPlatnosciPowyzej = DEFAULT_VALUE
    String oplataPrDo = DEFAULT_VALUE
    String oplataPrPowyzej = DEFAULT_VALUE
    String dinersClubDo = DEFAULT_VALUE
    String dinersClubPowyzej = DEFAULT_VALUE

//    daneAkceptanta
    String akceptantNazwaOficjalna = DEFAULT_VALUE
    String akceptantNazwaSieciowa = DEFAULT_VALUE
    String akceptantRegon = DEFAULT_VALUE

    String akceptantNazwaOficjalnaCbd = DEFAULT_VALUE
    String akceptantNazwaSieciowaCbd = DEFAULT_VALUE
    String akceptantRegonCbd = DEFAULT_VALUE

//    daneDoWydruku
    String nazwaDoWydrukuZTerminalaPos = DEFAULT_VALUE
    String wydrukNazwaDoWyszukwarki = DEFAULT_VALUE

    String wydrukUlicaTytul = DEFAULT_VALUE
    String wydrukUlica = DEFAULT_VALUE
    String wydrukNrDomu = DEFAULT_VALUE
    String wydrukNrMieszkania = DEFAULT_VALUE

    String wydrukMiasto = DEFAULT_VALUE
    String wydrukKodPocztowy = DEFAULT_VALUE
    String wydrukPoczta = DEFAULT_VALUE

    String wydrukLinia1 = DEFAULT_VALUE
    String wydrukLinia2 = DEFAULT_VALUE

//    danePunktu
    boolean czyDcc

    String dccKartyZagranicznePr = DEFAULT_VALUE

//    dccZakresUruchomienia
    String dccZakresUruchomienia = DEFAULT_VALUE

//    deklaracjeAkceptanta - 
    String informacjaHandlowa = DEFAULT_VALUE

//    dodajPunkt
//    dodatkoweUslugi - 
    String oplataZaDzienneZestawienieTransakcji = DEFAULT_VALUE
    String oplataZaMiesieczneZestawienieTransakcji = DEFAULT_VALUE
    String oplataZaPotwierdzenieWykonaniaPrzelewu = DEFAULT_VALUE
    String oplataZaDostarczeniePapieru = DEFAULT_VALUE
    String oplataZaZmianeGrafiki = DEFAULT_VALUE
    String oplataZaInstalacjePOS = DEFAULT_VALUE
    String oplataZaInstalacjeGPRS = DEFAULT_VALUE
    String oplataZaUruchomienieWalutyObcej = DEFAULT_VALUE

//    dodatkoweUslugi2
    String wydrukGrafikiCena = DEFAULT_VALUE
    String dzialaniaMatematyczneCena = DEFAULT_VALUE
    String pierwszaSesjaCena = DEFAULT_VALUE

//    dodatkoweUslugiMud - 
    String mudCena = DEFAULT_VALUE

//    dodatkoweUslugiUTAIntegracja
    String weryfikacjaPINCena = DEFAULT_VALUE
    String systemKasowyCena = DEFAULT_VALUE

//    formaDoladowania - 
    Boolean doladowania_tp
    Boolean doladowania_tk
    String srednia_sprzedaz_doladowan = DEFAULT_VALUE
    String srednia_sprzedaz_doladowan_slownie = DEFAULT_VALUE

//    ifplus - 
    String ifOplataVISA = DEFAULT_VALUE
    String ifOplataMasterCard = DEFAULT_VALUE
    String ifOplataDinersClub = DEFAULT_VALUE
    String ifOplataIKO = DEFAULT_VALUE
    String ifOplataPKOPB = DEFAULT_VALUE

//    informacjeDodatkowe - 
    String dzialalnoscForma = DEFAULT_VALUE
    String dzialalnoscFormaInna = DEFAULT_VALUE
    String dzialalnoscDokument = DEFAULT_VALUE
    String dzialalnoscDokumentInny = DEFAULT_VALUE

//    okresLojalnosciowy
    String okresLojalnosciowy = DEFAULT_VALUE

//    oplatyDCC
    String oplataZaPlatnoscWInnejWalucie = DEFAULT_VALUE
//    osobaDoKontaktu
    String kontaktTytul = DEFAULT_VALUE
    String kontaktImie = DEFAULT_VALUE
    String kontaktNazwisko = DEFAULT_VALUE
    String kontaktTelStacjonarny = DEFAULT_VALUE
    String kontaktTelKomorkowy = DEFAULT_VALUE
    String kontaktEmail = DEFAULT_VALUE

    String pozyskujacyImie = DEFAULT_VALUE
    String pozyskujacyNazwisko = DEFAULT_VALUE
    String pozyskujacyNumer = DEFAULT_VALUE

//    poziomOplatiWarunkiPlatnosciKarty - 
    String visaEUKKOPr = DEFAULT_VALUE
    String visaEUKDPr = DEFAULT_VALUE
    String visaEUKBPr = DEFAULT_VALUE
    String visaOutEUKKOPr = DEFAULT_VALUE
    String visaOutEUKDPr = DEFAULT_VALUE
    String visaOutEUKBPr = DEFAULT_VALUE
    String visaPolskaKKO1Pr = DEFAULT_VALUE
    String visaPolskaKKO2Pr = DEFAULT_VALUE
    String visaPolskaKD1Pr = DEFAULT_VALUE
    String visaPolskaKD2Pr = DEFAULT_VALUE
    String visaPolskaKBPr = DEFAULT_VALUE
    String mastercardEUKKPr = DEFAULT_VALUE
    String mastercardEUKDPr = DEFAULT_VALUE
    String mastercardEUKBLPr = DEFAULT_VALUE
    String mastercardEUMPr = DEFAULT_VALUE
    String mastercardOutEUKKPr = DEFAULT_VALUE
    String mastercardOutEUKDPr = DEFAULT_VALUE
    String mastercardOutEUKBPr = DEFAULT_VALUE
    String mastercardOutEUMPr = DEFAULT_VALUE
    String mastercardPolskaKK1Pr = DEFAULT_VALUE
    String mastercardPolskaKK2Pr = DEFAULT_VALUE
    String mastercardPolskaKK3Pr = DEFAULT_VALUE
    String mastercardPolskaKD1Pr = DEFAULT_VALUE
    String mastercardPolskaKD2Pr = DEFAULT_VALUE
    String mastercardPolskaKD3Pr = DEFAULT_VALUE
    String mastercardPolskaKBPr = DEFAULT_VALUE
    String mastercardPolskaM1Pr = DEFAULT_VALUE
    String mastercardPolskaM2Pr = DEFAULT_VALUE
    String mastercardPolskaM3Pr = DEFAULT_VALUE
    String visaPKOBPKKO1Pr = DEFAULT_VALUE
    String visaPKOBPKKO2Pr = DEFAULT_VALUE
    String visaPKOBPKD1Pr = DEFAULT_VALUE
    String visaPKOBPKD2Pr = DEFAULT_VALUE
    String visaPKOBPKB3Pr = DEFAULT_VALUE
    String mastercardPKOBPKK1Pr = DEFAULT_VALUE
    String mastercardPKOBPKK2Pr = DEFAULT_VALUE
    String mastercardPKOBPKK3Pr = DEFAULT_VALUE
    String mastercardPKOBPKD1Pr = DEFAULT_VALUE
    String mastercardPKOBPKD2LPr = DEFAULT_VALUE
    String mastercardPKOBPKD3Pr = DEFAULT_VALUE
    String mastercardPKOBPKBPr = DEFAULT_VALUE
    String mastercardPKOBPM1Pr = DEFAULT_VALUE
    String mastercardPKOBPM2Pr = DEFAULT_VALUE
    String mastercardPKOBPM3Pr = DEFAULT_VALUE
    String dinersClubPr = DEFAULT_VALUE
    String ikoPr = DEFAULT_VALUE
    String visaEUKKOSt = DEFAULT_VALUE
    String visaEUKDSt = DEFAULT_VALUE
    String visaEUKBSt = DEFAULT_VALUE
    String visaOutEUKKOSt = DEFAULT_VALUE
    String visaOutEUKDSt = DEFAULT_VALUE
    String visaOutEUKBSt = DEFAULT_VALUE
    String visaPolskaKKO1St = DEFAULT_VALUE
    String visaPolskaKKO2St = DEFAULT_VALUE
    String visaPolskaKD1St = DEFAULT_VALUE
    String visaPolskaKD2St = DEFAULT_VALUE
    String visaPolskaKBSt = DEFAULT_VALUE
    String mastercardEUKKSt = DEFAULT_VALUE
    String mastercardEUKDSt = DEFAULT_VALUE
    String mastercardEUKBLSt = DEFAULT_VALUE
    String mastercardEUMSt = DEFAULT_VALUE
    String mastercardOutEUKKSt = DEFAULT_VALUE
    String mastercardOutEUKDSt = DEFAULT_VALUE
    String mastercardOutEUKBSt = DEFAULT_VALUE
    String mastercardOutEUMSt = DEFAULT_VALUE
    String mastercardPolskaKK1St = DEFAULT_VALUE
    String mastercardPolskaKK2St = DEFAULT_VALUE
    String mastercardPolskaKK3St = DEFAULT_VALUE
    String mastercardPolskaKD1St = DEFAULT_VALUE
    String mastercardPolskaKD2St = DEFAULT_VALUE
    String mastercardPolskaKD3St = DEFAULT_VALUE
    String mastercardPolskaKBSt = DEFAULT_VALUE
    String mastercardPolskaM1St = DEFAULT_VALUE
    String mastercardPolskaM2St = DEFAULT_VALUE
    String mastercardPolskaM3St = DEFAULT_VALUE
    String visaPKOBPKKO1St = DEFAULT_VALUE
    String visaPKOBPKKO2St = DEFAULT_VALUE
    String visaPKOBPKD1St = DEFAULT_VALUE
    String visaPKOBPKD2St = DEFAULT_VALUE
    String visaPKOBPKB3St = DEFAULT_VALUE
    String mastercardPKOBPKK1St = DEFAULT_VALUE
    String mastercardPKOBPKK2St = DEFAULT_VALUE
    String mastercardPKOBPKK3St = DEFAULT_VALUE
    String mastercardPKOBPKD1St = DEFAULT_VALUE
    String mastercardPKOBPKD2LSt = DEFAULT_VALUE
    String mastercardPKOBPKD3St = DEFAULT_VALUE
    String mastercardPKOBPKBSt = DEFAULT_VALUE
    String mastercardPKOBPM1St = DEFAULT_VALUE
    String mastercardPKOBPM2St = DEFAULT_VALUE
    String mastercardPKOBPM3St = DEFAULT_VALUE
    String dinersClubSt = DEFAULT_VALUE
    String ikoSt = DEFAULT_VALUE

//    poziomOplatIWarunkiPlatnosciPP - 
    String pp_orange_tk = DEFAULT_VALUE
    String pp_orange_tp = DEFAULT_VALUE
    String pp_plus_tk = DEFAULT_VALUE
    String pp_plus_tp = DEFAULT_VALUE
    String pp_tmobile_tk = DEFAULT_VALUE
    String pp_tmobile_tp = DEFAULT_VALUE
    String pp_heyah_tk = DEFAULT_VALUE
    String pp_heyah_tp = DEFAULT_VALUE
    String pp_play_tk = DEFAULT_VALUE
    String pp_play_tp = DEFAULT_VALUE
    String pp_telegrosik_tk = DEFAULT_VALUE
    String pp_virginmobile_tk = DEFAULT_VALUE
    String pp_lycamobile_tk = DEFAULT_VALUE
    String pp_gtmobile_tk = DEFAULT_VALUE
    String pp_vectonemobile_tk = DEFAULT_VALUE
    String pp_delightmobile_tk = DEFAULT_VALUE
    String oplataZaOprogramowanieDoDoladowan = DEFAULT_VALUE

//    promocyjneObnizenieOplatyZaZestawPos
//    scoring

    String scoringMcc = DEFAULT_VALUE
    String scoringDzialalnosc = DEFAULT_VALUE
    String scoringSzczegolyDzialalnosci = DEFAULT_VALUE
    String scoringWlasnosc = DEFAULT_VALUE
    String scoringDzialalnoscCzas = DEFAULT_VALUE
    String scoringKoncesja = DEFAULT_VALUE
    String rodzajZezwolenia = DEFAULT_VALUE
    String scoringCharakterystyka = DEFAULT_VALUE
    String scoringCharakterystykaInna = DEFAULT_VALUE
    String scoringWielkoscPunktu = DEFAULT_VALUE
    String scoringAkceptacja = DEFAULT_VALUE
    String scoringMonitoring = DEFAULT_VALUE
    String scoringLokalizacjaPunktu = DEFAULT_VALUE
    String scoringTypPunktu = DEFAULT_VALUE
    String scoringTypPunktuInny = DEFAULT_VALUE
    String scoringWielkoscMiejscowosci = DEFAULT_VALUE
    String scoringOtwartyZamkniety = DEFAULT_VALUE

    Boolean scoringStanZadbany
    Boolean scoringSprzedazTowarowEkskluzywnych
    Boolean scoringPonad50ProcentObrotowWNocy
    Boolean scoringRuchTurystycznyPrzygraniczny
    Boolean scoringUslugiPlatneZGory

    String scoringCzestoscTransakcji = DEFAULT_VALUE
    String scoringIloscTransakcji = DEFAULT_VALUE
    String scoringDochodowosc = DEFAULT_VALUE
    String scoringDeklaracjaFinansowa = DEFAULT_VALUE
    String scoringDeklaracjaFinansowaObrotOgolem = DEFAULT_VALUE
    String scoringDeklaracjaFinansowaObrotNaKarty = DEFAULT_VALUE
    String scoringDeklaracjaFinansowaSredniObrot = DEFAULT_VALUE
    String scoringDeklaracjaFinansowaSredniaTransakcja = DEFAULT_VALUE

    //used only for calculation
    String progrnozaMiesieczna = DEFAULT_VALUE
    String liczbaPtkCbd = DEFAULT_VALUE

//    siedzibaAkceptanta - 
    String akceptantUlicaTytul = DEFAULT_VALUE
    String akceptantUlica = DEFAULT_VALUE
    String akceptantNrDomu = DEFAULT_VALUE
    String akceptantNrMieszkania = DEFAULT_VALUE

    String akceptantMiasto = DEFAULT_VALUE
    String akceptantKodPocztowy = DEFAULT_VALUE
    String akceptantPoczta = DEFAULT_VALUE

    String akceptantTelStacjonarny = DEFAULT_VALUE
    String akceptantFax = DEFAULT_VALUE
    String akceptantTelKomorkowy = DEFAULT_VALUE

    String akceptantUlicaTytulCbd = DEFAULT_VALUE
    String akceptantUlicaCbd = DEFAULT_VALUE
    String akceptantNrDomuCbd = DEFAULT_VALUE
    String akceptantNrMieszkaniaCbd = DEFAULT_VALUE

    String akceptantMiastoCbd = DEFAULT_VALUE
    String akceptantKodPocztowyCbd = DEFAULT_VALUE
    String akceptantPocztaCbd = DEFAULT_VALUE

    String akceptantTelStacjonarnyCbd = DEFAULT_VALUE
    String akceptantFaxCbd = DEFAULT_VALUE
    String akceptantTelKomorkowyCbd = DEFAULT_VALUE

//    umowa2 - 
    String miejsceUmowy //nie jest uzywane w pdf

//    zestawPosOdplatneUzywanie
    String isOdplatneUzywanieShown = DEFAULT_VALUE

    @Omit(inPopulate = true)
    String odpUzyTermCalc = DEFAULT_VALUE

    String odplatneUzywanie = DEFAULT_VALUE

    String odpUzyTermSzt = DEFAULT_VALUE
    String odpUzyPpSzt = DEFAULT_VALUE
    String odpUzyTermMies = DEFAULT_VALUE
    String odpUzyPpMies = DEFAULT_VALUE

//    serwis - 
    Boolean serwisZablokowany
    String obslugaTyp = DEFAULT_VALUE
    String obslugaEkonomicznyCena = DEFAULT_VALUE

//    rachunekBankowyKlienta
    String numerRachunkuBankowegoKlienta = DEFAULT_VALUE
    String bankKlienta = DEFAULT_VALUE

//    oplataDCCZaUruchomienie
    String oplataZaUruchomienieDCC = DEFAULT_VALUE

    //osobaUprawnionaDoPodpisaniaUmowy
    Boolean isFromBisnode = false
    Boolean isRepresentativesChangedManually = false
    String emailDoWysylkiDokumentu = DEFAULT_VALUE

    //beneficjenciRzeczywisci
    Boolean czyBeneficjentRzeczywisty
    Boolean akceptantJestSpolka
    String nazwaGieldy
    String isinAkceptanta
    Boolean akceptantJestPodmiotem
    Boolean akceptantJestOrganem
    Boolean akceptantNieMaBeneficjenta

    //dokumentyWeryfikacyjne
    Boolean beneficjentWeryfikacjaKRS
    String beneficjentKRS
    Boolean beneficjentWeryfikacjaDokumentTozsamosci
    Boolean beneficjentWeryfikacjaGielda
    Boolean beneficjentWeryfikacjaSpolka
    Boolean beneficjentWeryfikacjaKsiega
    Boolean beneficjentWeryfikacjaSchemat

//    liczbaMiesiecyZwolnieniaZNajm

    String nip = DEFAULT_VALUE

    String minCenaNajmu

    //uzgodnienie dyspozycji
    String dyspozycja

//    uwagi
    @Omit
    String notes = DEFAULT_VALUE

    @Omit
    transient Process process

    @Omit(inSave = true, inPopulate = true)
    List<PointCommand> points = ListUtils.lazyList([], FactoryUtils.instantiateFactory(PointCommand))
    @Omit(inSave = true, inPopulate = true)
    List<PointCommand> poses = ListUtils.lazyList([], FactoryUtils.instantiateFactory(PointCommand))
    @Omit(inSave = true, inPopulate = true)
    List<AllPointsCommand> allPoints = ListUtils.lazyList([], FactoryUtils.instantiateFactory(AllPointsCommand))
    @Omit(inSave = true, inPopulate = true)
    List<AllPosCommand> allPoses = ListUtils.lazyList([], FactoryUtils.instantiateFactory(AllPosCommand))
    @Omit(inSave = true, inPopulate = true)
    List<HirePaymentCommand> hirePaymentsByPoint = ListUtils.lazyList([], FactoryUtils.instantiateFactory(HirePaymentCommand))
    @Omit(inSave = true, inPopulate = true)
    List<HirePaymentCommand> hirePaymentsByPos = ListUtils.lazyList([], FactoryUtils.instantiateFactory(HirePaymentCommand))
    @Omit(inSave = true, inPopulate = true)
    List<HirePaymentCommand> hirePaymentsCurrent = ListUtils.lazyList([], FactoryUtils.instantiateFactory(HirePaymentCommand))
    @Omit(inSave = true, inPopulate = true)
    List<PosExchangeCommand> posExchanges = ListUtils.lazyList([], FactoryUtils.instantiateFactory(PosExchangeCommand))
    @Omit(inPopulate = true)
    List<RepresentativeCommand> representatives = ListUtils.lazyList([], FactoryUtils.instantiateFactory(RepresentativeCommand))
    @Omit(inPopulate = true)
    List<BeneficiaryCommand> beneficiaries = ListUtils.lazyList([], FactoryUtils.instantiateFactory(BeneficiaryCommand))

    @Omit
    transient Integer pointsAndPosesWithoutFormaDoladowania //eUmowy_ext-557

    @Omit
    String hasObslugaTyp
    @Omit
    String hasUmowaCzas

    @Omit
    String hasScoringDzialalnosc
    @Omit
    String hasScoringWlasnosc
    @Omit
    String hasScoringDzialalnoscCzas
    @Omit
    String hasScoringKoncesja
    @Omit
    String hasScoringCharakterystyka
    @Omit
    String hasScoringWielkoscPunktu
    @Omit
    String hasScoringAkceptacja
    @Omit
    String hasScoringMonitoring
    @Omit
    String hasScoringLokalizacjaPunktu
    @Omit
    String hasScoringTypPunktu
    @Omit
    String hasScoringWielkoscMiejscowosci
    @Omit
    String hasScoringOtwartyZamkniety
    @Omit
    String hasScoringCzestoscTransakcji
    @Omit
    String hasScoringIloscTransakcji
    @Omit
    String hasScoringDeklaracjaFinansowa

    @Omit
    String hasKontaktTel
    @Omit
    String hasDoladowania
    @Omit
    String hasAkceptantTel
    @Omit
    String hasInformacjaHandlowa
    @Omit
    String hasDccZakresUruchomienia
    @Omit
    Boolean hasAtLeastOneDoladowanie
    @Omit
    Boolean czyGift
    @Omit
    Boolean isRozszerzenie
    @Omit
    Boolean hasNewUmowaAndPrepaid
    @Omit
    Boolean hasNewUmowa
    @Omit
    boolean isBundleActivity

    @Omit
    String liczbaTerminali

    @Omit
    String liczbaPosZCbd

    String promObjNaj1
    String promObjNajLiczbaTerminali

    @Omit(inPopulate = true)
    Boolean isDoladowania_tp

    @Omit(inPopulate = true)
    Boolean isDoladowania_tk

    @Omit
    Boolean hasPrepaid

    @Omit
    Boolean hasDodaniePrepaid

    Boolean korespondencjaJakDlaMerchanta

    @Omit
    def defaultPointData
    @Omit
    def defaultPosData

    @Omit
    static constraints = {
        oplataZaDzienneZestawienieTransakcji(nullable: false, blank: false,  validator: { value, cmd, errors -> NumberValidator.validate(value, cmd, errors, propertyName)})
        oplataZaMiesieczneZestawienieTransakcji(nullable: false, blank: false,  validator: { value, cmd, errors -> NumberValidator.validate(value, cmd, errors, propertyName)})
        oplataZaPotwierdzenieWykonaniaPrzelewu(nullable: false, blank: false,  validator: { value, cmd, errors -> NumberValidator.validate(value, cmd, errors, propertyName)})
        oplataZaDostarczeniePapieru(nullable: false, blank: false,  validator: { value, cmd, errors -> NumberValidator.validate(value, cmd, errors, propertyName)})
        oplataZaZmianeGrafiki(nullable: false, blank: false,  validator: { value, cmd, errors -> NumberValidator.validate(value, cmd, errors, propertyName)})
        oplataZaInstalacjePOS(nullable: false, blank: false,  validator: { value, cmd, errors -> NumberValidator.validate(value, cmd, errors, propertyName)})
        oplataZaInstalacjeGPRS(nullable: false, blank: false,  validator: { value, cmd, errors -> NumberValidator.validate(value, cmd, errors, propertyName)})
        oplataZaUruchomienieWalutyObcej(nullable: false, blank: false,  validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) && ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "DCC_OPLATA_URUCHOMIENIE")
        })

        wydrukGrafikiCena(nullable:true, blank:false,  validator: { value, cmd, errors -> NumberValidator.validate(value, cmd, errors, propertyName)})
        dzialaniaMatematyczneCena(nullable:true, blank:false,  validator: { value, cmd, errors -> NumberValidator.validate(value, cmd, errors, propertyName)})

        pierwszaSesjaCena(nullable: true, blank: true,  validator: { value, cmd, errors -> NumberValidator.validate(value, cmd, errors, propertyName)})

        akceptantKontaktUlicaTytul(nullable: true, blank: true)
        akceptantKontaktUlica(nullable: false, blank: false, shared: "alphanumeric", validator: { value, cmd, errors ->
            MaxLengthValidator.validate(value, cmd, errors, 40, propertyName)
        })
        akceptantKontaktNrDomu(nullable: false, blank: false, shared: "alphanumeric")
        akceptantKontaktNrMieszkania(nullable: true, blank: false, shared: "alphanumeric")
        akceptantKontaktMiasto(nullable: false, blank: false, shared: "alphanumericWithBrackets", validator: { value, cmd, errors ->
            MaxLengthValidator.validate(value, cmd, errors, 33, propertyName)
        })
        akceptantKontaktKodPocztowy(nullable: false, blank: false, shared: "postalCodeValidator")
        akceptantKontaktPoczta(nullable: true, blank: true, shared: "alphanumeric", validator: { value, cmd, errors ->
            MaxLengthValidator.validate(value, cmd, errors, 33, propertyName)
        })
        dataAneksowanejUmowyPos(nullable: false, blank: false, shared: "date")
        dataAneksowanejUmowyPrepaid(nullable: false, blank: false, shared: "date")

        hasUmowaCzas(nullable: true, validator: { value, cmd, errors ->
            if (value && cmd.umowaCzas == DEFAULT_VALUE) {
                errors.rejectValue(propertyName, "default.atLeastOne.czasUmowy")
                return false
            }
            return true
        })

        //TODO REFACTORING - wrzucic to do jednego 'hasScoring' i walidowac wszystkie pola scoringowe od razu
        hasScoringDzialalnosc(nullable: true, validator: { value, cmd, errors ->
            ScoringValidator.validate(value, cmd, errors, propertyName, "scoringDzialalnosc", cmd.scoringDzialalnosc)
        })

        hasScoringWlasnosc(nullable: true, validator: { value, cmd, errors ->
            ScoringValidator.validate(value, cmd, errors, propertyName, "scoringWlasnosc", cmd.scoringWlasnosc)
        })

        hasScoringDzialalnoscCzas(nullable: true, validator: { value, cmd, errors ->
            ScoringValidator.validate(value, cmd, errors, propertyName, "scoringDzialalnoscCzas", cmd.scoringDzialalnoscCzas)
        })

        hasScoringKoncesja(nullable: true, validator: { value, cmd, errors ->
            ScoringValidator.validate(value, cmd, errors, propertyName, "scoringKoncesja", cmd.scoringKoncesja)
        })

        hasScoringCharakterystyka(nullable: true, validator: { value, cmd, errors ->
            ScoringValidator.validate(value, cmd, errors, propertyName, "scoringCharakterystyka", cmd.scoringCharakterystyka)
        })

        hasScoringWielkoscPunktu(nullable: true, validator: { value, cmd, errors ->
            ScoringValidator.validate(value, cmd, errors, propertyName, "scoringWielkoscPunktu", cmd.scoringWielkoscPunktu)
        })

        hasScoringAkceptacja(nullable: true, validator: { value, cmd, errors ->
            ScoringValidator.validate(value, cmd, errors, propertyName, "scoringAkceptacja", cmd.scoringAkceptacja)
        })

        hasScoringMonitoring(nullable: true, validator: { value, cmd, errors ->
            ScoringValidator.validate(value, cmd, errors, propertyName, "scoringMonitoring", cmd.scoringMonitoring)
        })

        hasScoringLokalizacjaPunktu(nullable: true, validator: { value, cmd, errors ->
            ScoringValidator.validate(value, cmd, errors, propertyName, "scoringLokalizacjaPunktu", cmd.scoringLokalizacjaPunktu)
        })

        hasScoringTypPunktu(nullable: true, validator: { value, cmd, errors ->
            ScoringValidator.validate(value, cmd, errors, propertyName, "scoringTypPunktu", cmd.scoringTypPunktu)
        })

        hasScoringWielkoscMiejscowosci(nullable: true, validator: { value, cmd, errors ->
            ScoringValidator.validate(value, cmd, errors, propertyName, "scoringWielkoscMiejscowosci", cmd.scoringWielkoscMiejscowosci)
        })

        hasScoringOtwartyZamkniety(nullable: true, validator: { value, cmd, errors ->
            ScoringValidator.validate(value, cmd, errors, propertyName, "scoringOtwartyZamkniety", cmd.scoringOtwartyZamkniety)
        })

        hasScoringCzestoscTransakcji(nullable: true, validator: { value, cmd, errors ->
            ScoringValidator.validate(value, cmd, errors, propertyName, "scoringCzestoscTransakcji", cmd.scoringCzestoscTransakcji)
        })

        hasScoringIloscTransakcji(nullable: true, validator: { value, cmd, errors ->
            ScoringValidator.validate(value, cmd, errors, propertyName, "scoringIloscTransakcji", cmd.scoringIloscTransakcji)
        })

        hasScoringDeklaracjaFinansowa(nullable: true, validator: { value, cmd, errors ->
            ScoringValidator.validate(value, cmd, errors, propertyName, "scoringDeklaracjaFinansowa", cmd.scoringDeklaracjaFinansowa)
        })

//END REFACTORING

        umowaCzas(nullable: false, blank: false)

        umowaOznOd(nullable: true, blank: true, shared: "date", validator: { value, cmd, errors ->
            if (value == null && cmd.umowaCzas == "oznaczony") {
                errors.rejectValue(propertyName, "default.validation.required.error", "Pole wymagane")
                return false
            }
            return true
        })

        umowaOznDo(nullable: true, blank: true, shared: "date", validator: { value, cmd, errors ->
            if (value == null && cmd.umowaCzas == "oznaczony") {
                errors.rejectValue(propertyName, "default.validation.required.error", "Pole wymagane")
                return false
            }
            return true
        })

        akceptantNazwaOficjalna(blank: false, validator: { value, cmd, errors ->
            TextValidator.isAlphanumeric(value, cmd, errors, propertyName)
        })
        akceptantNazwaSieciowa(nullable: true, blank: true, validator: { value, cmd, errors ->
            if(StringUtils.isEmpty(value)) return true

            return TextValidator.isAlphanumeric(value, cmd, errors, propertyName)
        })
        akceptantRegon(nullable: false, blank: false, matches: "~|[0-9]{9}")
        akceptantNazwaOficjalnaCbd(nullable: true)
        akceptantNazwaSieciowaCbd(nullable: true)
        akceptantRegonCbd(nullable: true)
        nazwaDoWydrukuZTerminalaPos(nullable: true, maxSize: 25)
        wydrukNazwaDoWyszukwarki(nullable: true)
        wydrukUlicaTytul(nullable: true, blank: true)
        wydrukUlica(nullable: false, blank: false, validator: { value, cmd, errors ->
            MaxLengthValidator.validate(value, cmd, errors, 40, propertyName)
        })
        wydrukNrDomu(nullable: false, blank: false, shared: "alphanumeric")
        wydrukNrMieszkania(nullable: true, blank: false, shared: "alphanumeric")
        wydrukMiasto(nullable: false, blank: false, shared: "alphanumericWithBrackets", validator: { value, cmd, errors ->
            MaxLengthValidator.validate(value, cmd, errors, 33, propertyName)
        })
        wydrukKodPocztowy(nullable: false, blank: false, shared: "postalCodeValidator")
        wydrukPoczta(nullable: true, blank: true, shared: "alphanumeric", validator: { value, cmd, errors ->
            MaxLengthValidator.validate(value, cmd, errors, 33, propertyName)
        })
        wydrukLinia1(nullable: true, blank: true)
        wydrukLinia2(nullable: true, blank: true)
        dccKartyZagranicznePr(nullable: false, blank: false,  validator: { value, cmd, errors -> NumberValidator.validate(value, cmd, errors, propertyName)})

        dccZakresUruchomienia(nullable: false, blank: false)
        hasDccZakresUruchomienia(nullable: true, validator: { value, cmd, errors ->
            if(!value){
                return true
            }
            if (cmd.dccZakresUruchomienia == DEFAULT_VALUE) {
                errors.rejectValue("dccZakresUruchomienia", "default.atLeastOne.dccZakresUruchomienia")
                return false
            }
        })
        dccZakresUruchomienia(nullable: false, blank: false)
        odplatneUzywanie(nullable: true, blank: false)

        odpUzyTermSzt(nullable: true, blank: false,  validator: { value, cmd, errors -> NumberValidator.validate(value, cmd, errors, propertyName)})
        odpUzyPpSzt(nullable: true, blank: false,  validator: { value, cmd, errors -> NumberValidator.validate(value, cmd, errors, propertyName)})
        isOdplatneUzywanieShown(nullable: true, blank: true, validator: { value, cmd, errors ->
            if ("tak".equals(value)){
                if ("one_for_all_terminals".equals(cmd.odplatneUzywanie)){
                    ConditionValidator.atLeastCalcValue(cmd.odpUzyTermMies, cmd, errors, "odpUzyTermMies", "CENA_NAJMU")
                } else if ("one_for_all_terminals_in_point".equals(cmd.odplatneUzywanie)){
                    HirePaymentValidator.validate(cmd.hirePaymentsByPoint, cmd, errors)
                } else if ("other_for_selected_terminals".equals(cmd.odplatneUzywanie)){
                    HirePaymentValidator.validate(cmd.hirePaymentsByPos, cmd, errors)
                }
            }
            return true
        })

        odpUzyPpMies(nullable: true, blank: false,  validator: { value, cmd, errors -> NumberValidator.validate(value, cmd, errors, propertyName)})

        informacjaHandlowa(nullable: false, blank: false)

        hasInformacjaHandlowa(nullable: true, validator: { value, cmd, errors ->
            if(!value){
                return true
            }

            if (cmd.informacjaHandlowa == DEFAULT_VALUE) {
                errors.rejectValue(propertyName, "default.atLeastOne.informacjaHandlowa")
                return false
            }

            return true
        })

        mudCena(nullable: true, blank: true,  validator: { value, cmd, errors -> NumberValidator.validate(value, cmd, errors, propertyName)})
        weryfikacjaPINCena(nullable: true,  validator: { value, cmd, errors -> NumberValidator.validate(value, cmd, errors, propertyName)})
        systemKasowyCena(nullable: true,  validator: { value, cmd, errors -> NumberValidator.validate(value, cmd, errors, propertyName)})

        doladowania_tk(nullable: true)
        doladowania_tp(nullable: true)
        hasDoladowania(nullable: true, validator: { value, cmd, errors ->
            if(value && (cmd.isDoladowania_tp || cmd.isDoladowania_tk)){
                if(!(cmd.doladowania_tk || cmd.doladowania_tp) && cmd.hasDodaniePrepaid){
                    errors.rejectValue(propertyName, "default.atLeastOne.doladowania")
                    return false
                }
            }
            return true
        })

        srednia_sprzedaz_doladowan(nullable:false, blank:false,  validator: { value, cmd, errors -> NumberValidator.validate(value, cmd, errors, propertyName)})
        srednia_sprzedaz_doladowan_slownie(nullable:false, blank:false, shared: "lettersOnly")
        ifOplataVISA(nullable:false, blank:false, shared: "number3Precision") //1.111 %, M
        ifOplataMasterCard(nullable:false, blank:false, shared: "number3Precision") //1.111 %, M
        ifOplataDinersClub(nullable:false, blank:false, shared: "number3Precision") //1.111 %, M
        ifOplataIKO(nullable:false, blank:false, shared: "number3Precision") //1.111 %, M
        ifOplataPKOPB(nullable:false, blank:false, shared: "number3Precision") //1.111 %, M
        dzialalnoscForma(nullable:true, blank:true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.hasNewUmowa && !cmd.dzialalnoscFormaInna && !errors.hasFieldErrors("dzialalnoscForma")
                    , "dzialalnoscForma", "dzialanoscForma.required")
        })
        dzialalnoscFormaInna(nullable:true, blank:true, shared: "alphanumeric", validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.hasNewUmowa && !cmd.dzialalnoscForma && !errors.hasFieldErrors("dzialalnoscForma"),
                    "dzialalnoscForma", "dzialanoscForma.required")
        })
        dzialalnoscDokument(nullable:true, blank:true)
        dzialalnoscDokumentInny(nullable:true, blank:true, shared: "alphanumeric")

        okresLojalnosciowy(nullable:true, blank:true, validator: {value, cmd, errors ->
            return ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "LICZBA_MIESIECY_LOJ")
        })
        oplataZaPlatnoscWInnejWalucie(nullable: false, blank: false)
        kontaktTytul(nullable: false, blank: false)
        kontaktImie(nullable: false, blank: false, shared: "lettersOnly")
        kontaktNazwisko(nullable: false, blank: false, shared: "lettersOnly")

        cashbackUpust(nullable: true, blank: true, validator: { value, cmd, errors ->
            if(!cmd.isCashbackUpustEditable) return true

            return ConditionValidator.atMostCalcValue(value, cmd, errors, propertyName, "CASHBACK_D")
        })

        oplataDeinstalacyjna(nullable: true, blank: true, validator: { value, cmd, errors ->
            return ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_DEINST_WARTOSC")
        })

        hasKontaktTel(nullable: true, validator: { value, process, errors ->
            if (value == null) {
                return true
            }

            if (!process.kontaktTelKomorkowy && !process.kontaktTelStacjonarny) {
                errors.rejectValue(propertyName, "default.atLeastOne.phoneNumber")
                return false
            }
            return true
        })

        kontaktTelKomorkowy(nullable: true)
        kontaktTelStacjonarny(nullable: true)
        kontaktEmail(nullable: true, blank: true, shared: "email")
        pozyskujacyImie(nullable: false, blank: false, shared: "lettersOnly", maxSize: 40)
        pozyskujacyNazwisko(nullable: false, blank: false, shared: "lettersOnly", maxSize: 100)
        pozyskujacyNumer(nullable: false, blank: false, maxSize: 12)
        emailDoWysylkiDokumentu(nullable: true, blank: true, shared: "email")

        visaEUKKOSt(nullable: false, blank: false,  validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
                    ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_53_ZL")
        })

        visaEUKDSt(nullable: false, blank: false,  validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
                    ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_12_ZL")
        })

        visaEUKBSt(nullable: false, blank: false,  validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
                    ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_13_ZL")
        })

        visaOutEUKKOSt(nullable: false, blank: false,  validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
                    ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_21_ZL")
        })

        visaOutEUKDSt(nullable: false, blank: false,  validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
                    ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_22_ZL")
        })

        visaOutEUKBSt(nullable: false, blank: false,  validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
                    ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_23_ZL")
        })

        visaPolskaKBSt(nullable: false, blank: false, validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName)
        })

        mastercardEUKKSt(nullable: false, blank: false,  validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
                    ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_41_ZL")
        })

        mastercardEUKDSt(nullable: false, blank: false,  validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
                    ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_42_ZL")
        })
        mastercardEUKBLSt(nullable: false, blank: false,  validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
                    ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_43_ZL")
        })
        mastercardEUMSt(nullable: false, blank: false,  validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
                    ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_44_ZL")
        })
        mastercardOutEUKKSt(nullable: false, blank: false,  validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
                    ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_51_ZL")
        })
        mastercardOutEUKDSt(nullable: false, blank: false,  validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
                    ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_52_ZL")
        })
        mastercardOutEUKBSt(nullable: false, blank: false,  validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
                    ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_53_ZL")
        })
        mastercardOutEUMSt(nullable: false, blank: false,  validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
                    ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_54_ZL")
        })

        dinersClubSt(nullable: true, blank: true,  validator: { value, cmd, errors -> NumberValidator.validate(value, cmd, errors, propertyName)
        })

        ikoSt(nullable: true, blank: true,  validator: { value, cmd, errors -> NumberValidator.validate(value, cmd, errors, propertyName)})
        visaEUKKOPr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_11_PROCENT")})
        visaEUKDPr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_12_PROCENT")})
        visaEUKBPr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_13_PROCENT")})
        visaOutEUKKOPr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_21_PROCENT")})
        visaOutEUKDPr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_22_PROCENT")})
        visaOutEUKBPr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_23_PROCENT")})
        visaPolskaKKO1Pr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_311_PROCENT")})
        visaPolskaKKO2Pr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_312_PROCENT")})
        visaPolskaKD1Pr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_321_PROCENT")})
        visaPolskaKD2Pr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_322_PROCENT")})
        visaPolskaKBPr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_33_PROCENT")})
        mastercardEUKKPr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_41_PROCENT")})
        mastercardEUKDPr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_42_PROCENT")})
        mastercardEUKBLPr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_43_PROCENT")})
        mastercardEUMPr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_44_PROCENT")})
        mastercardOutEUKKPr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_51_PROCENT")})
        mastercardOutEUKDPr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_52_PROCENT")})
        mastercardOutEUKBPr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_53_PROCENT")})
        mastercardOutEUMPr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_54_PROCENT")})
        mastercardPolskaKK1Pr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_611_PROCENT")})
        mastercardPolskaKK2Pr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_612_PROCENT")})
        mastercardPolskaKK3Pr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_613_PROCENT")})
        mastercardPolskaKD1Pr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_621_PROCENT")})
        mastercardPolskaKD2Pr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_622_PROCENT")})
        mastercardPolskaKD3Pr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_623_PROCENT")})
        mastercardPolskaKBPr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_63_PROCENT")})
        mastercardPolskaM1Pr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_641_PROCENT")})
        mastercardPolskaM2Pr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_642_PROCENT")})
        mastercardPolskaM3Pr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_643_PROCENT")})
        visaPKOBPKKO1Pr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_711_PROCENT")})
        visaPKOBPKKO2Pr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_712_PROCENT")})
        visaPKOBPKD1Pr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_721_PROCENT")})
        visaPKOBPKD2Pr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_722_PROCENT")})
        visaPKOBPKB3Pr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_73_PROCENT")})
        mastercardPKOBPKK1Pr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_811_PROCENT")})
        mastercardPKOBPKK2Pr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_812_PROCENT")})
        mastercardPKOBPKK3Pr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_813_PROCENT")})
        mastercardPKOBPKD1Pr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_821_PROCENT")})
        mastercardPKOBPKD2LPr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_822_PROCENT")})
        mastercardPKOBPKD3Pr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_823_PROCENT")})
        mastercardPKOBPKBPr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_83_PROCENT")})
        mastercardPKOBPM1Pr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_841_PROCENT")})
        mastercardPKOBPM2Pr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_842_PROCENT")})
        mastercardPKOBPM3Pr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_843_PROCENT")})
        dinersClubPr(nullable: false, blank: false, shared: "number3Precision", validator: {value, cmd, errors -> ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_9_PROCENT")})
        ikoPr(nullable: true, blank: true, shared: "number3Precision")


        visaPolskaKKO1St(nullable: false, blank: false,  validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
            ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_311_ZL")
        })
        visaPolskaKKO2St(nullable: false, blank: false, validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
            ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_312_ZL")
        })
        visaPolskaKD1St(nullable: false, blank: false, validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
            ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_321_ZL")
        })
        visaPolskaKD2St(nullable: false, blank: false, validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
            ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_322_ZL")
        })
        mastercardPolskaKK1St(nullable: false, blank: false, validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
            ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_611_ZL")
        })
        mastercardPolskaKK2St(nullable: false, blank: false, validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
            ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_612_ZL")
        })
        mastercardPolskaKK3St(nullable: false, blank: false, validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
            ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_613_ZL")
        })
        mastercardPolskaKD1St(nullable: false, blank: false, validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
            ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_621_ZL")
        })
        mastercardPolskaKD2St(nullable: false, blank: false, validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
            ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_622_ZL")
        })
        mastercardPolskaKD3St(nullable: false, blank: false, validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
            ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_623_ZL")
        })
        mastercardPolskaKBSt(nullable: false, blank: false, validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName)
        })
        mastercardPolskaM1St(nullable: false, blank: false, validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
            ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_641_ZL")
        })
        mastercardPolskaM2St(nullable: false, blank: false, validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
            ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_642_ZL")
        })
        mastercardPolskaM3St(nullable: false, blank: false, validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
            ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_643_ZL")
        })
        visaPKOBPKKO1St(nullable: false, blank: false, validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
            ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_711_ZL")
        })
        visaPKOBPKKO2St(nullable: false, blank: false, validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
            ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_712_ZL")
        })
        visaPKOBPKD1St(nullable: false, blank: false, validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
            ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_721_ZL")
        })
        visaPKOBPKD2St(nullable: false, blank: false, validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
            ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_722_ZL")
        })
        visaPKOBPKB3St(nullable: false, blank: false, validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName)
        })
        mastercardPKOBPKK1St(nullable: false, blank: false, validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
            ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_811_ZL")
        })
        mastercardPKOBPKK2St(nullable: false, blank: false, validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
            ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_812_ZL")
        })
        mastercardPKOBPKK3St(nullable: false, blank: false, validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
            ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_813_ZL")
        })
        mastercardPKOBPKD1St(nullable: false, blank: false, validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
            ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_821_ZL")
        })
        mastercardPKOBPKD2LSt(nullable: false, blank: false, validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
            ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_822_ZL")
        })
        mastercardPKOBPKD3St(nullable: false, blank: false, validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
            ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_823_ZL")
        })
        mastercardPKOBPKBSt(nullable: false, blank: false, validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName)
        })
        mastercardPKOBPM1St(nullable: false, blank: false, validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
            ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_841_ZL")
        })
        mastercardPKOBPM2St(nullable: false, blank: false, validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
            ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_842_ZL")
        })
        mastercardPKOBPM3St(nullable: false, blank: false, validator: { value, cmd, errors ->
            NumberValidator.validate(value, cmd, errors, propertyName) &&
            ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "OPLATA_MSC_843_ZL")
        })


        pp_orange_tk(nullable: true, blank: true,  validator: { value, cmd, errors ->
            PercentageValidator.validate(value, cmd, errors, propertyName) &&
                    TelekodzikValidator.validate(value, cmd, errors, propertyName)
        })
        pp_orange_tp(nullable: true, blank: true, validator: { value, cmd, errors ->
            PercentageValidator.validate(value, cmd, errors, propertyName) &&
                    TelepompkaValidator.validate(value, cmd, errors, propertyName)
        })
        pp_plus_tk(nullable: true, blank: true,  validator: { value, cmd, errors ->
            PercentageValidator.validate(value, cmd, errors, propertyName) &&
                    TelekodzikValidator.validate(value, cmd, errors, propertyName)
        })
        pp_plus_tp(nullable: true, blank: true,  validator: { value, cmd, errors ->
            PercentageValidator.validate(value, cmd, errors, propertyName) &&
                    TelepompkaValidator.validate(value, cmd, errors, propertyName)
        })
        pp_tmobile_tk(nullable: true, blank: true,  validator: { value, cmd, errors ->
            PercentageValidator.validate(value, cmd, errors, propertyName) &&
                    TelekodzikValidator.validate(value, cmd, errors, propertyName)
        })
        pp_tmobile_tp(nullable: true, blank: true,  validator: { value, cmd, errors ->
            PercentageValidator.validate(value, cmd, errors, propertyName) &&
                    TelepompkaValidator.validate(value, cmd, errors, propertyName)
        })
        pp_heyah_tk(nullable: true, blank: true,  validator: { value, cmd, errors ->
            PercentageValidator.validate(value, cmd, errors, propertyName) &&
                    TelekodzikValidator.validate(value, cmd, errors, propertyName)
        })
        pp_heyah_tp(nullable: true, blank: true,  validator: { value, cmd, errors ->
            PercentageValidator.validate(value, cmd, errors, propertyName) &&
                    TelepompkaValidator.validate(value, cmd, errors, propertyName)
        })
        pp_play_tk(nullable: true, blank: true,  validator: { value, cmd, errors ->
            PercentageValidator.validate(value, cmd, errors, propertyName) &&
                    TelekodzikValidator.validate(value, cmd, errors, propertyName)
        })
        pp_play_tp(nullable: true, blank: true,  validator: { value, cmd, errors ->
            PercentageValidator.validate(value, cmd, errors, propertyName) &&
                    TelepompkaValidator.validate(value, cmd, errors, propertyName)
        })
        pp_telegrosik_tk(nullable: true, blank: true,  validator: { value, cmd, errors ->
            PercentageValidator.validate(value, cmd, errors, propertyName) &&
                    TelekodzikValidator.validate(value, cmd, errors, propertyName)
        })
        pp_virginmobile_tk(nullable: true, blank: true,  validator: { value, cmd, errors ->
            PercentageValidator.validate(value, cmd, errors, propertyName) &&
                    TelekodzikValidator.validate(value, cmd, errors, propertyName)
        })
        pp_lycamobile_tk(nullable: true, blank: true,  validator: { value, cmd, errors ->
            PercentageValidator.validate(value, cmd, errors, propertyName) &&
                    TelekodzikValidator.validate(value, cmd, errors, propertyName)
        })
        pp_gtmobile_tk(nullable: true, blank: true,  validator: { value, cmd, errors ->
            PercentageValidator.validate(value, cmd, errors, propertyName) &&
                    TelekodzikValidator.validate(value, cmd, errors, propertyName)
        })
        pp_vectonemobile_tk(nullable: true, blank: true,  validator: { value, cmd, errors ->
            PercentageValidator.validate(value, cmd, errors, propertyName) &&
                    TelekodzikValidator.validate(value, cmd, errors, propertyName)
        })
        pp_delightmobile_tk(nullable: true, blank: true,  validator: { value, cmd, errors ->
            PercentageValidator.validate(value, cmd, errors, propertyName) &&
                    TelekodzikValidator.validate(value, cmd, errors, propertyName)
        })

        oplataZaOprogramowanieDoDoladowan(nullable: false, blank: false,  validator: { value, cmd, errors -> NumberValidator.validate(value, cmd, errors, propertyName)})


        scoringMcc(nullable: false, blank: false, validator: { value, cmd, errors ->
            CustomValidator.validate(value, cmd, errors, propertyName, "~|[0-9]{4}")
        })

        scoringDzialalnosc(nullable: false, blank: false)
        scoringSzczegolyDzialalnosci(nullable: true, blank: true)
        scoringWlasnosc(nullable: false, blank: false)
        scoringDzialalnoscCzas(nullable: false, blank: false)
        scoringKoncesja(nullable: false, blank: false)
        rodzajZezwolenia(nullable: true, blank: true)
        scoringCharakterystyka(nullable: false, blank: false)
        scoringCharakterystykaInna(nullable: true, blank: true)
        scoringWielkoscPunktu(nullable: false, blank: false)
        scoringAkceptacja(nullable: false, blank: false)
        scoringMonitoring(nullable: false, blank: false)
        scoringLokalizacjaPunktu(nullable: false, blank: false)
        scoringTypPunktu(nullable: false, blank: false)
        scoringTypPunktuInny(nullable: true, blank: true)
        scoringWielkoscMiejscowosci(nullable: false, blank: false)
        scoringOtwartyZamkniety(nullable: false, blank: false)
        scoringStanZadbany(nullable: true)
        scoringSprzedazTowarowEkskluzywnych(nullable: true) //Boolean
        scoringPonad50ProcentObrotowWNocy(nullable: true) //Boolean
        scoringRuchTurystycznyPrzygraniczny(nullable: true)//Boolean
        scoringUslugiPlatneZGory(nullable: true)//Boolean
        scoringCzestoscTransakcji(nullable: false, blank: false)
        scoringIloscTransakcji(nullable: false, blank: false)
        scoringDochodowosc(nullable: true, blank: true)
        scoringDeklaracjaFinansowa(nullable: false, blank: false)
        scoringDeklaracjaFinansowaObrotOgolem(nullable: true, blank: true)
        scoringDeklaracjaFinansowaObrotNaKarty(nullable: true, blank: true)
        scoringDeklaracjaFinansowaSredniObrot(nullable: true, blank: true)
        scoringDeklaracjaFinansowaSredniaTransakcja(nullable: true, blank: true)
        akceptantUlicaTytul(nullable: true, blank: true)
        akceptantUlica(nullable: false, blank: false, shared: "alphanumeric", validator: { value, cmd, errors ->
            SkipAddressValidator.validate(value, cmd, errors, propertyName) &&
            MaxLengthValidator.validate(value, cmd, errors, 40, propertyName)
        })
        akceptantNrDomu(nullable:false, shared: 'alphanumericWithSlash')
        akceptantNrMieszkania(nullable: true, shared: 'alphanumericWithSlash')
        akceptantMiasto(nullable: false, shared: "alphanumericWithBrackets", validator: { value, cmd, errors ->
            SkipAddressValidator.validate(value, cmd, errors, propertyName) &&
            MaxLengthValidator.validate(value, cmd, errors, 33, propertyName)
        })
        akceptantKodPocztowy(nullable:false, shared: "postalCodeValidator", validator: {value, cmd, errors ->
            SkipAddressValidator.validate(value, cmd, errors, propertyName)
        })
        akceptantPoczta(nullable: true, shared: "alphanumeric", validator: { value, cmd, errors ->
            MaxLengthValidator.validate(value, cmd, errors, 33, propertyName)
        })


        //TODO - sa dwa wpisy dla hasAkceptantTel
        hasAkceptantTel(validator: { value, process, errors ->
            if (value == null || process.akceptantTelKomorkowy == null) {
                return true
            }
            if (value.isEmpty() && process.akceptantTelKomorkowy.isEmpty()) {
                errors.rejectValue("akceptantTelStacjonarny", "default.atLeastOne.phoneNumber", "Nale\u017Cy poda\u0107 przynajmniej jeden numer telefonu")
                errors.rejectValue("akceptantTelKomorkowy", "default.atLeastOne.phoneNumber", "Nale\u017Cy poda\u0107 przynajmniej jeden numer telefonu")
                return false
            }
            return true
        })

        hasAkceptantTel(nullable: true, validator: { value, process, errors ->
            if (value == null) {
                return true
            }

            if (!process.akceptantTelStacjonarny && !process.akceptantTelKomorkowy) {
                errors.rejectValue(propertyName, "default.atLeastOne.phoneNumber")
                return false
            }
            return true
        })

        akceptantTelKomorkowy(nullable: true)
        akceptantTelStacjonarny(nullable: true)
        akceptantFax(nullable: true, blank: true)
        akceptantUlicaTytulCbd(nullable: true)
        akceptantUlicaCbd(nullable: true)
        akceptantNrDomuCbd(nullable: true)
        akceptantNrMieszkaniaCbd(nullable: true)
        akceptantMiastoCbd(nullable: true)
        akceptantKodPocztowyCbd(nullable: true)
        akceptantPocztaCbd(nullable: true)
        akceptantTelStacjonarnyCbd(nullable:true)
        akceptantFaxCbd(nullable: true)
        akceptantTelKomorkowyCbd(nullable:true)
        miejsceUmowy(nullable: true)
        hasObslugaTyp(nullable: true, validator: { value, cmd, errors ->
            if (value == null) {
                return true;
            }
            if (!cmd.obslugaTyp) {
                errors.rejectValue(propertyName, "default.atLeastOne.obslugaTyp")
                return false
            }
            return true
        })
        obslugaEkonomicznyCena(nullable:true, blank:false, validator: { value, cmd, errors -> NumberValidator.validate(value, cmd, errors, propertyName)})
        numerRachunkuBankowegoKlienta(nullable:true, blank:false, validator: {value, cmd, errors ->
            DEFAULT_VALUE.equals(value) ?: NumberValidator.accountNumber(value, cmd, errors, propertyName)
        })
        bankKlienta(nullable:true, blank:false)
        oplataZaUruchomienieDCC(nullable:false, blank:false, validator: {
            value, cmd, errors ->
                NumberValidator.validate(value, cmd, errors, propertyName) && ConditionValidator.atLeastCalcValue(value, cmd, errors, propertyName, "DCC_OPLATA_URUCHOMIENIE")
        })

        czyBeneficjentRzeczywisty(nullable: true, validator: {value, cmd, errors ->
            if(!cmd.hasAtLeastOneRepresentativeAbroad()) {
                return true
            }

            if(cmd.hasAtLeastOneRepresentativeAbroad() && (value == null)) {
                errors.rejectValue("czyBeneficjentRzeczywisty", "beneficiary.radio.required")
                return false
            }

            if(!value && !cmd.akceptantJestSpolka && !cmd.akceptantJestPodmiotem && !cmd.akceptantJestOrganem && !cmd.akceptantNieMaBeneficjenta) {
                errors.rejectValue("czyBeneficjentRzeczywisty", "atleast.one.option.required")
                return false
            }

            return true
        })

        beneficjentKRS(nullable: true, shared: "natural", maxSize: 20, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.beneficjentWeryfikacjaKRS, propertyName, "beneficiary.krs.required")
        })

        beneficjentWeryfikacjaDokumentTozsamosci(nullable: true, validator: ConditionValidator.oneVerificationDocument)

        akceptantJestSpolka(nullable: true)
        nazwaGieldy(nullable: true, maxSize: 50, validator: {value, cmd, errors ->
            if(cmd.akceptantJestSpolka && StringUtils.isEmpty(value)) {
                errors.rejectValue("nazwaGieldy", "nazwaGieldy.required")
                return false
            }

            return true
        })
        isinAkceptanta(nullable: true, maxSize: 12, validator: {value, cmd, errors ->
            return !(cmd.akceptantJestSpolka && NumberValidator.validateIsin(value, cmd, errors, "isinAkceptanta"))
        })

        nip(nullable:true)
        isFromBisnode(nullable:true)
        isRepresentativesChangedManually(nullable:true)
        notes(nullable:true, maxSize: 1000) //a1!
        points(nullable:true, validator: { value, cmd, errors ->
            return PointsValidator.validate(value, cmd, errors)
        })
        poses(nullable:true, validator: { value, cmd, errors ->
            return PosesValidator.validate(value, cmd, errors)
        })

        representatives(nullable: true, validator: {value, cmd, errors ->
            return RepresentativesValidator.validate(value, cmd, errors, propertyName)
        })
        beneficiaries(nullable: true, validator: {value, cmd, errors ->
            cmd.czyBeneficjentRzeczywisty ?
                RepresentativesValidator.validate(value, cmd, errors, propertyName) : true
        })

        allPoints(nullable:true)
        allPoses(nullable:true, validator: AllPosesValidator.validate)
        posExchanges(nullable: true, validator: PosExchangeValidator.validate)
        liczbaPosZCbd(nullable:true)
        korespondencjaJakDlaMerchanta(nullable:true)
        serwisZablokowany(nullable: true)

        liczbaTerminali(nullable:true, validator: { value, cmd, errors ->
            TerminalNumberValidator.validate(value, cmd, errors)
        })

        pointsAndPosesWithoutFormaDoladowania(nullable: true, validator: {value, cmd, errors ->
            int posesWithoutFormaDoladowania = ValidatorUtils.pointsWithoutFormaDoladowania(cmd.poses)
            int pointsWithoutFormaDoladowania = ValidatorUtils.pointsWithoutFormaDoladowania(cmd.points)
            int elementsWithoutFormaDoladowaniaTotal = posesWithoutFormaDoladowania + pointsWithoutFormaDoladowania
            int elementsTotal = cmd.poses.size() + cmd.points.size()

            if(elementsTotal > 0 && (elementsTotal == elementsWithoutFormaDoladowaniaTotal)) {
                errors.reject("default.atLeastOne.doladowania.funkcjaTerminala")
            }

            return true
        })
    }

    def checkIfFromCbd(def property) {
        def cbdName = property + "Cbd"
        return (this.metaClass.hasProperty(this, cbdName) && this."$cbdName"?.trim())
    }

    private boolean checkIfClientFromCbd(){
        return this.checkIfFromCbd("akceptantNazwaOficjalna")
    }

    public boolean hasAtLeastOneRepresentativeAbroad() {
        return representatives.any {AcceptorLocation.ABROAD.name().equals(it.locationType?.name())}
    }

    public String getMessageForProperty(String property){
        //metoda musi zostac, jest uzywana m. in. w validatorach
        return messageSource.getMessage("com.eservice.eumowy.command.ProcessCommand." + property + ".label", [] as Object[], property, Locale.getDefault())
    }

    public Integer getPosCountFromCBD() {
        Integer counter = 0

        allPoints?.each { allPoint ->
            if (allPoint?.cbdId != null) {
                counter += allPoint?.liczbaPos != null ? allPoint?.liczbaPos : 0
            }
        }

        return counter
    }

    public boolean isPersonForm() {
        return dzialalnoscForma && !DEFAULT_VALUE.equals(dzialalnoscForma) ? LegalForm.valueOf(dzialalnoscForma).isPerson() : null
    }

    public boolean isCompanyForm() {
        return dzialalnoscForma && !DEFAULT_VALUE.equals(dzialalnoscForma) ? LegalForm.valueOf(dzialalnoscForma).isCompany() : null
    }
}