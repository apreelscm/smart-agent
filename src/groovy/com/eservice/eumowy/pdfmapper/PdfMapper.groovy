package com.eservice.eumowy.pdfmapper

import com.eservice.eumowy.PointData;

class PdfMapper {

	static mapAllDataToPDFData(def process, def pd) {
		def pointsAndPosDataMap = mapPointAndPosDataToPDFData(pd)
		def processDataMap = mapProcessDataToPDFData(process)
		pointsAndPosDataMap.putAll(processDataMap)
		
		return pointsAndPosDataMap
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
			log.info "Key: " + key
			if (["class", "posDatas", "errors", "constraints", "", "", ""].contains(key) || value == null){
				return
			}
			
			if (PdfMapper.metaClass.respondsTo(PdfMapper, "map" + key.capitalize())) {
				PdfMapper."map${key.capitalize()}"(data, pd, key, value)
				return
			}
			
			data.put(key, [value] as String[]);
		}
		
		return data
	}
	
	static mapPosDataToPDFData(def pd) {
		Map<String, String[]> data = new HashMap<String, String[]>()
		
		pd.properties.each { key, value ->
			log.info "Key: " + key
			if (["class", "cbdId", "process", "point", "errors", "constraints", "", "", ""].contains(key) || value == null){
				return
			}
			
			if (PdfMapper.metaClass.respondsTo(PdfMapper, "map" + key.capitalize())) {
				PdfMapper."map${key.capitalize()}"(data, pd, key, value)
				return
			}
			
			data.put(key, [value] as String[]);
		}
		
		return data
	}
	
	static mapProcessDataToPDFData(def pd) {
		Map<String, String[]> data = new HashMap<String, String[]>()
		
		pd.each { processData ->
			
			if (PdfMapper.metaClass.respondsTo(PdfMapper, "map" + processData.name.capitalize())) {
				PdfMapper."map{$processData.name.capitalize()}"(data, processData, processData.name, processData.value)
				return
			}
			
			if ("true".equals(processData.value) == true || "false".equals(processData.value) == true) {
				data.put(processData.name, [processData.value, "", "checkbox"] as String[])
			}
			else {
				data.put(processData.name, [processData.value] as String[])
			}
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
}
