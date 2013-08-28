package com.eservice.eumowy

import com.eservice.eumowy.command.AllPointsCommand
import com.eservice.eumowy.command.AllPosCommand
import com.eservice.eumowy.command.PointCommand
import com.eservice.eumowy.command.ProcessCommand
import grails.util.Environment
import org.apache.commons.collections.FactoryUtils
import org.apache.commons.collections.ListUtils
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
                eq("phNumber", Integer.valueOf(filterPhNo));
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


    static def boolean isNumber(value){
        return value?.toString()?.isNumber()
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
                Process.ProcessStatus.NEW,
                Process.ProcessStatus.WAITING,
                Process.ProcessStatus.WAIT_FOR_SUBSRIPTION,
                Process.ProcessStatus.EDIT]
        )
    }

    def getLastIfIncompleteProcessForClientNip(String nip) {
        def result = Process.findByClient(Client.findByNip(nip),[sort: "id", order: "desc"])

        log.info("getLastIfIncompleteProcessForClient - client.nip = ${nip} , id = ${result?.id} status = ${result?.status}")

        return result?.status in [
                Process.ProcessStatus.NEW,
                Process.ProcessStatus.WAITING,
                Process.ProcessStatus.WAIT_FOR_SUBSRIPTION,
                Process.ProcessStatus.EDIT] ? result.id : null
    }

    def containsActivity(def activities, def activityCode) {
        return activities?.any{it.code.equals(activityCode)};
    }

    def createNewProcessCommand(def process, def calc){
        log.info("createNewProcessCommand processId = ${process.id}")
        def cmd = initProcessCommand(process)
        prepareProcessCommand(cmd, calc)
    }

    def createSavedProcessCommand(def process, def calc){
        log.info("createSavedProcessCommand processId = ${process.id}")
        def cmd = initProcessCommand(process)
        loadProcessData(process,cmd)
     //   prepareProcessCommand(cmd, calc, cbdMethods)
    }

    /**
     *  init data
     * */

    def initProcessCommand(def process) {
        def cmd = new ProcessCommand();
        cmd.process = process
        cmd.nip = process.client.nip
        cmd.notes = process.notesToCoa ?: "";
        cmd
    }

    def defaultMethods = ["getWyborDzialania","getLiczbaMiesiecyZwolnieniaZNajmu"]
    def cbdMethods = ["getAdresDoKorespondencjizAkecptantem","getDaneAkceptanta","getSiedzibaAkceptanta","getSerwis"]

    def prepareProcessCommand(def cmd, def calc, def restrictedMethods = []) {
        def exclusions = defaultMethods + restrictedMethods

        cmd.process.panels.each { Panel panel ->
            String panelFunctionName = "get${WordUtils.capitalize(panel.name)}"
            if(panelFunctionName in exclusions){ return }

            log.info("invokin ${panelFunctionName} on panelService")

            switch (Environment.getCurrent()) {
                case Environment.DEVELOPMENT:
                    panelMockService."${panelFunctionName}"(cmd)
                    break;
                case Environment.TEST:
                    panelService."${panelFunctionName}"(cmd,calc)
                    break;
                default:
                    panelService."${panelFunctionName}"(cmd,calc)
            }
        }
        cmd
    }

    /**
     *  create data
     * */
    def loadProcessData(def process,  def cmd) {
        log.info("loadProcessData - processData: ${process.processData}");
        process.processData.each {ProcessData data ->
            if(!cmd.hasProperty(data.name)){
                throw new NoSuchFieldException(data.name)
            }
            println("${data.name} => ${data.value}" )

            if(data.name in ["allPoses", "allPoints", "points"]){
                //TODO implement
            }
            else{
                cmd[data.name] = data.value ?: ""
            }
        }
        cmd
    }

    /**
     *  save data
     * */
    List<PointCommand> points = ListUtils.lazyList([], FactoryUtils.instantiateFactory(PointCommand))
    List<AllPointsCommand> allPoints = ListUtils.lazyList([], FactoryUtils.instantiateFactory(AllPointsCommand))
    List<AllPosCommand> allPoses = ListUtils.lazyList([], FactoryUtils.instantiateFactory(AllPosCommand))
    def getDataFromPanels(def cmd) {
        def processDataList = [];
        cmd.properties.each { key, value ->
            // println("getDataFromPanels start: ${key} : ${value}");
            if (["class","process", "cbdService", "errors", "constraints", "notes"].contains(key) || value == null){
                return
            }

            if(["allPoses", "allPoints", "points"].contains(key)){
                //TODO implementacja logiki dla punktow
                return;
            }

            processDataList.add(new ProcessData(name: "${key}", value:"${value ?: ''}"));
        }

        processDataList
    }

}