package com.eservice.eumowy

import com.eservice.eumowy.enums.options.AcceptorLocation
import com.eservice.eumowy.enums.options.IdentityDocumentType
import com.eservice.eumowy.enums.options.LegalForm
import com.eservice.eumowy.helpers.CommandHelper
import com.eservice.eumowy.helpers.PdfHelper
import com.eservice.eumowy.pdfmapper.PABRformMapper
import com.eservice.eumowy.pdfmapper.PEPdeclarationMapper
import grails.test.mixin.TestFor
import grails.test.mixin.web.ControllerUnitTestMixin
import org.apache.pdfbox.pdmodel.PDDocument
import org.junit.Before
import org.junit.Test
import pdfgenerator.PdfGenerator

import static com.eservice.eumowy.helpers.FieldsHelper.*
import static com.eservice.eumowy.helpers.PdfTestDataBuilder.*

@TestFor(PdfService)
class PdfIntegrTests extends ControllerUnitTestMixin{
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
        mockAppParametersService.demand.getFontUri(){
            -> PdfHelper.fontsPath
        }

        service.appParametersService =  mockAppParametersService.createMock()
    }

    static HashMap<String, String[]> generateCommonFields(){
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.put("dataUmowy", ["21-03-2013"] as String[]);
        data.put("nip", ["65032615970"] as String[]);
        data.put("dataAneksowanejUmowyPos", ["22.04.2013"] as String[]);
        data.put("dataAneksowanejUmowyPrepaid", ["11.05.2013"] as String[]);
        data.put("akceptantNazwa", ["Firma Handlowo Usługowa 'HandUs'"] as String[]);
        data.put("akceptantSiedziba", ["ul. Marszałkowska 3/4; 01-234 Warszawa"] as String[]);
        data.put("phNumer", ["12345"] as String[]);
        data.put("akceptantNazwaOficjalna", ["Nazwa oficjalna"] as String[]);

        return data;
    }

    static HashMap<String, String[]> generateDataPodpisaniaAneksuPOZField(){
        HashMap<String, String[]> result = new HashMap<String, String[]>();
        result.put("dataPodpisaniaAneksuPOZ", ["23.04.2013"] as String[]);
        return result;
    }

    private static addCheckbox(def data, def pdfName, def fieldValue, def value){
        data.put(pdfName, [fieldValue.equals(value), "", "checkbox"] as String[])
    }

    @Test
    void APUW1002160331() {  //AP/UW/1.002/16-03-31
        //given
        def subscriptions = [
                ["ACCEPTANT1", 2, 345, 370, 59, 28],
                ["ACCEPTANT2", 2, 345, 317, 59, 28],
                ["ACCEPTANT3", 2, 345, 265, 59, 28],
                ["ACCEPTANT4", 2, 345, 212, 59, 28],
                ["PH", 2, 465, 115, 59, 28]
        ]

        //when
        data.putAll(akceptantIReprezentanciFields())
        data.putAll(umowaOznaczonaFields())
        data.putAll(uslugiDodatkoweFields())
        data.putAll(wykazTerminaliPOSFields())
        data.putAll(okresLojalnosciowyIOplataDeinstalacyjnaFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))
        data.put("zalacznikNr4", ["4 - Nazwa zalacznika nr 4"] as String[]);

        //then
        process("APUW1.00216-03-31.pdf", "APUW1.00216-03-31_out.pdf", data)
    }

    @Test
    void APUWUDJ1000140707() { //AP/UW/UDJ/1.000/14-07-07
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
    void APUWRWT1001150305() { //AP/UW/RWT/1.001/15-03-05
        //given
        def subscriptions = [
                ["PH", 1, 260, 145, 59, 28]
        ]

        //when
        data.putAll(wykazTerminaliPOSFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process("APUWRWT1.00115-03-05.pdf", "APUWRWT1.00115-03-05_out.pdf", data)
    }

    @Test
    void APUWUD1000140707() { //AP/UW/UD/1.000/14-07-07
        //given
        def subscriptions = [
                ["PH", 1, 280, 161, 59, 28]
        ]

        //when
        data.putAll(uslugiDodatkoweFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process("APUWUD1.00014-07-07.pdf", "APUWUD1.00014-07-07_out.pdf", data)
    }

    @Test
    void APUWPON1000140707() { //AP/UW/PON/1.000/14-07-07
        //given
        def subscriptions = [
                ["PH", 1, 270, 125, 59, 28]
        ]

        //when
        data.putAll(wykazTerminaliPOSObjetychObnizkaNajmuFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process("APUWPON1.00014-07-07.pdf", "APUWPON1.00014-07-07_out.pdf", data)
    }

    @Test
    void APUWDED1000140707() { //AP/UW/DED/1.000/14-07-07
        //given
        def subscriptions = [
                ["PH", 2, 260, 68, 59, 28]
        ]

        //when
        data.putAll(warunkiHandlowePompkaIKodzikFields())
        data.putAll(upustPompkaIKodzikFields())
        data.put("oplataZaOprogramowanieDoDoladowan", ["123"] as String[]);
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process("APUWDED1.00014-07-07.pdf", "APUWDED1.00014-07-07_out.pdf", data)
    }

    @Test
    void APUPZT11000150305() { //AP/UPZT1/1.001/16-02-01
        //given
        def subscriptions = [
                ["ACCEPTANT1", 4, 160, 310, 59, 28],
                ["ACCEPTANT2", 4, 160, 267, 59, 28],
                ["ACCEPTANT3", 4, 160, 225, 59, 28],
                ["ACCEPTANT4", 4, 160, 183, 59, 28],
                ["PH", 4, 180, 68, 59, 28]
        ]

        //when
        data.putAll(akceptantIReprezentanciFields())
        data.putAll(umowaOznaczonaFields())
        data.putAll(listaPlacowekAkceptujacychFields())
        data.putAll(poziomOplatIWarunkiPlatnosciFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process("APUPZT11.00116-02-01.pdf", "APUPZT11.00116-02-01_out.pdf", data)
    }

    @Test
    void APUPZT21000150303() { //AP/UPZT2/1.001/16-02-01
        //given
        def subscriptions = [
                ["ACCEPTANT1", 4, 160, 460, 59, 28],
                ["ACCEPTANT2", 4, 160, 415, 59, 28],
                ["ACCEPTANT3", 4, 160, 373, 59, 28],
                ["ACCEPTANT4", 4, 160, 332, 59, 28],
                ["PH", 4, 190, 220, 59, 28]
        ]

        //when
        data.putAll(akceptantIReprezentanciFields())
        data.putAll(umowaOznaczonaFields())
        data.putAll(listaPlacowekAkceptujacychFields())
        data.putAll(specyfikacjaPoziomuOplatIWarunkowPlatnosciFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process("APUPZT21.00116-02-01.pdf", "APUPZT21.00116-02-01_out.pdf", data)
    }

    @Test
    void APUPZT31000150303() { //AP/UPZT3/1.001/16-02-01
        //given
        def subscriptions = [
                ["ACCEPTANT1", 4, 160, 310, 59, 28],
                ["ACCEPTANT2", 4, 160, 267, 59, 28],
                ["ACCEPTANT3", 4, 160, 225, 59, 28],
                ["ACCEPTANT4", 4, 160, 182, 59, 28],
                ["PH", 4, 190, 67, 59, 28]
        ]

        //when
        data.putAll(akceptantIReprezentanciFields())
        data.putAll(umowaOznaczonaFields())
        data.putAll(listaPlacowekAkceptujacychFields())
        data.putAll(specyfikacjaPoziomuOplatIWarunkowPlatnosciFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process("APUPZT31.00116-02-01.pdf", "APUPZT31.00116-02-01_out.pdf", data)
    }

    @Test
    void APUPZT41000150303() { //AP/UPZT4/1.001/16-02-01
        //given
        def subscriptions = [
                ["ACCEPTANT1", 4, 160, 460, 59, 28],
                ["ACCEPTANT2", 4, 160, 415, 59, 28],
                ["ACCEPTANT3", 4, 160, 373, 59, 28],
                ["ACCEPTANT4", 4, 160, 332, 59, 28],
                ["PH", 4, 190, 205, 59, 28]
        ]

        //when
        data.putAll(akceptantIReprezentanciFields())
        data.putAll(umowaOznaczonaFields())
        data.putAll(listaPlacowekAkceptujacychFields())
        data.putAll(specyfikacjaPoziomuOplatIWarunkowPlatnosciFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process("APUPZT41.00116-02-01.pdf", "APUPZT41.00116-02-01_out.pdf", data)
    }

    @Test
    void APUPZRWP1000140707() { //AP/UPZ/RWP/1.000/14-07-07
        //given
        def subscriptions = [
                ["PH", 1, 190, 115, 59, 28]
        ]

        //when
        data.putAll(listaPunktowPlacowekFields())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process("APUPZRWP1.00014-07-07.pdf", "APUPZRWP1.00014-07-07_out.pdf", data)
    }

    @Test
    void APUPZZSNT11001150305() { //AP/UPZ/ZSNT1/1.002/16-02-01
        //given
        def subscriptions = [
                ["PH", 1, 480, 105, 59, 28]
        ]

        //when
        data.putAll(poziomOplatIWarunkiPlatnosciFields())
        data.put("upustCashback", ["5"] as String[])
        data.put("dccKartyZagranicznePr", ["12"] as String[])
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process("APUPZZSNT11.00216-02-01.pdf", "APUPZZSNT11.00216-02-01_out.pdf", data)
    }

    @Test
    void APUPZZSNT21001150305() { //AP/UPZ/ZSNT2/1.002/16-02-01
        //given
        def subscriptions = [
                ["PH", 1, 260, 85, 59, 28]
        ]

        //when
        data.putAll(specyfikacjaPoziomuOplatIWarunkowPlatnosciFields())
        data.put("upustCashback", ["5"] as String[])
        data.put("dccKartyZagranicznePr", ["12"] as String[])
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process("APUPZZSNT21.00216-02-01.pdf", "APUPZZSNT21.00216-02-01_out.pdf", data)
    }

    @Test
    void KI_RODO() { //KI-RODO
        //given
        def subscriptions = [
                ["ACCEPTANT1", 2, 70, 135, 59, 28],
                ["ACCEPTANT2", 2, 70, 65, 59, 28],
                ["ACCEPTANT3", 2, 280, 135, 59, 28],
                ["ACCEPTANT4", 2, 280, 65, 59, 28],
        ]

        //when
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process("KI_RODO.pdf", "KI_RODO_out.pdf", data)
    }

    @Test
    void APUPZZSNT31001150305() { //AP/UPZ/ZSNT3/1.002/16-02-01
        //given
        def subscriptions = [
                ["PH", 1, 480, 85, 59, 28]
        ]

        //when
        data.putAll(specyfikacjaPoziomuOplatIWarunkowPlatnosciFields())
        data.put("upustCashback", ["5"] as String[])
        data.put("dccKartyZagranicznePr", ["12"] as String[])
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process("APUPZZSNT31.00216-02-01.pdf", "APUPZZSNT31.00216-02-01_out.pdf", data)
    }

    @Test
    void APUPZZSNT41001150305() { //AP/UPZ/ZSNT4/1.002/16-02-01
        //given
        def subscriptions = [
                ["PH", 1, 260, 80, 59, 28]
        ]

        //when
        data.putAll(specyfikacjaPoziomuOplatIWarunkowPlatnosciFields())
        data.put("upustCashback", ["5"] as String[])
        data.put("dccKartyZagranicznePr", ["12"] as String[])
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process("APUPZZSNT41.00216-02-01.pdf", "APUPZZSNT41.00216-02-01_out.pdf", data)
    }

    @Test
    void APAGFDF2005180701() { //AP-AG/F/DF/2.005/18-07-01
        //given
        def subscriptions = [
                ["ACCEPTANT1", 2, 115, 335, 59, 28],
                ["PH", 2, 230, 235, 59, 28]
        ]

        //when
        data.putAll(akceptantIReprezentanciFields())
        data.put("emailDoWysylkiDokumentu", ["jan.kowalski@gmail.com"] as String[])
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process("APAGFDF2.00518-07-01.pdf", "APAGFDF2.00518-07-01_out.pdf", data)
    }


    @Test
    void pakiet_UmowaWspolpracy() {
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
    void pakiet_PrzyjmowanieZaplaty() {
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
    void APAGFPABR1001180701() { //AP-AG/F/PABR/1.001/18-07-01
        //given
        Map representativeProperties = [name: "Jan", surname: "Kowalski", documentType: IdentityDocumentType.IDENTITY_CARD,
                country: "Daleko", address: "JakisAdres", documentNumber: "PL123", citizenship: "Polskie",
                locationType: AcceptorLocation.ABROAD, pesel: "91101706344"]

        Map beneficiaryProperties = [name: "Jan", surname: "Kowalski", documentType: IdentityDocumentType.PASSPORT,
                country: "Daleko", address: "JakisAdres", documentNumber: "PL123", citizenship: "Polskie",
                 votesPercentage: 59, ownsAcceptor: true, controlsAcceptor: false, overQuarterOfVotes: true]

        def subscriptions = [
                ["ACCEPTANT1", 2, 270, 508, 59, 28],
                ["ACCEPTANT2", 2, 360, 508, 59, 28],
                ["PH", 2, 440, 368, 59, 28],
                ["PH1", 2, 440, 210, 59, 28]
        ]

        //when
        process.processData.add(new ProcessData(id: 1, version: 0, name: 'dzialalnoscForma', value: 'spolka_cywilna'))
        process.processData.add(new ProcessData(name: 'dataUmowy', value: '10-05-2012'))
        process.processData.add(new ProcessData(name: 'nip', value: '12345'))
        process.processData.add(new ProcessData(name: 'dzialalnoscForma', value: LegalForm.PARTNERSHIP_COMPANY.name()))
        CommandHelper.setProperties(representative, representativeProperties)
        CommandHelper.setProperties(beneficiary, beneficiaryProperties)

        data.putAll(new PABRformMapper(process).getDataForMapping())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process("APAGFPABR1.00118-07-01.pdf", "APAGFPABR1.00118-07-01_out.pdf", data)
    }

    @Test
    void APAGFPEP1001180701() { //AP-AG/F/PEP/1.001/18-07-01
        //given
        Map representativeProperties = [isPolitician: false, locationType: AcceptorLocation.ABROAD]
        def subscriptions = [
                ["ACCEPTANT1", 1, 380, 295, 59, 28],
                ["ACCEPTANT2", 1, 320, 295, 59, 28],
                ["ACCEPTANT3", 1, 260, 295, 59, 28],
                ["ACCEPTANT4", 1, 440, 295, 59, 28]
        ]

        //when
        CommandHelper.setProperties(representative, representativeProperties)

        data.putAll(new PEPdeclarationMapper(process).getDataForMapping())
        data.putAll(PdfHelper.insertSignatures(subscriptions))

        //then
        process("APAGFPEP1.00118-07-01.pdf", "APAGFPEP1.00118-07-01_out.pdf", data)
    }

    void testAPUPZBSX() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.put("dataUmowy", ["10-05-2012"] as String[]);
        data.putAll(prepareDccData())
//			data.putAll(insertSignatures(1, 85, 185, 74, 43))

        def subscriptions = [
                ["ACCEPTANT1", 4, 70, 330, 94, 63],
                ["ACCEPTANT2", 4, 190, 330, 94, 63],
                ["ZARZAD1", 4, 320, 330, 85, 58],
                ["ZARZAD2", 4, 450, 330, 56, 58],
                ["PH", 4, 450, 230, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))

        process("APUPZBS2.00113-08-06 - Umowa o przyjmowanie zaplaty (wersja bez stawek plaskich)_do druku.pdf", "APUPZBS2.00113-08-06 - Umowa o przyjmowanie zaplaty (wersja bez stawek plaskich)_do druku_out.pdf", data)
    }

    @Test
    void testAPUWZOR1000140707() { //AP/UW/ZOR/1.000/14-07-07
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
    void testAPUWOOL1001161230() { //AP/UW/OOL/1.001/16-12-30
        //given
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.put("dataUmowy", ["10-05-2012"] as String[]);

        def subscriptions = [
                ["PH", 1, 277, 378, 59, 28]
        ]

        //when
        data.putAll(insertSignatures2(subscriptions))
        data.putAll(data)

        //then
        process("APUWOOL1.00116-12-30.pdf", "APUWOOL1.00116-12-30_out.pdf", data)
    }

    @Test
    void APAGFDP2005161011() { //AP-AG/F/DP/2.005/16-10-11
        def data = [:]
        def subscriptions = [
                ["ACCEPTANT1", 1, 263, 111, 59, 28],
                ["PH", 1, 90, 65, 59, 28]
        ]
        data.putAll(insertSignatures2(subscriptions))
        process("APAGFDP2.00516-10-11.pdf", "APAGFDP2.00516-10-11_out.pdf", data);
    }

    @Test
    void APFDS2001161011() { //AP/F/DS/2.001/16-10-11
        HashMap<String, String[]> data = prepareScoringData()
        def subscriptions = [
                ["PH", 1, 90, 100, 59, 28]
        ]
        data.putAll(insertSignatures2(subscriptions))
        process("APFDS2.00116-10-11.pdf", "APFDS2.00116-10-11_out.pdf", data);
    }

    @Test
    void APFZMP3000161011() { //AP/F/ZMP/3.000/16-10-11
        def data = [:]
        def subscriptions = [
                ["PH", 1, 90, 200,	59,	28]
        ]
        data.putAll(insertSignatures2(subscriptions))
        process("APFZMP3.00016-10-11.pdf", "APFZMP3.00016-10-11_out.pdf", data);
    }

    void testAPUPZBSXToImage() {
        String outFile =  "APUPZBS2.00113-08-06 - Umowa o przyjmowanie zaplaty (wersja bez stawek plaskich)_do druku_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("APUPZBS2.00113-08-06 - Umowa o przyjmowanie zaplaty (wersja bez stawek plaskich)_do druku.pdf", outFile, data);
        processToImage(outFile, 6)
    }

    void testAPUPZIFX() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.put("dataUmowy", ["10-05-2012"] as String[]);
        data.putAll(prepareDccData())

        //NOT OK
        def subscriptions = [
                ["ACCEPTANT1", 4, 80, 335, 94, 63],
                ["ACCEPTANT2", 4, 200, 335, 94, 63],
                ["ZARZAD1", 4, 330, 335, 85, 58],
                ["ZARZAD2", 4, 460, 335, 56, 58],
                ["PH", 4, 455, 230, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))


        process("APUPZIF2.00113-08-06 - Umowa o przyjmowanie zaplaty IF+_2013.pdf", "APUPZIF2.00113-08-06 - Umowa o przyjmowanie zaplaty IF+_2013_out.pdf", data)
    }

    void testAPUPZIFXToImage() {
        String outFile =  "APUPZIF2.00113-08-06 - Umowa o przyjmowanie zaplaty IF+_2013_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("APUPZIF2.00113-08-06 - Umowa o przyjmowanie zaplaty IF+_2013.pdf", outFile, data);
        processToImage(outFile, 1)
    }

    void testAPUPZIF2X() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.put("dataUmowy", ["10-05-2012"] as String[]);
        data.putAll(prepareDccData())

        // NOT OK
        def subscriptions = [
                ["ACCEPTANT1", 2, 80, 460, 94, 63],
                ["ACCEPTANT2", 2, 200, 460, 94, 63],
                ["ZARZAD1", 2, 330, 460, 85, 58],
                ["ZARZAD2", 2, 460, 460, 56, 58],
                ["PH", 2, 450, 360, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))
        process("APUPZIF2.00213-08-06_Aneks na IF+ i IKO.pdf", "APUPZIF2.00213-08-06_Aneks na IF+ i IKO_out.pdf", data)
    }

    void testAPUPZIF2XToImage() {
        String outFile =  "APUPZIF2.00213-08-06_Aneks na IF+ i IKO_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("APUPZIF2.00213-08-06_Aneks na IF+ i IKO.pdf", outFile, data);
        processToImage(outFile, 1)
    }

    // --------------- podstawowe dokumenty

    void testAPUNTSS() { //gotowe
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);

        data.putAll(prepareZestawPosOdplatneUzywanieData())
        data.putAll(prepareAggrementsFields())

        data.put("wydrukGrafikiCena", ["53"] as String[]);
        data.put("dzialaniaMatematyczneCena", ["53"] as String[]);
        data.put("pierwszaSesjaCena", ["53"] as String[]);
        data.put("walutaObcaCena", ["53"] as String[]);
        data.put("oplataZaUruchomienieDCC", ["200"] as String []);
        data.put("mudCena", ["53"] as String[]);
        data.put("obslugaPrestiz", ["true", "", "checkbox"] as String[]);
        data.put("obslugaKomfort", ["true", "", "checkbox"] as String[]);
        data.put("obslugaEkonomiczny", ["true", "", "checkbox"] as String[]);
        data.put("obslugaEkonomicznyCena", ["153"] as String[]);
        data.putAll(preparePunktyData())

        def subscriptions = [
                ["ACCEPTANT1", 2, 80, 460, 94, 63],
                ["ACCEPTANT2", 2, 200, 460, 94, 63],
                ["ZARZAD1", 2, 330, 460, 85, 58],
                ["ZARZAD2", 2, 460, 460, 56, 58],
                ["PH", 2, 450, 360, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))

        process("APUNTSS1.00312-01-16.pdf", "APUNTSS1.00312-01-16_out.pdf", data)

    }

    void testAPUNTSSToImage() {
        String outFile =  "APUNTSS1.00312-01-16_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("APUNTSS1.00312-01-16.pdf", outFile, data);
        processToImage(outFile, 1)
    }



    void testAPUNTSZAPOO() { //gotowe
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.put("dataPoczatkuUzywaniaPOZ", ["11.02.1998"] as String[]);
        data.put("dataKoncaUzywaniaPOZ", ["11.01.1998"] as String[]);
        data.put("numerPOSA", ["10"] as String[]);
        data.put("numerPOSB", ["20"] as String[]);
        data.put("numerPOSC", ["30"] as String[]);
        data.put("numerPOSD", ["40"] as String[]);
        data.put("numerPOSE", ["50"] as String[]);

        data.put("oplataPOSA", ["50"] as String[]);
        data.put("oplataPOSB", ["50"] as String[]);
        data.put("oplataPOSC", ["50"] as String[]);
        data.put("oplataPOSD", ["50"] as String[]);
        data.put("oplataPOSE", ["50"] as String[]);
        data.putAll(prepareZestawPosOdplatneUzywanieData())
        //NOT OK
        def subscriptions = [
                ["ACCEPTANT1", 1, 85, 288, 94, 63],
                ["ACCEPTANT2", 1, 205, 288, 94, 63],
                ["ZARZAD1", 1, 335, 288, 85, 58],
                ["ZARZAD2", 1, 465, 288, 56, 58],
                ["PH", 1, 444, 163, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))

        process("APUNTSZAPOO3.00212-01-16.pdf", "APUNTSZAPOO3.00212-01-16_out.pdf", data)
    }

    void testAPUNTSZAPOO3() { //gotowe
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.put("dataPoczatkuUzywaniaPOZ", ["11.02.1998"] as String[]);
        data.put("dataKoncaUzywaniaPOZ", ["11.01.1998"] as String[]);
        data.put("numerPOSA", ["10"] as String[]);
        data.put("numerPOSB", ["20"] as String[]);
        data.put("numerPOSC", ["30"] as String[]);
        data.put("numerPOSD", ["40"] as String[]);
        data.put("numerPOSE", ["50"] as String[]);

        data.put("oplataPOSA", ["50"] as String[]);
        data.put("oplataPOSB", ["50"] as String[]);
        data.put("oplataPOSC", ["50"] as String[]);
        data.put("oplataPOSD", ["50"] as String[]);
        data.put("oplataPOSE", ["50"] as String[]);
        data.putAll(prepareZestawPosOdplatneUzywanieData())
        //NOT OK
        def subscriptions = [
                ["ACCEPTANT1", 1, 85, 196, 94, 63],
                ["ACCEPTANT2", 1, 205, 196, 94, 63],
                ["ZARZAD1", 1, 335, 196, 85, 58],
                ["ZARZAD2", 1, 465, 196, 56, 58],
                ["PH", 1, 444, 73, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))

        process("APUNTSZAPOO3.0005_13-12-16_Aneks do Umowy najmu zestawu POS (okres promocyjny).pdf", "APUNTSZAPOO3.0005_13-12-16_Aneks do Umowy najmu zestawu POS (okres promocyjny)_out.pdf", data)
    }

    void testAPUNTSZAPOOToImage() {
        String outFile =  "APUNTSZAPOO3.00212-01-16_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("APUNTSZAPOO3.00212-01-16.pdf", outFile, data);
        processToImage(outFile, 1)
    }

    // New version
    void testAPUNTSZAPOO2() { //gotowe
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.put("dataPoczatkuUzywaniaPOZ", ["11.02.1998"] as String[]);
        data.put("dataKoncaUzywaniaPOZ", ["11.01.1998"] as String[]);
        data.put("numerPOSA", ["10"] as String[]);
        data.put("numerPOSB", ["20"] as String[]);
        data.put("numerPOSC", ["30"] as String[]);
        data.put("numerPOSD", ["40"] as String[]);
        data.put("numerPOSE", ["50"] as String[]);

        data.put("oplataPOSA", ["50"] as String[]);
        data.put("oplataPOSB", ["50"] as String[]);
        data.put("oplataPOSC", ["50"] as String[]);
        data.put("oplataPOSD", ["50"] as String[]);
        data.put("oplataPOSE", ["50"] as String[]);
        data.put("okresLojalnosciowy", ["4"] as String[]);
        data.putAll(prepareZestawPosOdplatneUzywanieData())
        //NOT OK
        def subscriptions = [
                ["ACCEPTANT1", 1, 85, 200, 94, 63],
                ["ACCEPTANT2", 1, 205, 200, 94, 63],
                ["ZARZAD1", 1, 335, 200, 85, 58],
                ["ZARZAD2", 1, 465, 200, 56, 58],
                ["PH", 1, 444, 100, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))

        process("APUNTSZAPOO3.000313-09-26-Aneks do Umowy najmu zestawu POS (okres promocyjny).pdf", "APUNTSZAPOO3.000313-09-26-Aneks do Umowy najmu zestawu POS (okres promocyjny)_out.pdf", data)
    }

    void testAPUNTSZAPOO2ToImage() {
        String outFile =  "APUNTSZAPOO3.000313-09-26-Aneks do Umowy najmu zestawu POS (okres promocyjny)_out2.pdf"
        def subscriptions = [
                ["ACCEPTANT1", 1, 85, 200, 94, 63],
                ["ACCEPTANT2", 1, 205, 200, 94, 63],
                ["ZARZAD1", 1, 335, 200, 85, 58],
                ["ZARZAD2", 1, 465, 200, 56, 58],
                ["PH", 1, 444, 100, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))
        process("APUNTSZAPOO3.000313-09-26-Aneks do Umowy najmu zestawu POS (okres promocyjny).pdf", outFile, data);
        processToImage(outFile, 1)
    }


    void testAPUNTSZOOL() { //gotowe
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.put("dataPoczatkuUzywaniaPOZ", ["11.02.1998"] as String[]);
        data.put("dataKoncaUzywaniaPOZ", ["11.01.1998"] as String[]);
        data.put("numerPOSA", ["10"] as String[]);
        data.put("numerPOSB", ["20"] as String[]);
        data.put("numerPOSC", ["30"] as String[]);
        data.put("numerPOSD", ["40"] as String[]);
        data.put("numerPOSE", ["50"] as String[]);

        data.put("oplataPOSA", ["50"] as String[]);
        data.put("oplataPOSB", ["50"] as String[]);
        data.put("oplataPOSC", ["50"] as String[]);
        data.put("oplataPOSD", ["50"] as String[]);
        data.put("oplataPOSE", ["50"] as String[]);
        data.put("okresLojalnosciowy", ["4"] as String[]);
        data.putAll(prepareZestawPosOdplatneUzywanieData())
        //NOT OK
        def subscriptions = [
                ["ACCEPTANT1", 1, 85, 300, 94, 63],
                ["ACCEPTANT2", 1, 205, 300, 94, 63],
                ["ZARZAD1", 1, 335, 300, 85, 58],
                ["ZARZAD2", 1, 465, 300, 56, 58],
                ["PH", 1, 465, 173, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))

        process("APUNTSZOOL1.00013-09-26-Aneks do Umowy najmu zestawu POS (odnowienie okres lojaln.).pdf", "APUNTSZOOL1.00013-09-26-Aneks do Umowy najmu zestawu POS (odnowienie okres lojaln.).pdf", data)
    }

    void testAPUNTSZOOL2() { //gotowe
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.put("dataPoczatkuUzywaniaPOZ", ["11.02.1998"] as String[]);
        data.put("dataKoncaUzywaniaPOZ", ["11.01.1998"] as String[]);
        data.put("numerPOSA", ["10"] as String[]);
        data.put("numerPOSB", ["20"] as String[]);
        data.put("numerPOSC", ["30"] as String[]);
        data.put("numerPOSD", ["40"] as String[]);
        data.put("numerPOSE", ["50"] as String[]);

        data.put("oplataPOSA", ["50"] as String[]);
        data.put("oplataPOSB", ["50"] as String[]);
        data.put("oplataPOSC", ["50"] as String[]);
        data.put("oplataPOSD", ["50"] as String[]);
        data.put("oplataPOSE", ["50"] as String[]);
        data.put("okresLojalnosciowy", ["4"] as String[]);
        data.putAll(prepareZestawPosOdplatneUzywanieData())
        // OK
        def subscriptions = [
                ["ACCEPTANT1", 1, 85, 300, 94, 63],
                ["ACCEPTANT2", 1, 205, 300, 94, 63],
                ["ZARZAD1", 1, 335, 300, 85, 58],
                ["ZARZAD2", 1, 465, 300, 56, 58],
                ["PH", 1, 465, 173, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))

        process("APUNTSZOOL1.0002_13-12-16_Aneks do Umowy najmu zestawu POS (odnawia okres lojaln).pdf", "APUNTSZOOL1.0002_13-12-16_Aneks do Umowy najmu zestawu POS (odnawia okres lojaln).pdf", data)
    }


    void testAPUNTSZAOOLToImage() {
        String outFile =  "APUNTSZOOL1.00013-09-26-Aneks do Umowy najmu zestawu POS (odnowienie okres lojaln.)_out2.pdf"
        def subscriptions = [
                ["ACCEPTANT1", 1, 85, 300, 94, 63],
                ["ACCEPTANT2", 1, 205, 300, 94, 63],
                ["ZARZAD1", 1, 335, 300, 85, 58],
                ["ZARZAD2", 1, 465, 300, 56, 58],
                ["PH", 1, 465, 173, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))
        process("APUNTSZOOL1.00013-09-26-Aneks do Umowy najmu zestawu POS (odnowienie okres lojaln.).pdf", outFile, data);
        processToImage(outFile, 1)
    }

    void testAPUNTSZAPOU() { //gotowe
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);

        data.putAll(prepareZestawPosOdplatneUzywanieData());

        data.put("wydrukGrafikiCena", ["53"] as String[]);
        data.put("dzialaniaMatematyczneCena", ["53"] as String[]);
        data.put("systemKasowyCena", ["53"] as String[]);
        data.put("pierwszaSesjaCena", ["53"] as String[]);
        data.put("weryfikacjaPINCena", ["53"] as String[]);
        data.put("punktAkceptacjaKart1", ["To jest pełna czterdziestoczteroliterowa naz"] as String[]);
        data.put("adresAkceptacjaKart1", ["1234567890123456789012345678901234567890123456789012345"] as String[]);
        data.put("punktAkceptacjaKart2", ["Pompeczki"] as String[]);
        data.put("adresAkceptacjaKart2", ["Kaszubska 2"] as String[]);
        data.put("punktAkceptacjaKart3", ["Orencz"] as String[]);
        data.put("adresAkceptacjaKart3", ["Pomarańczowa 11"] as String[]);
        data.put("punktAkceptacjaKart4", ["Timobajl"] as String[]);
        data.put("adresAkceptacjaKart4", ["Rózowa 1"] as String[]);
        data.put("platnoscTN1", ["TAK"] as String[]);
        data.put("platnoscTN2", ["NIE"] as String[]);
        data.put("platnoscTN3", ["NIE"] as String[]);
        data.put("platnoscTN4", ["TAK"] as String[]);
        data.put("integracjaTN1", ["TAK"] as String[]);
        data.put("integracjaTN2", ["TAK"] as String[]);
        data.put("integracjaTN3", ["NIE"] as String[]);
        data.put("integracjaTN4", ["TAK"] as String[]);
        data.put("utaTN1", ["NIE"] as String[]);
        data.put("utaTN2", ["NIE"] as String[]);
        data.put("utaTN3", ["NIE"] as String[]);
        data.put("utaTN4", ["NIE"] as String[]);
//        data.put("crossAdditional", ['_______________'] as String[])
        data.put("crossNew", ['______'] as String[])
        //NOT OK
        def subscriptions = [
                ["ACCEPTANT1", 2, 85, 395, 94, 63],
                ["ACCEPTANT2", 2, 205, 395, 94, 63],
                ["ZARZAD1", 2, 335, 395, 85, 58],
                ["ZARZAD2", 2, 465, 395, 56, 58],
                ["PH", 2, 455, 275, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))

        process("APUNTSZAPOU3.00212-01-16.pdf", "APUNTSZAPOU3.00212-01-16_out.pdf", data)
    }

    void testAPUNTSZAPOUToImage() {
        String outFile =  "APUNTSZAPOU3.00212-01-16_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("APUNTSZAPOU3.00212-01-16.pdf", outFile, data);
        processToImage(outFile, 1)
    }


    void testAPUNTSZAPOU2() { //gotowe
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);

        data.putAll(prepareZestawPosOdplatneUzywanieData());

        data.put("wydrukGrafikiCena", ["53"] as String[]);
        data.put("dzialaniaMatematyczneCena", ["53"] as String[]);
        data.put("systemKasowyCena", ["53"] as String[]);
        data.put("pierwszaSesjaCena", ["53"] as String[]);
        data.put("weryfikacjaPINCena", ["53"] as String[]);
        data.put("punktAkceptacjaKart1", ["To jest pełna czterdziestoczteroliterowa naz"] as String[]);
        data.put("adresAkceptacjaKart1", ["1234567890123456789012345678901234567890123456789012345"] as String[]);
        data.put("punktAkceptacjaKart2", ["Pompeczki"] as String[]);
        data.put("adresAkceptacjaKart2", ["Kaszubska 2"] as String[]);
        data.put("punktAkceptacjaKart3", ["Orencz"] as String[]);
        data.put("adresAkceptacjaKart3", ["Pomarańczowa 11"] as String[]);
        data.put("punktAkceptacjaKart4", ["Timobajl"] as String[]);
        data.put("adresAkceptacjaKart4", ["Rózowa 1"] as String[]);
        data.put("platnoscTN1", ["TAK"] as String[]);
        data.put("platnoscTN2", ["NIE"] as String[]);
        data.put("platnoscTN3", ["NIE"] as String[]);
        data.put("platnoscTN4", ["TAK"] as String[]);
        data.put("integracjaTN1", ["TAK"] as String[]);
        data.put("integracjaTN2", ["TAK"] as String[]);
        data.put("integracjaTN3", ["NIE"] as String[]);
        data.put("integracjaTN4", ["TAK"] as String[]);
        data.put("utaTN1", ["NIE"] as String[]);
        data.put("utaTN2", ["NIE"] as String[]);
        data.put("utaTN3", ["NIE"] as String[]);
        data.put("utaTN4", ["NIE"] as String[]);
//        data.put("crossAdditional", ['_______________'] as String[])
        data.put("crossNew", ['______'] as String[])
        data.put("okresLojalnosciowy", ['3'] as String[])
        //NOT OK
        def subscriptions = [
                ["ACCEPTANT1", 2, 85, 255, 94, 63],
                ["ACCEPTANT2", 2, 205, 255, 94, 63],
                ["ZARZAD1", 2, 335, 255, 85, 58],
                ["ZARZAD2", 2, 465, 255, 56, 58],
                ["PH", 2, 455, 110, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))

        process("APUNTSZAPOU3.00313-09-26-Aneks do umowy najmu zestawu POS (rozszerz wspolpr i nowe usl).pdf", "APUNTSZAPOU3.00313-09-26-Aneks do umowy najmu zestawu POS (rozszerz wspolpr i nowe usl)_out.pdf", data)
    }


    void testAPUNTSZAPOU3() { //gotowe
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);

        data.putAll(prepareZestawPosOdplatneUzywanieData());

        data.put("wydrukGrafikiCena", ["53"] as String[]);
        data.put("dzialaniaMatematyczneCena", ["53"] as String[]);
        data.put("systemKasowyCena", ["53"] as String[]);
        data.put("pierwszaSesjaCena", ["53"] as String[]);
        data.put("weryfikacjaPINCena", ["53"] as String[]);
        data.put("punktAkceptacjaKart1", ["To jest pełna czterdziestoczteroliterowa naz"] as String[]);
        data.put("adresAkceptacjaKart1", ["1234567890123456789012345678901234567890123456789012345"] as String[]);
        data.put("punktAkceptacjaKart2", ["Pompeczki"] as String[]);
        data.put("adresAkceptacjaKart2", ["Kaszubska 2"] as String[]);
        data.put("punktAkceptacjaKart3", ["Orencz"] as String[]);
        data.put("adresAkceptacjaKart3", ["Pomarańczowa 11"] as String[]);
        data.put("punktAkceptacjaKart4", ["Timobajl"] as String[]);
        data.put("adresAkceptacjaKart4", ["Rózowa 1"] as String[]);
        data.put("platnoscTN1", ["TAK"] as String[]);
        data.put("platnoscTN2", ["NIE"] as String[]);
        data.put("platnoscTN3", ["NIE"] as String[]);
        data.put("platnoscTN4", ["TAK"] as String[]);
        data.put("integracjaTN1", ["TAK"] as String[]);
        data.put("integracjaTN2", ["TAK"] as String[]);
        data.put("integracjaTN3", ["NIE"] as String[]);
        data.put("integracjaTN4", ["TAK"] as String[]);
        data.put("utaTN1", ["NIE"] as String[]);
        data.put("utaTN2", ["NIE"] as String[]);
        data.put("utaTN3", ["NIE"] as String[]);
        data.put("utaTN4", ["NIE"] as String[]);
//        data.put("crossAdditional", ['_______________'] as String[])
        data.put("crossNew", ['______'] as String[])
        data.put("okresLojalnosciowy", ['3'] as String[])
        //NOT OK
        def subscriptions = [
                ["ACCEPTANT1", 2, 85, 215, 94, 63],
                ["ACCEPTANT2", 2, 205, 215, 94, 63],
                ["ZARZAD1", 2, 335, 215, 85, 58],
                ["ZARZAD2", 2, 465, 215, 56, 58],
                ["PH", 2, 455, 120, 54, 32]
        ]
        data.putAll(insertSignatures2(subscriptions))

        process("APUNTSZAPOU3.0005_13-12-16_Aneks do umowy najmu zestawu POS (rozszerz wspolpr i nowe usl).pdf", "APUNTSZAPOU3.0005_13-12-16_Aneks do umowy najmu zestawu POS (rozszerz wspolpr i nowe usl)_out.pdf", data)
    }


    void testAPUNTSZAPOU2ToImage() {
        String outFile =  "APUNTSZAPOU3.00313-09-26-Aneks do umowy najmu zestawu POS (rozszerz wspolpr i nowe usl)_out2.pdf"
//        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        def subscriptions = [
                ["ACCEPTANT1", 2, 85, 255, 94, 63],
                ["ACCEPTANT2", 2, 205, 255, 94, 63],
                ["ZARZAD1", 2, 335, 255, 85, 58],
                ["ZARZAD2", 2, 465, 255, 56, 58],
                ["PH", 2, 455, 110, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))
        process("APUNTSZAPOU3.00313-09-26-Aneks do umowy najmu zestawu POS (rozszerz wspolpr i nowe usl).pdf", outFile, data);
        processToImage(outFile, 2)
    }

    void testAPUNTSZAWNZ() { //gotowe
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.putAll(generateDataPodpisaniaAneksuPOZField())
        data.putAll(prepareDodatkoweUslugiData())

        data.putAll(insertSignatures(1, 85, 370, 74, 43))

        //NOT OK
        def subscriptions = [
                ["ACCEPTANT1", 1, 85, 370, 94, 63],
                ["ACCEPTANT2", 1, 205, 370, 94, 63],
                ["ZARZAD1", 1, 335, 370, 85, 58],
                ["ZARZAD2", 1, 465, 370, 56, 58],
                ["PH", 1, 457, 206, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))

        process("APUNTSZAWNZ3.00212-01-16.pdf", "APUNTSZAWNZ3.00212-01-16_out.pdf", data)
    }

    void testAPUNTSZAWNZ2() { //gotowe
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.putAll(generateDataPodpisaniaAneksuPOZField())
        data.putAll(prepareDodatkoweUslugiData())

        //data.putAll(insertSignatures(1, 85, 370, 74, 43))

        //NOT OK
        def subscriptions = [
                ["ACCEPTANT1", 1, 85, 365, 94, 63],
                ["ACCEPTANT2", 1, 205, 365, 94, 63],
                ["ZARZAD1", 1, 335, 365, 85, 58],
                ["ZARZAD2", 1, 465, 365, 56, 58],
                ["PH", 1, 457, 236, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))

        process("APUNTSZAWNZ3.00413-12-16_Aneks do Umowy najmu zestawu POS (Nowa Tabela Oplat).pdf", "APUNTSZAWNZ3.00413-12-16_Aneks do Umowy najmu zestawu POS (Nowa Tabela Oplat)_out.pdf", data)
    }

    void testAPUNTSZAWNZToImage() {
        String outFile =  "APUNTSZAWNZ3.00212-01-16_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("APUNTSZAWNZ3.00212-01-16.pdf", outFile, data);
        processToImage(outFile, 1)
    }

    void testAPUNTSZDCC() { //gotowe
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.put("walutaObcaCena", ["40"] as String[]);
        data.put("mudCena", ["510"] as String[]);
        data.put("oplataZaUruchomienieWalutyObcej", ["50"] as String[]);
//		data.putAll(insertSignatures(1, 85, 280, 74, 43))

        def subscriptions = [
                ["ACCEPTANT1", 1, 85, 280, 94, 63],
                ["ACCEPTANT2", 1, 205, 280, 94, 63],
                ["ZARZAD1", 1, 335, 280, 85, 58],
                ["ZARZAD2", 1, 465, 280, 56, 58],
                ["PH", 1, 460, 168, 84, 53],
        ]
        data.putAll(insertSignatures2(subscriptions))
        process("APUNTSZDCC2.00212-01-16.pdf", "APUNTSZDCC2.00212-01-16_out.pdf", data)
    }

    void testAPUNTSZDCC2() { //gotowe
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.put("walutaObcaCena", ["40"] as String[]);
        data.put("mudCena", ["510"] as String[]);
        data.put("oplataZaUruchomienieWalutyObcej", ["50"] as String[]);
//		data.putAll(insertSignatures(1, 85, 280, 74, 43))

        def subscriptions = [
                ["ACCEPTANT1", 1, 85, 290, 94, 63],
                ["ACCEPTANT2", 1, 205, 290, 94, 63],
                ["ZARZAD1", 1, 335, 290, 85, 58],
                ["ZARZAD2", 1, 465, 290, 56, 58],
                ["PH", 1, 460, 158, 84, 53],
        ]
        data.putAll(insertSignatures2(subscriptions))
        process("APUNTSZDCC2.00413-12-16_Aneks do Umowy najmu zestawu POS (uruchomienie DCC).pdf", "APUNTSZDCC2.00413-12-16_Aneks do Umowy najmu zestawu POS (uruchomienie DCC)_out.pdf", data)
    }

    void testAPUNTSZDCCToImage() {
        String outFile =  "APUNTSZDCC2.00212-01-16_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("APUNTSZDCC2.00212-01-16.pdf", outFile, data);
        processToImage(outFile, 1)
    }

    void testAPUNTSZDCCZ() { //gotowe
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.put("walutaObcaCena", ["40"] as String[]);
        data.put("mudCena", ["510"] as String[]);
        data.put("oplataZaUruchomienieWalutyObcej", ["50"] as String[]);
//		data.putAll(insertSignatures(1, 85, 260, 74, 43))

        def subscriptions = [
                ["ACCEPTANT1", 1, 85, 260, 94, 63],
                ["ACCEPTANT2", 1, 205, 260, 94, 63],
                ["ZARZAD1", 1, 335, 260, 85, 58],
                ["ZARZAD2", 1, 465, 260, 56, 58],
                ["PH", 1, 458, 145, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))

        process("APUNTSZDCCZ1 00112-10-05_Aneks do UN_DCC zmiana warunkow.pdf", "APUNTSZDCCZ1 00112-10-05_Aneks do UN_DCC zmiana warunkow_out.pdf", data)
    }

    void testAPUNTSZDCCZToImage() {
        String outFile =  "APUNTSZDCCZ1 00112-10-05_Aneks do UN_DCC zmiana warunkow_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("APUNTSZDCCZ1 00112-10-05_Aneks do UN_DCC zmiana warunkow.pdf", outFile, data);
        processToImage(outFile, 1)
    }

    void testAPUNTSZOKOD() { //gotowe
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.put("okresLojalnosciowy", ["3"] as String[]);

        def subscriptions = [
                ["ACCEPTANT1", 1, 85, 250, 94, 63],
                ["ACCEPTANT2", 1, 205, 250, 94, 63],
                ["ZARZAD1", 1, 335, 250, 85, 58],
                ["ZARZAD2", 1, 465, 250, 56, 58],
                ["PH", 1, 455, 125, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))
        process("APUNTSZOKOD2.00312-01-16.pdf", "APUNTSZOKOD2.00312-01-16_out.pdf", data)
    }

    void testAPUNTSZOKODToImage() {
        String outFile =  "APUNTSZOKOD2.00312-01-16_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("APUNTSZOKOD2.00312-01-16.pdf", outFile, data);
        processToImage(outFile, 1)
    }

    void testAPUNTWAGOK() { //gotowe
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
//		data.putAll(insertSignatures(1, 85, 245, 74, 43))
        def subscriptions = [
                ["ACCEPTANT1", 1, 85, 245, 94, 63],
                ["ACCEPTANT2", 1, 205, 245, 94, 63],
                ["ZARZAD1", 1, 335, 245, 85, 58],
                ["ZARZAD2", 1, 465, 245, 56, 58],
                ["PH", 1, 455, 105, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))
        process("APUNTWAGOK1.00212-01-16.pdf", "APUNTWAGOK1.00212-01-16_out.pdf", data)
    }

    void testAPUNTWAGOKToImage() {
        String outFile =  "APUNTWAGOK1.00212-01-16_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("APUNTWAGOK1.00212-01-16.pdf", outFile, data);
        processToImage(outFile, 1)
    }

    void testAPUNTWAGON() { //gotowe
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.put("czasObslugiCena", ["3"] as String[]);
        data.putAll(this.data);
//		data.putAll(insertSignatures(1, 85, 235, 74, 43))
        def subscriptions = [
                ["ACCEPTANT1", 1, 85, 235, 94, 63],
                ["ACCEPTANT2", 1, 205, 235, 94, 63],
                ["ZARZAD1", 1, 335, 235, 85, 58],
                ["ZARZAD2", 1, 465, 235, 56, 58],
                ["PH", 1, 460, 96, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))

        process("APUNTWAGON1.00212-01-16.pdf", "APUNTWAGON1.00212-01-16_out.pdf", data)
    }

    void testAPUNTWAGONToImage() {
        String outFile =  "APUNTWAGON1.00212-01-16_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("APUNTWAGON1.00212-01-16.pdf", outFile, data);
        processToImage(outFile, 1)
    }

    void testAPUNTWAGOP() { //gotowe
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);

        def subscriptions = [
                ["ACCEPTANT1", 1, 80, 245, 94, 63],
                ["ACCEPTANT2", 1, 200, 245, 94, 63],
                ["ZARZAD1", 1, 330, 245, 85, 58],
                ["ZARZAD2", 1, 460, 245, 56, 58],
                ["PH", 1, 455, 105, 84, 53],
        ]
        data.putAll(insertSignatures2(subscriptions))
        process("APUNTWAGOP1.00212-01-16.pdf", "APUNTWAGOP1.00212-01-16_out.pdf", data)
    }

    void testAPUNTWAGOPToImage() {
        String outFile =  "APUNTWAGOP1.00212-01-16_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("APUNTWAGOP1.00212-01-16.pdf", outFile, data);
        processToImage(outFile, 1)
    }

    void testAPUNTWANOD() { //gotowe
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.putAll(generateDataPodpisaniaAneksuPOZField())
//		data.putAll(insertSignatures(1, 85, 225, 74, 43))

        def subscriptions = [
                ["ACCEPTANT1", 1, 85, 225, 94, 63],
                ["ACCEPTANT2", 1, 205, 225, 94, 63],
                ["ZARZAD1", 1, 335, 225, 85, 58],
                ["ZARZAD2", 1, 465, 225, 56, 58],
                ["PH", 1, 458, 62, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))

        process("APUNTWANOD1.00312-01-16_Aneks do UN_zmiana Tabeli Oplat.pdf", "APUNTWANOD1.00312-01-16_Aneks do UN_zmiana Tabeli Oplat_out.pdf", data)
    }

    void testAPUNTWANODToImage() {
        String outFile =  "APUNTWANOD1.00312-01-16_Aneks do UN_zmiana Tabeli Oplat_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("APUNTWANOD1.00312-01-16_Aneks do UN_zmiana Tabeli Oplat.pdf", outFile, data);
        processToImage(outFile, 1)
    }

    void testAPUNTZ() { //gotowe
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        // result.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "435", "15", "74", "43"] as String[]);
        data.putAll(this.data);
        data.putAll(prepareZestawPosOdplatneUzywanieData())

        data.put("wydrukGrafikiCena", ["53"] as String[]);
        data.put("dzialaniaMatematyczneCena", ["53"] as String[]);
        data.put("pierwszaSecjaCena", ["53"] as String[]);
        data.put("walutaObcaCena", ["53"] as String[]);
        data.put("mudCena", ["53"] as String[]);
        data.put("pierwszaSesjaCena", ["53"] as String[]);
        data.put("informacjaHandlowaNie", ["true", "", "checkbox"] as String[]);
        data.put("okresLojalnosciowy", ["3"] as String[]);
        data.putAll(prepareAggrementsFields())
        data.put("obslugaPrestiz", ["true", "", "checkbox"] as String[]);
        data.put("obslugaKomfort", ["true", "", "checkbox"] as String[]);
        data.put("obslugaEkonomiczny", ["true", "", "checkbox"] as String[]);
        data.put("obslugaEkonomicznyCena", ["153"] as String[]);

        data.putAll(preparePunktyData())

        data.put("oplataZaDzienneZestawienieTransakcji", ["200"] as String[]);
        data.put("oplataZaPotwierdzenieWykonaniaPrzelewu", ["22"] as String[]);
        data.put("oplataZaDostarczeniePapieru", ["333"] as String[]);
        data.put("oplataZaMiesieczneZestawienieTransakcji", ["455"] as String[]);
        data.put("oplataZaZmianeGrafiki", ["566"] as String[]);
        data.put("oplataZaInstalacjePOS", ["6575"] as String[]);
        data.put("oplataZaInstalacjeGPRS", ["987"] as String[]);
        data.put("oplataZaUruchomienieWalutyObcej", ["1345"] as String[]);
        //data.putAll(insertSignatures(4, 70, 165, 74, 43))
        //OK
        def subscriptions = [
                ["ACCEPTANT1", 4, 70, 165, 94, 63],
                ["ACCEPTANT2", 4, 190, 165, 94, 63],
                ["ZARZAD1", 4, 320, 165, 85, 58],
                ["ZARZAD2", 4, 450, 165, 56, 58],
                ["PH", 4, 447, 30, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))

        // process("APUNTZ2.00312-01-16.pdf", "APUNTZ2.00312-01-16_out.pdf", result)
        process("APUNTZ2.00312-01-16.pdf", "APUNTZ2.00312-01-16_out.pdf", data)
    }

    void testAPUNTZ2() { //gotowe
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        // result.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "435", "15", "74", "43"] as String[]);
        data.putAll(this.data);
        data.putAll(prepareZestawPosOdplatneUzywanieData())

        data.put("wydrukGrafikiCena", ["53"] as String[]);
        data.put("dzialaniaMatematyczneCena", ["53"] as String[]);
        data.put("pierwszaSecjaCena", ["53"] as String[]);
        data.put("walutaObcaCena", ["53"] as String[]);
        data.put("mudCena", ["53"] as String[]);
        data.put("pierwszaSesjaCena", ["53"] as String[]);
        data.put("informacjaHandlowaNie", ["true", "", "checkbox"] as String[]);
        data.put("okresLojalnosciowy", ["3"] as String[]);
        data.putAll(prepareAggrementsFields())
        data.put("obslugaPrestiz", ["true", "", "checkbox"] as String[]);
        data.put("obslugaKomfort", ["true", "", "checkbox"] as String[]);
        data.put("obslugaEkonomiczny", ["true", "", "checkbox"] as String[]);
        data.put("obslugaEkonomicznyCena", ["153"] as String[]);

        data.putAll(preparePunktyData())

        data.put("oplataZaDzienneZestawienieTransakcji", ["200"] as String[]);
        data.put("oplataZaPotwierdzenieWykonaniaPrzelewu", ["22"] as String[]);
        data.put("oplataZaDostarczeniePapieru", ["333"] as String[]);
        data.put("oplataZaMiesieczneZestawienieTransakcji", ["455"] as String[]);
        data.put("oplataZaZmianeGrafiki", ["566"] as String[]);
        data.put("oplataZaInstalacjePOS", ["6575"] as String[]);
        data.put("oplataZaInstalacjeGPRS", ["987"] as String[]);
        data.put("oplataZaUruchomienieWalutyObcej", ["1345"] as String[]);
        //data.putAll(insertSignatures(4, 70, 165, 74, 43))
        //NOT OK
        def subscriptions = [
                ["ACCEPTANT1", 4, 70, 160, 94, 63],
                ["ACCEPTANT2", 4, 190, 160, 94, 63],
                ["ZARZAD1", 4, 320, 160, 85, 58],
                ["ZARZAD2", 4, 450, 160, 56, 58],
                ["PH", 4, 447, 50, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))

        // process("APUNTZ2.00312-01-16.pdf", "APUNTZ2.00312-01-16_out.pdf", result)
        process("APUNTZ2.00513-12-16_Umowa Najmu Zestawu POS_zmienna Tabela.pdf", "APUNTZ2.00513-12-16_Umowa Najmu Zestawu POS_zmienna Tabela_out.pdf", data)
    }


    void testAPUNTZToImage() {
        String outFile =  "APUNTZ2.00312-01-16.pdf_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("APUNTZ2.00312-01-16.pdf", outFile, data);
        processToImage(outFile, 1)
    }

    void testATUSUFDU() {
        // HashMap<String, String[]> result = new HashMap<String, String[]>();
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        // result.put("reprezentant1_podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "2", "435", "15", "74", "43"] as String[]);
        // result.put("reprezentant2_podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "2", "435", "15", "74", "43"] as String[]);
        data.putAll(this.data);
        data.put("doladowania_tp", ["true", "", "checkbox"] as String[]);
        data.put("doladowania_tk", ["true", "", "checkbox"] as String[]);
        data.put("srednia_sprzedaz_doladowan", ["450"] as String[]);
        data.put("srednia_sprzedaz_doladowan_slownie", ["czterysta pięćdziesiąt"] as String[]);
        data.putAll(preparePoziomOplatIWarunkiPlatnosciData())
        //data.putAll(insertSignatures(2, 80, 545, 74, 43))
        //	process("ATUSUFDU4.004.130522.pdf", "ATUSUFDU4.004.130522_out.pdf", result)

        def subscriptions = [
                ["ACCEPTANT1", 2, 80, 545, 94, 63],
                ["ACCEPTANT2", 2, 200, 545, 94, 63],
                ["ZARZAD1", 2, 330, 545, 85, 58],
                ["ZARZAD2", 2, 460, 545, 56, 58],
                ["PH", 2, 450, 395, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))
        process("ATUSUFDU4.004.130522.pdf", "ATUSUFDU4.004.130522_out.pdf", data)
    }

    void testATUSUFDU2() {
        // HashMap<String, String[]> result = new HashMap<String, String[]>();
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        // result.put("reprezentant1_podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "2", "435", "15", "74", "43"] as String[]);
        // result.put("reprezentant2_podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "2", "435", "15", "74", "43"] as String[]);
        data.putAll(this.data);
        data.put("doladowania_tp", ["true", "", "checkbox"] as String[]);
        data.put("doladowania_tk", ["true", "", "checkbox"] as String[]);
        data.put("srednia_sprzedaz_doladowan", ["450"] as String[]);
        data.put("srednia_sprzedaz_doladowan_slownie", ["czterysta pięćdziesiąt"] as String[]);
        data.putAll(preparePoziomOplatIWarunkiPlatnosciData())
        //data.putAll(insertSignatures(2, 80, 545, 74, 43))
        //	process("ATUSUFDU4.004.130522.pdf", "ATUSUFDU4.004.130522_out.pdf", result)

        //NOT OK
        def subscriptions = [
                ["ACCEPTANT1", 2, 80, 585, 94, 63],
                ["ACCEPTANT2", 2, 200, 585, 94, 63],
                ["ZARZAD1", 2, 330, 585, 85, 58],
                ["ZARZAD2", 2, 460, 585, 56, 58],
                ["PH", 2, 450, 465, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))
        process("ATUSUFDU4.00513-12-16_Aneks do Umowy PrePaid.pdf", "ATUSUFDU4.00513-12-16_Aneks do Umowy PrePaid_out.pdf", data)
    }


    void testATUSUFDUToImage() {
        String outFile =  "ATUSUFDU4.004.130522_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("ATUSUFDU4.004.130522.pdf", outFile, data);
        processToImage(outFile, 1)
    }

    void testAPUPZBSAIKO() {
        HashMap<String, String[]> result = new HashMap<String, String[]>();
        data.putAll(this.data);
//		data.putAll(insertSignatures(1, 70, 155, 74, 43))

        def subscriptions = [
                ["ACCEPTANT1", 1, 70, 155, 94, 63],
                ["ACCEPTANT2", 1, 190, 155, 94, 63],
                ["ZARZAD1", 1, 320, 155, 85, 58],
                ["ZARZAD2", 1, 450, 155, 56, 58],
                ["PH", 1, 450, 60, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))

//		result.put("reprezentant1_podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "700", "150", "74", "43"] as String[]);
        // process("APUPZBSAIKO1.00013-03-25 - Aneks IKO.pdf", "APUPZBSAIKO1.00013-03-25 - Aneks IKO_out.pdf", result)
        process("APUPZBSAIKO1.00013-03-25 - Aneks IKO.pdf", "APUPZBSAIKO1.00013-03-25 - Aneks IKO_out.pdf", data)
    }

    void testAPUPZBSAIKOToImage() {
        String outFile =  "APUPZBSAIKO1.00013-03-25 - Aneks IKO_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("APUPZBSAIKO1.00013-03-25 - Aneks IKO.pdf", outFile, data);
        processToImage(outFile, 1)
    }

    //----------------------------------------------------------------------------------------

    void testAPUPZ2ACB() {
        // brak dokumentu
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
//		data.putAll(insertSignatures(2, 80, 145, 74, 43))
//        data.putAll(insertDirectSignature(2, 445, 40, 74, 43))

        // OK
        def subscriptions = [
                ["ACCEPTANT1", 2, 80, 145, 94, 63],
                ["ACCEPTANT2", 2, 200, 145, 94, 63],
                ["ZARZAD1", 2, 330, 145, 85, 58],
                ["ZARZAD2", 2, 460, 145, 56, 58],
                ["PH", 2, 445, 40, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))
        process("APUPZ2ACB1.00013-02-15 - Aneks do Umowy o przyjm po 2013 r (wprow Cashback).pdf", "APUPZ2ACB1.00013-02-15 - Aneks do Umowy o przyjm po 2013 r (wprow Cashback)_out.pdf", data)
    }

    void testAPUPZ2ACBToImage() {
        String outFile =  "APUPZ2ACB1.00013-02-15 - Aneks do Umowy o przyjm po 2013 r (wprow Cashback)_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("APUPZ2ACB1.00013-02-15 - Aneks do Umowy o przyjm po 2013 r (wprow Cashback).pdf", outFile, data);
        processToImage(outFile, 1)
    }

    void testAPUPZ2DCC() {
        // brak dokumentu
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.putAll(prepareDccData())
        data.put("noweZestPos", ["true", "", "checkbox"] as String[]);
        //data.put("obecneZestPos", ["true", "", "checkbox"] as String[]);
        data.put("phu", ["true", "", "checkbox"] as String[]);
        data.put("punktZakresUruchomienia1", ["Kodziki"] as String[]);
        data.put("adresZakresUruchomienia1", ["Wąwozowa 7"] as String[]);
        data.put("punktZakresUruchomienia2", ["Pompeczki"] as String[]);
        data.put("adresZakresUruchomienia2", ["Kaszubska 2"] as String[]);
        data.put("pos1", ["7"] as String[]);
        data.put("pos2", ["9"] as String[]);
        //data.putAll(insertSignatures(1, 85, 215, 74, 43))

        //NOT OK
        def subscriptions = [
                ["ACCEPTANT1", 1, 85, 230, 94, 63],
                ["ACCEPTANT2", 1, 205, 230, 94, 63],
                ["ZARZAD1", 1, 335, 230, 85, 58],
                ["ZARZAD2", 1, 465, 230, 56, 58],
                ["PH", 1, 443, 97, 84, 53]
        ]

        data.putAll(insertSignatures2(subscriptions))
        process("APUPZ2DCC1.00013-02-15 - Aneks do Umowy o przyjm po 2013 r (wprow DCC).pdf", "APUPZ2DCC1.00013-02-15 - Aneks do Umowy o przyjm po 2013 r (wprow DCC)_out.pdf", data)
    }

    void testAPUPZ2DCC2() {
        // brak dokumentu
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.putAll(prepareDccData())
        data.put("noweZestPos", ["true", "", "checkbox"] as String[]);
        //data.put("obecneZestPos", ["true", "", "checkbox"] as String[]);
        data.put("phu", ["true", "", "checkbox"] as String[]);
        data.put("punktZakresUruchomienia1", ["Kodziki"] as String[]);
        data.put("adresZakresUruchomienia1", ["Wąwozowa 7"] as String[]);
        data.put("punktZakresUruchomienia2", ["Pompeczki"] as String[]);
        data.put("adresZakresUruchomienia2", ["Kaszubska 2"] as String[]);
        data.put("pos1", ["7"] as String[]);
        data.put("pos2", ["9"] as String[]);
        //data.putAll(insertSignatures(1, 85, 215, 74, 43))

        //NOT OK
        def subscriptions = [
                ["ACCEPTANT1", 1, 85, 255, 94, 63],
                ["ACCEPTANT2", 1, 205, 255, 94, 63],
                ["ZARZAD1", 1, 335, 255, 85, 58],
                ["ZARZAD2", 1, 465, 255, 56, 58],
                ["PH", 1, 443, 137, 84, 53]
        ]

        data.putAll(insertSignatures2(subscriptions))
        process("APUNTSZDCCZ1.00313-12-16_Aneks do Umowy najmu zestawu POS (zmiana warunków DCC).pdf", "APUNTSZDCCZ1.00313-12-16_Aneks do Umowy najmu zestawu POS (zmiana warunków DCC)_out.pdf", data)
    }

    void testAPUP2DCCToImage() {
        String outFile =  "APUPZ2DCC1.00013-02-15 - Aneks do Umowy o przyjm po 2013 r (wprow DCC)_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+File.separator+"subscriptions"+File.separator+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("APUPZ2DCC1.00013-02-15 - Aneks do Umowy o przyjm po 2013 r (wprow DCC).pdf", outFile, data);
        processToImage(outFile, 2)
    }

    void testAPUPZACB() {
        // brak dokumentu
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
//		data.putAll(insertSignatures(2, 85, 220, 74, 43))

        def subscriptions = [
                ["ACCEPTANT1", 2, 85, 220, 94, 63],
                ["ACCEPTANT2", 2, 205, 220, 94, 63],
                ["ZARZAD1", 2, 335, 220, 85, 58],
                ["ZARZAD2", 2, 465, 220, 56, 58],
                ["PH", 2, 445, 105, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))

        process("APUPZACB2.00313-02-15 - Aneks do Umowy o przyjm zapl (dod Cashback).pdf", "APUPZACB2.00313-02-15 - Aneks do Umowy o przyjm zapl (dod Cashback)_out.pdf", data)
    }

    void testAPUPZACBToImage() {
        String outFile =  "APUPZACB2.00313-02-15 - Aneks do Umowy o przyjm zapl (dod Cashback)_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("APUPZACB2.00313-02-15 - Aneks do Umowy o przyjm zapl (dod Cashback).pdf", outFile, data);
        processToImage(outFile, 1)
    }

    void testAPUPZAWNZBS() {
        // brak dokumentu
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);

        data.putAll(preparePunktyData())
        data.putAll(preparePozopmOplatIWarunkiPlatnosciKartyData())

        data.putAll(insertSignatures(1, 85, 180, 74, 43))
        process("APUPZAWNZBS1.00013-01-25 - Aneks do umowy o przyjm zapl (bez stawek plaskich).pdf", "APUPZAWNZBS1.00013-01-25 - Aneks do umowy o przyjm zapl (bez stawek plaskich)_out.pdf", data)
    }

    void testAPUPZAWNZBSToImage() {
        String outFile =  "APUPZAWNZBS1.00013-01-25 - Aneks do umowy o przyjm zapl (bez stawek plaskich)_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("APUPZAWNZBS1.00013-01-25 - Aneks do umowy o przyjm zapl (bez stawek plaskich).pdf", outFile, data);
        processToImage(outFile, 1)
    }

    void testAPUPZAWNZS() {
        // brak dokumentu
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.putAll(preparePunktyData())
        data.putAll(preparePozopmOplatIWarunkiPlatnosciKartyData())
        data.putAll(insertSignatures(1, 80, 180, 74, 43))
        process("APUPZAWNZS1.00013-01-25 - Aneks do umowy o przyjm zapl (narzucone stawki plaskie).pdf", "APUPZAWNZS1.00013-01-25 - Aneks do umowy o przyjm zapl (narzucone stawki plaskie)_out.pdf", data)
    }

    void testAPUPZAWNZSToImage() {
        String outFile =  "APUPZAWNZS1.00013-01-25 - Aneks do umowy o przyjm zapl (narzucone stawki plaskie)_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("APUPZAWNZS1.00013-01-25 - Aneks do umowy o przyjm zapl (narzucone stawki plaskie).pdf", outFile, data);
        processToImage(outFile, 1)
    }

    void testAPUPZBS() {
        //brak dokumentu - stara wersja??
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.putAll(prepareAggrementsFields())
        data.put("umowaNieOzn", ["true", "", "checkbox"] as String[]);
        data.put("umowaOzn", ["true", "", "checkbox"] as String[]);
        data.put("umowaOznOd", ["06.10.2003"] as String[]);
        data.put("umowaOznDo", ["06.10.2010"] as String[]);

        data.putAll(preparePozopmOplatIWarunkiPlatnosciKartyData())
        data.putAll(preparePunktyData(30))
        data.putAll(insertSignatures(4, 80, 320, 74, 43))
        process("APUPZBS2.00013-01-25 - Umowa o przyjmowanie zaplaty (wersja bez stawek plaskich)_do druku.pdf", "APUPZBS2.00013-01-25 - Umowa o przyjmowanie zaplaty (wersja bez stawek plaskich)_do druku_out.pdf", data)
    }

    void testAPUPZBSToImage() {
        String outFile =  "APUPZBS2.00013-01-25 - Umowa o przyjmowanie zaplaty (wersja bez stawek plaskich)_do druku_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("APUPZBS2.00013-01-25 - Umowa o przyjmowanie zaplaty (wersja bez stawek plaskich)_do druku.pdf", outFile, data);
        processToImage(outFile, 1)
    }

    void testATUSU() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.putAll(prepareAggrementsFields())
        data.put("doladowania_tp", ["true", "", "checkbox"] as String[]);
        data.put("doladowania_tk", ["true", "", "checkbox"] as String[]);
        data.put("srednia_sprzedaz_doladowan", ["450"] as String[]);
        data.put("srednia_sprzedaz_doladowan_slownie", ["czterysta pięćdziesiąt"] as String[]);
        data.putAll(preparePoziomOplatIWarunkiPlatnosciData())
        data.put("akceptantNip", ["3004005003"] as String[]);
        data.put("numerRachunkuBankowegoKlienta", ["33333333333333"] as String[]);
        data.put("bankKlienta", ["33333333333333"] as String[]);
        data.put("miejsceUmowy", ["Kurniki Podlaskie"] as String[]);

        def subscriptions = [
                ["ACCEPTANT1", 3, 80, 580, 94, 63],
                ["ACCEPTANT2", 3, 200, 580, 94, 63],
                ["ZARZAD1", 3, 330, 580, 85, 58],
                ["ZARZAD2", 3, 460, 580, 56, 58],
                ["PH", 3, 450, 464, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))

        process("ATUSU5.00413-05-22.pdf", "ATUSU5.00413-05-22_out.pdf", data)
    }


    void testATUSU2() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.putAll(prepareAggrementsFields())
        data.put("doladowania_tp", ["true", "", "checkbox"] as String[]);
        data.put("doladowania_tk", ["true", "", "checkbox"] as String[]);
        data.put("srednia_sprzedaz_doladowan", ["450"] as String[]);
        data.put("srednia_sprzedaz_doladowan_slownie", ["czterysta pięćdziesiąt"] as String[]);
        data.putAll(preparePoziomOplatIWarunkiPlatnosciData())
        data.put("akceptantNip", ["3004005003"] as String[]);
        data.put("numerRachunkuBankowegoKlienta", ["33333333333333"] as String[]);
        data.put("bankKlienta", ["33333333333333"] as String[]);
        data.put("miejsceUmowy", ["Kurniki Podlaskie"] as String[]);
        //NOT OK
        def subscriptions = [
                ["ACCEPTANT1", 3, 80, 590, 94, 63],
                ["ACCEPTANT2", 3, 200, 590, 94, 63],
                ["ZARZAD1", 3, 330, 590, 85, 58],
                ["ZARZAD2", 3, 460, 590, 56, 58],
                ["PH", 3, 450, 484, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))

        process("ATUSU5.00613-12-16_Umowa w zakr sprzed i dystr do adowa  TP i TK.pdf", "ATUSU5.00613-12-16_Umowa w zakr sprzed i dystr do adowa  TP i TK_out.pdf", data)
    }


    void testATUSUToImage() {
        String outFile =  "ATUSU5.00413-05-22_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("ATUSU5.00413-05-22.pdf", outFile, data);
        processToImage(outFile, 1)
    }

    void testAPUPZIF() {
        // to chyba stary dokument jest; jeszcze nie obrobiony
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.putAll(prepareDccData())
        data.put("PKOBP", ["3"] as String[]);
        data.putAll(insertSignatures(2, 80, 460, 74, 43))
        process("APUPZIF2.00113-04-05.pdf", "APUPZIF2.00113-04-05_out.pdf", data)
    } // gotowe

    void testAPUPZIFToImage() {
        String outFile =  "APUPZIF2.00113-04-05_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("APUPZIF2.00113-04-05.pdf", outFile, data);
        processToImage(outFile, 1)
    }

    void testAPUPZIF2() {
        // to chyba stary dokument jest; jeszcze nie obrobiony
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.putAll(prepareAggrementsFields())
        data.put("umowaOzn", ["true", "", "checkbox"] as String[]);
        data.put("umowaNiezon", ["true", "", "checkbox"] as String[]);
        data.put("umowaOznOd", ["06.10.2003"] as String[]);
        data.put("umowaOznDo", ["06.10.2010"] as String[]);
        data.putAll(preparePunktyData())
        data.put("punkt6", ["AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAASSSSSD3456"] as String[]);
        data.put("adres6", ["Nieznana ulica  bo nie wiem 34A/259"] as String[]);

        data.putAll(insertSignatures(4, 80, 305, 74, 43))
        process("APUPZIF2.00013-03-26 - Umowa o przyjmowanie zaplaty IF+_2013.pdf", "APUPZIF2.00013-03-26 - Umowa o przyjmowanie zaplaty IF+_2013_out.pdf", data)
    }

    void testAPUPZIF2ToImage() {
        String outFile =  "APUPZIF2.00013-03-26 - Umowa o przyjmowanie zaplaty IF+_2013_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("APUPZIF2.00013-03-26 - Umowa o przyjmowanie zaplaty IF+_2013.pdf", outFile, data);
        processToImage(outFile, 1)
    }

    void testAPUPZDCC() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.putAll(prepareDccData())

        data.put("noweZestPos", ["true", "", "checkbox"] as String[]);
        data.put("obecneZestPos", ["true", "", "checkbox"] as String[]);
        data.put("phu", ["true", "", "checkbox"] as String[]);
        data.put("punktZakresUruchomienia1", ["Kodziki i inne teledoładowania co mają długą nazwę"] as String[]);
        data.put("adresZakresUruchomienia1", ["Wąwozowa 7"] as String[]);
        data.put("punktZakresUruchomienia2", ["Pompeczki"] as String[]);
        data.put("adresZakresUruchomienia2", ["Kaszubska 2"] as String[]);
        data.put("pos1", ["7"] as String[]);
        data.put("pos2", ["9"] as String[]);
        data.put("punkt3", ["SuperTele"] as String[]);
        data.put("adres3", ["Parówkowa"] as String[]);
        data.put("pos3", ["9999"] as String[]);
//		data.putAll(insertSignatures(1, 80, 240, 74, 43))
        //OK
        def subscriptions = [
                ["ACCEPTANT1", 1, 80, 240, 94, 63],
                ["ACCEPTANT2", 1, 200, 240, 94, 63],
                ["ZARZAD1", 1, 330, 240, 85, 58],
                ["ZARZAD2", 1, 460, 240, 56, 58],
                ["PH", 1, 440, 105, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))
        process("APUPZDCC2.00313-02-15 - Aneks do Umowy o przyjm zapl (wprow DCC).pdf", "APUPZDCC2.00313-02-15 - Aneks do Umowy o przyjm zapl (wprow DCC)_out.pdf", data)
    }

    void testAPUPZDCCToImage() {
        String outFile =  "APUPZDCC2.00313-02-15 - Aneks do Umowy o przyjm zapl (wprow DCC)_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("APUPZDCC2.00313-02-15 - Aneks do Umowy o przyjm zapl (wprow DCC).pdf", outFile, data);
        processToImage(outFile, 1)
    }

    void testAPUPZDCCZ() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.put("zalNumer", ["6"] as String[]);
        data.putAll(prepareDccData())
        def subscriptions = [
                ["ACCEPTANT1", 1, 85, 185, 94, 63],
                ["ACCEPTANT2", 1, 205, 185, 94, 63],
                ["ZARZAD1", 1, 335, 185, 85, 58],
                ["ZARZAD2", 1, 465, 185, 56, 58],
                ["PH", 1, 440, 70, 84, 53]
        ]

        data.putAll(insertSignatures2(subscriptions))
        process("APUPZDCCZ1.00213-02-15 - Aneks do Umowy o przyjm zapl. (zm. war. DCC).pdf", "APUPZDCCZ1.00213-02-15 - Aneks do Umowy o przyjm zapl. (zm. war. DCC)_out.pdf", data)
    }

    void testAPUPZDCCZToImage() {
        String outFile =  "APUPZDCCZ1.00213-02-15 - Aneks do Umowy o przyjm zapl. (zm. war. DCC)_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("APUPZDCCZ1.00213-02-15 - Aneks do Umowy o przyjm zapl. (zm. war. DCC).pdf", outFile, data);
        processToImage(outFile, 1)
    }

    void testAPUPZ() { //gotowe
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.putAll(prepareAggrementsFields())
        data.put("umowaOznOd", ["06.10.2003"] as String[]);
        data.put("umowaOznDo", ["06.10.2010"] as String[]);
        data.putAll(preparePunktyData())
        data.putAll(preparePozopmOplatIWarunkiPlatnosciKartyData())

        data.put("umowaNieOzn", ["true", "", "checkbox"] as String[]);
        data.put("umowaOzn", ["true", "", "checkbox"] as String[]);

        //NOT OK
        def subscriptions = [
                ["ACCEPTANT1", 4, 90, 308, 94, 63],
                ["ACCEPTANT2", 4, 210, 308, 94, 63],
                ["ZARZAD1", 4, 340, 308, 85, 58],
                ["ZARZAD2", 4, 470, 308, 56, 58],
                ["PH", 4, 458, 188, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))
        process("APUPZ2.00013-01-03 - Umowa o przyjmowanie zaplaty v. 2.000_z faksymile.pdf", "APUPZ2.00013-01-03 - Umowa o przyjmowanie zaplaty v. 2.000_z faksymile_out.pdf", data)
    }

    void testNr1() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.putAll(prepareAggrementsFields())
        data.put("umowaOznOd", ["06.10.2003"] as String[]);
        data.put("umowaOznDo", ["06.10.2010"] as String[]);
        data.putAll(preparePunktyData())
        data.putAll(preparePozopmOplatIWarunkiPlatnosciKartyData())

        data.put("umowaNieOzn", ["true", "", "checkbox"] as String[]);
        data.put("umowaOzn", ["true", "", "checkbox"] as String[]);

        //NOT OK
        def subscriptions = [
                ["ACCEPTANT1", 5, 90, 272, 94, 63],
                ["ACCEPTANT2", 5, 210, 272, 94, 63],
                ["ZARZAD1", 5, 340, 272, 85, 58],
                ["ZARZAD2", 5, 470, 272, 56, 58],
                ["PH", 5, 458, 152, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))
        process("APUPZT2.1000.13-12-23.pdf", "APUPZT2.1000.13-12-23_out.pdf", data)
    }


    void testNr2() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.putAll(prepareAggrementsFields())
        data.put("umowaOznOd", ["06.10.2003"] as String[]);
        data.put("umowaOznDo", ["06.10.2010"] as String[]);
        data.putAll(preparePunktyData())
        data.putAll(preparePozopmOplatIWarunkiPlatnosciKartyData())

        data.put("umowaNieOzn", ["true", "", "checkbox"] as String[]);
        data.put("umowaOzn", ["true", "", "checkbox"] as String[]);

        //NOT OK
        def subscriptions = [
                ["ACCEPTANT1", 5, 90, 242, 94, 63],
                ["ACCEPTANT2", 5, 210, 242, 94, 63],
                ["ZARZAD1", 5, 340, 242, 85, 58],
                ["ZARZAD2", 5, 470, 242, 56, 58],
                ["PH", 5, 458, 122, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))
        process("APUPZT1.1000.13-12-23.pdf", "APUPZT1.1000.13-12-23_out.pdf", data)
    }


    void testNr3() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.putAll(prepareAggrementsFields())
        data.put("umowaOznOd", ["06.10.2003"] as String[]);
        data.put("umowaOznDo", ["06.10.2010"] as String[]);
        data.putAll(preparePunktyData())
        data.putAll(preparePozopmOplatIWarunkiPlatnosciKartyData())

        data.put("umowaNieOzn", ["true", "", "checkbox"] as String[]);
        data.put("umowaOzn", ["true", "", "checkbox"] as String[]);

        //NOT OK
        def subscriptions = [
                ["ACCEPTANT1", 1, 90, 110, 94, 63],
                ["ACCEPTANT2", 1, 210, 110, 94, 63],
                ["ZARZAD1", 1, 340, 110, 85, 58],
                ["ZARZAD2", 1, 470, 110, 56, 58],
                ["PH", 1, 447, 2, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))
        process("APUPZT12IF.1000.13-12-23.pdf", "APUPZT12IF.1000.13-12-23_out.pdf", data)
    }


    void testNr4() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.putAll(prepareAggrementsFields())
        data.put("umowaOznOd", ["06.10.2003"] as String[]);
        data.put("umowaOznDo", ["06.10.2010"] as String[]);
        data.putAll(preparePunktyData())
        data.putAll(preparePozopmOplatIWarunkiPlatnosciKartyData())

        data.put("umowaNieOzn", ["true", "", "checkbox"] as String[]);
        data.put("umowaOzn", ["true", "", "checkbox"] as String[]);

        //NOT OK
        def subscriptions = [
                ["ACCEPTANT1", 2, 90, 130, 94, 63],
                ["ACCEPTANT2", 2, 210, 130, 94, 63],
                ["ZARZAD1", 2, 340, 130, 85, 58],
                ["ZARZAD2", 2, 470, 130, 56, 58],
                ["PH", 2, 447, 42, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))
        process("APUPZT12ACB.1000.13-12-23.pdf", "APUPZT12ACB.1000.13-12-23_out.pdf", data)
    }


    void testNr5() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.putAll(prepareAggrementsFields())
        data.put("umowaOznOd", ["06.10.2003"] as String[]);
        data.put("umowaOznDo", ["06.10.2010"] as String[]);
        data.putAll(preparePunktyData())
        data.putAll(preparePozopmOplatIWarunkiPlatnosciKartyData())

        data.put("umowaNieOzn", ["true", "", "checkbox"] as String[]);
        data.put("umowaOzn", ["true", "", "checkbox"] as String[]);

        //NOT OK
        def subscriptions = [
                ["ACCEPTANT1", 1, 90, 155, 94, 63],
                ["ACCEPTANT2", 1, 210, 155, 94, 63],
                ["ZARZAD1", 1, 340, 155, 85, 58],
                ["ZARZAD2", 1, 470, 155, 56, 58],
                ["PH", 1, 447, 62, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))
        process("APUPZT12AWNZ1.1000.13-12-23.pdf", "APUPZT12AWNZ1.1000.13-12-23_out.pdf", data)
    }

    void testNr6() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.putAll(prepareAggrementsFields())
        data.put("umowaOznOd", ["06.10.2003"] as String[]);
        data.put("umowaOznDo", ["06.10.2010"] as String[]);
        data.putAll(preparePunktyData())
        data.putAll(preparePozopmOplatIWarunkiPlatnosciKartyData())

        data.put("umowaNieOzn", ["true", "", "checkbox"] as String[]);
        data.put("umowaOzn", ["true", "", "checkbox"] as String[]);

        //NOT OK
        def subscriptions = [
                ["ACCEPTANT1", 1, 90, 185, 94, 63],
                ["ACCEPTANT2", 1, 210, 185, 94, 63],
                ["ZARZAD1", 1, 340, 185, 85, 58],
                ["ZARZAD2", 1, 470, 185, 56, 58],
                ["PH", 1, 447, 92, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))
        process("APUPZT12DCC.1000.13-12-23.pdf", "APUPZT12DCC.1000.13-12-23_out.pdf", data)
    }

    void testNr7() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        data.putAll(prepareAggrementsFields())
        data.put("umowaOznOd", ["06.10.2003"] as String[]);
        data.put("umowaOznDo", ["06.10.2010"] as String[]);
        data.putAll(preparePunktyData())
        data.putAll(preparePozopmOplatIWarunkiPlatnosciKartyData())

        data.put("umowaNieOzn", ["true", "", "checkbox"] as String[]);
        data.put("umowaOzn", ["true", "", "checkbox"] as String[]);

        //NOT OK
        def subscriptions = [
                ["ACCEPTANT1", 1, 90, 162, 94, 63],
                ["ACCEPTANT2", 1, 210, 162, 94, 63],
                ["ZARZAD1", 1, 340, 162, 85, 58],
                ["ZARZAD2", 1, 470, 162, 56, 58],
                ["PH", 1, 447, 72, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))
        process("APUPZT12DCCZ.1000.13-12-23.pdf", "APUPZT12DCCZ.1000.13-12-23_out.pdf", data)
    }

    void testAPUPZT1() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        //NOT OK
        def subscriptions = [
                ["ACCEPTANT1", 5, 90, 195, 94, 63],
                ["ACCEPTANT2", 5, 210, 195, 94, 63],
                ["ZARZAD1", 5, 340, 195, 85, 58],
                ["ZARZAD2", 5, 470, 195, 56, 58],
                ["PH", 5, 458, 72, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))
        process("APUPZT1.1001.14-01-31.pdf", "APUPZT1.1001.14-01-31_out.pdf", data)
    }

    void testAPUPZT2() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
        def subscriptions = [
                ["ACCEPTANT1", 5, 90, 195, 94, 63],
                ["ACCEPTANT2", 5, 210, 195, 94, 63],
                ["ZARZAD1", 5, 340, 195, 85, 58],
                ["ZARZAD2", 5, 470, 195, 56, 58],
                ["PH", 5, 458, 72, 84, 53]
        ]
        data.putAll(insertSignatures2(subscriptions))
        process("APUPZT2.1001.14-01-31.pdf", "APUPZT2.1001.14-01-31_out.pdf", data)
    }

    void testAPUPZToImage() {
        String outFile =  "APUPZ2.00013-01-03 - Umowa o przyjmowanie zaplaty v. 2.000_z faksymile_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("APUPZ2.00013-01-03 - Umowa o przyjmowanie zaplaty v. 2.000_z faksymile.pdf", outFile, data);
        processToImage(outFile, 1)
    }


    // ---------------------------------------------------------------------------------------
    void testFormularzAplikacyjny() {
        HashMap<String, String[]> data = generateFormularzAplikacyjnyFields();
        process("Formularz aplikacyjny_po_zmianach_18.01.2012.pdf", "Formularz aplikacyjny_po_zmianach_18.01.2012_out.pdf", data);
    }

    void testFormularzAplikacyjnyToImage() {
        String outFile =  "Formularz aplikacyjny_po_zmianach_18.01.2012_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("Formularz aplikacyjny_po_zmianach_18.01.2012.pdf", outFile, data);
        processToImage(outFile, 1)
    }

    void testAPFZMP() {
        def data = [:]
        def subscriptions = [
                ["PH", 1, 50, 175,	84,	53]
        ]
        data.putAll(insertSignatures2(subscriptions))
        process("APFZMP2.000.08-12-03.pdf", "APFZMP2.000.08-12-03_out.pdf", data);
    }

    void testFormularzDanychPunktuToImage() {
        String outFile =  "Formularz danych punktu_zmiany_15.05.2013_edited_out2.pdf"
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"subscriptions"+File.separator+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("Formularz danych punktu_zmiany_15.05.2013_edited.pdf", outFile, data);
        processToImage(outFile, 2)
    }

    void testFormularzScoringowy() {
        HashMap<String, String[]> data = prepareScoringData()
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"subscriptions"+File.separator+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("Formularz Scoringowy (oryginal).pdf", "Formularz Scoringowy (oryginal)_out.pdf", data);
    }

    void testFormularzScoringowyToImage() {
        String outFile =  "Formularz Scoringowy (oryginal)_out2.pdf"
        HashMap<String, String[]> data = prepareScoringData()
        data.put("podpis", [new File(PdfHelper.getTemplatePath()+"subscriptions"+File.separator+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("Formularz Scoringowy (oryginal).pdf", outFile, data);
        processToImage(outFile, 1)
    }

    void processToImage(pdfName, pageNumber) {
        log.info('processToImage - start')
        PDDocument document = null
        document = PDDocument.load(PdfHelper.fileTemplateOutPath+pdfName)

        int resolution = 100

        if (!success) {
            log.error "No writer found for PNG image format"
        }

        document.close()
        log.info('processToImage - stop')
    }

    void process(templateName, outName, data){
        byte[] pdf = service.fillPdfFormFromURI(PdfHelper.getTemplatePath()+templateName, data, PdfGenerator.FontType.ARIAL)

        assert pdf != null

        new File(PdfHelper.fileTemplateOutPath).mkdirs()

        new File(PdfHelper.fileTemplateOutPath + outName).withOutputStream {
            it.write pdf
        }

        println 'Writing pdf to: ' + PdfHelper.fileTemplateOutPath+outName
    }


    private HashMap<String, String[]> insertSignatures(int pageNo, int x, int y, int scaleX, int scaleY){
        HashMap<String, String[]> result = new HashMap<String, String[]>();
        result.put("reprezentant1_podpis", [new File(PdfHelper.getTemplatePath()+File.separator+"subscriptions"+File.separator+"signature1.jpg").toURI().toURL(), "", "signature", pageNo, x, y, scaleX, scaleY] as String[]);
        result.put("reprezentant2_podpis", [new File(PdfHelper.getTemplatePath()+File.separator+"subscriptions"+File.separator+"signature2.jpg").toURI().toURL(), "", "signature", pageNo, x+120, y, scaleX, scaleY] as String[]);
        result.put("zarzad1_podpis", [new File(PdfHelper.getTemplatePath()+File.separator+"subscriptions"+File.separator+"signature1.jpg").toURI().toURL(), "", "signature", pageNo, x+250, y, BOARD_MEMBER_1_X, BOARD_MEMBER_1_Y] as String[]);
        result.put("zarzad2_podpis", [new File(PdfHelper.getTemplatePath()+File.separator+"subscriptions"+File.separator+"signature3.jpg").toURI().toURL(), "", "signature", pageNo, x+380, y, BOARD_MEMBER_2_X, BOARD_MEMBER_2_Y] as String[]);
        return result;
    }

    private HashMap<String, String[]> insertSignatures2(def signatures){
        HashMap<String, String[]> result = new HashMap<String, String[]>();

        signatures.each{
            def person = it.get(0);
            def pageNo = it.get(1);
            def x = it.get(2);
            def y = it.get(3);
            def scaleX = it.get(4);
            def scaleY = it.get(5);

            result.put(person, [new File(PdfHelper.getTemplatePath()+File.separator+"subscriptions"+File.separator+"signature1.jpg").toURI().toURL(), "", "signature", pageNo, x, y, scaleX, scaleY] as String[])
        }
        return result;
    }
}