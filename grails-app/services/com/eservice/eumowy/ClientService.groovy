package com.eservice.eumowy

import grails.util.Environment

class ClientService {

    static transactional = false;

    def isClientNipValid(def nip) {

        if(!Environment.isDevelopmentMode()){ return true }

        if (nip.length() == 13) {
            nip = nip.replaceAll("-", "");
        }
        if (nip.length() != 10) return false;
        def weights = [6, 5, 7, 2, 3, 4, 5, 6, 7];
        String[] aNip = nip.split("");
        try {
            int sum = 0;
            for (int i = 0; i < weights.size(); i++) {
                sum += Integer.parseInt(aNip[i + 1]) * weights[i];
            }
            return (sum % 11) == Integer.parseInt(aNip[10]);
        } catch (NumberFormatException e) {
            return false;
        }
        return true
    }

    def clientExists(Client client) {
        return client?.id != null || client?.cbdId != null
    }
}
