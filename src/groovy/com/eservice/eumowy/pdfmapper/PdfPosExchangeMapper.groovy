package com.eservice.eumowy.pdfmapper

import com.eservice.eumowy.PosExchange

class PdfPosExchangeMapper extends AbstractPdfMapper{

    protected def mapOnlySelectedPosExchanges(PosExchange posExchange){
        def data = [:]
        data.put("numerPos1", [posExchange.posNumber] as String[])
        log.info "PosExchange numerPos1: " + posExchange.posNumber

        if ("PINPAD".equals(posExchange.newType)){
            // ustawiamy PINPAD w pierwszym wolnym polu... :|
            // https://ext.apreel.com/youtrack/issue/eUmowy_ext-552#comment=61-11776
            data.put('dialupTyp', [posExchange.newModel] as String[])
            data.put('dialupPPIlosc', ['1'] as String[])
        } else {
            data.put(getCorrectPlaceholder(posExchange.newType, posExchange.newModel, "Typ"), [posExchange.newModel] as String[])
            data.put(getCorrectPlaceholder(posExchange.newType, posExchange.newModel, "Ilosc"), ["1"] as String[])
            data.putAll(mapSimType(posExchange.newModel, posExchange.simType))

            boolean isPortable = isPortable(posExchange.newModel)

            addCheckbox(data, "posStacjonarny", false, isPortable)
            addCheckbox(data, "posPrzenosny", true, isPortable)

            if (isPortable){
                data.put('przenosnyBaza', ['1'] as String[])
            }
        }

        data
    }

    def addCheckbox(def data, def pdfName, def fieldValue, def value){
        data.put(pdfName, [fieldValue.equals(value), "", "checkbox"] as String[])
    }

    private def getCorrectPlaceholder(String posType, String posModel, String suffix) {
        String placeholder = posType.toLowerCase()+suffix
        getCorrectPlaceholder(placeholder, posModel)
    }

    private def getCorrectPlaceholder(String placeholder, String posModel){
        isPortable(posModel)?"przenosny"+placeholder.capitalize():placeholder
    }

    private def isPortable(String posModel) {
        return (posModel.contains("VX670") || posModel.contains("IWL220C"))
    }

    private def mapSimType(def posModel, def value){
        def tempData = [:]
        if (value !=null) {
            if ("Centertel".equals(value)){
                //Orange
                tempData.put(getCorrectPlaceholder('simPlus', posModel), ['_____'] as String[])
                tempData.put(getCorrectPlaceholder('simEra', posModel), ['_____'] as String[])
            } else if ("Polkomtel".equals(value)){
                //Polkomtel
                tempData.put(getCorrectPlaceholder('simOrange', posModel), ['_____'] as String[])
                tempData.put(getCorrectPlaceholder('simEra', posModel), ['_____'] as String[])
            } else if ("ERA".equals(value)){
                //T-Mobile
                tempData.put(getCorrectPlaceholder('simPlus', posModel), ['_____'] as String[])
                tempData.put(getCorrectPlaceholder('simOrange', posModel), ['_____'] as String[])
            }
        }
        tempData
    }
}
