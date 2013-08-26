package com.eservice.eumowy.pdfmapper

import com.eservice.eumowy.PointData;

class PdfMapper {

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
