package com.eservice.eumowy.pdfmapper

import org.apache.log4j.Logger


class PdfMapper {
	static Logger LOG = Logger.getLogger(PdfMapper.class)
	
	static mapAllDataToPDFData(def process, def points) {
		HashMap<String, String[]> dataMap = new HashMap<String, String[]>()
		
		points?.each { point ->
			dataMap.putAll(mapPointAndPosDataToPDFData(point))
		}
		
		dataMap.putAll(mapProcessDataToPDFData(process))
		
		return dataMap
	}
	
	static mapPointAndPosDataToPDFData(def pd) {
		def pointDataMap = mapPointDataToPDFData(pd)
		def posDataMap = mapPosDataToPDFData(pd.posDatas)
		pointDataMap.putAll(posDataMap)
		
		return pointDataMap
	}
	
	static mapPointDataToPDFData(def pd) {
		Map<String, String[]> data = new HashMap<String, String[]>()
		
		pd.properties.each { key, value ->
			log.info "PointData Key: " + key
			if (["class", "posDatas", "errors", "constraints", "processId", "cbdId", "pointDetailsId", "empty"].contains(key) || value == null){
				return
			}
			
			def methodName = "map" + key.capitalize()
			if (PdfMapper.metaClass.respondsTo(PdfMapper, methodName)) {
				PdfMapper."${methodName}"(data, pd, key, value)
				return
			}
			
			data.put(key, [value] as String[])
		}
		
		pd.pointDetails?.properties.each { key, value ->
			log.info "PointDataDetails Key: " + key
			if (["class", "posDatas", "errors", "constraints", "processId", "cbdId", "pointDetailsId", "empty"].contains(key) || value == null){
				return
			}
			
			def methodName = "map" + key.capitalize()
			if (PdfMapper.metaClass.respondsTo(PdfMapper, methodName)) {
				PdfMapper."${methodName}"(data, pd, key, value)
				return
			}
			
			data.put(key, [value] as String[])
		}
		
		return data
	}
	
	static mapPosDataToPDFData(def pd) {
		Map<String, String[]> data = new HashMap<String, String[]>()
		
		pd.properties.each { key, value ->
			log.info "PosData Key: " + key
			if (["class", "cbdId", "process", "point", "errors", "constraints", "empty", "", ""].contains(key) || value == null){
				return
			}
			
			def methodName = "map" + key.capitalize()
			if (PdfMapper.metaClass.respondsTo(PdfMapper, methodName)) {
				PdfMapper."${methodName}"(data, pd, key, value)
				return
			}
			
			data.put(key, [value] as String[])
		}
		
		pd.posDetails?.properties.each { key, value ->
			log.info "PosDataDetails Key: " + key
			if (["class", "cbdId", "process", "point", "errors", "constraints", "empty", "", ""].contains(key) || value == null){
				return
			}
			
			def methodName = "map" + key.capitalize()
			if (PdfMapper.metaClass.respondsTo(PdfMapper, methodName)) {
				PdfMapper."${methodName}"(data, pd, key, value)
				return
			}
			
			data.put(key, [value] as String[])
		}
		
		return data
	}
	
	static mapProcessDataToPDFData(def pd) {
		Map<String, String[]> data = new HashMap<String, String[]>()

		pd.each { processData ->
			
			def methodName = "map" + processData.name.capitalize()+"Process"

            if (PdfMapper.metaClass.respondsTo(PdfMapper, methodName)) {
				PdfMapper."${methodName}"(data, pd, processData.name, processData.value)
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
	
	private static mapNrMerchanta(def data, def pd, def key, def value) {
		data.put(key+"1", [value.substring(0, 5)] as String[])
		data.put(key+"2", [value.substring(5, 10)] as String[])
		data.put(key+"3", [value.substring(10, 12)] as String[])
		data.put(key+"4", [value.substring(12, 15)] as String[])
	}
	
	private static mapUlicaDoKorespondencji(def data, def pd, def key, def value) {
		data.put(key, [pd.ulicaDoKorespondencjiTyp + " " + value] as String[])
	}
	
	private static mapUlicaDoKorespondencjiTyp(def data, def pd, def key, def value) {}
	
	private static mapScoringDochodowoscProcess(def data, def pd, def key, def value) {
		data.put("dochodowosc", [value] as String[])
	}

    private static mapReprezentant1ImieProcess(def data, def pd, def key, def value) {
        data.put("reprezentant1", [value + " " + getFromPointDataSet(pd, 'reprezentant1Nazwisko')] as String[])
    }

    private static mapReprezentant2ImieProcess(def data, def pd, def key, def value) {
        data.put("reprezentant2", [value + " " + getFromPointDataSet(pd, 'reprezentant2Nazwisko')] as String[])
    }

    private static mapAkceptantUlicaProcess(def data, def pd, def key, def value) {
        data.put("akceptantSiedziba", [getFromPointDataSet(pd, 'akceptantUlicaTytul') + " " + value + " " + getFromPointDataSet(pd, 'akceptantNrDomu') + "/" + getFromPointDataSet(pd, 'akceptantNrMieszkania') + " " + getFromPointDataSet(pd, 'akceptantMiasto')] as String[])
    }

    private static mapAkceptantNazwaOficjalnaProcess(def data, def pd, def key, def value) {
        data.put("akceptantNazwa", [value] as String[])
    }

    private static mapScoringTypPunktuProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["centrumHandlowe":"centrum_handlowe", "pawilonyHandlowe": "pawilony_handlowe", "budynekWolnoStojacy": "budynek_wolnostojacy", "osiedleMieszkaniowe": "osiedle_mieszkaniowe","targowisko": "targowisko", "inna": "inny"], value)
    }

    private static mapScoringOtwartyZamknietyProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["czynne": "czynne", "nieczynne": "nieczynne"], value)
    }

    private static mapScoringLokalizacjaPunktuProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["trasaPrzelotowa": "trasa_przelotowa", "centrumMiasta": "centrum_miasta", "peryferiaMiasta": "peryferia_miasta"], value)
    }

    private static mapScoringKoncesjaProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["dzialalnoscWymagaLicencjiTak": "true", "dzialalnoscWymagaLicencjiNie": "false"], value)
    }

    private static mapScoringAkceptacjaProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["akceptacjaKartPlatniczychTak": "true", "akceptacjaKartPlatniczychNie": "false"], value)
    }

    private static mapScoringMonitoringProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["wPunktachMonitoringTak": "true", "wPunktachMonitoringNie": "false"], value)
    }

    private static mapScoringDzialalnoscProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["handel":"handel", "uslugi":"uslugi"], value)
    }

    private static mapScoringWlasnoscProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["wlasnosc":"wlasnosc", "wynajem":"wynajem"], value)
    }

    private static mapScoringDzialalnoscCzasProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["powyzej5lat": "5<", "od1do5lat": "1-5", "ponizejRoku": "<1"], value)
    }

    private static mapScoringWielkoscPunktuProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["powyzej400m2": "400<", "od50do400m2": "50-400", "do50m2": "<50"], value)
    }

    private static mapScoringWielkoscMiejscowosciProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["miastoPonad500tysChb": "500<", "miastoOd100Do500tysChb": "100-500", "miastoOd50Do90tysChb": "50-99", "miastoPonizej50tysChb": "<50", "wies": "wies"], value)
    }

    private static mapScoringCharakterystykaProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["salon":"salon", "sklep":"sklep", "stoisko":"stoisko", "stacjaPaliw":"stacja_paliw", "inny":"inny"], value)
    }

    private static mapScoringCzestoscTransakcjiProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["kilkaRazyWMiesiacu":"kilka_miesiecznie", "kilkaRazyWTygodniu":"kilka_tygodniowo", "coDrugiDzien":"co_drugi_dzien", "codziennie":"codziennie"], value)
    }

    private static mapScoringIloscTransakcjiProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["od0do4":"0-4", "od5do10":"5-10", "powyzej10":"<10"], value)
    }

    private static mapScoringSprzedazTowarowEkskluzywnychProcess(def data, def pd, def key, def value) {
        addCheckbox(data, 'sprzedazTowarowEkskluzywnych', 'true', value);
        println 'Working with property: ' + key
    }

    private static mapScoringPonad50ProcentObrotowWNocyProcess(def data, def pd, def key, def value) {
        addCheckbox(data, 'ponad50ProcentObrotowWNocy', 'true', value);
        println 'Working with property: ' + key
    }

    private static mapScoringRuchTurystycznyPrzygranicznyProcess(def data, def pd, def key, def value) {
        addCheckbox(data, 'ruchTurystycznyPrzygraniczny', 'true', value);
        println 'Working with property: ' + key
    }

    private static mapScoringUslugiPlatneZGoryProcess(def data, def pd, def key, def value) {
        println 'Working with property: ' + key
        addCheckbox(data, 'uslugiPlatneZGory', 'true', value);
    }

    private static mapScoringStanZadbanyProcess(def data, def pd, def key, def value) {
        println 'Working with property: ' + key
        addCheckbox(data, key, 'true', value);
    }
	
	private static mapDccZakresUruchomieniaProcess(def data, def pd, def key, def value) {
		addCheckboxes(data, ["noweZestPos":"obecne_i_nowe", "obecneZestPos":"obecne", "phu":"wskazane"], value)
	}

    private static mapinformacjaHandlowaProcess(def data, def pd, def key, def value) {
        println "DUPA - przerabiam informacjaHandlowa - DUPA"

        addCheckboxes(data, ["informacjaHandlowaTak": "true", "informacjaHandlowaNie": "false"], value)
    }

    private static mapObslugaTypProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["obsugaPrestiz": "prestige", "obslugaKomfort": "comfort", "obslugaEkonomiczny": "economic"], value)
    }

    private static mapUmowaCzasProcess(def data, def pd, def key, def value) {
        addCheckboxes(data, ["umNieOzn": "nieoznaczony", "umOzn": "oznaczony"], value)
    }

    private static getFromPointDataSet(def pd, def key){
        def result = pd.find{ processData -> processData.name.equals(key)}
        (result && result?.value)?result?.value:""
    }

    private static addCheckbox(def data, def pdfName, def fieldValue, def value){
        data.put(pdfName, [fieldValue.equals(value), "", "checkbox"] as String[])
    }

    private static addCheckboxes(def data, def pdfKeyValue, def value){
        pdfKeyValue.each{ k, v ->  data.put(k, [v.equals(value), "", "checkbox"] as String[])}
    }
}
