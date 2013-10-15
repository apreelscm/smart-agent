package com.eservice.eumowy.pdfmapper

import java.text.DecimalFormat
import java.text.NumberFormat

class PdfProcessMapper extends AbstractPdfMapper{

    def calculatorService
    def calc
    def pointMapper

    public PdfProcessMapper (def calculatorService, def calc, def pointMapper){
        this.calculatorService = calculatorService
        this.calc = calc
        this.pointMapper = pointMapper
    }

    protected def mapOnlyProcessData(def processInstance){
        def dataMap = [:]
        dataMap.putAll(mapProcessToPDFData(processInstance))
        dataMap.putAll(mapProcessDataToPDFData(processInstance.processData))

        def points = processInstance?.points;
        println "Ilosc punktow: " + points?.size()

        if (points != null && points.size()>0){
            //APUNTSS, APUNTZ2
            dataMap.putAll(pointMapper.mapPointsSpecial(points.findAll{ point -> (point.czyWybranyAkceptacjaKart && point.tytulPlatnosci)}, ["nazwa":"punktTytulPlatnosci", "miejscowosc":"adresTytulPlatnosci"]));

            //APUPZDCC2, APUPZ2DC1
            dataMap.putAll(pointMapper.mapPointsSpecial(points.findAll{ point -> (point.czyWybranyAkceptacjaKart && point.czyWybranyZakresUruchomienia)}, ["nazwa":"punktZakresUruchomienia", "miejscowosc":"adresZakresUruchomienia"]));

            //APUPZIF2, APUPZ2, APUPZBS2
            dataMap.putAll(pointMapper.mapPointsSpecial(points.findAll{ point -> point.cbdId == null || (point.posDatas && point.posDatas.findAll{ pos -> pos.tpsId == null}.size()>0)}, ["nazwa":"punktAkceptacjaKart", "miejscowosc":"adresAkceptacjaKart"]));

            //APUPZAWNZBS1, APUPZAWNZS1
            dataMap.putAll(pointMapper.mapPointsSpecial(points.findAll{ point -> point.cbdId == null || (point.posDatas && point.posDatas.findAll{ pos -> pos.tpsId == null}.size()>0)}, ["nazwa":"punkt", "miejscowosc":"adres"]));

            //APUNTSZAPOU3
            dataMap.putAll(pointMapper.mapPointsSpecial(points.findAll{ point -> point.czyWybranyAkceptacjaKart}, ["nazwa":"punktTN", "miejscowosc":"adresTN", "tytulPlatnosci":"platnoscTN", "systemKasowy":"integracjaTN", "uta":"utaTN"]));
        }

        return dataMap;
    }

    private def mapProcessDataToPDFData(def process) {
        Map<String, String[]> data = new HashMap<String, String[]>()

        process.each { processData ->
            //formatowanie procentowej wartosci platnosci karty
            if (processData.name.endsWith('Pr') && !['oplataVISAPr', 'oplataMasterCardPr', 'oplataMaestroPr'].contains(processData.name)){
                formatDoubleValue(data, processData, '%')
                return
            }

            //formatowanie stalej wartosci platnosci karty
            if (processData.name.endsWith('St')){
                formatDoubleValue(data, processData, 'zł')
                return
            }

            def methodName = "map" + processData.name.capitalize()+"Process"
            if (PdfProcessMapper.metaClass.respondsTo(this, methodName)) {
                this."${methodName}"(data, process, processData.name, processData.value)
                return
            }

            if ("true".equals(processData.value) == true || "false".equals(processData.value) == true) {
                data.put(processData.name, [processData.value, "", "checkbox"] as String[])
            }else {
                data.put(processData.name, [processData.value] as String[])
            }
        }

        data.each { key, value ->
            LOG.info "Mapping < " + key + " : " + value + " >"
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

    private def mapProcessToPDFData(def processInstance){
        Map<String, String[]> data = new HashMap<String, String[]>()

        def phNumber = processInstance?.phNumber

        data.put("phNumer", [phNumber?.toString()] as String[])
        data.put("osobaPozyskalaAkceptantaNr", [phNumber?.toString()] as String[])
        data.put("osobaPodpisalaUmoweNr", [phNumber?.toString()] as String[])
        //to na jakis formularz jest
        if (phNumber != null && phNumber?.toString().size()==5){
            data.put("NrSprzedazowyPH1", [phNumber?.toString().substring(0, 3)] as String[])
            data.put("NrSprzedazowyPH2", [phNumber?.toString().substring(3, 4)] as String[])
        } else {
            data.put("NrSprzedazowyPH1", [phNumber?.toString()] as String[])
        }

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

    private mapKontaktImieProcess(def data, def pd, def key, def value) {
        data.put("imieINazwiskoOsobyDoKontaktu", [value + " " + getFromProcessDataSet(pd, 'kontaktNazwisko')] as String[])
    }

    private mapKontaktTytulProcess(def data, def pd, def key, def value){
        data.put(key, [value] as String[]);
        addCheckboxes(data, ["panDoKontaktu": "Pan", "paniDoKontaktu": "Pani"], value)
    }

    private mapReprezentant1TytulProcess(def data, def pd, def key, def value){
        data.put(key, [value] as String[]);
        addCheckboxes(data, ["pan1": "Pan", "pani1": "Pani"], value)
    }

    private mapReprezentant2TytulProcess(def data, def pd, def key, def value){
        if ((getFromProcessDataSet(pd, 'reprezentant2Imie')) != null && !"".equals(getFromProcessDataSet(pd, 'reprezentant2Imie')) && (getFromProcessDataSet(pd, 'reprezentant2Nazwisko')) && !"".equals(getFromProcessDataSet(pd, 'reprezentant2Nazwisko'))){
            data.put(key, [value] as String[]);
            addCheckboxes(data, ["pan2": "Pan", "pani2": "Pani"], value)
        }
    }

    private mapKontaktEmailProcess(def data, def pd, def key, def value) {
        data.put("email", [value] as String[])
    }

    private mapNipProcess(def data, def pd, def key, def value){
        data.put(key, [value] as String[]);
        data.put("akceptantNip", [value] as String[]);
    }

    private mapWydrukGrafikiCenaProcess(def data, def pd, def key, def value){
        mapFieldWithStartDate(data, pd, key, value, "wydrukGrafikiData");
    }

    private mapDzialaniaMatematyczneCenaProcess(def data, def pd, def key, def value){
        mapFieldWithStartDate(data, pd, key, value, "dzialaniaMatematyczneData");
    }

    private mapTytulPlatnosciCenaProcess(def data, def pd, def key, def value){
        mapFieldWithStartDate(data, pd, key, value, "tytulPlatnosciData");
    }

    private mapPierwszaSesjaCenaProcess(def data, def pd, def key, def value){
        mapFieldWithStartDate(data, pd, key, value, "pierwszaSesjaData");
    }

    private mapSystemKasowyCenaProcess(def data, def pd, def key, def value){
        mapFieldWithStartDate(data, pd, key, value, "systemKasowyData");
    }

    private mapWeryfikacjaPINCenaProcess(def data, def pd, def key, def value){
        mapFieldWithStartDate(data, pd, key, value, "weryfikacjaPINData");
    }

    private mapCzasObslugiCenaProcess(def data, def pd, def key, def value){
        mapFieldWithStartDate(data, pd, key, value, "czasObslugiData");
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

    private mapReprezentant1ImieProcess(def data, def pd, def key, def value) {
        data.put("reprezentant1", [value + " " + getFromProcessDataSet(pd, 'reprezentant1Nazwisko')] as String[])
    }

    private mapReprezentant2ImieProcess(def data, def pd, def key, def value) {
        data.put("reprezentant2", [value + " " + getFromProcessDataSet(pd, 'reprezentant2Nazwisko')] as String[])
    }

    private mapPozyskujacyImieProcess(def data, def pd, def key, def value){
        data.put("osobaPozyskalaAkceptanta", [value + " " + getFromProcessDataSet(pd, 'pozyskujacyNazwisko')] as String[])
        data.put("osobaPodpisalaUmowe", [value + " " + getFromProcessDataSet(pd, 'pozyskujacyNazwisko')] as String[])
    }

    private mapAkceptantUlicaProcess(def data, def pd, def key, def value) {
        data.put("akceptantUlica", [value] as String[])
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
        if (value != null){
            if ("spolka_akcyjna".equals(value)){
                value = "spolka";
                data.put("spolkaText", ["akcyjna"] as String[])
            } else if ("spolka_zoo".equals(value)){
                value = "spolka";
                data.put("spolkaText", ["z o.o."] as String[])
            } else if ("spolka_komandytowa".equals(value)){
                value = "spolka";
                data.put("spolkaText", ["komandytowa"] as String[])
            }

            addCheckboxes(data, ["spolkaCywilna":"spolka_cywilna", "osobaFizyczna":"osoba_fizyczna", "spolka":"spolka", "inne1":"inne"], value)

            if ("inne".equals(value)){
                data.put("inneText", [getFromProcessDataSet(pd, "dzialalnoscFormaInna")] as String[])
            }
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
            data.put('oplataZaUruchomienieDCC', [value] as String[]);
        }
    }

    private mapIsZestawPosOdplatneUzywanieShownProcess(def data, def pd, def key, def value){
        if (value != null && "tak".equals(value)){
            def suffixes = ['A', 'B', 'C'];

            def resultNormalMap = new TreeMap<Integer, Integer>();
            addToPosMap(pd, resultNormalMap, 'oplPOSDialUpIlosc', 'oplPOSDialUpNormalneMies', 'oplPOSDialUpNormalnePP')
            addToPosMap(pd, resultNormalMap, 'oplPOSVPNIlosc', 'oplPOSVPNNormalneMies', 'oplPOSVPNNormalnePP')
            addToPosMap(pd, resultNormalMap, 'oplPOSSSLIlosc', 'oplPOSSSLNormalneMies', 'oplPOSSSLNormalnePP')
            addToPosMap(pd, resultNormalMap, 'oplPOSWiFiIlosc', 'oplPOSWiFiNormalneMies', 'oplPOSWiFiNormalnePP')
            addToPosMap(pd, resultNormalMap, 'oplPOSGPRSIlosc', 'oplPOSGPRSNormalneMies', 'oplPOSGPRSNormalnePP')
            addToData(data, resultNormalMap, 'oplatyPOSIlosc', 'oplatyPOSCena', suffixes)

            def resultPrefMap = new TreeMap<Integer, Integer>();
            addToPosMap(pd, resultPrefMap, 'oplPOSDialUpIlosc', 'oplPOSDialUpPreferencyjneMies', 'oplPOSDialUpPreferencyjnePP')
            addToPosMap(pd, resultPrefMap, 'oplPOSVPNIlosc', 'oplPOSVPNPreferencyjneMies', 'oplPOSVPNPreferencyjnePP')
            addToPosMap(pd, resultPrefMap, 'oplPOSSSLIlosc', 'oplPOSSSLPreferencyjneMies', 'oplPOSSSLPreferencyjnePP')
            addToPosMap(pd, resultPrefMap, 'oplPOSWiFiIlosc', 'oplPOSWiFiPreferencyjneMies', 'oplPOSWiFiPreferencyjnePP')
            addToPosMap(pd, resultPrefMap, 'oplPOSGPRSIlosc', 'oplPOSGPRSPreferencyjneMies', 'oplPOSGPRSPreferencyjnePP')
            addToData(data, resultPrefMap, 'oplatyPOSPrefIlosc', 'oplatyPOSPrefCena', suffixes)
        }
    }

    private def addToData(def data, def resultNormalMap, def countPdfFileName, def pricePdfFileName, def suffixes) {
        resultNormalMap.eachWithIndex{ key, value, index ->
            if (index < suffixes.size()){
                data.put(pricePdfFileName+suffixes[index], [key.toString()] as String[])
                data.put(countPdfFileName+suffixes[index], [value.toString()] as String[])
            }
        }
    }

    private def addToPosMap(def pd, def resultMap, def countKey, def normalPriceKey, def normalPricePPKey) {
        def countResult = convertToInteger(getFromProcessDataSet(pd, countKey));

        if (countResult.isDigit && countResult.value>0){
            def priceResult = convertToInteger(getFromProcessDataSet(pd, normalPriceKey));
            def pricePPResult = convertToInteger(getFromProcessDataSet(pd, normalPricePPKey));

            def priceSum = 0;
            if (priceResult.isDigit && priceResult.value>0){
                priceSum+=priceResult.value
            }
            if (pricePPResult.isDigit && pricePPResult.value>0){
                priceSum+=pricePPResult.value
            }

            if (priceSum>0){
                resultMap.put(priceSum, countResult.value+=resultMap.containsKey(priceSum)?resultMap.get(priceSum):0)
            }
        }
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

    //--------------------------------------------------------------------------------------------------

    private def formatDoubleValue(def data, def processData, def suffix) {
        if (processData && processData.value && processData.value.isDouble()){
            DecimalFormat df = (DecimalFormat)NumberFormat.getNumberInstance(new Locale("pl", "PL"));
            def fn = df.format(processData.value.toDouble()) + ' ' + suffix;
            data.put(processData.name, [fn] as String[])
        }
    }

    private mapFieldWithStartDate(def data, def pd, def key, def value, def dateFieldName){
        if (value != null && !"".equals(value)){
            data.put(key, [value] as String[])
            addDateField(data, dateFieldName, getFromProcessDataSet(pd, "dataUmowy"));
        }
    }

    private getFromProcessDataSet(def processDataSet, def key){
        def result = processDataSet.find{ processData -> processData.name.equals(key)}
        (result && result?.value)?result?.value:""
    }

}
