package com.eservice.eumowy

class DaneAkceptantaService {

    def cbdDAO

    private static final def GET_OFICJALNA_NAZWA_AKCEPTANTA_VAT = "getOficjalnaNazwaAkceptantaVAT"
    private static final def GET_REGON = "getRegon"

    def getOficjalnaNazwaAkceptantaVAT(def clientNip) {
        def row = cbdDAO.selectOne(GET_OFICJALNA_NAZWA_AKCEPTANTA_VAT,[nip:clientNip])
        return row;
    }

    def getRegon(def clientNip) {
        def row = cbdDAO.selectOne(GET_REGON,[nip:clientNip])
        return row;
    }
}
