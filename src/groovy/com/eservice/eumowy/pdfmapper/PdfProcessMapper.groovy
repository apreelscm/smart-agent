package com.eservice.eumowy.pdfmapper

import com.eservice.eumowy.HirePayment

import java.text.DecimalFormat
import java.text.NumberFormat

class PdfProcessMapper extends AbstractPdfMapper{

    def calculatorService
    def calc
    def pointMapper
    def posMapper
	
	private static final EMPTY_VALUES = ["", "-"]

    public PdfProcessMapper (def calculatorService, def calc, def pointMapper, def posMapper){
        this.calculatorService = calculatorService
        this.calc = calc
        this.pointMapper = pointMapper
        this.posMapper = posMapper
    }

    protected def mapOnlyProcessData(def processInstance){
        def dataMap = [:]
        dataMap.putAll(mapProcessToPDFData(processInstance))
        dataMap.putAll(mapProcessDataToPDFData(processInstance.processData))

        def points = processInstance?.points
        LOG.info("Ilosc punktow: " + points?.size())

        if (points != null && points.size()>0){
            //APUNTSS, APUNTZ2
            //TODO - nie uzupelniamy tabelek w tych dokumentach

            //APUPZDCC2, APUPZ2DC1
            dataMap.putAll(pointMapper.mapPointsSpecial(points.findAll{ point -> (point != null && point.czyWybranyAkceptacjaKart && point.czyWybranyZakresUruchomienia)}, ["nazwa":"punktZakresUruchomienia", "miejscowosc":"adresZakresUruchomienia"]));

            //APUPZIF2, APUPZ2, APUPZBS2
            dataMap.putAll(pointMapper.mapPointsSpecial(points.findAll{ point -> point != null && ((point.isLocal() == true) || (point.posDatas && point.posDatas.findAll{ pos -> pos != null && pos?.isLocal() == true}.size()>0))}, ["nazwa":"punktAkceptacjaKart", "miejscowosc":"adresAkceptacjaKart"]));

            //APUPZAWNZBS1, APUPZAWNZS1
            dataMap.putAll(pointMapper.mapPointsSpecial(points.findAll{ point -> point != null && ((point.isLocal() == true) || (point.posDatas && point.posDatas.findAll{ pos -> pos != null && pos?.isLocal() == true}.size()>0))}, ["nazwa":"punkt", "miejscowosc":"adres"]));

            //APUNTSZAPOU3
            dataMap.putAll(pointMapper.mapPointsSpecial(points.findAll{ point -> point?.czyWybranyAkceptacjaKart}, ["nazwa":"punktTN", "miejscowosc":"adresTN", "systemKasowy":"integracjaTN", "uta":"utaTN"]));

            //APUNTSZAPOO3
            def poses = []
            points.each{ p ->
                poses.addAll(p?.posDatas.findAll {pos -> pos != null && pos?.czyWybrany})
            }
            dataMap.putAll(posMapper.mapPosSpecial(poses))


    //----- sumowanie oplat z posow i hire payments - start
            def normalPrefMaps = posMapper.mapPosesNotFromCBD(findPosesNotFromCbd(points));

            def normalResult = normalPrefMaps.normalResult
            def prefResult = normalPrefMaps.prefResult

            def normalMapFromProcess = [:]
            if (processInstance.processData.find{"isOdplatneUzywanieShown".equals(it.name) && "tak".equals(it.value)}){
                normalMapFromProcess = mapOdplatneUzywanie(processInstance)
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

            //mamy w pelni zapelnione finalResult
            def suffixes = ['A', 'B', 'C']
            finalResult.eachWithIndex { def entry, int i ->
                if (i<suffixes.size()){
                    dataMap.put('oplatyPOSIlosc' + suffixes.get(i), [entry.value.toString()] as String[])
                    dataMap.put('oplatyPOSCena' + suffixes.get(i), [entry.key.toString()] as String[])
                }
            }
            prefResult.eachWithIndex { def entry, int i ->
                if (i<suffixes.size()){
                    dataMap.put('oplatyPOSPrefIlosc' + suffixes.get(i), [entry.value.toString()] as String[])
                    dataMap.put('oplatyPOSPrefCena' + suffixes.get(i), [entry.key.toString()] as String[])
                }
            }
    //----- sumowanie oplat z posow i hire payments - finish

        } else {

            //gdy nie ma zadnych punktow to i tak musimy zmapowac to co jest w HirePayments (jesli oczywiscie jest)
            if (processInstance.processData.find{"isOdplatneUzywanieShown".equals(it.name) && "tak".equals(it.value)}){
                def normalMapFromProcess = mapOdplatneUzywanie(processInstance)
                def suffixes = ['A', 'B', 'C']
                normalMapFromProcess.eachWithIndex { def entry, int i ->
                    if (i<suffixes.size()){
                        dataMap.put('oplatyPOSIlosc' + suffixes.get(i), [entry.value.toString()] as String[])
                        dataMap.put('oplatyPOSCena' + suffixes.get(i), [entry.key.toString()] as String[])
                    }
                }
            }
        }


        def newToCross = ['dodatkowyPunkt', 'dodatkowyPos']
        if (processInstance.activities.findAll { activity -> 'aneks'.equals(activity.code)}.size()>0){
            dataMap.put("crossAdditional", ['_______________'] as String[])
        } else if (processInstance.activities.size()==1 && processInstance.activities.findAll { activity -> newToCross.contains(activity.code)}.size()>0){
            dataMap.put("crossNew", ['_____'] as String[])
        }

        //Na potrzeby dokumentu: APUPZAWNZBS1.00113-08-06
        if (processInstance.activities.findAll { activity -> ['dodatkowyPos', 'dodatkowyPunkt'].contains(activity.code)}.size()>0){
            addCheckbox(dataMap, 'zalacznik2', "true", "true")
        }

        //Na potrzeby dokumentu: APUPZAWNZBS1.00113-08-06
        if (processInstance.activities.findAll { activity -> 'zmianaProwizji'.equals(activity.code)}.size()>0){
            addCheckbox(dataMap, 'zalacznik3', "true", "true")
        }
        return dataMap;
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

    private def mapOdplatneUzywanie(def processInstance){
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

    private mapWydrukGrafikiCenaProcess(def data, def pd, def key, def value){
        mapFieldWithStartDate(data, pd, key, value, "wydrukGrafikiData");
    }

    private mapDzialaniaMatematyczneCenaProcess(def data, def pd, def key, def value){
        mapFieldWithStartDate(data, pd, key, value, "dzialaniaMatematyczneData");
    }

    private mapTytulPlatnosciCenaProcess(def data, def pd, def key, def value){
        //TODO co tutaj zrobic, bo to pole zostalo usuniete???
//        mapFieldWithStartDate(data, pd, key, value, "tytulPlatnosciData");
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

    private mapObslugaEkonomicznyCenaProcess(def data, def pd, def key, def value){
        data.put(key, [value] as String[])
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
            } else if ("spolka_jawna".equals(value)){
                value = "spolka";
                data.put("spolkaText", ["jawna"] as String[])
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

    private def formatDoubleValue(def value){
        ((DecimalFormat)NumberFormat.getNumberInstance(new Locale("pl", "PL"))).format(value)
    }

    private mapFieldWithStartDate(def data, def pd, def key, def value, def dateFieldName){
        if (value !=null && !EMPTY_VALUES.contains(value)){
            data.put(key, [value] as String[])
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
