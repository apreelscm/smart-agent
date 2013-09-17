package com.eservice.eumowy
import com.eservice.eumowy.dao.CbdDAO
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

    //@Cacheable(value="getUlicaComboBox")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_UNCOMMITTED, readOnly = true)
    def getUlicaComboBox() {
        switch (Environment.getCurrent()) {
            case Environment.DEVELOPMENT:
                return []
            case Environment.TEST:
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
        dictionary.put(GET_BANK, cbdDAO.selectMany(DICTIONARY_PATH + GET_BANK));
        //return [[klucz: "1", wartosc: "PKO PB"], [klucz: "2", wartosc: "Alior Bank"], [klucz: "3", wartosc: "Millenium Bank"], [klucz: "4", wartosc: "GE Bank"]]
    	return dictionary[GET_BANK]
	}
	
	def getPosTypeComboBox(def nipNum) {
		dictionary.putAt(GET_POS_TYPE_COMBOBOX, cbdDAO.selectMany(DICTIONARY_PATH+GET_POS_TYPE_COMBOBOX, [nip: nipNum]))
		return dictionary[GET_POS_TYPE_COMBOBOX]
	}

}