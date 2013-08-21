package com.eservice.eumowy.command
import com.eservice.eumowy.Panel
import com.eservice.eumowy.Process
/**
 * User: Dominik Walczak
 * Date: 20.08.13 Time: 10:22
 *
 */
class ProcessCommand implements Serializable{

    def transient cbdService

    String nip

    transient Process process

    List<PointCommand> points = [] //  points[n].someProperty

    def initialize(Process process){
        this.process = process
        this.nip = process.client.nip

        // pobranie danych dla wybranych paneli
        for (Panel panel : process.panels){

        }
        int i = 1
        //def adresDoKorespondencji = cbdService.getAdresDoKorespondencji(process.client.nip);
    }



}
