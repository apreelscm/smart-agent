package com.eservice.eumowy
import com.eservice.eumowy.dao.CbdDAO
import grails.plugin.cache.Cacheable
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

class DictionaryService {

    CbdDAO cbdDAO

    def dictionary = [:]

    private static final def DICTIONARY_PATH = "dictionary/"

    public static final def GET_ULICA_COMBOBOX = "getUlicaComboBox"
    public static final def GET_PAN_PANI = "getPanPaniComboBox"

    @Cacheable(value="getUlicaComboBox")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_UNCOMMITTED, readOnly = true)
    def getUlicaComboBox() {
        dictionary.put(GET_ULICA_COMBOBOX, cbdDAO.selectMany(DICTIONARY_PATH + GET_ULICA_COMBOBOX));
        return dictionary[GET_ULICA_COMBOBOX]
    }

    @Cacheable(value="getPanPaniComboBox")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_UNCOMMITTED, readOnly = true)
    def getPanPaniComboBox() {
        dictionary.put(GET_PAN_PANI, cbdDAO.selectMany(DICTIONARY_PATH + GET_PAN_PANI));
        return dictionary[GET_PAN_PANI]
    }
}