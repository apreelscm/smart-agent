package com.eservice.eumowy
import com.eservice.eumowy.dao.CbdDAO
import com.eservice.eumowy.util.EumowyCustomEnvironment
import grails.util.Environment
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

class DictionaryService {

    CbdDAO cbdDAO

    CbdService cbdService

    def dictionary = [:]

    private static final def DICTIONARY_PATH = "dictionary/"

    public static final def GET_ULICA_COMBOBOX = "getUlicaComboBox"
    public static final def GET_POS_TYPE_COMBOBOX = "getPosTypeComboBox"
    public static final def GET_CBD_POINTS_COMBOBOX = "getCbdPointsComboBox"

    //@Cacheable(value="getUlicaComboBox")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_UNCOMMITTED, readOnly = true)
    def getUlicaComboBox() {
        getFromDictionary(GET_ULICA_COMBOBOX, [])
    }

    def getPosTypeComboBox(def nip, def medium) {
        cbdService.getPosTypes(DICTIONARY_PATH + GET_POS_TYPE_COMBOBOX, nip, medium)
    }

    def getCbdPointsComboBox(def nip) {
        cbdService.getCbdPoints(DICTIONARY_PATH + GET_CBD_POINTS_COMBOBOX, nip)
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