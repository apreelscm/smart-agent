package com.eservice.eumowy.pdfmapper

import com.eservice.eumowy.util.DateUtils

class PdfPosMapper extends AbstractPdfMapper{

    private static EXCLUDE_FROM_POS = ["class", "cbdId", "process", "point", "errors", "constraints", "empty", ""]
    private static EXCLUDE_FROM_POS_DETAILS = ["class", "cbdId", "process", "point", "errors", "constraints", "empty", "dialupTyp", "dialupCena", "vpnTyp", "vpnCena", "sslTyp", "sslCena", "wifiTyp", "wifiCena", "gprsCena"]

	public static final ZERO_VALUES = ["", "0"]

    public def mapPosSpecial(def poses) {
        println 'Zaczynam Mapowac!!!!'

        def suffixes = ['A', 'B', 'C', 'D', 'E']

        def newPoses = [];
        if (poses.size()>suffixes.size()){
            LOG.info('To much poses - ' + poses.size() + '. Trimming to: ' + suffixes.size());
            newPoses.addAll(poses.subList(0, suffixes.size()))
        } else {
            newPoses.addAll(poses)
        }

        def data = [:];
        if (newPoses.size()>0){
            data.put("dataPoczatkuUzywaniaPOZ", [DateUtils.parseDate(newPoses[0].dataOd)] as String[])
            data.put("dataKoncaUzywaniaPOZ", [DateUtils.parseDate(newPoses[0].dataDo)] as String[])

            newPoses.eachWithIndex{ pos, i ->
                data.put("numerPOS"+suffixes[i], [pos.numerZestawuPos] as String[])
                data.put("oplataPOS"+suffixes[i], [pos.wysokoscOplaty] as String[])
            }
        }
        return data
        println 'Koncze Mapowac!!!!'
    }

    public def mapPosesDataToPDFData(def posesData) {
        def data = [:]
        posesData?.each { pos ->
            //gdy bedzie potrzeba pobrania danych bezposrednio z POS, trzeba odhashowac ponizsza linijke
            //mapperClosure(pos.properties, data, "Pos", EXCLUDE_FROM_POS, pos);
            mapperClosure(pos.posDetails?.properties, data, "PosDataDetails", EXCLUDE_FROM_POS_DETAILS, pos);
        }
        data
    }

    private mapperClosure = { source, data, methodSuffix, exclude, pos ->
        source.each { key, value ->
            if (exclude.contains(key) || value == null){
                return
            }

            def methodName = "map" + key.capitalize() + methodSuffix
            if (PdfPosMapper.metaClass.respondsTo(this, methodName)) {
                this."${methodName}"(data, pos, key, value)
                return
            }
            data.put(key, [value] as String[])
        }
    }

    private mapPreautoryzacjaPosDataDetails(def data, def posesData, def key, def value) {
        addCheckbox(data, key, true, value);
    }

    private mapBrakFunkcjiZwrotuPosDataDetails(def data, def posesData, def key, def value) {
        addCheckbox(data, key, true, value);
    }

    private mapZwrotNaHasloPosDataDetails(def data, def posesData, def key, def value) {
        addCheckbox(data, key, true, value);
    }

    private mapAnalizaZbioruPosDataDetails(def data, def posesData, def key, def value) {
        addCheckbox(data, key, true, value);
    }

    private mapIntegracjaZSysKasPosDataDetails(def data, def posesData, def key, def value) {
        addCheckbox(data, key, true, value);
    }

    private mapZwrotyIKOPosDataDetails(def data, def posesData, def key, def value) {
        addCheckbox(data, key, true, value);
    }

    private mapLogowaniePrzedKazdaTransakcjaPosDataDetails(def data, def posesData, def key, def value) {
        addCheckbox(data, key, true, value);
    }

    private mapLogowanieZmianowePosDataDetails(def data, def posesData, def key, def value) {
        addCheckbox(data, key, true, value);
    }

    private mapNapiwek1PosDataDetails(def data, def posesData, def key, def value) {
        addCheckbox(data, key, true, value);
    }

    private mapTelePompkaPosDataDetails(def data, def posesData, def key, def value) {
        addCheckbox(data, key, true, value);
    }

    private mapTeleKodzikPosDataDetails(def data, def posesData, def key, def value) {
        addCheckbox(data, key, true, value);
    }

    private mapKartaPodarunkowaPosDataDetails(def data, def posesData, def key, def value) {
        addCheckbox(data, key, true, value);
    }

    private mapInneWyposazenieSslPosDataDetails(def data, def posesData, def key, def value) {
        addCheckbox(data, key, true, value);
    }

    private mapInneWyposazenieGprsPosDataDetails(def data, def posesData, def key, def value) {
        addCheckbox(data, key, true, value);
    }

    private mapZamkniecieDniaOdPosDataDetails(def data, def posesData, def key, def value){
        mapWithPattern(data, value, ~/\d{2}:\d{2}/, ":", "zamkniecieDniaOd");
    }

    private mapZamkniecieDniaDoPosDataDetails(def data, def posesData, def key, def value){
        mapWithPattern(data, value, ~/\d{2}:\d{2}/, ":", "zamkniecieDniaDo");
    }

    private mapImieInformatykStatycznaPosDataDetails(def data, def posesData, def key, def value){
        data.put("imieINazwiskoInformatykStatyczna", [value + " " + getFromPosDataDetails(posesData, 'nazwiskoInformatykStatyczna')] as String[]);
    }

    private mapImieInformatykDynamicznaPosDataDetails(def data, def posesData, def key, def value){
        data.put("imieINazwiskoInformatykDynamiczna", [value + " " + getFromPosDataDetails(posesData, 'nazwiskoInformatykDynamiczna')] as String[]);
    }

    private getFromPosDataDetails(def posesData, def key){
        getPropertyFromObject(posesData?.posDetails, key)
    }
	
	//--------------------------ZESTAW POS-----------------------------
	private mapDialupIloscPosDataDetails(def data, def posesData, def key, def value){
		if(value !=null && !ZERO_VALUES.contains(value)){
			data.put(key, [value] as String[]);
			data.put("dialupCena", [(getFromPosDataDetails(posesData, 'dialupCena'))] as String[])
			data.put("dialupTyp", [(getFromPosDataDetails(posesData, 'dialupTyp'))] as String[])
		}
	}
	
	private mapDialupIloscPPPosDataDetails(def data, def posesData, def key, def value){
		if(value !=null && !ZERO_VALUES.contains(value)){
			data.put(key, [value] as String[]);
			data.put("dialupCena", [(getFromPosDataDetails(posesData, 'dialupCena'))] as String[])
			data.put("dialupTyp", [(getFromPosDataDetails(posesData, 'dialupTyp'))] as String[])
		}
	}
	
	private mapVpnIloscPosDataDetails(def data, def posesData, def key, def value){
		if(value !=null && !ZERO_VALUES.contains(value)){
			data.put(key, [value] as String[]);
			data.put("vpnCena", [(getFromPosDataDetails(posesData, 'vpnCena'))] as String[])
			data.put("vpnTyp", [(getFromPosDataDetails(posesData, 'vpnTyp'))] as String[])
		}
	}
	
	private mapVpnIloscPPPosDataDetails(def data, def posesData, def key, def value){
		if(value !=null && !ZERO_VALUES.contains(value)){
			data.put(key, [value] as String[]);
			data.put("vpnCena", [(getFromPosDataDetails(posesData, 'vpnCena'))] as String[])
			data.put("vpnTyp", [(getFromPosDataDetails(posesData, 'vpnTyp'))] as String[])
		}
	}
	
	private mapSslIloscPosDataDetails(def data, def posesData, def key, def value){
		if(value !=null && !ZERO_VALUES.contains(value)){
			data.put(key, [value] as String[]);
			data.put("sslCena", [(getFromPosDataDetails(posesData, 'sslCena'))] as String[])
			data.put("sslTyp", [(getFromPosDataDetails(posesData, 'sslTyp'))] as String[])
		}
	}
	
	private mapSslIloscPPPosDataDetails(def data, def posesData, def key, def value){
		if(value !=null && !ZERO_VALUES.contains(value)){
			data.put(key, [value] as String[]);
			data.put("sslCena", [(getFromPosDataDetails(posesData, 'sslCena'))] as String[])
			data.put("sslTyp", [(getFromPosDataDetails(posesData, 'sslTyp'))] as String[])
		}
	}
	
	private mapWifiIloscPosDataDetails(def data, def posesData, def key, def value){
		if(value !=null && !ZERO_VALUES.contains(value)){
			data.put(key, [value] as String[]);
			data.put("wifiCena", [(getFromPosDataDetails(posesData, 'wifiCena'))] as String[])
			data.put("wifiTyp", [(getFromPosDataDetails(posesData, 'wifiTyp'))] as String[])
		}
	}
	
	private mapWifiIloscPPPosDataDetails(def data, def posesData, def key, def value){
		if(value !=null && !ZERO_VALUES.contains(value)){
			data.put(key, [value] as String[]);
			data.put("wifiCena", [(getFromPosDataDetails(posesData, 'wifiCena'))] as String[])
			data.put("wifiTyp", [(getFromPosDataDetails(posesData, 'wifiTyp'))] as String[])
		}
	}
	
	private mapGprsIloscPosDataDetails(def data, def posesData, def key, def value){
		if(value !=null && !ZERO_VALUES.contains(value)){
			data.put(key, [value] as String[]);
			data.put("gprsCena", [(getFromPosDataDetails(posesData, 'gprsCena'))] as String[])
			data.put("gprsTyp", [(getFromPosDataDetails(posesData, 'gprsTyp'))] as String[])
		}
	}
	
	private mapGprsIloscPPPosDataDetails(def data, def posesData, def key, def value){
		if(value !=null && !ZERO_VALUES.contains(value)){
			data.put(key, [value] as String[]);
			data.put("gprsCena", [(getFromPosDataDetails(posesData, 'gprsCena'))] as String[])
			data.put("gprsTyp", [(getFromPosDataDetails(posesData, 'gprsTyp'))] as String[])
		}
	}
	
	private mapBazaIloscPosDataDetails(def data, def posesData, def key, def value){
		if(value !=null && !ZERO_VALUES.contains(value)){
			data.put(key, [value] as String[]);
		}
	}
	
	//----------------------CHECKBOX PRZENOSNY-------------------------
	
	private mapGprsTypPosDataDetails(def data, def posesData, def key, def value){
		if ("Verifone Vx670 GPRS".equals(value)){
			addCheckbox(data, "przenosnyDol", true, true);
		}
	}
}
