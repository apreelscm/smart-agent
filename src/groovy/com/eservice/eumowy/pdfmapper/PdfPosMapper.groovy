package com.eservice.eumowy.pdfmapper

class PdfPosMapper extends AbstractPdfMapper{

    private static EXCLUDE_FROM_POS = ["class", "cbdId", "process", "point", "errors", "constraints", "empty", ""]
    private static EXCLUDE_FROM_POS_DETAILS = ["class", "cbdId", "process", "point", "errors", "constraints", "empty"]

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
	
	//----------------------CHECKBOX PRZENOSNY-------------------------
	
	private mapGprsTypPosDataDetails(def data, def posesData, def key, def value){
		data.put(key, [value] as String[]);
		if ("Verifone Vx670 GPRS".equals(value)){
			addCheckbox(data, "przenosnyDol", true, true);
		}
	}
}
