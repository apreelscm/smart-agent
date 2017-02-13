package com.eservice.eumowy.pdfmapper

import com.eservice.eumowy.PosData
import com.eservice.eumowy.PosDataDetails
import com.eservice.eumowy.util.DateUtils

class PdfPosMapper extends AbstractPdfMapper{

    private static EXCLUDE_FROM_POS_DETAILS = ["class", "cbdId", "process", "point", "errors", "constraints", "empty", "dialupTyp", "dialupCena", "vpnTyp", "vpnCena", "sslTyp", "sslCena", "wifiTyp", "wifiCena", "gprsCena"]

	public static final ZERO_VALUES = ["", "0"]

    public Map mapPosesNotFromCBD(List<PosData> poses){
        Map result = new TreeMap<Integer, BigDecimal>()

        poses?.each { pos ->
			if (pos == null)
				return
				
            pos.posDetails?.each { PosDataDetails posDetail ->
                addToPosMap(result, posDetail.dialupIlosc, posDetail.dialupCena)
                addToPosMap(result, posDetail.dialupPPIlosc, posDetail.dialupPPCena)
                addToPosMap(result, posDetail.vpnIlosc, posDetail.vpnCena)
                addToPosMap(result, posDetail.vpnPPIlosc, posDetail.vpnPPCena)
                addToPosMap(result, posDetail.sslIlosc, posDetail.sslCena)
                addToPosMap(result, posDetail.sslPPIlosc, posDetail.sslPPCena)
                addToPosMap(result, posDetail.gprsIlosc, posDetail.gprsCena)
                addToPosMap(result, posDetail.gprsPPIlosc, posDetail.gprsPPCena)
                addToPosMap(result, posDetail.gprsIloscPortable, posDetail.gprsCenaPortable)
                addToPosMap(result, posDetail.pinPadIlosc, posDetail.pinPadCena)
            }
        }

        return result
    }

    public def mapPosesDataToPDFData(def posesData) {
        def data = [:]
        posesData?.each { pos ->
			if (!pos) return
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

    private mapIntegracjaRsPosDataDetails(def data, def posesData, def key, def value) {
        addCheckbox(data, key, true, value);
    }

    private mapIntegracjaUsbPosDataDetails(def data, def posesData, def key, def value) {
        addCheckbox(data, key, true, value);
    }

    private mapIntegracjaEthPosDataDetails(def data, def posesData, def key, def value) {
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

    private mapPlanowanaDataInstalacjiPosDataDetails(def data, def posesData, def key, def value) {
        addDateField(data, key, DateUtils.formatWithTimezone(value))

        addSeparatedDateFields(data, data.get(key)[0], "dataInstalacji")
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
			data.put("dialupPPCena", [(getFromPosDataDetails(posesData, 'dialupPPCena'))] as String[])
			data.put("dialupPPTyp", [(getFromPosDataDetails(posesData, 'dialupPPTyp'))] as String[])
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
			data.put("vpnPPCena", [(getFromPosDataDetails(posesData, 'vpnPPCena'))] as String[])
			data.put("vpnPPTyp", [(getFromPosDataDetails(posesData, 'vpnPPTyp'))] as String[])
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
			data.put("sslPPCena", [(getFromPosDataDetails(posesData, 'sslPPCena'))] as String[])
			data.put("sslPPTyp", [(getFromPosDataDetails(posesData, 'sslPPTyp'))] as String[])
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
			data.put("gprsPPCena", [(getFromPosDataDetails(posesData, 'gprsPPCena'))] as String[])
			data.put("gprsPPTyp", [(getFromPosDataDetails(posesData, 'gprsPPTyp'))] as String[])
		}
	}

    private mapGprsIloscPortablePosDataDetails(def data, def posesData, def key, def value){
        if(value !=null && !ZERO_VALUES.contains(value)){
            data.put(key, [value] as String[]);
            data.put("gprsCenaPortable", [(getFromPosDataDetails(posesData, 'gprsCenaPortable'))] as String[])
            data.put("gprsTypPortable", [(getFromPosDataDetails(posesData, 'gprsTypPortable'))] as String[])
        }
    }
	
	private mapBazaIloscPosDataDetails(def data, def posesData, def key, def value){
		if(value !=null && !ZERO_VALUES.contains(value)){
			data.put(key, [value] as String[]);
		}
	}

    private mapKartaSimTypPosDataDetails(Map data, PosData posData, String key, String value) {
        boolean isStationary = posData.posDetails.isStationary()
        boolean isPortable = posData.posDetails.isPortable()

        if (isStationary || isPortable) {
            if ("Centertel".equals(value)) {
                if (isStationary) {
                    data.put('simPlus', ['_____'] as String[]);
                    data.put('simEra', ['_____'] as String[]);
                }
                if (isPortable) {
                    data.put('simPlusPortable', ['_____'] as String[]);
                    data.put('simEraPortable', ['_____'] as String[]);
                }
            } else if ("Polkomtel".equals(value)) {
                if (isStationary) {
                    data.put('simOrange', ['_____'] as String[]);
                    data.put('simEra', ['_____'] as String[]);
                }
                if (isPortable) {
                    data.put('simOrangePortable', ['_____'] as String[]);
                    data.put('simEraPortable', ['_____'] as String[]);
                }
            } else if ("ERA".equals(value)) {
                if (isStationary) {
                    data.put('simPlus', ['_____'] as String[]);
                    data.put('simOrange', ['_____'] as String[]);
                }
                if (isPortable) {
                    data.put('simPlusPortable', ['_____'] as String[]);
                    data.put('simOrangePortable', ['_____'] as String[]);
                }
            }
        }
    }

	//----------------------CHECKBOX PRZENOSNY-------------------------

    private def addToPosMap(def resultMap, def count, def price) {

        if (count > 0){
            def priceSum = 0;
            if (price > 0){
                priceSum += price
            }

            if (priceSum > 0) {
                resultMap.put(priceSum, count + (resultMap.containsKey(priceSum) ? resultMap.get(priceSum) : 0))
            }
        }
    }
}
