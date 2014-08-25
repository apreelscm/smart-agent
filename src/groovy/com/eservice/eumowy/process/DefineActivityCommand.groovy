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
class DefineActivityCommand {

    def dodatkowyPunkt
    def nowaUmowa
    def wymianaUmowyZaplaty
    def zmianaProwizji
    def dodanieDcc
    def dodanieCashBack
    def zmianaWarunkowPrepaid
    def poprawDane
    def dodatkowyPos
    def wymianaUmowyNajmu
    def dodaniePrepaid
    def zmianaWarunkowDcc
	def dodanieAneksuKosztyPlus
    def zmianaOkresuLojalnosciowego
    def prestiz
    def promocyjneObnizenieNajmu
    def odrzucDokumenty
    def uzupelnijPodpisy
    def komfort
    def aneks
    def ekonomiczny
    def wymianaTerminala
    def pakietStart
    def pakietStartPlus
    def pakietMobilny
    def logoKalkulatorSesja
    String notes

    def selectedActivities = []

    static constraints = {
        notes size: 1..250, blank: true,nullable: true
        selectedActivities nullable: false, validator: {val,obj ->
            def tmpList = obj.properties.findResults { it.value == "on" ? it.key : null };
            obj.selectedActivities = tmpList;

            if(tmpList.size() == 0 && !obj.notes){
                return 'DefineActivityCommand.selectedActivities.validator.invalid'
            }
        }
    }

}