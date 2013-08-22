package com.eservice.eumowy

import com.eservice.eumowy.command.ProcessCommand
import grails.util.Environment
import org.apache.commons.lang.WordUtils

import java.text.SimpleDateFormat

class ProcessService {

    static final def DATE_FORMAT = "dd-MM-yyyy";

    def panelService
    def panelMockService

    def searchProcessByFilters(def params) {
        def filterObserved = params.filterObserved
        def filterStatus = params.filterStatus
        def filterNip = params.filterNip

        def filterPhNo = params.filterPhNo
        def filterDateFrom = params.filterDateFrom
        def filterDateTo = params.filterDateTo

//        log.info(filterObserved + " --- " + filterStatus);
//        log.info("max: " + params.max + "; offset: " + params.offset + "; sort: " + params.sort + "; order: " + params.order);
//        log.info("filterPhNo: " + params.filterPhNo + "; filterDateFrom: " + params.filterDateFrom + "; filterDateTo: " + params.filterDateTo)

        def clientCriteria = Process.createCriteria()
        def searchResults = clientCriteria.list(
                max: params.max,
                offset: params.offset,
                sort: params.sort,
                order: params.order){

            if (filterStatus){
                //jesli filterStatus nie jest pusty to po nim ograniczamy
                eq("status", Process.ProcessStatus.valueOf(filterStatus))
            }

            if(isNumber(filterNip)) {
                client {
                    eq("nip", filterNip)
                }
            }

            if(isNumber(filterPhNo)) {
                eq("phNumber", Long.valueOf(filterPhNo));
            }

            if(isDate(filterDateFrom) && isDate(filterDateTo)) {
                ge("dateCreated", parseDate(filterDateFrom))
                le("dateCreated", addDays(parseDate(filterDateTo), 1))
            }

            if ("isObserved".equals(filterObserved)){
                eq("observed", true)
            }
        }
        [searchResults: searchResults, searchResultSize: searchResults.getTotalCount()]
    }


    static def boolean isNumber(number){
        try {
            number && !"".equals(number) && Long.valueOf(number)
        } catch(Exception e){
            false
        }
    }

    static def boolean isDate(date){
        try {
            date != null && !"".equals(date) && parseDate(date)
        } catch (Exception e){
            false
        }
    }

    static def Date parseDate(dateStr){
        new SimpleDateFormat(DATE_FORMAT).parse(dateStr)
    }

    static def String formatDate(date){
        new SimpleDateFormat(DATE_FORMAT).format(date)
    }

    static def Date addDays(date, days){
        new Date(date.getTime()+days*86400000L)
    }

    /**
     * sprawdzanie, czy w eUmowy istnieje dla danego Akceptanta niezakończony Proces
     **/
    def hasIncompleteProcessForClient(Client client) {
        return client.id != null && Process.findByClientAndStatusInList(client, [
                Process.ProcessStatus.WAITING,Process.
                ProcessStatus.WAIT_FOR_SUBSRIPTION,
                Process.ProcessStatus.EDIT]
        )
    }

    def containsActivity(def activities, def activityCode) {
        return activities?.any{it.code.equals(activityCode)};
    }

    def getDataForPanels(final def process) {
        def exclusions = ["getWyborDzialania","getLiczbaMiesiecyZwolnieniaZNajmu"]

        def cmd = new ProcessCommand();
        cmd.process = process
        cmd.nip = process.client.nip

        process.panels.each { Panel panel ->
            String panelFunctionName = "get${WordUtils.capitalize(panel.name)}"
            log.info("invokin ${panelFunctionName} on panelService")

            if(panelFunctionName in exclusions){ return }

            switch (Environment.getCurrent()) {
                case Environment.DEVELOPMENT:
                    panelMockService."${panelFunctionName}"(cmd)
                    break;
                case Environment.TEST:
                    panelService."${panelFunctionName}"(cmd)
                    break;
            }
        }
        cmd
    }

    def getDataFromPanels(def cmd) {
        def processDataList = [];
        cmd.properties.each { key, value ->
            if (!["class", "cbdService", "errors"].contains(key)) {//} && value){
                println("${key} : ${value}");
                processDataList.add(new ProcessData(name: key, value: value));
            }
        }
        processDataList
    }
}
