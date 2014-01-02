package com.eservice.eumowy.pdfmapper

import com.eservice.eumowy.util.DateUtils

class PdfPosMapper extends AbstractPdfMapper{

    private static EXCLUDE_FROM_POS = ["class", "cbdId", "process", "point", "errors", "constraints", "empty", ""]
    private static EXCLUDE_FROM_POS_DETAILS = ["class", "cbdId", "process", "point", "errors", "constraints", "empty", "dialupTyp", "dialupCena", "vpnTyp", "vpnCena", "sslTyp", "sslCena", "wifiTyp", "wifiCena", "gprsCena"]

	public static final ZERO_VALUES = ["", "0"]

    public def mapPosSpecial(def poses) {
        LOG.info('Zaczynam Mapowac!!!!')

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
            data.put("dataPoczatkuUzywaniaPOZ", [DateUtils.getFormattedDate(newPoses[0].dataOd, DateUtils.DD_MM_YYYY)] as String[])
            data.put("dataKoncaUzywaniaPOZ", [DateUtils.getFormattedDate(newPoses[0].dataDo, DateUtils.DD_MM_YYYY)] as String[])

            newPoses.eachWithIndex{ pos, i ->
                data.put("numerPOS"+suffixes[i], [pos.numerZestawuPos] as String[])
                data.put("oplataPOS"+suffixes[i], [pos.wysokoscOplaty] as String[])
            }
        }
        LOG.info('Koncze Mapowac!!!!')
        return data
    }

    public def mapPosesNotFromCBD(def poses){
        def resultNormalMap = new TreeMap<Integer, BigDecimal>();
        def resultPrefMap = new TreeMap<Integer, BigDecimal>();
        poses?.each { pos ->
			if (pos == null)
				return
				
            pos.posDetails?.each { posDetail ->
                addToPosMap(resultNormalMap, posDetail.dialupIlosc, posDetail.dialupCena, posDetail.dialupPPCena)
                addToPosMap(resultNormalMap, posDetail.vpnIlosc, posDetail.vpnCena, posDetail.vpnPPCena)
                addToPosMap(resultNormalMap, posDetail.sslIlosc, posDetail.sslCena, posDetail.sslPPCena)
                addToPosMap(resultNormalMap, posDetail.gprsIlosc, posDetail.gprsCena, posDetail.gprsPPCena)
                addToPosMap(resultNormalMap, posDetail.pinPadIlosc, posDetail.pinPadCena, 0)
                addToPosMap(resultNormalMap, posDetail.wifiIlosc, posDetail.wifiCena, 0)

                addToPosMap(resultPrefMap, posDetail.dialupIlosc, posDetail.dialupCenaPreferencyjna, posDetail.dialupPPCenaPreferencyjna)
                addToPosMap(resultPrefMap, posDetail.vpnIlosc, posDetail.vpnCenaPreferencyjna, posDetail.vpnPPCenaPreferencyjna)
                addToPosMap(resultPrefMap, posDetail.sslIlosc, posDetail.sslCenaPreferencyjna, posDetail.sslPPCenaPreferencyjna)
                addToPosMap(resultPrefMap, posDetail.gprsIlosc, posDetail.gprsCenaPreferencyjna, posDetail.gprsPPCenaPreferencyjna)
                addToPosMap(resultPrefMap, posDetail.pinPadIlosc, posDetail.pinPadCenaPreferencyjna, 0)
                addToPosMap(resultPrefMap, posDetail.wifiIlosc, posDetail.wifiCenaPreferencyjna, 0)
            }
        }
        return [normalResult: resultNormalMap, prefResult: resultPrefMap]
    }

    public def mapPosesDataToPDFData(def posesData) {
        def data = [:]
        posesData?.each { pos ->
			if (pos == null)
				return
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

    private mapKartaSimTypPosDataDetails(def data, def posesData, def key, def value){
        if (value !=null) {
            if ("Centertel".equals(value)){
                //Orange
                data.put('simPlus', ['_____'] as String[]);
                data.put('simEra', ['_____'] as String[]);
            } else if ("Polkomtel".equals(value)){
                //Polkomtel
                data.put('simOrange', ['_____'] as String[]);
                data.put('simEra', ['_____'] as String[]);
            } else if ("ERA".equals(value)){
                //T-Mobile
                data.put('simPlus', ['_____'] as String[]);
                data.put('simOrange', ['_____'] as String[]);
            }
        }
    }

	//----------------------CHECKBOX PRZENOSNY-------------------------
	
	private mapGprsTypPosDataDetails(def data, def posesData, def key, def value){
		if ("Verifone Vx670 GPRS".equals(value)){
			addCheckbox(data, "przenosnyDol", true, true);
		}
	}

    private def addToPosMap(def resultMap, def count, def price, def pricePP) {

        if (count > 0){
            def priceSum = 0;
            if (price > 0){
                priceSum += price
            }
            if (pricePP > 0){
                priceSum += pricePP
            }

            if (priceSum > 0){
                resultMap.put(priceSum, count + (resultMap.containsKey(priceSum) ? resultMap.get(priceSum) : 0))
            }
        }
    }

    private def addToData(def data, def resultMap, def countPdfFieldName, def pricePdfFieldName, def suffixes) {
        resultMap.eachWithIndex{ key, value, index ->
            if (index < suffixes.size()){
                data.put(countPdfFieldName+suffixes[index], [value.toString()] as String[])
                data.put(pricePdfFieldName+suffixes[index], [key.toString()] as String[])
            }
        }
    }
}
