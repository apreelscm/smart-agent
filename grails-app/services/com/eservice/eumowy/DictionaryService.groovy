package com.eservice.eumowy
import com.eservice.eumowy.dao.CbdDAO
import com.eservice.eumowy.util.EumowyCustomEnvironment
import grails.util.Environment
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

class DictionaryService {

    CbdDAO cbdDAO

    def dictionary = [:]

    private static final def DICTIONARY_PATH = "dictionary/"

    public static final def GET_ULICA_COMBOBOX = "getUlicaComboBox"
    public static final def GET_BANK = "getBank"
    public static final def GET_POS_TYPE_COMBOBOX = "getPosTypeComboBox"
    public static final def GET_CBD_POINTS_COMBOBOX = "getCbdPointsComboBox"

    //@Cacheable(value="getUlicaComboBox")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_UNCOMMITTED, readOnly = true)
    def getUlicaComboBox() {
        getFromDictionary(GET_ULICA_COMBOBOX, [])
    }

    def getBankComboBox() {
        getFromDictionary(GET_BANK, [])
    }

    def getPosTypeComboBox(def nipNum, def medium) {
        getFromDictionary(GET_POS_TYPE_COMBOBOX, [nip:nipNum, medium:medium])
    }

    def getCbdPointsComboBox(def nipNum) {
        getFromDictionary(GET_CBD_POINTS_COMBOBOX, [nip:nipNum])
    }

    private def getFromDictionary(def name, def params){
        switch (Environment.getCurrent().getName()) {
            case EumowyCustomEnvironment.MOCK.getName():
                return []
            default:
                dictionary.putAt(name, cbdDAO.selectMany(DICTIONARY_PATH + name, params))
                return dictionary[name]
        }
    }

}