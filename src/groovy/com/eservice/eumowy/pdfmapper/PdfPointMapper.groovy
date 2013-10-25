package com.eservice.eumowy.pdfmapper

class PdfPointMapper extends AbstractPdfMapper{

    static def EXCLUDE_FROM_POINT = ["class", "posDatas", "errors", "constraints", "process", "processId", "cbdId", "pointDetails", "empty"];
    static def EXCLUDE_FROM_POINT_DATA_DETAILS = ["class", "posDatas", "errors", "constraints", "processId", "cbdId", "pointDetailsId", "empty"];

    static def ALLOW_NULL_POINT = [];
    static def ALLOW_NULL_POINT_DATA_DETAILS = ["wydrukUlica", "wydrukMiasto", "wydrukPoczta", "wydrukNrDomu", "wydrukNrLokalu", "wydrukKodPocztowy",
                                                "nazwaDoWydrukuZTerminalaPos", "nipPunktu"];

    public def mapPointsSpecial(def points, def mapping) {
        def data = [:];
        def myIndex = 1;
        points?.each { point ->
            point.properties.each { key, value ->
                if (key != null && mapping.containsKey(key)){
                    def methodName = "map" + key.capitalize() + "Point"
                    if (PdfPointMapper.metaClass.respondsTo(this, methodName)) {
                        this."${methodName}"(data, point, mapping.get(key), value, myIndex)
                    }
                }
            }
            myIndex++
        }
        return data
    }

    public def mapPointDataToPDFData(def pointData) {
        def data = [:];
        mapperClosure(pointData.properties, data, "Point", EXCLUDE_FROM_POINT, ALLOW_NULL_POINT, pointData, true)
        mapperClosure(pointData.pointDetails?.properties, data, "PointDataDetails", EXCLUDE_FROM_POINT_DATA_DETAILS, ALLOW_NULL_POINT_DATA_DETAILS, pointData, false)
        return data
    }

    private def mapperClosure = {source, data, methodSuffix, exclude, allowNull, pd, withIndex ->
        source.each { key, value ->
            if ((!allowNull.contains(key) && value == null) || exclude.contains(key)){
                return
            } else {
                def methodName = "map" + key.capitalize() + methodSuffix
                if (PdfPointMapper.metaClass.respondsTo(this, methodName)) {
                    if (withIndex){
                        this."${methodName}"(data, pd, key, value, -1)
                    } else {
                        this."${methodName}"(data, pd, key, value)
                    }
                    return
                }
                data.put(key, [value] as String[])
            }
        }
        return data
    }

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

    private mapSystemKasowyPoint(def data, def pd, def key, def value, def index) {
        mapYesNoField(data, (index == -1)?key:key+index, value);
    }

    private mapUtaPoint(def data, def pd, def key, def value, def index) {
        mapYesNoField(data, (index == -1)?key:key+index, value);
    }


    //------------------PointDataDetails----------------------------

    private mapWydrukUlicaPointDataDetails(def data, def pointData, def key, def value){
        data.put(key, [pointData.cbdId == null ? value : pointData.ulica] as String[]);
    }

    private mapWydrukMiastoPointDataDetails(def data, def pointData, def key, def value){
        data.put(key, [pointData.cbdId == null ? value : pointData.miejscowosc] as String[]);
    }

    private mapWydrukPocztaPointDataDetails(def data, def pointData, def key, def value){
        data.put(key, [pointData.cbdId == null ? value : pointData.poczta] as String[]);
    }

    private mapWydrukNrDomuPointDataDetails(def data, def pointData, def key, def value){
        data.put(key, [pointData.cbdId == null ? value : pointData.nrBudynku] as String[]);
    }

    private mapWydrukNrLokaluPointDataDetails(def data, def pointData, def key, def value){
        data.put(key, [pointData.cbdId == null ? value : pointData.nrLokalu] as String[]);
    }

    private mapWydrukKodPocztowyPointDataDetails(def data, def pointData, def key, def value){
        data.put(key, [value] as String[]);
        mapWithPattern(data, pointData.cbdId == null ? value : pointData.kodPocztowy, ~/\d{2}-\d{3}/, "-", "wydrukKodPocztowy");
    }

    private mapNipPunktuPointDataDetails(def data, def pointData, def key, def value){
        data.put(key, [pointData.cbdId == null ? value : pointData.nip] as String[]);
    }

    private mapNazwaDoWydrukuZTerminalaPosPointDataDetails(def data, def pointData, def key, def value){
        data.put(key, [pointData.cbdId == null ? value : pointData.nazwa] as String[]);
    }

    private mapKorespondencjaKodPocztowyPointDataDetails(def data, def pointData, def key, def value){
        data.put(key, [value] as String[]);
        mapWithPattern(data, value, ~/\d{2}-\d{3}/, "-", "korespondencjaKodPocztowy");
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

	//----------------------CHECKBOX PRZENOSNY-------------------------
	
	private mapGprsTypPointDataDetails(def data, def pointData, def key, def value){
		data.put(key, [value] as String[]);
		if ("Verifone Vx670 GPRS".equals(value)){
			addCheckbox(data, "przenosnyDol", true, true);
		}
	}
	
    //----------------------------UTILS--------------------------------

    private mapYesNoField(def data, def key, def value){
        data.put(key, [value?"TAK":"NIE"] as String[])
    }

    private getFromPointDataDetails(def pointData, def key){
        getPropertyFromObject(pointData?.pointDetails, key)
    }

}
