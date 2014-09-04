package com.eservice.eumowy
import com.eservice.eumowy.dao.CbdDAO
import com.eservice.eumowy.util.EumowyCustomEnvironment
import grails.util.Environment
import org.apache.commons.collections.CollectionUtils
import org.apache.commons.collections.Predicate
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

class DictionaryService {

    CbdDAO cbdDAO

    CbdService cbdService

    def dictionary = [:]

    enum PosType {
        STATIONARY("stacjonarny"), PORTABLE("przenośny")

        public String posType;
        public PosType(String posType) {
            this.posType = posType;
        }
    }

    private static final def DICTIONARY_PATH = "dictionary/"

    public static final def GET_ULICA_COMBOBOX = "getUlicaComboBox"
    public static final def GET_POS_TYPE_COMBOBOX = "getPosTypeComboBox"
    public static final def GET_EXT_POS_TYPE_COMBOBOX = "getExtendedPosTypeComboBox"
    public static final def GET_CBD_POINTS_COMBOBOX = "getCbdPointsComboBox"
    public static final def GET_SIM_CARD_COMBOBOX = "getSimCardComboBox"
	public static final def GET_MCC_COMBOBOX = "getMccComboBox"

    //@Cacheable(value="getUlicaComboBox")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_UNCOMMITTED, readOnly = true)
    def getUlicaComboBox() {
        getFromDictionary(GET_ULICA_COMBOBOX, [])
    }

    def getSimCardComboBox() {
        getFromDictionary(GET_SIM_CARD_COMBOBOX, [])
    }

    List getPosTypeComboBox(def medium) {
        cbdService.getPosTypes(DICTIONARY_PATH + GET_POS_TYPE_COMBOBOX, medium)
    }

    List getPosTypeComboBox(String medium, String type, String isPINPad) {
        boolean needPINPad = "true".equalsIgnoreCase(isPINPad) ? true : false
        PosType posType = PosType.valueOf(type)
        String posTypeName = PosType.valueOf(type).posType
        List posTypes = cbdService.getPosTypes(DICTIONARY_PATH + GET_EXT_POS_TYPE_COMBOBOX, medium, posTypeName)

        if(posType == PosType.STATIONARY) {
            return CollectionUtils.select(posTypes, new Predicate() {
                @Override
                boolean evaluate(Object object) {
                    boolean hasPINPad = object.value.contains("PINPad")
                    if(needPINPad && !hasPINPad) {
                        return false
                    } else if (!needPINPad && hasPINPad) {
                        return false
                    }
                    return true
                }
            })
        }

        return posTypes
    }

    def getCalculatorDevicesTypes(def medium) {
        cbdService.getCalculatorDevicesTypes(medium)
    }

    def getCbdPointsComboBox(def nip) {
        cbdService.getCbdPoints(DICTIONARY_PATH + GET_CBD_POINTS_COMBOBOX, nip)
    }

    def getMccComboBox() {
        List mccCodes = cbdService.getMccCodes(DICTIONARY_PATH + GET_MCC_COMBOBOX);
        List mccCodesWithoutEmptyValues = []

        mccCodes.each {
            if(it.code != '') mccCodesWithoutEmptyValues.add(it)
        }

        return mccCodesWithoutEmptyValues
    }

    private def getFromDictionary(def name, def params){
        switch (Environment.getCurrent().getName()) {
            case EumowyCustomEnvironment.MOCK.getName():
                return []
            default:
                def key = createKey(name, params)
                if (!dictionary.containsKey(key)){
                    dictionary.putAt(key, cbdDAO.selectMany(DICTIONARY_PATH + name, params))
                }
                return dictionary[key]
        }
    }

    private def createKey(def name, def params){
        def key = name;
        params.each({ elem ->
            key = '_' + key + elem.value
        })
        key
    }
}