package com.eservice.eumowy.pdfmapper

import com.eservice.eumowy.util.DateUtils
import org.apache.log4j.Logger

import java.text.DecimalFormat
import java.text.NumberFormat


class PdfMapper {
    static Logger LOG = Logger.getLogger(PdfMapper.class)

    private int pointIndex=0;
    private int pointAcceptCardCount =0;
    private int pointRangeCount =0;

    def mapToPDFData(def processInstance){
        HashMap<String, String[]> dataMap = new HashMap<String, String[]>()
        dataMap.putAll(mapProcessToPDFData(processInstance))
        dataMap.putAll(mapAllDataToPDFData(processInstance.processData, processInstance.points))
        return dataMap
    }

    def mapAllDataToPDFData(def process, def points) {
        HashMap<String, String[]> dataMap = new HashMap<String, String[]>()

        dataMap.putAll(mapProcessDataToPDFData(process))
        dataMap.putAll(mapPointsDataToPDFData(points))

        return dataMap
    }

    private def mapPointsDataToPDFData(def points) {
        Map<String, String[]> data = new HashMap<String, String[]>()
        points?.eachWithIndex { point, index ->
            pointIndex = index;

            data.putAll(mapPointDataToPDFData(point))
            data.putAll(mapPosesDataToPDFData(point.posDatas))
        }
        return data
    }

    private def mapProcessToPDFData(def processInstance){
        Map<String, String[]> data = new HashMap<String, String[]>()
        data.put("phNumer", [processInstance.phNumber.toString()] as String[])
        data.put("mid", [processInstance.client.mid?:'{mid}'] as String[])
        return data
    }

    private def mapPointDataToPDFData(def pointData) {
        Map<String, String[]> data = new HashMap<String, String[]>()

//        boolean acceptCardIsChoosen = false;
        boolean rangeIsChoosen, acceptCardIsChoosen = false;

        if (pointData.czyWybranyAkceptacjaKart){
            pointAcceptCardCount++
            acceptCardIsChoosen = true;
        }

        if (pointData.czyWybranyZakresUruchomienia){
            pointRangeCount++
            rangeIsChoosen = true
        }

        pointData.properties.each { key, value ->
            log.info "PointData Key: " + key + " value: " + value
            if (["class", "posDatas", "errors", "constraints", "processId", "cbdId", "pointDetailsId", "empty"].contains(key) || value == null){
                return
            } else if (["tytulPlatnosci", "systemKasowy", "uta"].contains(key)) {
                if (acceptCardIsChoosen){
                    def methodName = "map" + key.capitalize() + "Point"
                    if (PdfMapper.metaClass.respondsTo(this, methodName)) {
                        this."${methodName}"(data, pointData, key, value, pointAcceptCardCount)
                        return
                    }
                }
            } else {
                //TODO -
                def methodName = "map" + key.capitalize() + "Point"
                if (PdfMapper.metaClass.respondsTo(this, methodName)) {
                    this."${methodName}"(data, pointData, key, value, pointAcceptCardCount)
                    return
                }
                // w ostatecznosci...
                data.put(key, [value] as String[])
            }
        }

        data.putAll(mapPointDataDetailsToPDFData(pointData, acceptCardIsChoosen, rangeIsChoosen));
        return data
    }

    private def mapPointDataDetailsToPDFData(def pointData, def acceptCardIsChoosen, def rangeIsChoosen) {
        Map<String, String[]> data = new HashMap<String, String[]>()

        pointData.pointDetails?.properties.each { key, value ->
//            println  "PointDataDetails Key: " + key  +  " value: " + value + " index: " + index

            if (["class", "posDatas", "errors", "constraints", "processId", "cbdId", "pointDetailsId", "empty"].contains(key) || value == null){
                return
            } else if (["wydrukUlica", "nazwaDoWydrukuZTerminalaPos"].contains(key)){
                //specjalne traktowanie pol do obslugi tabelek
				data.put(key, [value] as String[])
			
			
                def pdfProperty;

                if (acceptCardIsChoosen){
                    if ("wydrukUlica".equals(key)){
                        pdfProperty="adresAkceptacjaKart";
                    } else if ("nazwaDoWydrukuZTerminalaPos".equals(key)){
                        pdfProperty="punktAkceptacjaKart";
                    }

                    if (pdfProperty?.trim()){
                        def methodName = "map" + key.capitalize() + "PointDataDetails"
                        if (PdfMapper.metaClass.respondsTo(this, methodName)) {
                            this."${methodName}"(data, pointData, pdfProperty, value, pointAcceptCardCount)
                        }
                    }
                }

                if (rangeIsChoosen){
                    if ("wydrukUlica".equals(key)){
                        pdfProperty="adresZakresUruchomienia";
                    } else if ("nazwaDoWydrukuZTerminalaPos".equals(key)){
                        pdfProperty="punktZakresUruchomienia";
                    }

                    if (pdfProperty?.trim()){
                        def methodName = "map" + key.capitalize() + "PointDataDetails"
                        if (PdfMapper.metaClass.respondsTo(this, methodName)) {
                            this."${methodName}"(data, pointData, pdfProperty, value, pointRangeCount)
                        }
                    }
                }
            } else {
                def methodName = "map" + key.capitalize() + "PointDataDetails"
                if (PdfMapper.metaClass.respondsTo(this, methodName)) {
                    this."${methodName}"(data, pointData, key, value, pointIndex+1)
                    return
                }
                data.put(key, [value] as String[])
            }
        }
        return data
    }

    private def mapPosesDataToPDFData(def posesData) {
        Map<String, String[]> data = new HashMap<String, String[]>()
        posesData?.each { posData ->
            data.putAll(mapPosDataToPDFData(posData))
        }
        return data
    }

    private def mapPosDataToPDFData(def pos){
        Map<String, String[]> data = new HashMap<String, String[]>()

        //MAP properties
        pos.properties.each { key, value ->
            log.info "PosData Key: " + key
            if (["class", "cbdId", "process", "point", "errors", "constraints", "empty", "", ""].contains(key) || value == null){
                return
            }

            def methodName = "map" + key.capitalize()
            if (PdfMapper.metaClass.respondsTo(this, methodName)) {
                this."${methodName}"(data, pos, key, value)
                return
            }

            data.put(key, [value] as String[])
        }

        //MAP posDetails properties
        pos.posDetails?.properties.each { key, value ->
            log.info "PosDataDetails Key: " + key + " value: " + value

            if (["class", "cbdId", "process", "point", "errors", "constraints", "empty"].contains(key) || value == null){
                return
            }

            def methodName = "map" + key.capitalize()
            if (PdfMapper.metaClass.respondsTo(this, methodName)) {
                this."${methodName}"(data, pos, key, value)
                return
            }

            data.put(key, [value] as String[])
        }

        return data
    }

    private def mapProcessDataToPDFData(def process) {
        Map<String, String[]> data = new HashMap<String, String[]>()

        process.each { processData ->
//            println  "ProcessData name: " + processData.name  +  " value: " + processData.value

            //formatowanie procentowej wartosci platnosci karty
            if (processData.name.endsWith('Pr')){
                formatDoubleValue(data, processData, '%')
                return
            }

            //formatowanie stalej wartosci platnosci karty
            if (processData.name.endsWith('St')){
                formatDoubleValue(data, processData, 'zł')
                return
            }

            def methodName = "map" + processData.name.capitalize()+"Process"
            if (PdfMapper.metaClass.respondsTo(this, methodName)) {
                this."${methodName}"(data, process, processData.name, processData.value)
                return
            }

            if ("true".equals(processData.value) == true || "false".equals(processData.value) == true) {
                data.put(processData.name, [processData.value, "", "checkbox"] as String[])
            }
            else {
                data.put(processData.name, [processData.value] as String[])
            }
        }

        data.each { key, value ->
            log.info "Mapping < " + key + " : " + value + " >"
        }

        return data
    }

    //------------------- POINT METHODS --------------------------------

    private mapNrMerchantaPoint(def data, def pd, def key, def value, def index) {
        data.put(key+"1", [value.substring(0, 5)] as String[])
        data.put(key+"2", [value.substring(5, 10)] as String[])
        data.put(key+"3", [value.substring(10, 12)] as String[])
        data.put(key+"4", [value.substring(12, 15)] as String[])
    }

    private mapUlicaDoKorespondencjiPoint(def data, def pd, def key, def value, def index) {
        data.put(key, [pd.ulicaDoKorespondencjiTyp + " " + value] as String[])
    }

    private  mapTytulPlatnosciPoint(def data, def pd, def key, def value, def index) {
        data.put("platnoscTN"+index, [value?"TAK":"NIE"] as String[])
    }

    private mapSystemKasowyPoint(def data, def pd, def key, def value, def index) {
        data.put("integracjaTN"+index, [value?"TAK":"NIE"] as String[])
    }

    private mapUtaPoint(def data, def pd, def key, def value, def index) {
        data.put("utaTN"+index, [value?"TAK":"NIE"] as String[])
    }

    //------------------- POINT DATA DETAILS METHODS --------------------------------

    private mapWydrukUlicaPointDataDetails(def data, def pointData, def key, def value, def index) {

        //TODO - mozna pomyslec nad sprawdzeniem czy mieszkanie jest puste

        def result = (value + " " + getFromPointDataDetails(pointData, 'wydrukNrDomu') +"/"+ getFromPointDataDetails(pointData, 'wydrukNrLokalu') + " " + getFromPointDataDetails(pointData, 'wydrukMiasto') + " " + getFromPointDataDetails(pointData, 'wydrukKodPocztowy'))
        data.put(key+index, [result] as String[])
    }

    private mapNazwaDoWydrukuZTerminalaPosPointDataDetails(def data, def pointData, def key, def value, def index) {
        data.put(key+index, [value] as String[])
    }

    private mapKorespondencjaPocztaPointDataDetails(def data, def pointData, def key, def value, def index){
        data.put(key, [value] as String[]);
        String[] split = key.split("-");
        for (int i=0; i<split.length; i++){
            data.put("korespondencjaKodPocztowy"+(i+1), [split[i]] as String[])
        }
    }

    private mapKontaktWPunkcieTelKomorkowyPointDataDetails(def data, def pointData, def key, def value, def index){
        data.put(key, [value] as String[]);
        String[] split = key.split("-");
        for (int i=0; i<split.length; i++){
            data.put("komorka"+(i+1), [split[i]] as String[])
        }
    }

    private mapKontaktWPunkcieImiePointDataDetails(def data, def pointData, def key, def value, def index){
        data.put(key, [value] as String[]);
        data.put("imieINazwisko", [value + " " + getFromPointDataDetails(pointData, 'kontaktWPunkcieNazwisko')] as String[]);
    }

    private mapKontaktWPunkcieTytulPointDataDetails(def data, def pointData, def key, def value, def index){
        data.put(key, [value] as String[]);
        addCheckboxes(data, ["pan": "Pan", "pani": "Pani"], value)
    }

    private mapKontaktWPunkcieEmailPointDataDetails(def data, def pointData, def key, def value, def index){
        data.put(key, [value] as String[]);
        data.put("email", [value] as String[])
    }


    //BRAK numeru stacjonarnego, numeru faksu i numeru rachunku bankowego

    //------------------- PROCESS METHODS --------------------------------

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

    private mapAkceptantUlicaProcess(def data, def pd, def key, def value) {
     //TODO - mozna sprawdzic czy jest numer mieszkania
	// data.put("akceptantUlica", [value] as String[])
	   data.put("akceptantSiedziba", [(getSiedzibaAkceptanta(getFromProcessDataSet(pd, 'akceptantUlicaTytul'), getFromProcessDataSet(pd, 'akceptantUlica'), getFromProcessDataSet(pd, 'akceptantNrDomu'), getFromProcessDataSet(pd, 'akceptantNrMieszkania'), getFromProcessDataSet(pd, 'akceptantKodPocztowy'), getFromProcessDataSet(pd, 'akceptantMiasto')))] as String[])
		
    // data.put("akceptantSiedziba", [getFromProcessDataSet(pd, 'akceptantUlicaTytul') + " " + value + " " + getFromProcessDataSet(pd, 'akceptantNrDomu') + "/" + getFromProcessDataSet(pd, 'akceptantNrMieszkania') + " " + getFromProcessDataSet(pd, 'akceptantKodPocztowy') + " " + getFromProcessDataSet(pd, 'akceptantMiasto')] as String[])
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
        println 'Working with property: ' + key
    }

    private mapScoringPonad50ProcentObrotowWNocyProcess(def data, def pd, def key, def value) {
        addCheckbox(data, 'ponad50ProcentObrotowWNocy', 'true', value);
        println 'Working with property: ' + key
    }

    private mapScoringRuchTurystycznyPrzygranicznyProcess(def data, def pd, def key, def value) {
        addCheckbox(data, 'ruchTurystycznyPrzygraniczny', 'true', value);
        println 'Working with property: ' + key
    }

    private mapScoringUslugiPlatneZGoryProcess(def data, def pd, def key, def value) {
        println 'Working with property: ' + key
        addCheckbox(data, 'uslugiPlatneZGory', 'true', value);
    }

    private mapScoringStanZadbanyProcess(def data, def pd, def key, def value) {
        println 'Working with property: ' + key
        addCheckbox(data, key, 'true', value);
    }

    private mapDccZakresUruchomieniaProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["noweZestPos":"obecne_i_nowe", "obecneZestPos":"obecne", "phu":"wskazane"], value)
    }

    private mapInformacjaHandlowaProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["informacjaHandlowaTak": "true", "informacjaHandlowaNie": "false"], value)
    }

    private mapObslugaTypProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["obsugaPrestiz": "prestige", "obslugaKomfort": "comfort", "obslugaEkonomiczny": "economic"], value)
    }

    private mapUmowaCzasProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["umNieOzn": "nieoznaczony", "umOzn": "oznaczony"], value)
    }
	
	//------------------- STRINGBUILDER -------------------------------------
	private String getSiedzibaAkceptanta(String streetType, String street, String houseNumber, String flatNumber, String postalCode, String city){
		def sb = new StringBuilder();
		sb.append(streetType);
		sb.append(" ");
		sb.append(street);
		sb.append(" ");
		sb.append(houseNumber);
		if (flatNumber != null) {
			sb.append("/");
			sb.append(flatNumber)
			}
		sb.append(", ");
		sb.append(postalCode);
		sb.append(" ");
		sb.append(city);
		return sb.toString();
	}
	 
    //------------------- OTHER/UTIL METHODS --------------------------------

    private getFromProcessDataSet(def processDataSet, def key){
        def result = processDataSet.find{ processData -> processData.name.equals(key)}
        (result && result?.value)?result?.value:""
    }

    private getFromPointData(def pointData, def key){
        getPropertyFromObject(pointData, key);
    }

    private getFromPointDataDetails(def pointData, def key){
        getPropertyFromObject(pointData?.pointDetails, key)
    }

    private getPropertyFromObject(def object, def property){
        if (object?.hasProperty(property)){
            return object."${property}" ?: "";
        } else {
            return ""
        }
    }

    private addCheckbox(def data, def pdfName, def fieldValue, def value){
        data.put(pdfName, [fieldValue.equals(value), "", "checkbox"] as String[])
    }

    private addDateField(def data, def key, def value){
        data.put(key, [value?.trim()?DateUtils.getFormattedDate(DateUtils.parseWithTimezone(value), DateUtils.YYYY_MM_DD):""] as String[])
    }

    private addCheckboxes(def data, def pdfKeyValue, def value){
        pdfKeyValue.each{ k, v ->  data.put(k, [v.equals(value), "", "checkbox"] as String[])}
    }

    private def formatDoubleValue(def data, def processData, def suffix) {
        if (processData && processData.value && processData.value.isDouble()){
            DecimalFormat df = (DecimalFormat)NumberFormat.getNumberInstance(new Locale("pl", "PL"));
            def fn = df.format(processData.value.toDouble()) + ' ' + suffix;
            data.put(processData.name, [fn] as String[])
        }
    }

}
