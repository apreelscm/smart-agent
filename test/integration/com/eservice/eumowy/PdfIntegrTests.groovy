package com.eservice.eumowy

import com.eservice.eumowy.enums.options.AcceptorLocation
import com.eservice.eumowy.enums.options.IdentityDocumentType
import com.eservice.eumowy.enums.options.LegalForm
import com.eservice.eumowy.helpers.CommandHelper
import com.eservice.eumowy.helpers.PdfHelper
import com.eservice.eumowy.pdfmapper.PABRPEBformMapper
import com.eservice.eumowy.pdfmapper.PEPdeclarationMapper
import grails.test.mixin.TestFor
import grails.test.mixin.web.ControllerUnitTestMixin
import org.junit.Before
import org.junit.Test
import pdfgenerator.PdfGenerator

import static com.eservice.eumowy.helpers.FieldsHelper.*
import static com.eservice.eumowy.helpers.PdfTestDataBuilder.*

@TestFor(PdfService)
class PdfIntegrTests extends ControllerUnitTestMixin {
    private Process process
    private Representative representative
    private Representative beneficiary

    public static final int BOARD_MEMBER_1_X = 85;
    public static final int BOARD_MEMBER_1_Y = 58;

    public static final int BOARD_MEMBER_2_X = 56;
    public static final int BOARD_MEMBER_2_Y = 59;

    static Map<String, String[]> data;

    @Before
    public void setUp() {
        process = new Process()
        process.processData = new HashSet<ProcessData>()
        process.representatives = new ArrayList<Representative>()

        representative = new Representative(type: Representative.Type.REPRESENTATIVE)
        beneficiary = new Representative(type: Representative.Type.BENEFICIARY)
        process.representatives.add(representative)
        process.representatives.add(beneficiary)

        data = new HashMap<String, String[]>();
        data.putAll(generateCommonFields())

        def mockAppParametersService = mockFor(AppParametersService, true)
        mockAppParametersService.demand.getFontUri() {
            -> PdfHelper.fontsPath
        }

        service.appParametersService = mockAppParametersService.createMock()
    }

    static HashMap<String, String[]> generateCommonFields() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.put("dataUmowy", ["21-03-2013"] as String[]);
        data.put("nip", ["65032615970"] as String[]);
        data.put("dataAneksowanejUmowyPos", ["22.04.2013"] as String[]);
        data.put("dataAneksowanejUmowyPrepaid", ["11.05.2013"] as String[]);
        data.put("akceptantNazwa", ["Firma Handlowo Usługowa 'HandUs'"] as String[]);
        data.put("akceptantUlicaTytul", ["ul."] as String[]);
        data.put("akceptantUlica", ["Marszałkowska"] as String[]);
        data.put("akceptantNrDomu", ["3"] as String[]);
        data.put("akceptantNrMieszkania", ["4"] as String[]);
        data.put("akceptantMiasto", ["Warszawa"] as String[]);
        data.put("akceptantKodPocztowy", ["01-234"] as String[]);
        data.put("akceptantSiedziba", ["ul. Marszałkowska 3/4; 01-234 Warszawa"] as String[]);
        data.put("phNumer", ["12345"] as String[]);
        data.put("akceptantNazwaOficjalna", ["Nazwa oficjalna"] as String[]);

        return data;
    }

    @Test
    void AP_UW_UDJ() {
        //given
        def subscriptions = [
                ["PH", 1, 150, 370, 59, 28]
        ]

        //when
        data.putAll(okresLojalnosciowyIOplataDeinstalacyjnaFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process("APUWUDJ1.00014-07-07.pdf", "APUWUDJ1.00014-07-07_out.pdf", data)
    }

    @Test
    void AP_UW_DED() {
        //given
        def subscriptions = [
                ["PH", 3, 370, 270, 59, 28]
        ]

        //when
        data.putAll(warunkiHandlowePompkaIKodzikFields())
        data.putAll(upustPompkaIKodzikFields())
        data.putAll(markedDeliveryType())
        data.put("oplataZaOprogramowanieDoDoladowan", ["123"] as String[]);
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process(PdfHelper.DOCUMENTS_PATH_21_01_01 + "APUWDED1.00221-01-01.pdf",
                PdfHelper.DOCUMENTS_PATH_21_01_01,
                "APUWDED1.00221-01-01_out.pdf",
                data)
    }

    @Test
    void AP_UPZT1() {
        given:
        def subscriptions = [
                ["ACCEPTANT1", 5, 185, 610, 59, 28],
                ["ACCEPTANT2", 5, 185, 560, 59, 28],
                ["ACCEPTANT3", 5, 185, 510, 59, 28],
                ["ACCEPTANT4", 5, 185, 460, 59, 28],
                ["PH", 5, 390, 355, 59, 28]
        ]

        when:
        data.putAll(akceptantIReprezentanciFields())
        data.putAll(umowaOznaczonaFields())
        data.putAll(markedDeliveryType())
        data.putAll(listaPlacowekAkceptujacychFields())
        data.putAll(platnoscZaTypKarty())
        data.putAll(poziomOplatIWarunkiPlatnosciFields())
        data.putAll(specyfikacjaPoziomuOplatIWarunkowPlatnosciFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        then:
        process(PdfHelper.DOCUMENTS_PATH_21_01_01 + "APUPZT11.00621-01-01.pdf",
                PdfHelper.DOCUMENTS_PATH_21_01_01,
                "APUPZT11.00621-01-01_out.pdf",
                data)
    }

    @Test
    void AP_UPZT2() {
        given:
        def subscriptions = [
                ["ACCEPTANT1", 5, 185, 610, 59, 28],
                ["ACCEPTANT2", 5, 185, 560, 59, 28],
                ["ACCEPTANT3", 5, 185, 510, 59, 28],
                ["ACCEPTANT4", 5, 185, 460, 59, 28],
                ["PH", 5, 390, 345, 59, 28]
        ]

        when:
        data.putAll(akceptantIReprezentanciFields())
        data.putAll(umowaOznaczonaFields())
        data.putAll(markedDeliveryType())
        data.putAll(listaPlacowekAkceptujacychFields())
        data.putAll(poziomOplatIWarunkiPlatnosciFields())
        data.putAll(specyfikacjaPoziomuOplatIWarunkowPlatnosciFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        then:
            process(PdfHelper.DOCUMENTS_PATH_21_01_01 + "APUPZT21.00621-01-01.pdf",
                    PdfHelper.DOCUMENTS_PATH_21_01_01,
                    "APUPZT21.00621-01-01_out.pdf",
                    data)
    }

    @Test
    void AP_UPZT3() {
        given:
        def subscriptions = [
                ["ACCEPTANT1", 5, 185, 610, 59, 28],
                ["ACCEPTANT2", 5, 185, 560, 59, 28],
                ["ACCEPTANT3", 5, 185, 510, 59, 28],
                ["ACCEPTANT4", 5, 185, 460, 59, 28],
                ["PH", 5, 390, 345, 59, 28]
        ]

        when:
        data.putAll(akceptantIReprezentanciFields())
        data.putAll(umowaOznaczonaFields())
        data.putAll(markedDeliveryType())
        data.putAll(listaPlacowekAkceptujacychFields())
        data.putAll(platnoscZaTypKarty())
        data.putAll(poziomOplatIWarunkiPlatnosciFields())
        data.putAll(specyfikacjaPoziomuOplatIWarunkowPlatnosciFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        then:
            process(PdfHelper.DOCUMENTS_PATH_21_01_01 + "APUPZT31.00621-01-01.pdf",
                    PdfHelper.DOCUMENTS_PATH_21_01_01,
                    "APUPZT31.00621-01-01_out.pdf",
                    data)
    }

    @Test
    void AP_UPZT4() {
        given:
        def subscriptions = [
                ["ACCEPTANT1", 5, 185, 610, 59, 28],
                ["ACCEPTANT2", 5, 185, 560, 59, 28],
                ["ACCEPTANT3", 5, 185, 510, 59, 28],
                ["ACCEPTANT4", 5, 185, 460, 59, 28],
                ["PH", 5, 390, 335, 59, 28]
        ]

        when:
        data.putAll(akceptantIReprezentanciFields())
        data.putAll(umowaOznaczonaFields())
        data.putAll(markedDeliveryType())
        data.putAll(listaPlacowekAkceptujacychFields())
        data.putAll(poziomOplatIWarunkiPlatnosciFields())
        data.putAll(specyfikacjaPoziomuOplatIWarunkowPlatnosciFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        then:
            process(PdfHelper.DOCUMENTS_PATH_21_01_01 + "APUPZT41.00621-01-01.pdf",
                    PdfHelper.DOCUMENTS_PATH_21_01_01, "APUPZT41.00621-01-01_out.pdf",
                    data)
    }

    @Test
    void AP_UPZ_ZSNT1() {
        given:
        def subscriptions = [
                ["PH", 1, 460, 92, 59, 28]
        ]

        when:
        data.putAll(umowaOznaczonaFields())
        data.putAll(markedDeliveryType())
        data.putAll(poziomOplatIWarunkiPlatnosciFields())
        data.putAll(specyfikacjaPoziomuOplatIWarunkowPlatnosciFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        then:
            process(PdfHelper.DOCUMENTS_PATH_21_01_01 + "APUPZZSNT11.00621-01-01.pdf",
                    PdfHelper.DOCUMENTS_PATH_21_01_01, "APUPZZSNT11.00621-01-01_out.pdf",
                    data)
    }

    @Test
    void AP_UPZ_ZSNT2() {
        given:
        def subscriptions = [
                ["PH", 1, 200, 115, 59, 28]
        ]

        when:
        data.putAll(umowaOznaczonaFields())
        data.putAll(markedDeliveryType())
        data.putAll(poziomOplatIWarunkiPlatnosciFields())
        data.putAll(specyfikacjaPoziomuOplatIWarunkowPlatnosciFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        then:
            process(PdfHelper.DOCUMENTS_PATH_21_01_01 + "APUPZZSNT21.00621-01-01.pdf",
                    PdfHelper.DOCUMENTS_PATH_21_01_01, "APUPZZSNT21.00621-01-01_out.pdf",
                    data)
    }

    @Test
    void AP_UPZ_ZSNT3() {
        given:
        def subscriptions = [
                ["PH", 1, 460, 92, 59, 28]
        ]

        when:
        data.putAll(akceptantIReprezentanciFields())
        data.putAll(umowaOznaczonaFields())
        data.putAll(markedDeliveryType())
        data.putAll(listaPlacowekAkceptujacychFields())
        data.putAll(poziomOplatIWarunkiPlatnosciFields())
        data.putAll(specyfikacjaPoziomuOplatIWarunkowPlatnosciFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        then:
            process(PdfHelper.DOCUMENTS_PATH_21_01_01 + "APUPZZSNT31.00521-01-01.pdf",
                    PdfHelper.DOCUMENTS_PATH_21_01_01, "APUPZZSNT31.00521-01-01_out.pdf",
                    data)
    }

    @Test
    void AP_UPZ_ZSNT4() {
        given:
        def subscriptions = [
                ["PH", 1, 200, 117, 59, 28]
        ]

        when:
        data.putAll(akceptantIReprezentanciFields())
        data.putAll(umowaOznaczonaFields())
        data.putAll(listaPlacowekAkceptujacychFields())
        data.putAll(markedDeliveryType())
        data.putAll(poziomOplatIWarunkiPlatnosciFields())
        data.putAll(specyfikacjaPoziomuOplatIWarunkowPlatnosciFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        then:
            process(PdfHelper.DOCUMENTS_PATH_21_01_01 + "APUPZZSNT41.00521-01-01.pdf",
                    PdfHelper.DOCUMENTS_PATH_21_01_01, "APUPZZSNT41.00521-01-01_out.pdf",
                    data)
    }

    @Test
    void AP_UW() {
        given:
        def subscriptions = [
                ["ACCEPTANT1", 4, 135, 585, 59, 28],
                ["ACCEPTANT2", 4, 135, 535, 59, 28],
                ["ACCEPTANT3", 4, 135, 490, 59, 28],
                ["ACCEPTANT4", 4, 135, 440, 59, 28],
                ["ACCEPTANT1_APUW_zal4", 6, 110, 330, 59, 28],
                ["ACCEPTANT2_APUW_zal4", 6, 110, 280, 59, 28],
                ["ACCEPTANT3_APUW_zal4", 6, 110, 230, 59, 28],
                ["ACCEPTANT4_APUW_zal4", 6, 110, 180, 59, 28],
                ["PH", 4, 375, 288, 59, 28]
        ]

        when:
        data.putAll(akceptantIReprezentanciFields())
        data.putAll(umowaOznaczonaFields())
        data.putAll(listaPlacowekAkceptujacychFields())
        data.putAll(poziomOplatIWarunkiPlatnosciFields())
        data.putAll(specyfikacjaPoziomuOplatIWarunkowPlatnosciFields())
        data.putAll(wykazTerminaliPOSFields())
        data.putAll(markedDeliveryType())
        data.putAll(uslugiDodatkoweFields())
        data.put("umowaNieOzn", ["true", "", "checkbox"] as String[]);
        data.put("czyUslugaLogo", ["true", "", "checkbox"] as String[]);
        data.put("czyUslugaLogo", ["true", "", "checkbox"] as String[]);
        data.put("czyUslugaKalkulator", ["true", "", "checkbox"] as String[]);
        data.put("czyUslugaDcc", ["true", "", "checkbox"] as String[]);
        data.put("czyUslugaMUD", ["true", "", "checkbox"] as String[]);
        data.put("obslugaPrestiz", ["true", "", "checkbox"] as String[]);
        data.put("obslugaKomfort", ["true", "", "checkbox"] as String[]);
        data.put("obslugaEkonomiczny", ["true", "", "checkbox"] as String[]);
        data.put("ssr_true", ["true", "", "checkbox"] as String[]);
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        then:
            process(PdfHelper.DOCUMENTS_PATH_21_01_01 + "APUW1.00821-01-01.pdf",
                    PdfHelper.DOCUMENTS_PATH_21_01_01, "APUW1.00821-01-01_out.pdf",
                    data)
    }

    @Test
    void AP_UW_AWU() {
        given:
            def subscriptions = [
                    ["ACCEPTANT1", 1, 185, 425, 59, 28],
                    ["ACCEPTANT2", 1, 185, 372, 59, 28],
                    ["ACCEPTANT3", 1, 185, 322, 59, 28],
                    ["ACCEPTANT4", 1, 185, 270, 59, 28],
                    ["PH", 1, 375, 150, 59, 28],
                    ["ACCEPTANT1_1", 5, 150, 605, 59, 28],
                    ["ACCEPTANT2_1", 5, 150, 562, 59, 28],
                    ["ACCEPTANT3_1", 5, 150, 513, 59, 28],
                    ["ACCEPTANT4_1", 5, 150, 465, 59, 28],
                    ["PH_1", 5, 375, 310, 59, 28]
            ]

        when:
            data.putAll(akceptantIReprezentanciFields())
            data.putAll(umowaOznaczonaFields())
            data.putAll(listaPlacowekAkceptujacychFields())
            data.putAll(poziomOplatIWarunkiPlatnosciFields())
            data.putAll(specyfikacjaPoziomuOplatIWarunkowPlatnosciFields())
            data.putAll(wykazTerminaliPOSFields())
            data.putAll(markedDeliveryType())
            data.putAll(uslugiDodatkoweFields())
            data.put("umowaNieOzn", ["true", "", "checkbox"] as String[]);
            data.put("czyUslugaLogo", ["true", "", "checkbox"] as String[]);
            data.put("czyUslugaLogo", ["true", "", "checkbox"] as String[]);
            data.put("czyUslugaKalkulator", ["true", "", "checkbox"] as String[]);
            data.put("czyUslugaDcc", ["true", "", "checkbox"] as String[]);
            data.put("czyUslugaMUD", ["true", "", "checkbox"] as String[]);
            data.put("obslugaPrestiz", ["true", "", "checkbox"] as String[]);
            data.put("obslugaKomfort", ["true", "", "checkbox"] as String[]);
            data.put("obslugaEkonomiczny", ["true", "", "checkbox"] as String[]);
            data.put("ssr_true", ["true", "", "checkbox"] as String[]);
            data.putAll(PdfHelper.insertSignatures(subscriptions))

        then:
            process(PdfHelper.DOCUMENTS_PATH_21_01_01 + "APUWAWU1.00021-01-01.pdf",
                    PdfHelper.DOCUMENTS_PATH_21_01_01, "APUWAWU1.00021-01-01_out.pdf",
                    data)
    }

    @Test
    void AP_UW_OOL() {
        given:
        def subscriptions = [
                ["PH", 1, 285, 380, 55, 24]
        ]

        when:
        data.putAll(akceptantIReprezentanciFields())
        data.putAll(umowaOznaczonaFields())
        data.putAll(markedDeliveryType())
        data.putAll(uslugiDodatkoweFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        then:
        process(PdfHelper.JULY_18TH_2020_DOCUMENTS_PATH + "APUWOOL1.00420-02-28.pdf",
                PdfHelper.JULY_18TH_2020_DOCUMENTS_PATH,
                "APUWOOL1.00420-02-28_out.pdf",
                data)
    }

    @Test
    void AP_UW_PON() {
        given:
        def subscriptions = [
                ["PH", 1, 277, 126, 55, 24]
        ]

        when:
        data.putAll(wykazTerminaliPOSObjetychObnizkaNajmuFields())
        data.putAll(markedDeliveryType())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        then:
            process(PdfHelper.DOCUMENTS_PATH_21_01_01 + "APUWPON1.00421-01-01.pdf",
                    PdfHelper.DOCUMENTS_PATH_21_01_01, "APUWPON1.00421-01-01_out.pdf",
                    data)
    }

    @Test
    void AP_UW_RWT() {
        given:
        def subscriptions = [
                ["PH", 1, 380, 265, 55, 24]
        ]

        when:
        data.putAll(wykazTerminaliPOSObjetychObnizkaNajmuFields())
        data.putAll(wykazTerminaliPOSFields())
        data.putAll(promObjFields())
        data.putAll(markedDeliveryType())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        then:
            process(PdfHelper.DOCUMENTS_PATH_21_01_01 + "APUWRWT1.00521-01-01.pdf",
                    PdfHelper.DOCUMENTS_PATH_21_01_01, "APUWRWT1.00521-01-01_out.pdf",
                    data)
    }

    @Test
    void AP_UW_UD() {
        given:
        def subscriptions = [
                ["PH", 1, 285, 225, 55, 24]
        ]

        when:
        data.putAll(uslugiDodatkoweFields())
        data.putAll(markedDeliveryType())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        then:
            process(PdfHelper.DOCUMENTS_PATH_21_01_01 + "APUWUD1.00421-01-01.pdf",
                    PdfHelper.DOCUMENTS_PATH_21_01_01, "APUWUD1.00421-01-01_out.pdf",
                    data)
    }

    @Test
    void AP_UPZ_RWP() {
        //given
        def subscriptions = [
                ["PH", 1, 390, 382, 59, 28]
        ]

        //when
        data.putAll(listaPunktowPlacowekFields())
        data.putAll(markedDeliveryType())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process(PdfHelper.DOCUMENTS_PATH_21_01_01 + "APUPZRWP1.00321-01-01.pdf",
                PdfHelper.DOCUMENTS_PATH_21_01_01,
                "APUPZRWP1.00321-01-01_out.pdf", data)
    }

    @Test
    void KI_RODO() {
        //given
        def subscriptions = [
                ["ACCEPTANT1", 2, 350, 357, 59, 28],
                ["ACCEPTANT2", 2, 350, 307, 59, 28],
                ["ACCEPTANT3", 2, 350, 258, 59, 28],
                ["ACCEPTANT4", 2, 350, 209, 59, 28],
        ]

        //when
        data.putAll(akceptantIReprezentanciFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process(PdfHelper.JULY_18TH_2020_DOCUMENTS_PATH + "KI_RODO20-07-18.pdf",
                PdfHelper.JULY_18TH_2020_DOCUMENTS_PATH,"KI_RODO20-07-18_out.pdf", data)
    }

    @Test
    void AP_AGF_DF() {
        //given
        def subscriptions = [
                ["ACCEPTANT1", 2, 405, 261, 59, 28],
                ["ACCEPTANT2", 2, 405, 232, 59, 28],
                ["ACCEPTANT3", 2, 405, 205, 59, 28],
                ["ACCEPTANT4", 2, 405, 175, 59, 28],
                ["PH", 2, 230, 95, 59, 28]
        ]

        //when
        data.putAll(akceptantIReprezentanciFields())
        data.put("emailDoWysylkiDokumentu", ["jan.kowalski@gmail.com"] as String[])
        data.put("katRyzykaKlientaWartosc", ["Wysokie ryzyko"] as String[])
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process(PdfHelper.DOCUMENTS_PATH_21_01_01 + "APAGFDF2.00821-01-01.pdf",
                PdfHelper.DOCUMENTS_PATH_21_01_01,
                "APAGFDF2.00821-01-01_out.pdf", data)
    }


    @Test
    void UMOWA_WSPOLPRACY_PAKIET_MOBILNY() {
        //given
        def subscriptions = [
                ["ACCEPTANT1", 2, 50, 262, 59, 28],
                ["ACCEPTANT2", 2, 50, 292, 59, 28],
                ["PH", 2, 165, 199, 59, 28]
        ]

        //when
        data.putAll(akceptantIReprezentanciFields())
        data.putAll(okresLojalnosciowyIOplataDeinstalacyjnaFields())
        data.put("cenaPakietu", ["1337"] as String[]);
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process("Umowa_wspolpracy_Pakiet Mobilny.pdf", "Umowa_wspolpracy_Pakiet Mobilny_out.pdf", data)
    }

    @Test
    void PRZYJMOWANIE_ZAPLATY_PAKIET_MOBILNY() {
        //given
        def subscriptions = [
                ["ACCEPTANT1", 5, 150, 310, 64, 33],
                ["ACCEPTANT2", 5, 150, 270, 64, 33],
                ["PH", 5, 450, 130, 84, 53]
        ]

        //when
        data.putAll(akceptantIReprezentanciFields())
        data.putAll(okresLojalnosciowyIOplataDeinstalacyjnaFields())
        data.putAll(listaPlacowekFields())
        data.putAll(wartoscTransackjiPlatniczych())
        data.put("dccKartyZagranicznePr", ["23"] as String[]);
        data.put("dinersClubDo", ["12"] as String[]);

        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process("Przyjmowanie_zaplaty_Pakiet Mobilny.pdf", "Przyjmowanie_zaplaty_Pakiet Mobilny_out.pdf", data)
    }

    @Test
    void AP_AGF_PABR() {
        //given
        Map representativeProperties = [name        : "Jan", surname: "Kowalski", documentType: IdentityDocumentType.IDENTITY_CARD,
                                        country     : "Daleko", address: "JakisAdres", documentNumber: "PL123", citizenship: "Polskie",
                                        locationType: AcceptorLocation.ABROAD, pesel: "91101706344"]

        Map beneficiaryProperties = [name           : "Jan", surname: "Kowalski", documentType: IdentityDocumentType.PASSPORT,
                                     country        : "Daleko", address: "JakisAdres", documentNumber: "PL123", citizenship: "Polskie",
                                     votesPercentage: 59, ownsAcceptor: true, controlsAcceptor: false, overQuarterOfVotes: true]

        def subscriptions = [
                ["ACCEPTANT1", 2, 460, 477, 59, 28],
                ["ACCEPTANT2", 2, 460, 447, 59, 28],
                ["ACCEPTANT3", 2, 460, 417, 59, 28],
                ["ACCEPTANT4", 2, 460, 387, 59, 28],
                ["PH", 2, 440, 253, 59, 28],
                ["PH1", 2, 440, 95, 59, 28]
        ]

        //when
        process.processData.add(new ProcessData(id: 1, version: 0, name: 'dzialalnoscForma', value: 'spolka_cywilna'))
        process.processData.add(new ProcessData(name: 'dataUmowy', value: '10-05-2012'))
        process.processData.add(new ProcessData(name: 'nip', value: '12345'))
        process.processData.add(new ProcessData(name: 'dzialalnoscForma', value: LegalForm.PARTNERSHIP_COMPANY.name()))
        CommandHelper.setProperties(representative, representativeProperties)
        CommandHelper.setProperties(beneficiary, beneficiaryProperties)

        data.putAll(new PABRPEBformMapper(process).getDataForMapping())
        data.putAll(akceptantIReprezentanciFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process("20-11-09/APAGFPABR1.00320-11-09.pdf",
                "20-11-09/","APAGFPABR1.00320-11-09_out.pdf", data)
    }

    @Test
    void APAG_F_PEP() {
        //given
        Map representativeProperties = [isPolitician: false, locationType: AcceptorLocation.ABROAD]
        def subscriptions = [
                ["ACCEPTANT1", 1, 445, 335, 59, 28],
                ["ACCEPTANT2", 1, 500, 315, 59, 28],
                ["ACCEPTANT3", 1, 445, 292, 59, 28],
                ["ACCEPTANT4", 1, 500, 270, 59, 28]
        ]

        //when
        CommandHelper.setProperties(representative, representativeProperties)

        data.putAll(new PEPdeclarationMapper(process).getDataForMapping())
        data.putAll(akceptantIReprezentanciFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process(PdfHelper.JULY_18TH_2020_DOCUMENTS_PATH + "APAGFPEP1.00218-07-01.pdf",
                PdfHelper.JULY_18TH_2020_DOCUMENTS_PATH,"APAGFPEP1.00218-07-01_out.pdf", data)
    }

    @Test
    void AP_UW_ZOR() {
        //given
        HashMap<String, String[]> properties = new HashMap<String, String[]>();
        properties.put("doladowania_tp", ["true", "", "checkbox"] as String[]);
        properties.put("doladowania_tk", ["true", "", "checkbox"] as String[]);
        properties.put("srednia_sprzedaz_doladowan", ["450"] as String[]);
        properties.put("srednia_sprzedaz_doladowan_slownie", ["czterysta pięćdziesiąt"] as String[]);
        properties.putAll(preparePoziomOplatIWarunkiPlatnosciData())
        properties.put("akceptantNip", ["3004005003"] as String[]);
        properties.put("numerRachunkuBankowegoKlienta", ["33333333333333"] as String[]);
        properties.put("bankKlienta", ["33333333333333"] as String[]);
        properties.put("miejsceUmowy", ["Kurniki Podlaskie"] as String[]);

        def subscriptions = [
                ["ACCEPTANT1", 1, 345, 450, 59, 28],
                ["ACCEPTANT11", 1, 345, 93, 59, 28]
        ]

        //when
        data.putAll(insertSignatures2(subscriptions))
        data.putAll(properties)

        //then
        process("APUWZOR1.00014-07-07.pdf", "APUWZOR1.00014-07-07_out.pdf", data)
    }

    @Test
    void APAG_F_DP() { //AP-AG/F/DP/2.006/16-10-11
        def data = [:]
        def subscriptions = [
                ["ACCEPTANT1", 1, 405, 144, 54, 24],
                ["ACCEPTANT2", 1, 460, 125, 54, 24],
                ["ACCEPTANT3", 1, 405, 104, 54, 24],
                ["ACCEPTANT4", 1, 460, 85, 54, 24],
                ["PH", 1, 95, 45, 59, 28]
        ]

        data.putAll(akceptantIReprezentanciFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))
        process(PdfHelper.JULY_18TH_2020_DOCUMENTS_PATH + "APAGFDP2.00616-10-11.pdf",
                PdfHelper.JULY_18TH_2020_DOCUMENTS_PATH,"APAGFDP2.00616-10-11_out.pdf", data);
    }

    @Test
    void AP_F_DS() {
        HashMap<String, String[]> data = prepareScoringData()
        def subscriptions = [
                ["PH", 1, 90, 100, 59, 28]
        ]
        data.putAll(insertSignatures2(subscriptions))
        process(PdfHelper.JULY_1_DAY_2018_DOCUMENTS_PATH + "APFDS2.00116-10-11.pdf",
                "APFDS2.00116-10-11_out.pdf", data);
    }

    @Test
    void AP_F_ZMP() {
        def data = [:]
        def subscriptions = [
                ["PH", 1, 90, 200, 59, 28]
        ]
        data.putAll(insertSignatures2(subscriptions))
        process("APFZMP3.00016-10-11.pdf", "APFZMP3.00016-10-11_out.pdf", data);
    }

    void process(templateName, outName, data) {
        byte[] pdf = service.fillPdfFormFromURI(PdfHelper.fileTemplatePath + templateName, data, PdfGenerator.FontType.ARIAL)

        assert pdf != null

        new File(PdfHelper.fileTemplateOutPath).mkdirs()

        new File(PdfHelper.fileTemplateOutPath + outName).withOutputStream {
            it.write pdf
        }

        println 'Writing pdf to: ' + PdfHelper.fileTemplateOutPath + outName
    }

    void process(templateName, nestedOutputFolder, outName, data) {
        if (nestedOutputFolder) {
            new File(PdfHelper.fileTemplateOutPath + nestedOutputFolder).mkdirs()
        }

        process(templateName, nestedOutputFolder + outName, data)
    }


    private HashMap<String, String[]> insertSignatures(int pageNo, int x, int y, int scaleX, int scaleY) {
        HashMap<String, String[]> result = new HashMap<String, String[]>();
        result.put("reprezentant1_podpis", [new File(PdfHelper.getTemplatePath() + File.separator + "subscriptions" + File.separator + "signature1.jpg").toURI().toURL(), "", "signature", pageNo, x, y, scaleX, scaleY] as String[]);
        result.put("reprezentant2_podpis", [new File(PdfHelper.getTemplatePath() + File.separator + "subscriptions" + File.separator + "signature2.jpg").toURI().toURL(), "", "signature", pageNo, x + 120, y, scaleX, scaleY] as String[]);
        result.put("zarzad1_podpis", [new File(PdfHelper.getTemplatePath() + File.separator + "subscriptions" + File.separator + "signature1.jpg").toURI().toURL(), "", "signature", pageNo, x + 250, y, BOARD_MEMBER_1_X, BOARD_MEMBER_1_Y] as String[]);
        result.put("zarzad2_podpis", [new File(PdfHelper.getTemplatePath() + File.separator + "subscriptions" + File.separator + "signature3.jpg").toURI().toURL(), "", "signature", pageNo, x + 380, y, BOARD_MEMBER_2_X, BOARD_MEMBER_2_Y] as String[]);
        return result;
    }

    private HashMap<String, String[]> insertSignatures2(def signatures) {
        HashMap<String, String[]> result = new HashMap<String, String[]>();

        signatures.each {
            def person = it.get(0);
            def pageNo = it.get(1);
            def x = it.get(2);
            def y = it.get(3);
            def scaleX = it.get(4);
            def scaleY = it.get(5);

            result.put(person, [new File(PdfHelper.getTemplatePath() + File.separator + "subscriptions" + File.separator + "signature1.jpg").toURI().toURL(), "", "signature", pageNo, x, y, scaleX, scaleY] as String[])
        }
        return result;
    }
}
