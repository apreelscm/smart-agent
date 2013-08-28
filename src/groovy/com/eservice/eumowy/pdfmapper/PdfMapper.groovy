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

    private static getFromPointDataSet(def pd, def key){
        def result = pd.find{ processData -> processData.name.equals(key)}
        (result && result?.value)?result?.value:""
    }
}
