package com.eservice.eumowy.process

import grails.validation.Validateable

/**
 * Created with IntelliJ IDEA.
 * User: mariusz.kaczkowski
 * Date: 01.08.13
 * Time: 10:05
 * To change this template use File | Settings | File Templates.
 */

@Validateable
class GetCalculatorCommand {

    def nip

    static constraints = {
        nip nullable: false, validator:{String nip, obj -> obj.isNIPValid(nip) ? true :  'GetCalculatorCommand.nip.validator.invalid'}
    }

    def isNIPValid(String nip) {
        println("isNIPValid nip:"+nip);
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
    }
}