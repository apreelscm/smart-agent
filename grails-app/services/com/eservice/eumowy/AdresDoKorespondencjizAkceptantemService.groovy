package com.eservice.eumowy

import com.eservice.eumowy.dto.AdresDoKorespondencjizAkceptantem

class AdresDoKorespondencjizAkceptantemService {

    def cbdSqlService

    private static final def GET_ULICA_TYPES = "getUlicaTypes"
    private static final def GET_ADDRESS = "getAdres"

    def getUlicaTypes(def clientNip) {
        return  cbdSqlService.selectMany(GET_ULICA_TYPES,[nip:clientNip]);
    }

    def getAdres(def clientNip) {
        return new AdresDoKorespondencjizAkceptantem(cbdSqlService.selectOne(GET_ADDRESS,[nip:clientNip]));
    }
}
