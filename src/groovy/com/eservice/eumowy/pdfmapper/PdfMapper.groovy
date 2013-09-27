package com.eservice.eumowy.pdfmapper

import com.eservice.eumowy.util.DateUtils
import org.apache.log4j.Logger

import java.text.DecimalFormat
import java.text.NumberFormat


class PdfMapper {
    static Logger LOG = Logger.getLogger(PdfMapper.class)

    private int myIndex = 0;

	def calc
	def calculatorService

	PdfMapper(def calc, def calculatorService){
		this.calc = calc
		this.calculatorService = calculatorService
	}

	def mapOnlyProcessData(def processInstance){
		HashMap<String, String[]> dataMap = new HashMap<String, String[]>()
		dataMap.putAll(mapProcessToPDFData(processInstance))
		dataMap.putAll(mapProcessDataToPDFData(processInstance.processData))

        def points = processInstance?.points;
        println "Ilosc punktow: " + points?.size()

        if (points != null && points.size()>0){
            //TODO - czy takie filtrowanie wystarczy; czy nie trzeba sprawdzac jeszcze czy punkt jest wybrany ( pole - czyWybranyAkceptacjaKart).
            dataMap.putAll(mapPointsSpecial(points.findAll{ point -> point.tytulPlatnosci}, ["nazwa":"punktTytulPlatnosci", "miejscowosc":"adresTytulPlatnosci"]));
            dataMap.putAll(mapPointsSpecial(points.findAll{ point -> point.czyWybranyZakresUruchomienia}, ["nazwa":"punktZakresUruchomienia", "miejscowosc":"adresZakresUruchomienia"]));
            dataMap.putAll(mapPointsSpecial(points.findAll{ point -> point.czyWybranyAkceptacjaKart}, ["nazwa":"punktAkceptacjaKart", "miejscowosc":"adresAkceptacjaKart"]));
            dataMap.putAll(mapPointsSpecial(points, ["nazwa":"punkt", "miejscowosc":"adres"]));
            dataMap.putAll(mapPointsSpecial(points, ["tytulPlatnosci":"platnoscTN","systemKasowy":"integracjaTN","uta":"utaTN"]));
        }

		dataMap.putAll(mapProcessCalcToPDFData())
		return dataMap;
	}

    private def mapPointsSpecial(def points, def mapping) {
        Map<String, String[]> data = new HashMap<String, String[]>()
        myIndex = 1;
        points?.each { point ->
            point.properties.each { key, value ->
                if (key != null && mapping.containsKey(key)){
                    def methodName = "map" + key.capitalize() + "Point"
                    if (PdfMapper.metaClass.respondsTo(this, methodName)) {
                        this."${methodName}"(data, point, mapping.get(key), value, myIndex)
                    }
                }
            }
            myIndex++
        }
        return data
    }

    def mapOnlyPointData(def point){
        Map<String, String[]> data = new HashMap<String, String[]>()

        data.putAll(mapPointDataToPDFData(point))
        data.putAll(mapPosesDataToPDFData(point.posDatas))
        return data
    }

    //FOR TEST ONLY !!!
    def mapAllDataToPDFData(def process, def points) {
        HashMap<String, String[]> dataMap = new HashMap<String, String[]>()

        dataMap.putAll(mapProcessDataToPDFData(process))
        dataMap.putAll(mapPointsDataToPDFData(points))

        return dataMap
    }

    //FOR TEST ONLY !!!
    private def mapPointsDataToPDFData(def points) {
        Map<String, String[]> data = new HashMap<String, String[]>()
        points?.eachWithIndex { point, index ->

            data.putAll(mapPointDataToPDFData(point))
            data.putAll(mapPosesDataToPDFData(point.posDatas))
        }
        return data
    }

	private def mapProcessCalcToPDFData() {
		Map<String, String[]> data = new HashMap<String, String[]>()
		println 'Jestem w mapowaniu danych z Kalkulatora!!!!'

		if (calc != null && calculatorService != null){
			println 'Pobieram dane z kalkulatora!!!!'
            data.put('oplatyPOSMiesiacNaliczania', [calculatorService.getCalcProperty('E_LICZBA_MIES_ZWOL_NAJ_1')] as String[])
		}
		return data
	}
	
    private def mapProcessToPDFData(def processInstance){
        Map<String, String[]> data = new HashMap<String, String[]>()
        data.put("phNumer", [processInstance.phNumber.toString()] as String[])
		data.put("osobaPozyskalaAkceptantaNr", [processInstance.phNumber.toString()] as String[])

        //to na jakis formularz jest
		if (processInstance.phNumber.toString() != null && processInstance.phNumber.toString().size()==5){
			data.put("NrSprzedazowyPH1", [processInstance.phNumber.toString().substring(0, 3)] as String[])
			data.put("NrSprzedazowyPH2", [processInstance.phNumber.toString().substring(3, 4)] as String[])
		} else {
		    data.put("NrSprzedazowyPH1", [processInstance.phNumber.toString()] as String[])
		}
		
        data.put("mid", [processInstance.client.mid?:'{mid}'] as String[])
        return data
    }

    private def mapPointDataToPDFData(def pointData) {
        Map<String, String[]> data = new HashMap<String, String[]>()
        pointData.properties.each { key, value ->
//            log.info "PointData Key: " + key + " value: " + value
            if (["class", "posDatas", "errors", "constraints", "process", "processId", "cbdId", "pointDetails", "empty"].contains(key) || value == null){
                return
            } else {
                def methodName = "map" + key.capitalize() + "Point"
                if (PdfMapper.metaClass.respondsTo(this, methodName)) {
                    //TEORETYCZNIE tutaj nie jest potrzebny zaden indeks stad -1
                    //ale na wszelki wypadek w kodzie jest na to zabezpieczenie.
                    this."${methodName}"(data, pointData, key, value, -1)
                    return
                }
                // w ostatecznosci...
                data.put(key, [value] as String[])
            }
        }

        data.putAll(mapPointDataDetailsToPDFData(pointData));
        return data
    }

    private def mapPointDataDetailsToPDFData(def pointData) {
        Map<String, String[]> data = new HashMap<String, String[]>()

        pointData.pointDetails?.properties.each { key, value ->
//            println  "PointDataDetails Key: " + key  +  " value: " + value + " index: " + index

            if (["class", "posDatas", "errors", "constraints", "processId", "cbdId", "pointDetailsId", "empty"].contains(key) || value == null){
                return
            } else {
                def methodName = "map" + key.capitalize() + "PointDataDetails"
                if (PdfMapper.metaClass.respondsTo(this, methodName)) {
                    this."${methodName}"(data, pointData, key, value)
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
            if (["class", "cbdId", "process", "point", "errors", "constraints", "empty", ""].contains(key) || value == null){
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

    //------------------- POINT METHODS --------------------------------

    private mapMiejscowoscPoint(def data, def pd, def key, def value, def index) {
        data.put((index == -1)?key:key+index, [getAddress("", pd.ulica, pd.nrBudynku, pd.nrLokalu, pd.kodPocztowy, value)] as String[])
    }

    private mapNazwaPoint(def data, def pd, def key, def value, def index) {
        data.put((index == -1)?key:key+index, [value] as String[])
    }

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
        mapYesNoField(data, (index == -1)?key:key+index, value);
    }

    private mapSystemKasowyPoint(def data, def pd, def key, def value, def index) {
        mapYesNoField(data, (index == -1)?key:key+index, value);
    }

    private mapUtaPoint(def data, def pd, def key, def value, def index) {
        mapYesNoField(data, (index == -1)?key:key+index, value);
    }

    //------------------- POINT DATA DETAILS METHODS --------------------------------

    private mapKorespondencjaKodPocztowyPointDataDetails(def data, def pointData, def key, def value){
        data.put(key, [value] as String[]);
        mapWithPattern(data, value, ~/\d{2}-\d{3}/, "-", "korespondencjaKodPocztowy");
    }

	private mapWydrukKodPocztowyPointDataDetails(def data, def pointData, def key, def value){
		data.put(key, [value] as String[]);
        mapWithPattern(data, value, ~/\d{2}-\d{3}/, "-", "wydrukKodPocztowy");
	}

	
    private mapKontaktWPunkcieTelKomorkowyPointDataDetails(def data, def pointData, def key, def value){
        data.put(key, [value] as String[]);
        mapWithPattern(data, value, ~/\d{3}-\d{3}-\d{3}/, "-", "komorka");
    }

    private mapKontaktWPunkcieTelStacjonarnyPointDataDetails(def data, def pointData, def key, def value){
        data.put(key, [value] as String[]);
        mapFaxOrPhone(key, data, value, "kierunkowy1", "stacjonarny");
    }

    private mapKontaktWPunkcieFaxPointDataDetails(def data, def pointData, def key, def value){
        data.put(key, [value] as String[]);
        mapFaxOrPhone(key, data, value, "kierunkowy2", "nrFaksu");
    }

    private mapKontaktWPunkcieImiePointDataDetails(def data, def pointData, def key, def value){
        data.put(key, [value] as String[]);
        data.put("imieINazwisko", [value + " " + getFromPointDataDetails(pointData, 'kontaktWPunkcieNazwisko')] as String[]);
    }

    private mapKontaktWPunkcieTytulPointDataDetails(def data, def pointData, def key, def value){
        data.put(key, [value] as String[]);
        addCheckboxes(data, ["pan": "Pan", "pani": "Pani"], value)
    }

    private mapKontaktWPunkcieEmailPointDataDetails(def data, def pointData, def key, def value){
        data.put(key, [value] as String[]);
        data.put("email", [value] as String[])
    }

	private mapNumerRachunkuBankowegoPointDataDetails(def data, def pointData, def key, def value) {
		data.put(key, [value] as String[]);
        mapWithPattern(data, value, ~/\d{2}\s\d{4}\s\d{4}\s\d{4}\s\d{4}\s\d{4}\s\d{4}/, " ", "numerRachunkuBankowego");
	}
	
	private mapUwagiDodatkowePointDataDetails(def data, def pointData, def key, def value, def index) {
		data.put(key, [value] as String[]);
	}
	
	// ------------------ POS METHODS ------------------------------------
	
	private mapZamkniecieDniaOd(def data, def posesData, def key, def value){
		if (value != null){
			def pattern = ~/\d{2}:\d{2}/
			if (pattern.matcher(value).matches()){
				final String[] split = value.split(":");
				data.put("zamkniecieDniaOd1", [split[0]] as String[]);
				data.put("zamkniecieDniaOd2", [split[1]] as String[]);
			}
		}
	}
	
	private mapZamkniecieDniaDo(def data, def posesData, def key, def value){
		if (value != null){
			def pattern = ~/\d{2}:\d{2}/
			if (pattern.matcher(value).matches()){
				final String[] split = value.split(":");
				data.put("zamkniecieDniaDo1", [split[0]] as String[]);
				data.put("zamkniecieDniaDo2", [split[1]] as String[]);
			}
		}
	}

    //------------------- PROCESS METHODS --------------------------------
	
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
		if ((getFromProcessDataSet(pd, 'reprezentant2Imie')) != null && (getFromProcessDataSet(pd, 'reprezentant2Nazwisko'))){
		data.put(key, [value] as String[]);
		addCheckboxes(data, ["pan2": "Pan", "pani2": "Pani"], value)
		}
	}
	
	private mapKontaktEmailProcess(def data, def pd, def key, def value) {
		data.put("email", [value] as String[])
	}
	
	private mapOplataZaUruchomienieWalutyObcejProcess(def data, def pd, def key, def value) {
		data.put("walutaObcaCena", [value] as String[])
	}
	
	private mapNipProcess(def data, def pointData, def key, def value){
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
		
		if (value != null && !"".equals(value)){
			def pattern = ~/\d{4}-\d{2}-\d{2}/
			if (pattern.matcher(value).matches()){
				final String[] split = value.split("-");
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
	
	private mappozyskujacyNumerProcess(def data, def pd, def key, def value) {
		data.put("osobaPozyskalaAkceptantaNr",[value] as String[])
		data.put("osobaPodpisalaUmoweNr",[value] as String[])
	}

    private mapAkceptantUlicaProcess(def data, def pd, def key, def value) {
	   data.put("akceptantUlica", [value] as String[])
	   data.put("akceptantSiedziba", [(getAddress(getFromProcessDataSet(pd, 'akceptantUlicaTytul'), getFromProcessDataSet(pd, 'akceptantUlica'), getFromProcessDataSet(pd, 'akceptantNrDomu'), getFromProcessDataSet(pd, 'akceptantNrMieszkania'), getFromProcessDataSet(pd, 'akceptantKodPocztowy'), getFromProcessDataSet(pd, 'akceptantMiasto')))] as String[])
		
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

            addCheckboxes(data, ["spolkaCywilna":"spolka_cywilna", "osobaFizyczna":"osoba_fizyczna", "spolka":"spolka", "inne1":""], value)

            if ("".equals(value)){
                data.put("inneText", [getFromProcessDataSet(pd, "dzialalnoscFormaInna")] as String[])
            }
        }
    }

    private mapDzialalnoscDokumentProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["zaswiadczenieZEwidencji": "ewidencja", "umowaSpolkiCywilnej": "umowa_spolki_cywilnej", "odpisZKRS":"krs", "inne2":""], value)
        if ("".equals(value)){
            data.put("inneText2", [getFromProcessDataSet(pd, "dzialalnoscDokumentInny")] as String[])
        }
    }

	//------------------- STRINGBUILDER -------------------------------------
	private String getAddress(String streetType, String street, String houseNumber, String flatNumber, String postalCode, String city){
		def sb = new StringBuilder();

		sb.append(streetType).append(" ").append(street).append(" ").append(houseNumber);
		if (flatNumber != null && !"".equals(flatNumber)) {
			sb.append("/").append(flatNumber);
	    }
		sb.append(", ").append(postalCode).append(" ").append(city);
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

    private mapYesNoField(def data, def key, def value){
        data.put(key, [value?"TAK":"NIE"] as String[])
    }

    private def mapWithPattern(def data, def value, def pattern, def delimenter, def fieldName){
        if (value != null){
            if (pattern.matcher(value).matches()){
                final String[] split = value.split(delimenter);
                for (int i=0; i<split.length; i++){
                    data.put(fieldName+(i+1), [split[i]] as String[])
                }
            } else {
                //TODO - wyciagnac z patterna warunek i go wypisac.
                LOG.error('[Wartosc - ' + value + '] nie spelnia zalozonego warunku. value = ' + value )
            }
        }
    }

    private void mapFaxOrPhone(def key, def data, def phoneNumber, def kierName, def otherName){
        //(11) 222-33-44
        def pattern = ~/\(\d{2}\) \d{3}-\d{2}-\d{2}/

        if (phoneNumber != null){
            if (pattern.matcher(phoneNumber).matches()){
                data.put(kierName, [phoneNumber.substring(phoneNumber.lastIndexOf('(') +1, phoneNumber.indexOf(')'))] as String[]);

                def parts = phoneNumber.substring(phoneNumber.lastIndexOf(' ')+1).split('-');
                for (int i=0; i<parts.length; i++){
                    data.put(otherName+(i+1), [parts[i]] as String[])
                }
            } else {
                LOG.error('[key ' + key + '] nie spelnia warunku: (d{2}) d{3}-d{2}-d{2} value = ' + phoneNumber )
            }
        }
    }

    private mapFieldWithStartDate(def data, def pd, def key, def value, def dateFieldName){
        if (value != null && !"".equals(value)){
            data.put(key, [value] as String[])
            addDateField(data, dateFieldName, getFromProcessDataSet(pd, "dataUmowy"));
        }
    }
}
