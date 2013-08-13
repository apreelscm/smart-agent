package com.eservice.eumowy

import grails.plugin.cache.Cacheable
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

class DictionaryService {
    def cbdSqlService

    def dictionary

    private static final def DICTIONARY_PATH = "dictionary/"
    private static final def GET_ULICA_COMBOBOX = "getUlicaComboBox"
    private static final def GET_PAN_PANI = "getPanPaniComboBox"

    @Cacheable(value="getUlicaComboBox")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getUlicaComboBox() {
        if( dictionary[GET_ULICA_COMBOBOX]){
            return
        }

        def result =  cbdSqlService.selectMany(DICTIONARY_PATH + GET_ULICA_COMBOBOX);
        dictionary[GET_ULICA_COMBOBOX] = result
    }

    @Cacheable(value="getPanPaniComboBox")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getPanPaniComboBox() {
        if( dictionary[GET_PAN_PANI]){
            return
        }

        def result =  cbdSqlService.selectMany(DICTIONARY_PATH + GET_PAN_PANI);
        dictionary[GET_PAN_PANI] = result
    }
}