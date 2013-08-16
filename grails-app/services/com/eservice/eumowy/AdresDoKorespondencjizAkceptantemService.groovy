package com.eservice.eumowy

import com.eservice.eumowy.dto.AdresDoKorespondencjizAkceptantem

class AdresDoKorespondencjizAkceptantemService {

    def cbdDAO

    private static final def GET_ADRES_DO_KORESPONDENCJI = "getAdresDoKorespondencji"

    def getAdresDoKorespondencji(def clientNip) {
        return new AdresDoKorespondencjizAkceptantem(cbdDAO.selectOne(GET_ADRES_DO_KORESPONDENCJI,[nip:clientNip]));
    }
}
