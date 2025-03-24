package com.eservice.eumowy

import com.eservice.eumowy.enums.options.AcceptorLocation
import com.eservice.eumowy.enums.options.IdentityDocumentType
import com.eservice.eumowy.enums.options.LegalForm
import com.eservice.eumowy.helpers.CommandHelper
import com.eservice.eumowy.helpers.PdfHelper
import com.eservice.eumowy.pdfmapper.PABR_PEP_formMapper
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
    void AP_UW_DED_24_08_01() {
        //given
        def subscriptions = [
                ['PH', 3, 380, 235, 59, 28]
        ]

        //when
        data.putAll(warunkiHandlowePompkaIKodzikFields())
        data.putAll(upustPompkaIKodzikFields())
        data.putAll(markedDeliveryType())
        data.put("oplataZaOprogramowanieDoDoladowan", ["123"] as String[]);
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process("APUWDED1.00324-08-01.pdf",
                "",
                "APUWDED1.00324-08-01.pdf",
                data)
    }

    @Test
    void AP_UPZT1() {
        given:
        def subscriptions = [
                ["ACCEPTANT1", 6, 300, 675, 59, 28],
                ["ACCEPTANT2", 6, 300, 645, 59, 28],
                ["ACCEPTANT3", 6, 300, 615, 59, 28],
                ["ACCEPTANT4", 6, 300, 580, 59, 28],
                ["PH", 6, 380, 485, 59, 28]
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
        process(PdfHelper.DOCUMENTS_PATH_22_09_01 + "APUPZT11.00922-09-01.pdf",
                PdfHelper.DOCUMENTS_PATH_22_09_01,
                "APUPZT11.00922-09-01_out.pdf",
                data)
    }

    @Test
    void AP_UPZT1_24_08_01() {
        given:
        def subscriptions = [
                ['ACCEPTANT1', 5, 195, 245, 30, 14],
                ['ACCEPTANT2', 5, 195, 225, 30, 14],
                ['ACCEPTANT3', 5, 195, 205, 30, 14],
                ['ACCEPTANT4', 5, 195, 185, 30, 14],
                ['PH', 5, 380, 45, 59, 28]
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
        process("APUPZT11.01024-08-01.pdf",
                "",
                "APUPZT11.01024-08-01.pdf",
                data)
    }

    @Test
    void AP_UPZT1_25_03_01() {
        given:
        def subscriptions = [
                ['ACCEPTANT1', 5, 195, 255, 30, 14],
                ['ACCEPTANT2', 5, 195, 235, 30, 14],
                ['ACCEPTANT3', 5, 195, 215, 30, 14],
                ['ACCEPTANT4', 5, 195, 195, 30, 14],
                ['PH', 5, 380, 50, 59, 28]
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
        process("APUPZT11.01125-03-01.pdf",
                "",
                "APUPZT11.01125-03-01.pdf",
                data)
    }

    @Test
    void AP_UPZT2() {
        given:
        def subscriptions = [
                ["ACCEPTANT1", 6, 300, 675, 59, 28],
                ["ACCEPTANT2", 6, 300, 645, 59, 28],
                ["ACCEPTANT3", 6, 300, 615, 59, 28],
                ["ACCEPTANT4", 6, 300, 580, 59, 28],
                ["PH", 6, 380, 485, 59, 28]
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
            process(PdfHelper.DOCUMENTS_PATH_22_09_01 + "APUPZT21.00922-09-01.pdf",
                    PdfHelper.DOCUMENTS_PATH_22_09_01,
                    "APUPZT21.00922-09-01_out.pdf",
                    data)
    }

    @Test
    void AP_UPZT2_24_08_01() {
        given:
        def subscriptions = [
                ['ACCEPTANT1', 5, 195, 245, 30, 14],
                ['ACCEPTANT2', 5, 195, 225, 30, 14],
                ['ACCEPTANT3', 5, 195, 205, 30, 14],
                ['ACCEPTANT4', 5, 195, 185, 30, 14],
                ['PH', 5, 380, 45, 59, 28]
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
        process("APUPZT21.01024-08-01.pdf",
                "",
                "APUPZT21.01024-08-01.pdf",
                data)
    }

    @Test
    void AP_UPZT2_25_03_01() {
        given:
        def subscriptions = [
                ['ACCEPTANT1', 5, 195, 260, 30, 14],
                ['ACCEPTANT2', 5, 195, 240, 30, 14],
                ['ACCEPTANT3', 5, 195, 220, 30, 14],
                ['ACCEPTANT4', 5, 195, 200, 30, 14],
                ['PH', 5, 380, 55, 59, 28]
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
        process("APUPZT21.01125-03-01.pdf",
                "",
                "APUPZT21.01125-03-01.pdf",
                data)
    }

    @Test
    void AP_UPZT3() {
        given:
        def subscriptions = [
                ["ACCEPTANT1", 6, 300, 675, 59, 28],
                ["ACCEPTANT2", 6, 300, 645, 59, 28],
                ["ACCEPTANT3", 6, 300, 615, 59, 28],
                ["ACCEPTANT4", 6, 300, 580, 59, 28],
                ["PH", 6, 380, 485, 59, 28]
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
            process(PdfHelper.DOCUMENTS_PATH_22_09_01 + "APUPZT31.00922-09-01.pdf",
                    PdfHelper.DOCUMENTS_PATH_22_09_01,
                    "APUPZT31.00922-09-01_out.pdf",
                    data)
    }

    @Test
    void AP_UPZT3_24_08_01() {
        given:
        def subscriptions = [
                ['ACCEPTANT1', 5, 195, 245, 30, 14],
                ['ACCEPTANT2', 5, 195, 225, 30, 14],
                ['ACCEPTANT3', 5, 195, 205, 30, 14],
                ['ACCEPTANT4', 5, 195, 185, 30, 14],
                ['PH', 5, 380, 45, 59, 28]
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
        process("APUPZT31.01024-08-01.pdf",
                "",
                "APUPZT31.01024-08-01.pdf",
                data)
    }

    @Test
    void AP_UPZT3_25_03_01() {
        given:
        def subscriptions = [
                ['ACCEPTANT1', 5, 195, 255, 30, 14],
                ['ACCEPTANT2', 5, 195, 235, 30, 14],
                ['ACCEPTANT3', 5, 195, 215, 30, 14],
                ['ACCEPTANT4', 5, 195, 195, 30, 14],
                ['PH', 5, 380, 55, 59, 28]
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
        process("APUPZT31.01125-03-01.pdf",
                "",
                "APUPZT31.01125-03-01.pdf",
                data)
    }

    @Test
    void AP_UPZT4() {
        given:
        def subscriptions = [
                ["ACCEPTANT1", 6, 300, 675, 59, 28],
                ["ACCEPTANT2", 6, 300, 645, 59, 28],
                ["ACCEPTANT3", 6, 300, 615, 59, 28],
                ["ACCEPTANT4", 6, 300, 580, 59, 28],
                ["PH", 6, 380, 485, 59, 28]
        ]

        when:
        data.putAll(akceptantIReprezentanciFields())
        data.putAll(umowaOznaczonaFields())
        data.putAll(markedDeliveryType())
        data.putAll(listaPlacowekAkceptujacychFields())
        data.putAll(poziomOplatIWarunkiPlatnosciFields())
        data.putAll(specyfikacjaPoziomuOplatIWarunkowPlatnosciFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))
        data.put("dccTransaction", ["true", "", "checkbox"] as String[]);
        data.put("cashbackTransaction", ["true", "", "checkbox"] as String[]);

        then:
            process(PdfHelper.DOCUMENTS_PATH_22_09_01 + "APUPZT41.00922-09-01.pdf",
                    PdfHelper.DOCUMENTS_PATH_22_09_01, "APUPZT41.00922-09-01_out.pdf",
                    data)
    }

    @Test
    void AP_UPZT4_24_08_01() {
        given:
        def subscriptions = [
                ['ACCEPTANT1', 5, 195, 245, 30, 14],
                ['ACCEPTANT2', 5, 195, 225, 30, 14],
                ['ACCEPTANT3', 5, 195, 205, 30, 14],
                ['ACCEPTANT4', 5, 195, 185, 30, 14],
                ['PH', 5, 380, 45, 59, 28]
        ]

        when:
        data.putAll(akceptantIReprezentanciFields())
        data.putAll(umowaOznaczonaFields())
        data.putAll(markedDeliveryType())
        data.putAll(listaPlacowekAkceptujacychFields())
        data.putAll(poziomOplatIWarunkiPlatnosciFields())
        data.putAll(specyfikacjaPoziomuOplatIWarunkowPlatnosciFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))
        data.put("dccTransaction", ["true", "", "checkbox"] as String[]);
        data.put("cashbackTransaction", ["true", "", "checkbox"] as String[]);

        then:
        process("APUPZT41.01024-08-01.pdf",
                "", "APUPZT41.01024-08-01.pdf",
                data)
    }

    @Test
    void AP_UPZT4_25_03_01() {
        given:
        def subscriptions = [
                ['ACCEPTANT1', 5, 195, 260, 30, 14],
                ['ACCEPTANT2', 5, 195, 240, 30, 14],
                ['ACCEPTANT3', 5, 195, 220, 30, 14],
                ['ACCEPTANT4', 5, 195, 200, 30, 14],
                ['PH', 5, 380, 55, 59, 28]
        ]

        when:
        data.putAll(akceptantIReprezentanciFields())
        data.putAll(umowaOznaczonaFields())
        data.putAll(markedDeliveryType())
        data.putAll(listaPlacowekAkceptujacychFields())
        data.putAll(poziomOplatIWarunkiPlatnosciFields())
        data.putAll(specyfikacjaPoziomuOplatIWarunkowPlatnosciFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))
        data.put("dccTransaction", ["true", "", "checkbox"] as String[]);
        data.put("cashbackTransaction", ["true", "", "checkbox"] as String[]);

        then:
        process("APUPZT41.01125-03-01.pdf",
                "", "APUPZT41.01125-03-01.pdf",
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
            process(PdfHelper.DOCUMENTS_PATH_22_09_01 + "APUPZZSNT11.00621-01-01.pdf",
                    PdfHelper.DOCUMENTS_PATH_22_09_01, "APUPZZSNT11.00621-01-01_out.pdf",
                    data)
    }

    @Test
    void AP_UPZ_ZSNT1_24_08_01() {
        given:
        def subscriptions = [
                ['PH', 1, 395, 125, 59, 28]
        ]

        when:
        data.putAll(umowaOznaczonaFields())
        data.putAll(markedDeliveryType())
        data.putAll(poziomOplatIWarunkiPlatnosciFields())
        data.putAll(specyfikacjaPoziomuOplatIWarunkowPlatnosciFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        then:
        process("APUPZZSNT11.00724-08-01.pdf",
                "", "APUPZZSNT11.00724-08-01.pdf",
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
            process(PdfHelper.DOCUMENTS_PATH_22_09_01 + "APUPZZSNT21.00621-01-01.pdf",
                    PdfHelper.DOCUMENTS_PATH_22_09_01, "APUPZZSNT21.00621-01-01_out.pdf",
                    data)
    }

    @Test
    void AP_UPZ_ZSNT2_24_08_01() {
        given:
        def subscriptions = [
                ['PH', 1, 250, 150, 59, 28]
        ]

        when:
        data.putAll(umowaOznaczonaFields())
        data.putAll(markedDeliveryType())
        data.putAll(poziomOplatIWarunkiPlatnosciFields())
        data.putAll(specyfikacjaPoziomuOplatIWarunkowPlatnosciFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        then:
        process("APUPZZSNT21.00724-08-01.pdf",
                "", "APUPZZSNT21.00724-08-01.pdf",
                data)
    }

    @Test
    void AP_UPZ_ZSNT2_25_03_01() {
        given:
        def subscriptions = [
                ['PH', 1, 385, 150, 59, 28]
        ]

        when:
        data.putAll(umowaOznaczonaFields())
        data.putAll(markedDeliveryType())
        data.putAll(poziomOplatIWarunkiPlatnosciFields())
        data.putAll(specyfikacjaPoziomuOplatIWarunkowPlatnosciFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        then:
        process("APUPZZSNT21.00724-08-01_final.pdf",
                "", "APUPZZSNT21.00724-08-01_final.pdf",
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
            process(PdfHelper.DOCUMENTS_PATH_22_09_01 + "APUPZZSNT31.00621-01-01.pdf",
                    PdfHelper.DOCUMENTS_PATH_22_09_01, "APUPZZSNT31.00621-01-01_out.pdf",
                    data)
    }

    @Test
    void AP_UPZ_ZSNT3_24_08_01() {
        given:
        def subscriptions = [
                ['PH', 1, 395, 125, 59, 28]
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
        process("APUPZZSNT31.00624-08-01.pdf",
                "", "APUPZZSNT31.00624-08-01.pdf",
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
            process(PdfHelper.DOCUMENTS_PATH_22_09_01 + "APUPZZSNT41.00621-01-01.pdf",
                    PdfHelper.DOCUMENTS_PATH_22_09_01, "APUPZZSNT41.00621-01-01_out.pdf",
                    data)
    }

    @Test
    void AP_UPZ_ZSNT4_24_08_01() {
        given:
        def subscriptions = [
                ['PH', 1, 175, 160, 59, 28]
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
        process("APUPZZSNT41.00624-08-01.pdf",
                "", "APUPZZSNT41.00624-08-01.pdf",
                data)
    }

    @Test
    void AP_UPZ_ZSNT4_25_03_01() {
        given:
        def subscriptions = [
                ['PH', 1, 385, 160, 59, 28]
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
        process("APUPZZSNT41.00624-08-01_final.pdf",
                "", "APUPZZSNT41.00624-08-01_final.pdf",
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
    void AP_UW_24_08_01() {
        given:
        def subscriptions = [
                ['ACCEPTANT1', 4, 190, 375, 59, 28],
                ['ACCEPTANT2', 4, 190, 340, 59, 28],
                ['ACCEPTANT3', 4, 190, 305, 59, 28],
                ['ACCEPTANT4', 4, 190, 265, 59, 28],
                ['PH', 4, 375, 85, 59, 28]
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
        process("APUW1.01024-08-01.pdf",
                "", "APUW1.01024-08-01.pdf",
                data)
    }

    @Test
    void AP_UW_25_03_01() {
        given:
        def subscriptions = [
                ['ACCEPTANT1', 4, 190, 360, 59, 28],
                ['ACCEPTANT2', 4, 190, 325, 59, 28],
                ['ACCEPTANT3', 4, 190, 290, 59, 28],
                ['ACCEPTANT4', 4, 190, 250, 59, 28],
                ['PH', 4, 375, 85, 59, 28]
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
        process("APUW1.01125-03-01.pdf",
                "", "APUW1.01125-03-01.pdf",
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
    void AP_UW_AWU_24_08_01() {
        given:
        def subscriptions = [
                ['ACCEPTANT1', 1, 215, 360, 30, 14],
                ['ACCEPTANT2', 1, 215, 340, 30, 14],
                ['ACCEPTANT3', 1, 215, 320, 30, 14],
                ['ACCEPTANT4', 1, 215, 300, 30, 14],
                ['PH', 1, 375, 130, 59, 28],
                ['ACCEPTANT1_1', 5, 200, 375, 59, 28],
                ['ACCEPTANT2_1', 5, 200, 340, 59, 28],
                ['ACCEPTANT3_1', 5, 200, 305, 59, 28],
                ['ACCEPTANT4_1', 5, 200, 265, 59, 28],
                ['PH_1', 5, 375, 85, 59, 28]
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
        process("APUWAWU1.00124-08-01.pdf",
                "", "APUWAWU1.00124-08-01.pdf",
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
    void AP_UW_PON_24_08_01() {
        given:
        def subscriptions = [
                ['PH', 1, 380, 150, 55, 24]
        ]

        when:
        data.putAll(wykazTerminaliPOSObjetychObnizkaNajmuFields())
        data.putAll(markedDeliveryType())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        then:
        process("APUWPON1.00524-08-01.pdf",
                "", "APUWPON1.00524-08-01.pdf",
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
    void AP_UW_RWT_24_08_01() {
        given:
        def subscriptions = [
                ['PH', 1, 380, 215, 55, 24]
        ]

        when:
        data.putAll(wykazTerminaliPOSObjetychObnizkaNajmuFields())
        data.putAll(wykazTerminaliPOSFields())
        data.putAll(promObjFields())
        data.putAll(markedDeliveryType())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        then:
        process("APUWRWT1.00624-08-01.pdf",
                "", "APUWRWT1.00624-08-01.pdf",
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
    void AP_UW_UD_24_08_01() {
        given:
        def subscriptions = [
                ['PH', 1, 385, 225, 55, 24]
        ]

        when:
        data.putAll(uslugiDodatkoweFields())
        data.putAll(markedDeliveryType())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        then:
        process("APUWUD1.00524-08-01.pdf",
                "", "APUWUD1.00524-08-01.pdf",
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
    void AP_UPZ_RWP_24_08_01() {
        //given
        def subscriptions = [
                ['PH', 1, 80, 460, 59, 28]
        ]

        //when
        data.putAll(listaPunktowPlacowekFields())
        data.putAll(markedDeliveryType())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process("APUPZRWP1.00424-08-01.pdf",
                "",
                "APUPZRWP1.00424-08-01.pdf", data)
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
    void KI_RODO_24_08_01() {
        //given
        def subscriptions = [
                ['ACCEPTANT1', 2, 210, 170, 59, 28],
                ['ACCEPTANT2', 2, 210, 130, 59, 28],
                ['ACCEPTANT3', 2, 210, 95, 59, 28],
                ['ACCEPTANT4', 2, 210, 55, 59, 28],
        ]

        //when
        data.putAll(akceptantIReprezentanciFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process("KI_RODO24-08-01.pdf",
                "","KI_RODO24-08-01_out.pdf", data)
    }

    @Test
    void KI_RODO_25_02_01() {
        //given
        def subscriptions = [
                ['ACCEPTANT1', 2, 210, 170, 59, 28],
                ['ACCEPTANT2', 2, 210, 130, 59, 28],
                ['ACCEPTANT3', 2, 210, 95, 59, 28],
                ['ACCEPTANT4', 2, 210, 55, 59, 28],
        ]

        //when
        data.putAll(akceptantIReprezentanciFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process("Kl_RODO25-02-01.pdf",
                "","KI_RODO25-02-01_out.pdf", data)
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

        data.putAll(new PABR_PEP_formMapper(process).getDataForMapping())
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
    void AP_FDP() { //AP/FDP/2.001/21-07-26
        def data = [:]
        def subscriptions = [
                ["ACCEPTANT1", 1, 405, 225, 54, 24],
                ["ACCEPTANT2", 1, 460, 210, 54, 24],
                ["ACCEPTANT3", 1, 405, 187, 54, 24],
                ["ACCEPTANT4", 1, 460, 165, 54, 24],
                ["PH", 1, 280, 115, 59, 28]
        ]

        data.putAll(akceptantIReprezentanciFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))
        process("APFDP2.00121-07-26.pdf", "","APFDP2.00121-07-26_out.pdf", data);
    }

    @Test
    void AP_FDP_24_08_01_with_signatures() {
        def data = [:]
        def subscriptions = [
                ['ACCEPTANT1', 1, 290, 145, 27, 12],
                ['ACCEPTANT2', 1, 290, 130, 27, 12],
                ['ACCEPTANT3', 1, 290, 110, 27, 12],
                ['ACCEPTANT4', 1, 290, 95, 27, 12],
                ['PH', 1, 170, 40, 59, 28]
        ]

        data.putAll(akceptantIReprezentanciFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))
        process("APFDP2.00224-08-01_z_podpisem_Akceptanta.pdf", "","APFDP2.00224-08-01_z_podpisem_Akceptanta.pdf", data);
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
    void AP_F_DS_24_08_01() {
        HashMap<String, String[]> data = prepareScoringData()
        def subscriptions = [
                ["PH", 1, 90, 100, 59, 28]
        ]
        data.putAll(insertSignatures2(subscriptions))
        process("APFDS2.00424-08-01.pdf",
                "APFDS2.00424-08-01.pdf", data);
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

    @Test
    void NEW_PABR_PEB() { //APAGFPABRPEP1.00521-01-14.pdf
        def data = [:]
        def subscriptions = [
            ["ACCEPTANT1", 2, 375, 400, 20, 22],
            ["ACCEPTANT2", 2, 375, 412, 20, 22],
            ["ACCEPTANT3", 2, 375, 424, 20, 22],
            ["ACCEPTANT4", 2, 375, 446, 20, 22],
            ["PH", 2, 460, 328, 40, 22],
            ["PH", 2, 460, 185, 40, 22],
        ]

        data.putAll(akceptantIReprezentanciFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))
        processRaw("C:\\opt\\eumowy\\pdf_templates\\APAGFPABRPEP1.00521-01-14.pdf",
            "C:\\opt\\eumowy\\pdf_templates\\test.pdf", data);
    }

    @Test
    void PABR_PEP_25_03_01() {
        def data = [:]
        def subscriptions = [
                ["ACCEPTANT1", 2, 375, 570, 20, 22],
                ["ACCEPTANT2", 2, 375, 590, 20, 22],
                ["ACCEPTANT3", 2, 375, 610, 20, 22],
                ["ACCEPTANT4", 2, 375, 630, 20, 22],
                ["PH2", 2, 465, 435, 40, 22],
                ["PH1", 2, 115, 435, 40, 22],
        ]

        data.putAll(akceptantIReprezentanciFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))
        process("PABR_PEP25.0303.pdf", "PABR_PEP25.0303.pdf", data)
    }

    @Test
    void APUW_OZWU() {
        given:
        data.putAll(akceptantIReprezentanciFields())
        data.putAll(umowaOznaczonaFields())
        data.putAll(listaPlacowekAkceptujacychFields())
        data.putAll(poziomOplatIWarunkiPlatnosciFields())
        data.putAll(specyfikacjaPoziomuOplatIWarunkowPlatnosciFields())
        data.putAll(wykazTerminaliPOSFields())
        data.putAll(markedDeliveryType())
        data.putAll(uslugiDodatkoweFields())
        data.put("ssr_true", ["true", "", "checkbox"] as String[])
        data.put("akceptantKodPocztowy1", ["00"] as String[])
        data.put("akceptantKodPocztowy2", ["660"] as String[])

        then:
        process("APUW_OZWU1.00021-07-26.pdf", "APUW_OZWU1.00021-07-26_out.pdf", data)
    }

    @Test
    void APUW_OZWU_24_08_01() {
        given:
        data.putAll(akceptantIReprezentanciFields())
        data.putAll(umowaOznaczonaFields())
        data.putAll(listaPlacowekAkceptujacychFields())
        data.putAll(poziomOplatIWarunkiPlatnosciFields())
        data.putAll(specyfikacjaPoziomuOplatIWarunkowPlatnosciFields())
        data.putAll(wykazTerminaliPOSFields())
        data.putAll(markedDeliveryType())
        data.putAll(uslugiDodatkoweFields())
        data.put("ssr_true", ["true", "", "checkbox"] as String[])
        data.put("akceptantKodPocztowy1", ["00"] as String[])
        data.put("akceptantKodPocztowy2", ["660"] as String[])

        then:
        process("APUW_OZWU1.00124-08-01.pdf", "APUW_OZWU1.00124-08-01.pdf", data)
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

    void processRaw(inPath, outName, data) {
        byte[] pdf = service.fillPdfFormFromURI(inPath, data, PdfGenerator.FontType.ARIAL)

        assert pdf != null

        new File(outName).withOutputStream {
            it.write pdf
        }

        println 'Writing pdf to: ' + outName
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
