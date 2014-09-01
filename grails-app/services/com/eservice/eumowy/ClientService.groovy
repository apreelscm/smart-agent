package com.eservice.eumowy

import grails.util.Environment

class ClientService {

    static transactional = false;

    def updateClientName(def client, def cmd) {

        if(!client || cmd.akceptantNazwaOficjalnaCbd ){
            return
        }

        client.name = cmd.akceptantNazwaOficjalna
        client.save()
    }

}
