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
    public static final def GET_PAN_PANI = "getPanPaniComboBox"
    public static final def GET_BANK = "getBank"
    public static final def GET_POS_TYPE_COMBOBOX = "getPosTypeComboBox"
    public static final def GET_CBD_POINTS_COMBOBOX = "getCbdPointsComboBox"

    //@Cacheable(value="getUlicaComboBox")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_UNCOMMITTED, readOnly = true)
    def getUlicaComboBox() {
        switch (Environment.getCurrent().getName()) {
            case EumowyCustomEnvironment.MOCK.getName():
                return []
            default:
                dictionary.put(GET_ULICA_COMBOBOX, cbdDAO.selectMany(DICTIONARY_PATH + GET_ULICA_COMBOBOX));
                return dictionary[GET_ULICA_COMBOBOX]
        }

    }

//    @Cacheable(value="getBank")
//    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_UNCOMMITTED, readOnly = true)
//    def getBankComboBox() {
//        dictionary.put(GET_BANK, cbdDAO.selectMany(DICTIONARY_PATH + GET_BANK));
//        return dictionary[GET_BANK]
//    }

    def getBankComboBox() {
        switch (Environment.getCurrent().getName()) {
            case EumowyCustomEnvironment.MOCK.getName():
                return []
            default:
                dictionary.put(GET_BANK, cbdDAO.selectMany(DICTIONARY_PATH + GET_BANK));
                return dictionary[GET_BANK]
        }

    }

    def getPosTypeComboBox(def nipNum) {
        switch (Environment.getCurrent().getName()) {
            case EumowyCustomEnvironment.MOCK.getName():
                return []
            default:
                dictionary.putAt(GET_POS_TYPE_COMBOBOX, cbdDAO.selectMany(DICTIONARY_PATH+GET_POS_TYPE_COMBOBOX, [nip: nipNum]))
                return dictionary[GET_POS_TYPE_COMBOBOX]
        }
    }

    def getCbdPointsComboBox(def nipNum) {
        switch (Environment.getCurrent().getName()) {
            case EumowyCustomEnvironment.MOCK.getName():
                return []
            default:
                dictionary.putAt(GET_CBD_POINTS_COMBOBOX, cbdDAO.selectMany(DICTIONARY_PATH+GET_CBD_POINTS_COMBOBOX, [nip: nipNum]))
                return dictionary[GET_CBD_POINTS_COMBOBOX]
        }
    }

}