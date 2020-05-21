package com.eservice.eumowy.pdfmapper

import com.eservice.eumowy.enums.options.Disposition
import com.eservice.eumowy.enums.options.LegalForm

import static com.eservice.eumowy.ActivityHelper.*
import com.eservice.eumowy.HirePayment

import java.text.DecimalFormat
import java.text.NumberFormat
import com.eservice.eumowy.Process


class PdfProcessMapper extends AbstractPdfMapper{

    def calculatorService
    def calc
    PdfPointMapper pointMapper
    PdfPosMapper posMapper
    Process processInstance
    Map dataMap = [:]
	
	private static final EMPTY_VALUES = ["", "-"]

    public PdfProcessMapper (Process processInstance, def calculatorService, def calc){
        this.calculatorService = calculatorService
        this.calc = calc
        this.processInstance = processInstance

        pointMapper = new PdfPointMapper()
        posMapper = new PdfPosMapper()
    }

    protected def mapOnlyProcessData(){
        dataMap.putAll(mapProcessToPDFData())
        dataMap.putAll(mapProcessDataToPDFData(processInstance.processData))
        dataMap.put("siedzibaAkceptanta", [getAdresAkceptanta(processInstance)] as String[])

        def points = processInstance?.points
        LOG.info("Ilosc punktow: " + points?.size())

        if (points != null && points.size()>0){
            //APUPZDCC2, APUPZ2DC1
            if ("wskazane".equals(getFromProcessDataSet(processInstance?.processData, "dccZakresUruchomienia"))){
                dataMap.putAll(pointMapper.mapPointsSpecial(points.findAll{ point -> (point != null && point.czyWybranyZakresUruchomienia)}, ["nazwa":"punktZakresUruchomienia", "miejscowosc":"adresZakresUruchomienia", "liczbaPos":"pos"]));
            }

            //APUPZIF2, APUPZ2, APUPZBS2

            def sum = [];
            //Robi sie coraz wiekszy sajgon, w przypadku wymiany umowy przenosimy punkty stare...
            sum.addAll(points.findAll{ point -> !point?.isLocal() && point?.czyWybranyWymianaUmowy})
            //...a w przypadku nowej umowy punkty nowe
            sum.addAll(points.findAll{ point -> point?.isLocal() || point?.posDatas?.any{ pos -> pos?.isLocal()}})

            dataMap.putAll(pointMapper.mapPointsSpecial(sum,  ["nazwa":"punktAkceptacjaKart", "miejscowosc":"adresAkceptacjaKart"]));

            dataMap.putAll(pointMapper.mapPointsSpecial(points.findAll{ point -> point?.isLocal()},
                    ["nazwa":"punkt", "miejscowosc":"adres"]));

            dataMap.putAll(pointMapper.mapPointsSpecial(points.findAll{ point -> point?.czyWybranyAkceptacjaKart},
                    ["nazwa":"punktTN", "miejscowosc":"adresTN", "systemKasowy":"integracjaTN", "uta":"utaTN"]));


    //----- sumowanie oplat z posow i hire payments - start
            def normalResult = posMapper.mapPosesNotFromCBD(findPosesNotFromCbd(points))

            def normalMapFromProcess = [:]
            if (processInstance.processData.find{"isOdplatneUzywanieShown".equals(it.name) && "tak".equals(it.value)}){
                normalMapFromProcess = mapOdplatneUzywanie()
            }

            def finalResult = [:]
            normalResult?.each {
                finalResult.put(it.key, it.value);
            }

            normalMapFromProcess?.each {
                def price = it.key;
                def count = it.value;

                if (finalResult.containsKey(price)){
                    finalResult.put(price, finalResult.get(price) + count)
                } else {
                    finalResult.put(price, count)
                }
            }

            mapPosesPayments(finalResult)
        } else {
            if (processInstance.processData.find{"isOdplatneUzywanieShown".equals(it.name) && "tak".equals(it.value)}) {
                mapPosesPayments(mapOdplatneUzywanie())
            }
        }

        def newToCross = ['dodatkowyPunkt', 'dodatkowyPos']
        def additonalToCross = ['aneks', 'wymianaUmowyNajmu']

        if (processInstance.activities.any { activity -> additonalToCross.contains(activity.code)}) {
            dataMap.put("crossAdditional", ['_______________'] as String[])
        } else if (processInstance.activities.any { activity -> newToCross.contains(activity.code)}) {
            dataMap.put("crossNew", ['_____'] as String[])
        }

        setAttachmentsNames()

        setFirstAttachmentCheckbox()

        setSecondAttachmentCheckbox()

        setTransactionsCheckboxes()

        setServicesData()

        setDistributionDetails()

        return dataMap;
    }

    private void mapPosesPayments(LinkedHashMap poses) {
        List<String> suffixes = ['A', 'B', 'C', 'D', 'E']

        poses.eachWithIndex { def entry, int i ->
            if (i < suffixes.size()) {
                dataMap.put('oplatyPOSIlosc' + suffixes.get(i), [entry.value.toString()] as String[])
                dataMap.put('oplatyPOSCena' + suffixes.get(i), [entry.key.toString()] as String[])
            }
        }
    }

    private void setAttachmentsNames() {
        if (hasCombination(processInstance, ['nowaUmowa', 'wymianaUmowyNajmu'], ['dodaniePrepaid', 'zmianaWarunkowPrepaid'])) {
            dataMap.put("zalacznikNr4",
                    ["4 - Załącznik do Umowy Współpracy w zakresie sprzedaży i dystrybucji elektronicznych doładowań"] as String[])
        }

        if (hasCombination(processInstance, ['nowaUmowa', 'wymianaUmowyNajmu'], ['promocyjneObnizenieNajmu'])) {
            int attachmentNumber = dataMap.containsKey("zalacznikNr4") ? 5 : 4
            dataMap.put("zalacznikNr" + attachmentNumber,
                    [attachmentNumber + " - Załącznik do Umowy Współpracy w zakresie sprzedaży i dystrybucji elektronicznych doładowań"] as String[])
        }
    }

    private void setFirstAttachmentCheckbox() {
        if(hasAtLeastOne(processInstance, ["dodatkowyPunkt", "dodatkowyPos"])) {
            dataMap.put("dodatkowyZalacznik1", checkedCheckbox)
        }

        if(contains(processInstance, "aneks")) {
            dataMap.put("nowyZalacznik1", checkedCheckbox)
        }
    }

    private void setSecondAttachmentCheckbox() {
        if(hasNoCombination(processInstance, ["dodanieDcc"], ["logoKalkulatorSesja", "ekonomiczny", "komfort", "prestiz"]) ||
            isOnlyActivity(processInstance, "logoKalkulatorSesja") ||
            isOnlyActivity(processInstance, "ekonomiczny") ||
            isOnlyActivity(processInstance, "komfort") ||
            isOnlyActivity(processInstance, "prestiz"))
        {
            dataMap.put("dodatkowyZalacznik2", checkedCheckbox)
            return
        }

        if(hasNoCombination(processInstance, ["zmianaWarunkowDcc"], ["logoKalkulatorSesja", "ekonomiczny", "komfort", "prestiz"]) ||
            hasCombination(processInstance, ["logoKalkulatorSesja"], ["dodanieDcc", "zmianaWarunkowDcc"]) ||
            hasCombination(processInstance, ["logoKalkulatorSesja"], ["ekonomiczny", "komfort", "prestiz"]) ||
            hasCombination(processInstance, ["ekonomiczny"], ["dodanieDcc", "zmianaWarunkowDcc", "logoKalkulatorSesja"]) ||
            hasCombination(processInstance, ["komfort"], ["dodanieDcc", "zmianaWarunkowDcc", "logoKalkulatorSesja"]) ||
            hasCombination(processInstance, ["prestiz"], ["dodanieDcc", "zmianaWarunkowDcc", "logoKalkulatorSesja"]))
        {
            dataMap.put("nowyZalacznik2", checkedCheckbox)
        }
    }

    private void setTransactionsCheckboxes() {
        if (hasAtLeastOne(processInstance, ["dodanieDcc", "zmianaWarunkowDcc"])) {
            dataMap.put("dccTransaction", checkedCheckbox)
        }
        if (hasAtLeastOne(processInstance, ["dodanieCashBack", "zmianaWarunkowCashback"])) {
            dataMap.put("cashbackTransaction", checkedCheckbox)
        }
        if (hasAtLeastOne(processInstance, ["dodanieAneksuKosztyPlus", "zmianaProwizji", "nowaUmowa", "wymianaUmowyZaplaty"])) {
            dataMap.put("paymentTransaction", checkedCheckbox)
        }
    }

    private void setServicesData() {
        //TODO: zamiast tych elsow zrobic adnotacje w ProcessCommand z defaultMappperValue or smth

        if(processInstance.hasData("wydrukGrafikiCena")) {
            dataMap.put("czyUslugaLogo", checkedCheckbox)
        } else {
            dataMap.put("wydrukGrafikiCena", getPdfValue("-"))
        }

        if(processInstance.hasData("dzialaniaMatematyczneCena")) {
            dataMap.put("czyUslugaKalkulator", checkedCheckbox)
        } else {
            dataMap.put("dzialaniaMatematyczneCena", getPdfValue("-"))
        }

        if(processInstance.hasData("mudCena")) {
            dataMap.put("czyUslugaMUD", checkedCheckbox)
        } else {
            dataMap.put("mudCena", getPdfValue("-"))
        }

        if(processInstance.hasData("oplataZaPlatnoscWInnejWalucie") || processInstance.hasData("oplataZaUruchomienieDCC")) {
            dataMap.put("czyUslugaDcc", checkedCheckbox)
        } else {
            dataMap.put("oplataZaPlatnoscWInnejWalucie", getPdfValue("-"))
            dataMap.put("oplataZaUruchomienieDCC", getPdfValue("-"))
        }

        if(contains(processInstance, "prestiz")) {
            dataMap.put("obslugaPrestiz", checkedCheckbox)
        }
        if(contains(processInstance, "komfort")) {
            dataMap.put("obslugaKomfort", checkedCheckbox)
        }
        if(contains(processInstance, "ekonomiczny") || processInstance.hasData("obslugaEkonomicznyCena")) {
            dataMap.put("obslugaEkonomiczny", checkedCheckbox)
        }
    }

    private void setDistributionDetails() {
        String value = processInstance.getData("dyspozycja")
        String dispositionFieldName

        if(!value) return;

        Disposition.values().each { disposition ->
            if(!value.equals(disposition.name())) {
                dispositionFieldName = String.format("dyspozycja%s", disposition.name())
                dataMap.put(dispositionFieldName, ['_______________'] as String[])
            }
        }
    }

    private def findPosesNotFromCbd(def points){
        def posesNotFromCBD = []
        points.findAll { point ->
            if(point.posDatas){
                if(point.isLocal() == true) {
                    posesNotFromCBD.addAll(point.posDatas?.findAll{ pos -> pos != null && pos?.isChildCopy() == false})
                } else {
                    posesNotFromCBD.addAll(point.posDatas?.findAll{ pos -> pos != null && pos?.isLocal() == true && pos?.isChildCopy() == false})
                }
            }
        }
        posesNotFromCBD
    }

    private def mapOdplatneUzywanie(){
        def sumMap = [:]
        def pd = processInstance.processData;

        if ('one_for_all_terminals'.equals(getFromProcessDataSet(pd, "odplatneUzywanie"))) {

            BigDecimal sum = 0
            String termPrice = getFromProcessDataSet(pd, "odpUzyTermMies");
            if (termPrice.isNumber()){
                def bd = termPrice.toBigDecimal()
                if (bd > 0){
                    sum += bd
                }
            }

            String ppPrice = getFromProcessDataSet(pd, "odpUzyPpMies");
            if (ppPrice.isNumber()){
                def bd = ppPrice.toBigDecimal()
                if (bd > 0){
                    sum += bd
                }
            }

            if (sum >0){
                sumMap.put(sum, getFromProcessDataSet(pd, "odpUzyTermSzt").toInteger())
            }
        } else if (['one_for_all_terminals_in_point', 'other_for_selected_terminals'].contains(getFromProcessDataSet(pd, "odplatneUzywanie"))){
            processInstance.hirePayments?.findAll{it.isVisible}.each{ HirePayment hp ->

                //Gdy bedzie PP trzeba dodac hp.currentPpPayment
                BigDecimal sum = hp.isChoosen ? hp.newTermPayment : hp.currentTermPayment
                Integer count = hp.termCount

                if (sum>0){
                    count+=(sumMap.containsKey(sum)?sumMap.get(sum):0)
                    sumMap.put(sum, count)
                }
            }
        }
        sumMap
    }


    private def mapProcessDataToPDFData(def processData) {
        Map<String, String[]> data = new HashMap<String, String[]>()

        processData.each { processDataItem ->
            //formatowanie procentowej wartosci platnosci karty
            if (processDataItem.name.endsWith('Pr') && !['oplataVISAPr', 'oplataMasterCardPr', 'oplataMaestroPr'].contains(processDataItem.name)){
                formatDoubleValue(data, processDataItem, '%')
                return
            }

            //formatowanie stalej wartosci platnosci karty
            if (processDataItem.name.endsWith('St')){
                formatDoubleValue(data, processDataItem, 'zł')
                return
            }

            //kwoty bez suffixu (tam, gdzie suffix jest podany w szablonie na sztywno)
            if (processDataItem.name.endsWith('Nr')) {
                formatDoubleValue(data, processDataItem)
                return
            }

            def methodName = "map" + processDataItem.name.capitalize()+"Process"
            if (isMappingMethodExists(methodName)) {
                this."${methodName}"(data, processData, processDataItem.name, processDataItem.value)
                return
            }

            if ("true".equals(processDataItem.value) == true || "false".equals(processDataItem.value) == true) {
                data.put(processDataItem.name, [processDataItem.value, "", "checkbox"] as String[])
            }else {
                data.put(processDataItem.name, [processDataItem.value] as String[])
            }
        }

        def result = data.findAll {
            def value = it.value;
            if (value != null && value.size()==1){
                if (value[0] != null && "null".equals(value[0].trim())){
                    return true
                }
            }
            return false
        }

        result.each { key, value ->
            data.remove(key)
        }

        return data
    }

    private def mapProcessToPDFData(){
        Map<String, String[]> data = new HashMap<String, String[]>()

        def phNumber = processInstance?.phNumber

        data.put("phNumer", [phNumber?.toString()] as String[])
        data.put("osobaPozyskalaAkceptantaNr", [phNumber?.toString()] as String[])
        data.put("osobaPodpisalaUmoweNr", [phNumber?.toString()] as String[])

        /* // Znaczniki do PDFow

        data.put("nrIdentyfikacjiPunktu", ['{nrPunktu}'] as String[])
        data.put("sprawaNr", ['{outletId}'] as String[])
        data.put("nrUmowy", ['{nrUmowy}'] as String[]) */

        def picm = processInstance?.client?.mid;
        data.put("mid", [picm?:''] as String[])
        data.put("nrMerchanta1", [picm?picm.toString().substring(0, 5):''] as String[])
        data.put("nrMerchanta2", [picm?picm.toString().substring(5, 10):''] as String[])
        data.put("nrMerchanta3", [picm?picm.toString().substring(10, 12):''] as String[])
        data.put("nrMerchanta4", [picm?picm.toString().substring(12, 14):''] as String[])

        return data
    }

    private mapNumerRachunkuBankowegoKlientaProcess(def data, def pd, def key, def value){
        data.put(key, [value?.replaceAll(" ","")] as String[]);
    }

    private mapAkceptantNrMieszkaniaProcess(def data, def pd, def key, def value){
        data.put(key, [value] as String[]);
        data.put("akceptantNrLokalu", [value] as String[])
    }

    private mapLiczbaMiesZwolNaj1Process(def data, def pd, def key, def value){
        if (value && value.isInteger()){
            data.put('oplatyPOSMiesiacNaliczania', [String.valueOf(value.toInteger()+1)] as String[])
            LOG.info "Mapping " + key + " with value " + value + " => setting " + String.valueOf(value.toInteger()+1)
        } else {
            data.put('oplatyPOSMiesiacNaliczania', ["1"] as String[])
            LOG.info "Mapping " + key + " with value " + value + " => setting 1"
        }
    }

    private mapAkceptantTelKomorkowyProcess(def data, def pd, def key, def value){
        data.put(key, [value] as String[]);
        mapWithPattern(data, value, ~/\d{3}-\d{3}-\d{3}/, "-", "telKomorkowy");
    }

    private mapAkceptantTelStacjonarnyProcess(def data, def pd, def key, def value){
        data.put(key, [value] as String[]);
        mapFaxOrPhone(key, data, value, "kierunkowyStacjonarny", "telStacjonarny");
    }

    private mapKontaktTelStacjonarnyProcess(def data, def pd, def key, def value){
        data.put(key, [value] as String[]);
        mapFaxOrPhone(key, data, value, "kierunkowyStacjonarnyDoKontaktu", "telStacjonarnyDoKontaktu");
    }

    private mapAkceptantFaxProcess(def data, def pd, def key, def value){
        data.put(key, [value] as String[]);
        mapFaxOrPhone(key, data, value, "kierunkowyFaks", "faks");
    }

    private mapKontaktTelKomorkowyProcess(def data, def pd, def key, def value){
        data.put(key, [value] as String[]);
        mapWithPattern(data, value, ~/\d{3}-\d{3}-\d{3}/, "-", "telKomorkowyDoKontaktu");
    }

    private mapAkceptantKodPocztowyProcess(def data, def pd, def key, def value){
        data.put(key, [value] as String[]);
        mapWithPattern(data, value, ~/\d{2}-\d{3}/, "-", "akceptantKodPocztowy");
    }

    private mapAkceptantKontaktKodPocztowyProcess(def data, def pd, def key, def value){
        data.put(key, [value] as String[]);
        mapWithPattern(data, value, ~/\d{2}-\d{3}/, "-", "akceptantKontaktKodPocztowy");
    }

    private mapAkceptantKontaktUlicaProcess(def data, def pd, def key, def value){
        data.put(key, [getFromProcessDataSet(pd, 'akceptantKontaktUlicaTytul') + " " + value] as String[]);
    }

    private mapKontaktImieProcess(def data, def pd, def key, def value) {
        data.put("imieOsobyDoKontaktu", [value] as String[])
        data.put("nazwiskoOsobyDoKontaktu", [getFromProcessDataSet(pd, 'kontaktNazwisko')] as String[])
        data.put("imieINazwiskoOsobyDoKontaktu", [value + " " + getFromProcessDataSet(pd, 'kontaktNazwisko')] as String[])
    }

    private mapKontaktTytulProcess(def data, def pd, def key, def value){
        data.put(key, [value] as String[]);
        addCheckboxes(data, ["panDoKontaktu": "Pan", "paniDoKontaktu": "Pani"], value)
    }

    private mapKontaktEmailProcess(def data, def pd, def key, def value) {
        data.put("email", [value] as String[])
    }

    private mapIfOplataPKOPBProcess(def data, def pd, def key, def value) {
        data.put(key, [value] as String[])
        data.put("ifOplataPKOBP", [value] as String[])
    }

    private mapIfOplataVISAProcess(def data, def pd, def key, def value) {
        data.put(key, [value] as String[])
        data.put("ifOplataVisa", [value] as String[])
    }

    private mapNipProcess(def data, def pd, def key, def value){
        data.put(key, [value] as String[]);
        data.put("akceptantNip", [value] as String[]);
    }

    private mapWydrukGrafikiCenaProcess(def data, def pd, def key, def value) {
        if (value != null && !EMPTY_VALUES.contains(value)) {
            data.put(key, [formatDoubleValue(value.toDouble()) + ' zł'] as String[])
        }
        mapFieldWithStartDateWithoutSettingValue(data, pd, key, value, "wydrukGrafikiData");
    }

    private mapDzialaniaMatematyczneCenaProcess(def data, def pd, def key, def value) {
        if (value == null && EMPTY_VALUES.contains(value)) {
            return
        }

        if (value == '0') {
            data.put('dzialaniaMatematyczneCenaTxt', ['ujęta w cenie najmu'] as String[])
        } else {
            data.put(key, [formatDoubleValue(value.toDouble()) + ' zł'] as String[])
        }
        mapFieldWithStartDateWithoutSettingValue(data, pd, key, value, "dzialaniaMatematyczneData");
    }

    private mapTytulPlatnosciCenaProcess(def data, def pd, def key, def value){
        //TODO co tutaj zrobic, bo to pole zostalo usuniete???
//        mapFieldWithStartDate(data, pd, key, value, "tytulPlatnosciData");
    }

    private mapSystemKasowyCenaProcess(def data, def pd, def key, def value){
        mapFieldWithStartDate(data, pd, key, value, "systemKasowyData");
    }

    private mapWeryfikacjaPINCenaProcess(def data, def pd, def key, def value){
        mapFieldWithStartDate(data, pd, key, value, "weryfikacjaPINData");
    }

    private mapObslugaEkonomicznyCenaProcess(def data, def pd, def key, def value){
        if (value == '0') {
            data.put('obslugaEkonomicznyCenaTxt', ['ujęta w cenie najmu'] as String[])
        } else {
            data.put(key, [formatDoubleValue(value.toDouble()) + ' zł'] as String[])
        }

        //pole inaczej nazywa sie na pdfach, inaczej w calej reszcie stad ponizsza linijka
        mapFieldWithStartDate(data, pd, "czasObslugiCena", value, "czasObslugiData");
    }

    private mapUmowaOznOdProcess(def data, def pd, def key, def value){
        addDateField(data, key, value);
    }

    private mapUmowaOznDoProcess(def data, def pd, def key, def value){
        addDateField(data, key, value);
    }

    private mapDataAneksowanejUmowyPosProcess(def data, def pd, def key, def value){
        addDateField(data, key, value);
    }

    private mapDataAneksowanejUmowyPrepaidProcess(def data, def pd, def key, def value){
        addDateField(data, key, value);
    }

    private mapDataUmowyProcess(def data, def pd, def key, def value){
        addDateField(data, key, value);

        def fValueList = data.get(key)
        def nValue;

        if (fValueList.size()>0){
            nValue = fValueList[0];
        }

        if (nValue != null && !"".equals(nValue)){
            def pattern = ~/\d{4}-\d{2}-\d{2}/
            if (pattern.matcher(nValue).matches()){
                final String[] split = nValue.split("-");
                data.put("data1", [split[2]] as String[]);
                data.put("data2", [split[1]] as String[]);
                data.put("data3", [split[0]] as String[]);
            }
        }
    }

    private mapScoringDochodowoscProcess(def data, def pd, def key, def value) {
        data.put("dochodowosc", [value] as String[])
    }

    private mapPozyskujacyImieProcess(def data, def pd, def key, def value){
        data.put("osobaPozyskalaAkceptanta", [value + " " + getFromProcessDataSet(pd, 'pozyskujacyNazwisko')] as String[])
        data.put("osobaPodpisalaUmowe", [value + " " + getFromProcessDataSet(pd, 'pozyskujacyNazwisko')] as String[])
    }

    private mapAkceptantUlicaProcess(def data, def pd, def key, def value) {
        data.put("akceptantUlica", [getFromProcessDataSet(pd, 'akceptantUlicaTytul') + " " + value] as String[])
        data.put("akceptantSiedziba", [(getAddress(getFromProcessDataSet(pd, 'akceptantUlicaTytul'), getFromProcessDataSet(pd, 'akceptantUlica'), getFromProcessDataSet(pd, 'akceptantNrDomu'), getFromProcessDataSet(pd, 'akceptantNrMieszkania'), getFromProcessDataSet(pd, 'akceptantKodPocztowy'), getFromProcessDataSet(pd, 'akceptantMiasto')))] as String[])
    }

    private mapAkceptantNazwaOficjalnaProcess(def data, def pd, def key, def value) {
        data.put("akceptantNazwa", [value] as String[])
        data.putAt("akceptantNazwaOficjalna", [value] as String[])
    }

    private mapScoringTypPunktuProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["centrumHandlowe":"centrum_handlowe", "pawilonyHandlowe": "pawilony_handlowe", "budynekWolnoStojacy": "budynek_wolnostojacy", "osiedleMieszkaniowe": "osiedle_mieszkaniowe","targowisko": "targowisko", "inna": "inny"], value)
    }

    private mapScoringOtwartyZamknietyProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["czynne": "czynne", "nieczynne": "nieczynne"], value)
    }

    private mapScoringLokalizacjaPunktuProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["trasaPrzelotowa": "trasa_przelotowa", "centrumMiasta": "centrum_miasta", "peryferiaMiasta": "peryferia_miasta"], value)
    }

    private mapScoringKoncesjaProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["dzialalnoscWymagaLicencjiTak": "true", "dzialalnoscWymagaLicencjiNie": "false"], value)
    }

    private mapScoringAkceptacjaProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["akceptacjaKartPlatniczychTak": "true", "akceptacjaKartPlatniczychNie": "false"], value)
    }

    private mapScoringMonitoringProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["wPunktachMonitoringTak": "true", "wPunktachMonitoringNie": "false"], value)
    }

    private mapScoringDzialalnoscProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["handel":"handel", "uslugi":"uslugi"], value)
    }

    private mapScoringWlasnoscProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["wlasnosc":"wlasnosc", "wynajem":"wynajem"], value)
    }

    private mapScoringDzialalnoscCzasProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["powyzej5lat": "5<", "od1do5lat": "1-5", "ponizejRoku": "<1"], value)
    }

    private mapScoringWielkoscPunktuProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["powyzej400m2": "400<", "od50do400m2": "50-400", "do50m2": "<50"], value)
    }

    private mapScoringWielkoscMiejscowosciProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["miastoPonad500tysChb": "500<", "miastoOd100Do500tysChb": "100-500", "miastoOd50Do90tysChb": "50-99", "miastoPonizej50tysChb": "<50", "wies": "wies"], value)
    }

    private mapScoringCharakterystykaProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["salon":"salon", "sklep":"sklep", "stoisko":"stoisko", "stacjaPaliw":"stacja_paliw", "inny":"inny"], value)
    }

    private mapScoringCzestoscTransakcjiProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["kilkaRazyWMiesiacu":"kilka_miesiecznie", "kilkaRazyWTygodniu":"kilka_tygodniowo", "coDrugiDzien":"co_drugi_dzien", "codziennie":"codziennie"], value)
    }

    private mapScoringIloscTransakcjiProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["od0do4":"0-4", "od5do10":"5-10", "powyzej10":"<10"], value)
    }

    private mapScoringSprzedazTowarowEkskluzywnychProcess(def data, def pd, def key, def value) {
        addCheckbox(data, 'sprzedazTowarowEkskluzywnych', 'true', value);
    }

    private mapScoringPonad50ProcentObrotowWNocyProcess(def data, def pd, def key, def value) {
        addCheckbox(data, 'ponad50ProcentObrotowWNocy', 'true', value);
    }

    private mapScoringRuchTurystycznyPrzygranicznyProcess(def data, def pd, def key, def value) {
        addCheckbox(data, 'ruchTurystycznyPrzygraniczny', 'true', value);
    }

    private mapScoringUslugiPlatneZGoryProcess(def data, def pd, def key, def value) {
        addCheckbox(data, 'uslugiPlatneZGory', 'true', value);
    }

    private mapScoringStanZadbanyProcess(def data, def pd, def key, def value) {
        addCheckbox(data, key, 'true', value);
    }

    private mapDccZakresUruchomieniaProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["noweZestPos":"obecne_i_nowe", "obecneZestPos":"obecne", "phu":"wskazane"], value)
    }

    private mapInformacjaHandlowaProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["informacjaHandlowaTak": "true", "informacjaHandlowaNie": "false"], value)
    }

    private mapObslugaTypProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["obslugaPrestiz": "prestige", "obslugaKomfort": "comfort", "obslugaEkonomiczny": "economic"], value)
    }

    private mapUmowaCzasProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["umowaNieOzn": "nieoznaczony", "umowaOzn": "oznaczony"], value)
    }

    private mapDzialalnoscFormaProcess(def data, def pd, def key, def value) {
        if (value == null) return

        [
                "osobaFizyczna": LegalForm.PERSON.name(),
                "spolkaZoo": LegalForm.ZOO_COMPANY.name(),
                "spolkaAkcyjna": LegalForm.STOCK_COMPANY.name(),
                "spolkaCywilna": LegalForm.PARTNERSHIP_COMPANY.name(),
                "spolkaKomandytowa": LegalForm.LIMITED_COMPANY.name(),
                "spolkaKomandytowoAkcyjna": LegalForm.LIMITED_STOCK_COMPANY.name(),
                "spolkaJawna": LegalForm.OPEN_COMPANY.name(),
                "spolkaPartnerska": LegalForm.PARTNERSHIP.name(),
                "spoldzielnia": LegalForm.COOPERATIVE.name(),
                "fundacja": LegalForm.FOUNDATION.name(),
                "zakladOpiekiZdrowotnej": LegalForm.HEALTHCARE_CENTER.name(),
                "instytut": LegalForm.INSTITUTE.name(),
                "instytucjaKultury": LegalForm.CULTURAL_INSTITUTION.name(),
                "jednostkaBudzetowa": LegalForm.BUDGETARY_UNIT.name(),
                "jednostkaOswiaty": LegalForm.EDUCATION_UNIT.name(),
                "samorzadGospodarczy": LegalForm.ECONOMIC_SELF_GOVERNMENT.name(),
        ].each {
            data.put(it.key, getCheckboxData(it.value != value))
        }
    }

    private mapDzialalnoscDokumentProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["zaswiadczenieZEwidencji": "ewidencja", "umowaSpolkiCywilnej": "umowa_spolki_cywilnej", "odpisZKRS":"krs", "inne2":"inne"], value)
        if ("inne".equals(value)){
            data.put("inneText2", [getFromProcessDataSet(pd, "dzialalnoscDokumentInny")] as String[])
        }
    }

    private def mapOplataZaUruchomienieDCCProcess(def data, def pd, def key, def value){
        if (value == null && calculatorService != null && "NIE".equals(calculatorService.getCalcProperty(calc,'CZY_DCC'))){
            data.put('oplataZaUruchomienieDCC', ['-'] as String[]);
        } else {
            data.put('oplataZaUruchomienieDCC', [formatDoubleValue(value.toDouble()) + ' zł'] as String[]);
        }
    }

    private def mapCardsOutOfEUProcess(def data, def pd, def key, def value) {
        addCheckbox(data, 'cardsOutOfEU', 'true', value == 'TAK' ? 'true' : 'false');
    }

    private def mapCardsInEUNotInPLProcess(def data, def pd, def key, def value) {
        addCheckbox(data, 'cardsInEUNotInPL', 'true', value == 'TAK' ? 'true' : 'false');
    }

    private def mapCardsInPLProcess(def data, def pd, def key, def value) {
        addCheckbox(data, 'cardsInPL', 'true', value == 'TAK' ? 'true' : 'false');
    }

    private def mapPp_orange_tkProcess(def data, def process, def key, def value){
        setUpustDlaTypuDoladowania(data, process, key, value, "doladowania_tk")
    }

    private def mapPp_plus_tkProcess(def data, def process, def key, def value){
        setUpustDlaTypuDoladowania(data, process, key, value, "doladowania_tk")
    }

    private def mapPp_tmobile_tkProcess(def data, def process, def key, def value){
        setUpustDlaTypuDoladowania(data, process, key, value, "doladowania_tk")
    }

    private def mapPp_heyah_tkProcess(def data, def process, def key, def value){
        setUpustDlaTypuDoladowania(data, process, key, value, "doladowania_tk")
    }

    private def mapPp_play_tkProcess(def data, def process, def key, def value){
        setUpustDlaTypuDoladowania(data, process, key, value, "doladowania_tk")
    }

    private def mapPp_telegrosik_tkProcess(def data, def process, def key, def value){
        setUpustDlaTypuDoladowania(data, process, key, value, "doladowania_tk")
    }

    private def mapPp_virginmobile_tkProcess(def data, def process, def key, def value){
        setUpustDlaTypuDoladowania(data, process, key, value, "doladowania_tk")
    }

    private def mapPp_lycamobile_tkProcess(def data, def process, def key, def value){
        setUpustDlaTypuDoladowania(data, process, key, value, "doladowania_tk")
    }

    private def mapPp_gtmobile_tkProcess(def data, def process, def key, def value){
        setUpustDlaTypuDoladowania(data, process, key, value, "doladowania_tk")
    }

    private def mapPp_vectonemobile_tkProcess(def data, def process, def key, def value){
        setUpustDlaTypuDoladowania(data, process, key, value, "doladowania_tk")
    }

    private def mapPp_delightmobile_tkProcess(def data, def process, def key, def value){
        setUpustDlaTypuDoladowania(data, process, key, value, "doladowania_tk")
    }

    private def mapPp_orange_tpProcess(def data, def process, def key, def value){
        setUpustDlaTypuDoladowania(data, process, key, value, "doladowania_tp")
    }

    private def mapPp_plus_tpProcess(def data, def process, def key, def value){
        setUpustDlaTypuDoladowania(data, process, key, value, "doladowania_tp")
    }

    private def mapPp_tmobile_tpProcess(def data, def process, def key, def value){
        setUpustDlaTypuDoladowania(data, process, key, value, "doladowania_tp")
    }

    private def mapPp_heyah_tpProcess(def data, def process, def key, def value){
        setUpustDlaTypuDoladowania(data, process, key, value, "doladowania_tp")
    }

    private def mapPp_play_tpProcess(def data, def process, def key, def value){
        setUpustDlaTypuDoladowania(data, process, key, value, "doladowania_tp")
    }

    private def convertToInteger(def value){
        def resultInt
        try {
            resultInt = value.toInteger();
        } catch (Exception e){
            return ["isDigit":false]
        }
        return ["isDigit":true, "value":resultInt]
    }

    private def convertToDouble(def value){
        def resultDouble
        try {
            resultDouble = value.toDouble();
        } catch (Exception e){
            return ["isDigit":false]
        }
        return ["isDigit":true, "value":resultDouble]
    }

    //--------------------------------------------------------------------------------------------------

    private def formatDoubleValue(def data, def processData, def suffix) {
        if (processData && processData.value && processData.value.isDouble()){
            data.put(processData.name, [formatDoubleValue(processData.value.toDouble()) + ' ' + suffix] as String[])
        } else if ("-".equals(processData.value)){
            data.put(processData.name, [processData.value + ' ' + suffix] as String[])
        }
    }

    private def formatDoubleValue(def data, def processData) {
        if (processData && processData.value && processData.value.isDouble()){
            data.put(processData.name, [formatDoubleValue(processData.value.toDouble())] as String[])
        } else if ("-".equals(processData.value)){
            data.put(processData.name, [processData.value] as String[])
        }
    }

    private def formatDoubleValue(def value){
        ((DecimalFormat)NumberFormat.getNumberInstance(new Locale("pl", "PL"))).format(value)
    }

    private mapFieldWithStartDate(def data, def pd, def key, def value, def dateFieldName){
        if (value !=null && !EMPTY_VALUES.contains(value)){
            data.put(key, [value] as String[])
            addDateField(data, dateFieldName, getFromProcessDataSet(pd, "dataUmowy"));
        }
    }

    private mapFieldWithStartDateWithoutSettingValue(def data, def pd, def key, def value, def dateFieldName){
        if (value !=null && !EMPTY_VALUES.contains(value)){
            addDateField(data, dateFieldName, getFromProcessDataSet(pd, "dataUmowy"));
        }
    }

    private getFromProcessDataSet(def processDataSet, def key){
        def result = processDataSet.find{ processData -> processData.name.equals(key)}
        (result && result?.value) ? result?.value:""
    }

    private Boolean isMappingMethodExists(String methodName) {
        return PdfProcessMapper.metaClass.respondsTo(this, methodName)
    }

    private void setUpustDlaTypuDoladowania(def data, def process, def key, def value, def typDoladownaia) {
        if(getFromProcessDataSet(process, typDoladownaia).equals("true")){
            data.put(key, [value] as String[]);
        } else {
            data.put(key, ['-'] as String[]);
        }
    }

}
