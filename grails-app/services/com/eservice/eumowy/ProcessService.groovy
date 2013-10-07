package com.eservice.eumowy

import com.eservice.eumowy.command.AllPointsCommand
import com.eservice.eumowy.command.AllPosCommand
import com.eservice.eumowy.command.PointCommand
import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.util.DateUtils
import com.eservice.eumowy.util.EumowyCustomEnvironment
import grails.util.Environment
import groovy.sql.GroovyRowResult
import org.apache.commons.lang.WordUtils
import serializationutils.SerializationUtils

class ProcessService {

    def sessionFactory
    def panelService
    def cbdService
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

            if(DateUtils.isDate(filterDateFrom, DateUtils.DD_MM_YYYY) && DateUtils.isDate(filterDateTo, DateUtils.DD_MM_YYYY)) {
                ge("dateCreated", DateUtils.parseDate(filterDateFrom, DateUtils.DD_MM_YYYY))
                le("dateCreated", DateUtils.addDays(DateUtils.parseDate(filterDateTo, DateUtils.DD_MM_YYYY), 1))
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

    /**
     * sprawdzanie, czy w eUmowy istnieje dla danego Akceptanta niezakończony Proces
     **/

/*    def isProcessWithStatus(Client client, def statusList) {
        return client.id != null && Process.findByClientAndStatusInList(client, statusList)
    }*/

    def getLastProcessWhenInStatus(def nip, def statusList) {
        sessionFactory.currentSession.clear()
        def crit = Process.createCriteria()
        def result = crit.list {
            client {
                eq("nip", nip)
            }
            order("lastUpdated", "desc")
            maxResults(1)
        }
        log.info("getLastProcessWhenInStatus - nip = ${nip} , id = ${result?.id} status = ${result?.status}, statusList = ${statusList}")
        return (result && nip && (result.status in statusList)) ? result : null
    }

    def getLastProcessWhenNotInStatus(def nip, def statusList) {
        //sessionFactory.currentSession.clear()
        def crit = Process.createCriteria()
        def result = crit.get {
            client {
                eq("nip", nip)
            }
            order("lastUpdated", "desc")
            maxResults(1)
        }
        log.info("getLastProcessWhenNotInStatus - nip = ${nip} , id = ${result?.id} status = ${result?.status}, statusList = ${statusList}")
        return (result && nip && !(result.status in statusList)) ? result : null
    }

    def getLastProcessForClient(def nip) {
        sessionFactory.currentSession.clear()
        def crit = Process.createCriteria()
        def result = crit.get {
            client {
                eq("nip", nip)
            }
            order("lastUpdated", "desc")
            maxResults(1)
        }
        log.info("getLastProcessForClient - nip = ${nip} , id = ${result?.id}, status = ${result?.status}")
        result ?: new Process(result)
    }

    def containsActivity(def activities, def activityCode) {
        return activities?.any{it.code.equals(activityCode)};
    }

    def getNewProcessCommand(def process, def calc){
        log.info("getNewProcessCommand processId = ${process.id}")
        def cmd = initProcessCommand(process)
        cmd.allPoints?.addAll(getPointsToAllPointsCommandList(process, cmd))
        cmd.allPoses?.addAll(getPosesToAllPosCommandList(process, cmd))
        prepareProcessCommand(cmd, calc,)
    }

    def getSavedProcessCommand(def process, def calc){
        log.info("getSavedProcessCommand processId = ${process.id}")
        def cmd = initProcessCommand(process)
        loadProcessData(process,cmd)
        cmd.points?.clear()
        cmd.poses?.clear()
        cmd.allPoints?.clear()
        cmd.allPoses?.clear()
        cmd.points?.addAll(getLocalPointsToPointCommandList(process))
        cmd.poses?.addAll(getLocalPosesToPointCommandList(process))
        cmd.allPoints?.addAll(getPointsToAllPointsCommandList(process, cmd))
        cmd.allPoses?.addAll(getPosesToAllPosCommandList(process, cmd))

        cmd.notes = process.notesToCoa

        prepareProcessCommand(cmd, calc, cbdMethods)
    }

    def getRepresentative1(def process) {
        return [name: process?.processData?.find { pd -> pd.name == "reprezentant1Imie" }?.value, surname: process?.processData?.find { pd -> pd.name == "reprezentant1Nazwisko" }?.value]
    }

    def getRepresentative2(def process) {
        return [name: process?.processData?.find { pd -> pd.name == "reprezentant2Imie" }?.value, surname: process?.processData?.find { pd -> pd.name == "reprezentant2Nazwisko" }?.value]
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

        def populateMethod;

        switch (Environment.getCurrent().getName()) {
            case EumowyCustomEnvironment.MOCK.getName():
                panelMockService.init(cmd)
                populateMethod = {command, calculator, functionName ->
                    panelMockService."${functionName}"(command)
                }
                break;
            default:
                panelService.init(cmd, calc)
                populateMethod = {command, calculator, functionName ->
                    panelService."${functionName}"(command, calculator)
                }
                break;
        }

        cmd.process.panels.each { Panel panel ->

            if (panel!=null){
                //po usunieciu panelu z procesu w jego miesjsce wchodzi null, trzeba to obsluzyc.
                String panelFunctionName = "get${WordUtils.capitalize(panel.name)}"
                if(panelFunctionName in exclusions){ return }

                log.info("invokin ${panelFunctionName} on panelService")
                populateMethod.call(cmd, calc, panelFunctionName)
//
//                switch (Environment.getCurrent().getName()) {
//                    case EumowyCustomEnvironment.MOCK.getName():
//                        panelMockService."${panelFunctionName}"(cmd)
//                        break;
//                    default:
//                        panelService."${panelFunctionName}"(cmd,calc)
//                }
            }
        }

        cmd
    }

    /**
     *  create data
     * */
    def loadProcessData(def process,  def cmd) {
        process.processData?.each {ProcessData data ->

            //oplataMasteroPr - zmiana nazwy tego parametru wymusila dodanie go do tej listy.
            if(data.name in ["dataUmowy","punktyTytulPlatnosci","punktySystemKasowy","punktyUta","punktyWybrane", "oplataMasteroPr", "clientFromCbd"]){
                return
            }

            if(!cmd.hasProperty(data.name)){
                throw new NoSuchFieldException(data.name)
            }

            if(data.name in ["allPoses", "allPoints", "points", "poses"]){
                //TODO implement
            } else if (data.name in ["umowaOznOd", "umowaOznDo", "dataAneksowanejUmowyPos", "dataAneksowanejUmowyPrepaid"]){
                cmd[data.name] = data.value?.trim()? DateUtils.getFormattedDate(DateUtils.parseWithTimezone(data.value), DateUtils.YYYY_MM_DD) : "";
                return;
            } else{
                //println("data.name:"+data.name+ " value:"+data.value)
                cmd[data.name] = data.value ?: ""
            }
        }

        cmd
    }

    def getLocalPointsToPointCommandList(def process) {
        def localPoints = []
        process.points.each { PointData point ->

            /* Don't load points from CBD */
            if (point.cbdId != null) {
                return
            }

            PointCommand pc = new PointCommand()

            point.properties.each { key, value ->
               //log.info "PointData Key: " + key
                if (["process",
                        "processId",
                        "pointDetails",
                        "posDatas",
                        "processId",
                        //"cbdId",
                        "pointDetailsId",
                ].contains(key) || value == null){
                    return
                }

                if (PointCommand.metaClass.respondsTo(PointCommand, "set"+key.capitalize())) {
                    pc."set${key.capitalize()}"(value)
                }
            }

            pc.id = point.id

            point.pointDetails?.properties.each { key, value ->
                //log.info "PointDataDetails Key: " + key
                if (["point",
                        "pointId",
                ].contains(key) || value == null){
                    return
                }

                if (PointCommand.metaClass.respondsTo(PointCommand, "set"+key.capitalize())) {
                    pc."set${key.capitalize()}"(value)
                }
            }

            //def posData = point.posDatas != null && point.posDatas.size() > 0 ? point.posDatas[0] : null
            def posData = point.posDatas?.find { PosData pd -> pd.tpsId == null }

            posData?.properties.each { key, value ->
                log.info "PosData Key: " + key
                if (["tpsId",
                        "point"].contains(key) || value == null){
                    return
                }

                if (PointCommand.metaClass.respondsTo(PointCommand, "set"+key.capitalize())) {
                    pc."set${key.capitalize()}"(value)
                }
            }

            posData?.posDetails?.properties.each { key, value ->
                //log.info "PosDataDetails Key: " + key
                if (["class", "cbdId", "process", "point", "errors", "constraints", "empty", "", ""].contains(key) || value == null){
                    return
                }

                if (PointCommand.metaClass.respondsTo(PointCommand, "set"+key.capitalize())) {
                    pc."set${key.capitalize()}"(value)
                }
            }

            localPoints.add(pc)
        }
        localPoints
    }

    def getLocalPosesToPointCommandList(def process) {
        def localPoses = []
        process.points.each { PointData point ->
            point.posDatas?.each { PosData posData ->

                /* Don't load POSes from CBD */
                if (posData.tpsId != null) {
                    return
                }

                PointCommand pc = new PointCommand()
                pc.id = posData.id

                point.properties.each { key, value ->
                   // log.info "PointData Key: " + key
                    if (["class", "posDatas", "errors", "constraints", "processId", "cbdId", "pointDetailsId", "empty"].contains(key) || value == null){
                        return
                    }

                    if (PointCommand.metaClass.respondsTo(PointCommand, "set"+key.capitalize())) {
                        pc."set${key.capitalize()}"(value)
                    }
                }

                point.pointDetails?.properties.each { key, value ->
//                    log.info "PointDataDetails Key: " + key
                    if (["class", "posDatas", "errors", "constraints", "processId", "cbdId", "pointDetailsId", "empty"].contains(key) || value == null){
                        return
                    }

                    if (PointCommand.metaClass.respondsTo(PointCommand, "set"+key.capitalize())) {
                        pc."set${key.capitalize()}"(value)
                    }
                }

                posData?.properties.each { key, value ->
//                    log.info "PosData Key: " + key
                    if (["class", "cbdId", "process", "point", "errors", "constraints", "empty", "", ""].contains(key) || value == null){
                        return
                    }

                    if (PointCommand.metaClass.respondsTo(PointCommand, "set"+key.capitalize())) {
                        pc."set${key.capitalize()}"(value)
                    }
                }

                posData?.posDetails?.properties.each { key, value ->
//                    log.info "PosDataDetails Key: " + key
                    if (["class", "cbdId", "process", "point", "errors", "constraints", "empty", "", ""].contains(key) || value == null){
                        return
                    }

                    if (PointCommand.metaClass.respondsTo(PointCommand, "set"+key.capitalize())) {
                        pc."set${key.capitalize()}"(value)
                    }
                }

                localPoses.add(pc)
            }
        }
        localPoses
    }

    def getLocalPointsToAllPointsCommandList(def process, def pointsList) {
        process.points?.each { PointData point ->

            AllPointsCommand apc = new AllPointsCommand()
            apc.id = point.id
            apc.setCzyCbd(false)
            point.properties.each { key, value ->
                if (["process",
                        "processId",
                        "pointDetails",
                        "posDatas",
                        "processId",
                        "pointDetailsId",
                ].contains(key) || value == null){
                    return
                }

                if (AllPointsCommand.metaClass.respondsTo(AllPointsCommand, "set"+key.capitalize())) {
                    apc."set${key.capitalize()}"(value)
                }
            }

            pointsList.add(apc)
        }
    }

    def getLocalPosesToAllPosesCommandList(def process, def posesList) {
        process.points?.each { PointData point ->
            point.posDatas?.each { PosData posData ->

                AllPosCommand apc = new AllPosCommand()
                apc.setCzyCbd(false)
                apc.id = posData.id
                posData.properties.each { key, value ->
//                    log.info "PosData Key: " + key
                    if (["class", "process", "point", "errors", "constraints", "empty", "", ""].contains(key) || value == null){
                        return
                    }

                    if (AllPosCommand.metaClass.respondsTo(AllPosCommand, "set"+key.capitalize())) {
                        apc."set${key.capitalize()}"(value)
                    }
                }

                posesList.add(apc)
            }
        }
    }

    def getCbdPointsToAllPointsCommandList(def cmd, def pointsList) {
        def cbdPoints = cbdService.getZakresUruchomieniaPunktyGrid(cmd.nip)
        cbdPoints?.each { GroovyRowResult row ->
            AllPointsCommand apc = new AllPointsCommand()

            apc.setCzyCbd(true)
            apc.setCbdId(Integer.valueOf(row.get("id").toString()))
            apc.setKodPocztowy(row.get("kod_pocztowy"))
            apc.setLiczbaPos(Integer.valueOf(row.get("liczba_pos").toString()))
            apc.setMiejscowosc(row.get("miejscowosc"))
            apc.setNazwa(row.get("nazwa"))
            apc.setNrBudynku(row.get("nr_budynku"))
            apc.setUlica(row.get("ulica"))

            pointsList.add(apc)
        }
    }

    def getCbdPosesToAllPosesCommandList(def cmd, def posesList) {
        def cbdPoses = cbdService.getPromocyjneObinzenieOplatGrid(cmd.nip)
        cbdPoses.each { GroovyRowResult row ->
            AllPosCommand apc = new AllPosCommand()

            apc.setCzyCbd(true)
			apc.setCbdId(Integer.valueOf(row.get("cbd_id").toString()))
            apc.setTpsId(Integer.valueOf(row.get("tps_id").toString()))
            apc.setNumerZestawuPos(Integer.valueOf(row.get("numer_logiczny").toString()))
            /* TODO The rest data should be loaded from calculator here! */

            posesList.add(apc)
        }
    }

    def getPointsToAllPointsCommandList(def process, def cmd) {
        def localPoints = [], cbdPoints = [], result = []
        getLocalPointsToAllPointsCommandList(process, localPoints)
        getCbdPointsToAllPointsCommandList(cmd, cbdPoints)

        /* Merge them with possibly new data from CBD */
        localPoints.each { AllPointsCommand apc ->

            if (apc.cbdId != null) {
                AllPointsCommand point = cbdPoints.find { AllPointsCommand i -> i.cbdId == apc.cbdId }

                /* Update field data */
                if (point) {
                    apc.nazwa = point.nazwa
                    apc.ulica = point.ulica
                    apc.kodPocztowy = point.kodPocztowy
                    apc.liczbaPos = point.liczbaPos
                    apc.miejscowosc = point.miejscowosc
                    apc.nrBudynku = point.nrBudynku
                    apc.czyCbd = true // Mark for update in local (eumowy) db
                    cbdPoints.remove(cbdPoints.findIndexOf { AllPointsCommand i -> i.cbdId == apc.cbdId })
                }
                else {
                    apc.cbdId = -1 // Mark the point for deletion from local (eumowy) db
                }
            }

            result.add(apc)
        }

        result.addAll(cbdPoints)
        result
    }

    def getPosesToAllPosCommandList(def process, def cmd) {
        def localPoses = [], cbdPoses = [], result = []
        getLocalPosesToAllPosesCommandList(process, localPoses)
        getCbdPosesToAllPosesCommandList(cmd, cbdPoses)

        /* Merge them with possibly new data from CBD */
        localPoses.each { AllPosCommand apc ->
            if (apc.tpsId != null) {
                AllPosCommand pos = cbdPoses.find { AllPosCommand i -> i.tpsId == apc.tpsId	}

                /* Update field data */
                if (pos) {
                    apc.dataDo = pos.dataDo
                    apc.dataOd = pos.dataOd
                    apc.numerZestawuPos = pos.numerZestawuPos
                    apc.wysokoscOplaty = pos.wysokoscOplaty
                    apc.czyCbd = true // Mark for update in local (eumowy) db
                    cbdPoses.remove(cbdPoses.findIndexOf { AllPosCommand i -> i.tpsId == apc.tpsId })
                }
                else {
                    apc.tpsId = -1 // Mark POS for deletion from local (eumowy) db
                }
            }

            result.add(apc)
        }
        result.addAll(cbdPoses)
        result
    }

    /** save data */
    def populateProcessWithData(Process process, def cmd){
        def processDataList = getDataFromPanels(cmd)

        //zapis obecnej daty na potrzeby dokumentow
        addCurrentDate(processDataList);

        //process.processData?.clear()
        processDataList.each { ProcessData data ->
            def foundData = process.processData.find { it.name == data.name }
            if(!foundData){
                process.addToProcessData(data)
                log.info("process data: ${data.processId?.class} ${data.version?.class} ${data.name?.class} ${data.id?.class}")
            }else if(data.value != foundData.value){
                foundData.value = data.value
            }
        }

        def pointDataList = getPointCommandsToPointDataList(cmd)

        pointDataList?.each { PointData point ->
            if (point.cbdId == -1) {
                point.delete(flush: true)
                return
            }

            point.save(flush: true)
            process.addToPoints(point)
        }

		def posDataList = getPointCommandsToPosDataList(cmd)

		posDataList?.each { PointData point ->
			if (point.cbdId == -1) {
				point.delete(flush: true)
				return
			}

			point.save(flush: true)
			process.addToPoints(point);
		}

        process.notesToCoa = cmd.notes //notesToCOA

        process
    }

    def addCurrentDate(def processDataList){
        processDataList.add(new ProcessData([name: 'dataUmowy', value: DateUtils.formatWithTimezone(DateUtils.getCurrentDate())]))
    }

    def getDataFromPanels(def cmd) {
        def processDataList = [];
        cmd.properties.each { key, value ->

            if (["class","process", "cbdService", "errors", "constraints","calc","calculatorService",
                    "notes", "hasUmowaCzas", "hasKontaktTel", "hasDoladowania", "hasAkceptantTel",
                    "hasInformacjaHandlowa","liczbaTerminali", "atLeastClosure", "nullableTrueBlankFalse",
                    "defaultPointData", "maxLengthClosure", "skipAddressValidationClosure"]
                    .contains(key) || value == ProcessCommand.DEFAULT_VALUE){
                return
            }

            if(["allPoses", "allPoints", "points"].contains(key)){
                //TODO implementacja logiki dla punktow
                return;
            }

            if(["umowaOznOd", "umowaOznDo", "dataAneksowanejUmowyPos", "dataAneksowanejUmowyPrepaid", "dataUmowy"].contains(key)){
                processDataList.add(new ProcessData(name: "${key}", value:"${DateUtils.formatWithTimezoneFromStr(value)}"));
                return;
            }

            processDataList.add(new ProcessData(name: "${key}", value:"${value ?: ''}"));
        }
        processDataList
    }

    def getPointCommandsToPointDataList(def cmd) {
        def pointsList = []

        cmd.points?.each { PointCommand pc ->
            boolean isNew = false
            if (pc == null) {
                log.info "PointCommand is NULL - skipping!"
                return
            }

            PointData pointData
            PointDataDetails pointDataDetails
            PosData posData
            PosDataDetails posDataDetails

            ArrayList<PosData> pdList = new ArrayList<PosData>()

            if (pc.id == null) {
				log.info "NEW POINT!"
                pointData = new PointData()
                pointDataDetails = new PointDataDetails()

                posData = new PosData()
                posDataDetails = new PosDataDetails()
                isNew = true
            }
            else {
				log.info "EXISTING POINT!"
                pointData = PointData.get(pc.id)

                if (pointData != null) {
                    log.info "getPointCommandsToPointDataList - Znaleziono punkt o id: " + pc.id
                    pointDataDetails = pointData.pointDetails

                    posData = pointData.posDatas?.getAt(0)
                    posDataDetails = posData?.posDetails

                    if (posData == null) {
                        log.info "getPointCommandsToPointDataList - Brakujacy POS dla Danych punktu o id: " + pc.id + " - Tworzę nowy!"
                        posData = new PosData()
                        posDataDetails = new PosDataDetails()
                    }

                    //pointData.id = pc.id
                }
                else {
                    log.info "getPointCommandsToPointDataList - Nie znaleziono punktu o id: " + pc.id
                    return
                }
            }

            pdList.add(posData)

            pc.properties.each { key, value ->
                log.info "PCProperties " + key + ": " + value
                if (PointData.metaClass.respondsTo(PointData, "set" + key.capitalize())) {
                    pointData."set${key.capitalize()}"(value)
                }

                if (PointDataDetails.metaClass.respondsTo(PointDataDetails, "set" + key.capitalize()) && key != 'id') {
                    pointDataDetails."set${key.capitalize()}"(value)
                }

                if (PosData.metaClass.respondsTo(PosData, "set" + key.capitalize())  && key != 'id') {
                    // FIXME krytyczne obejscie, nigdy nie powinno byc null
                    posData?."set${key.capitalize()}"(value)
                }

                if (PosDataDetails.metaClass.respondsTo(PosDataDetails, "set" + key.capitalize()) && key != 'id') {
                    posDataDetails."set${key.capitalize()}"(value)
                }
            }

			// Create POSes with same values
			def terminalCount = 0
			terminalCount += posDataDetails?.gprsIlosc != null ? posDataDetails?.gprsIlosc : 0
			terminalCount += posDataDetails?.dialupIlosc != null ? posDataDetails?.dialupIlosc : 0
			terminalCount += posDataDetails?.vpnIlosc != null ? posDataDetails?.vpnIlosc : 0
			terminalCount += posDataDetails?.sslIlosc != null ? posDataDetails?.sslIlosc : 0
			terminalCount += posDataDetails?.wifiIlosc != null ? posDataDetails?.wifiIlosc : 0
			terminalCount += posDataDetails?.bazaIlosc != null ? posDataDetails?.bazaIlosc : 0
			
			// Create cloned poses only when they are not already cloned
			if (terminalCount > 1 && terminalCount > pointData.liczbaPos) {
				for (int i = 0; i < terminalCount; i++) {
					PosData posDataNew
					PosDataDetails posDataDetailsNew
					Serializable posDataSer = posData
					posDataNew = SerializationUtils.clone(posDataSer) // as PosData
					posDataNew.id = null
					posDataNew.version = 0
					posDataDetailsNew = SerializationUtils.clone(posDataDetails)  // as PosDataDetails
					posDataDetailsNew.id = null
					posDataDetailsNew.version = 0
					
					posDataNew.setPosDetails(posDataDetailsNew)
					posDataNew.setPoint(pointData)
					posDataDetailsNew.setPos(posDataNew)

					pdList.add(posDataNew)
				}
			}
			
			// Set telePomka and teleKodzik based on terminalIlosc
			if (pc.terminalIlosc != null && pc.terminalIlosc > 0 && terminalCount > pointData.liczbaPos) {
				for (int i = 0; i < pc.terminalIlosc && i < pdList.size; i++) {
					pdList.get(i).posDetails?.telePompka = posData.posDetails?.telePompka
					pdList.get(i).posDetails?.teleKodzik = posData.posDetails?.teleKodzik
				}
			}
			else if (pc.terminalIlosc != null && pc.terminalIlosc > 0 && terminalCount == pointData.liczbaPos) {
				for (int i = 0; i < pc.terminalIlosc; i++) {
					pointData.posDatas?.get(i).posDetails?.telePompka = posData.posDetails?.telePompka
					pointData.posDatas?.get(i).posDetails?.teleKodzik = posData.posDetails?.teleKodzik
				}
			}

            pointData.nip = pointDataDetails.nipPunktu
            pointData.nazwa = pointDataDetails.nazwaDoWyszukiwarki
            pointData.ulica = pointDataDetails.korespondencjaUlica
            pointData.nrLokalu = pointDataDetails.korespondencjaNrLokalu
            pointData.nrBudynku = pointDataDetails.korespondencjaNrDomu
            pointData.miejscowosc = pointDataDetails.korespondencjaMiasto
            pointData.kodPocztowy = pointDataDetails.korespondencjaKodPocztowy
            pointData.poczta = pointDataDetails.korespondencjaPoczta
            pointData.liczbaPos = pdList.size()
            pointData.save()
            //if (isNew == true) {
            posData.setPosDetails(posDataDetails)
            posData.setPoint(pointData)

            pointData.setPointDetails(pointDataDetails)
            pointData.setPosDatas(pdList)

            posDataDetails.setPos(posData)
            pointDataDetails.setPoint(pointData)
            pointDataDetails.save()
            posData.save()
            //}

            pointsList.add(pointData)
			
			
        }

        /* Save points from AllPointsCommand */
        cmd.allPoints?.each { AllPointsCommand apc ->
            //boolean isNew = false
            if (apc == null) {
                log.info "AllPointCommand is NULL - skipping!"
                return
            }
			
			PointData point
			
			if (apc.id != null) {
				log.info "ZNALAZLEM PUNKT - ALLPOINTS"
				point = PointData.get(apc.id)
			}
			else {
				log.info "NIE ZNALAZLEM PUNKTU - ALLPOINTS"
				point = new PointData()
			}
			
            if (point != null) {
                // Update data from CBD
                if (apc.cbdId != null) {
                    point.nazwa = apc.nazwa
                    point.ulica = apc.ulica
                    point.kodPocztowy = apc.kodPocztowy
                    point.liczbaPos = apc.liczbaPos
                    point.miejscowosc = apc.miejscowosc
                    point.nrBudynku = apc.nrBudynku
                    point.cbdId = apc.cbdId
                }

                point.czyWybranyAkceptacjaKart = apc.czyWybranyAkceptacjaKart
                point.czyWybranyZakresUruchomienia = apc.czyWybranyZakresUruchomienia
                point.tytulPlatnosci = apc.tytulPlatnosci
                point.systemKasowy = apc.systemKasowy
                point.uta = apc.uta

                //if (isNew == true) {
                pointsList.add(point)
                //}
            }
            else {
                log.info "Nie znaleziono punktu o id: " + apc.id
            }
        }

        return pointsList
    }

    //TODO - metod nieuzywana????
    def getPointCommandsToPosDataList(def cmd) {
        def pointsList = []
        cmd.poses?.each { PointCommand pc ->
			boolean isNew = false
			if (pc == null) {
				log.info "getPointCommandsToPosDataList: PointCommand is NULL - skipping!"
				return
			}

			PointData pointData
			PointDataDetails pointDataDetails
			PosData posData
			PosDataDetails posDataDetails

			ArrayList<PosData> pdList = new ArrayList<PosData>()

			if (pc.id == null) {
				log.info "NOWY PUNKT DLA POS"
				pointData = new PointData()
				pointDataDetails = new PointDataDetails()

				posData = new PosData()
				posDataDetails = new PosDataDetails()
				isNew = true
			}
			else {
				posData = PosData.get(pc.id)
				
				if (posData != null) {
					pointData = posData.point
					posDataDetails = posData.posDetails

					if (pointData != null) {
						log.info "getPointCommandsToPosDataList - Znaleziono punkt o id: " + pointData.id + " dla POS o id: " + pc.id
						pointDataDetails = pointData.pointDetails
					}
					else {
						log.info "getPointCommandsToPosDataList - Brakujacy punkt dla POS o id: " + pc.id
						pointData = new PointData()
						pointDataDetails = new PointDataDetails()
					}
				}
				else {
					log.info "getPointCommandsToPosDataList - Nie znaleziono POS o id: " + pc.id
					return
				}
			}

			pdList.add(posData)

			pc.properties.each { key, value ->
				log.info "PCPOSProperties " + key + ": " + value
				if (PointData.metaClass.respondsTo(PointData, "set" + key.capitalize()) && key != 'id') {
					pointData."set${key.capitalize()}"(value)
				}

				if (PointDataDetails.metaClass.respondsTo(PointDataDetails, "set" + key.capitalize()) && key != 'id') {
					pointDataDetails."set${key.capitalize()}"(value)
				}

				if (PosData.metaClass.respondsTo(PosData, "set" + key.capitalize())) {
					// FIXME krytyczne obejscie, nigdy nie powinno byc null
					posData?."set${key.capitalize()}"(value)
				}

				if (PosDataDetails.metaClass.respondsTo(PosDataDetails, "set" + key.capitalize()) && key != 'id') {
					posDataDetails."set${key.capitalize()}"(value)
				}
			}

			// Create POSes with same values
			def terminalCount = 0
			terminalCount += posDataDetails?.gprsIlosc != null ? posDataDetails?.gprsIlosc : 0
			terminalCount += posDataDetails?.dialupIlosc != null ? posDataDetails?.dialupIlosc : 0
			terminalCount += posDataDetails?.vpnIlosc != null ? posDataDetails?.vpnIlosc : 0
			terminalCount += posDataDetails?.sslIlosc != null ? posDataDetails?.sslIlosc : 0
			terminalCount += posDataDetails?.wifiIlosc != null ? posDataDetails?.wifiIlosc : 0
			terminalCount += posDataDetails?.bazaIlosc != null ? posDataDetails?.bazaIlosc : 0
			
			// Create cloned poses only when they are not already cloned
			if (terminalCount > 1 && terminalCount > pointData.liczbaPos) {
				for (int i = 0; i < terminalCount; i++) {
					PosData posDataNew
					PosDataDetails posDataDetailsNew
					Serializable posDataSer = posData
					posDataNew = SerializationUtils.clone(posDataSer) // as PosData
					posDataNew.id = null
					posDataNew.version = 0
					posDataDetailsNew = SerializationUtils.clone(posDataDetails)  // as PosDataDetails
					posDataDetailsNew.id = null
					posDataDetailsNew.version = 0
					
					posDataNew.setPosDetails(posDataDetailsNew)
					posDataNew.setPoint(pointData)
					posDataDetailsNew.setPos(posDataNew)

					pdList.add(posDataNew)
				}
			}
			
			// Set telePomka and teleKodzik based on terminalIlosc
			if (pc.terminalIlosc != null && pc.terminalIlosc > 0 && terminalCount > pointData.liczbaPos) {
				for (int i = 0; i < pc.terminalIlosc && i < pdList.size; i++) {
					pdList.get(i).posDetails?.telePompka = posData.posDetails?.telePompka
					pdList.get(i).posDetails?.teleKodzik = posData.posDetails?.teleKodzik
				}
			}
			else if (pc.terminalIlosc != null && pc.terminalIlosc > 0 && terminalCount == pointData.liczbaPos) {
				for (int i = 0; i < pc.terminalIlosc; i++) {
					pointData.posDatas?.get(i).posDetails?.telePompka = posData.posDetails?.telePompka
					pointData.posDatas?.get(i).posDetails?.teleKodzik = posData.posDetails?.teleKodzik
				}
			}

			pointData.nip = pointDataDetails.nipPunktu
			pointData.nazwa = pointDataDetails.nazwaDoWyszukiwarki
			pointData.ulica = pointDataDetails.korespondencjaUlica
			pointData.nrLokalu = pointDataDetails.korespondencjaNrLokalu
			pointData.nrBudynku = pointDataDetails.korespondencjaNrDomu
			pointData.miejscowosc = pointDataDetails.korespondencjaMiasto
			pointData.kodPocztowy = pointDataDetails.korespondencjaKodPocztowy
			pointData.poczta = pointDataDetails.korespondencjaPoczta
			pointData.liczbaPos = pdList.size()
			pointData.save()
			//if (isNew == true) {
			posData.setPosDetails(posDataDetails)
			posData.setPoint(pointData)

			pointData.setPointDetails(pointDataDetails)
			pointData.setPosDatas(pdList)

			posDataDetails.setPos(posData)
			pointDataDetails.setPoint(pointData)
			posData.save()
			//}

			pointsList.add(pointData)
        }

        /* Save poses from AllPosCommand */
        cmd.allPoses?.each { AllPosCommand apc ->
			
            if (apc == null) {
                log.info "AllPosCommand is NULL - skipping!"
                return
            }
			ArrayList<PosData> pdList = new ArrayList<PosData>()
            PosData pos;
			PointData point;

            if (apc.id != null) {
                pos = PosData.get(apc.id)
				log.info "Got POS Data!"
				if (pos != null) {
					point = pos.point
				}
				
            }
			else if (apc.cbdId != null) {
				
				point = PointData.findByCbdId(apc.cbdId)
				
				if (point != null) {
					log.info "FOUND POINT FOR CBD POS"
				}
				else {
					log.info "DIDN'T FIND POINT FOR CBD POS"
					point = new PointData()
				}
			}
            else {
                pos = new PosData()
				point = new PointData()
            }

			if (pos != null) {
				if (apc.tpsId != null) {
					pos.dataOd = apc.dataOd
					pos.dataDo = apc.dataDo
					pos.numerZestawuPos = apc.numerZestawuPos
					pos.wysokoscOplaty = apc.wysokoscOplaty
				}
				
				pdList.add(pos)
				pos.czyWybrany = apc.czyWybrany
				pos.tpsId = apc.tpsId
				
				point.cbdId = apc.cbdId
				point.save()
				
				point.setPosDatas(pdList)
				
				pos.setPoint(point)
				pos.save()
			}

            pointsList.add(point)
        }

        return pointsList
    }

    def findDocumentByName(def documents, def name) {
        if (documents != null) {
            for(DocumentFile df : documents) {
                if (df.name.equals(name))
                    return df
            }
        }

        return null
    }

}