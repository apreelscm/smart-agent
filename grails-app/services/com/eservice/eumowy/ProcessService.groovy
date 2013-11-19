package com.eservice.eumowy

import com.eservice.eumowy.annotation.DateField
import com.eservice.eumowy.annotation.Omit
import com.eservice.eumowy.command.*
import com.eservice.eumowy.util.DateUtils
import com.eservice.eumowy.util.EumowyCustomEnvironment
import grails.util.Environment
import groovy.sql.GroovyRowResult
import org.apache.commons.lang.WordUtils
import org.codehaus.groovy.grails.web.binding.DataBindingUtils
import serializationutils.SerializationUtils

class ProcessService {

    def sessionFactory
    def panelService
    def cbdService
    def panelMockService
    def calculatorService

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
        cmd.allPoses?.addAll(getPosesToAllPosCommandList(process, cmd, calc))
		
		cmd.allPoints?.each { AllPointsCommand apc ->
			if (apc.cbdId == -1) {
				PointData point = PointData.findById(apc.id)
				if (point != null) {
					boolean atLeastOneLocalPos = point.posDatas?.findAll { PosData pos -> pos.tpsId == null } != null
					if (atLeastOneLocalPos == false) {
						log.info "USUWAM PUNKT O ID: " + point.id
						process.removeFromPoints(point)
						process.discard()
					}
					else {
						log.info "ZERUJĘ CBDID - PUNKT O ID: " + point.id
						point.cbdId = null
						point.save()
						apc.cbdId = null
					}
					//point.delete(flush: true)
				}
			}
		}
      //  process.save(flush:true)
		cmd.allPoints?.removeAll { it.cbdId == -1 }

        cmd.hirePaymentsByPoint?.addAll(getHirePaymentByPointCommandList(cmd, calc))
        cmd.hirePaymentsByPos?.addAll(getHirePaymentByPosCommandList(cmd, calc))

        prepareProcessCommand(cmd, calc,)
		prepareAllPosCommands(cmd.allPoses, calc)
		
		cmd
	}

    def getSavedProcessCommand(def process, def calc, def newProcess){
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
        cmd.allPoses?.addAll(getPosesToAllPosCommandList(process, cmd, calc))
        cmd.hirePaymentsByPoint?.clear()
        cmd.hirePaymentsByPos?.clear()

        if (newProcess){
            cmd.hirePaymentsByPoint?.addAll(getHirePaymentByPointCommandList(cmd, calc))
            cmd.hirePaymentsByPos?.addAll(getHirePaymentByPosCommandList(cmd, calc))
        } else {
            cmd.hirePaymentsByPoint?.addAll(getHirePaymentCommandFromHirePayments(process.hirePayments.findAll{ it.tpsId == null}))
            cmd.hirePaymentsByPos?.addAll(getHirePaymentCommandFromHirePayments(process.hirePayments.findAll{ it.tpsId != null}))
        }

        //FIXME Optymalizacja usuwania, z uwzglednieniem tego, zeby nie zamykalo sesji, bo potem
		//		w metodzie prepareProcessCommand jest zamknieta sesja i nie mozna zrobic lazyLoad na liscie paneli
		cmd.allPoses?.each { AllPosCommand apc ->
			if (apc.tpsId == -1) {
				PosData pos = PosData.findById(apc.id)
				if (pos != null) {
					log.info "USUWAM POS O ID: " + pos.id
					PointData point = pos.point
					point.removeFromPosDatas(pos)
					point.save()
				}
			}
		}
		cmd.allPoses?.removeAll { it.tpsId == -1 }
		process.save()
		cmd.allPoints?.each { AllPointsCommand apc ->
			if (apc.cbdId == -1) {
				PointData point = PointData.findById(apc.id)
				if (point != null) {
					boolean atLeastOneLocalPos = point.posDatas?.findAll { PosData pos -> pos.tpsId == null } != null
					if (atLeastOneLocalPos == false) {
						log.info "USUWAM PUNKT O ID: " + point.id
						process.removeFromPoints(point)
						point.delete(flush: true)
					}
					else {
						log.info "ZERUJĘ CBDID - PUNKT O ID: " + point.id
						point.cbdId = null
						point.save()
						apc.cbdId = null
					}
				}
			}
		}
		process.save(flush:true)
		
		cmd.allPoints?.removeAll { it.cbdId == -1 }
		

        cmd.notes = process.notesToCoa
		
        prepareProcessCommand(cmd, calc, cbdMethods)
		preparePointCommands(cmd.points, cmd.poses, calc)
		//prepareAllPosCommands(cmd.allPoses, calc)
		
		cmd
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

	def preparePointCommands(def points, def poses, def calc) {
		points.each { PointCommand pc ->
			panelService.setupPointDataFromCalc(pc, calc)
		}
		poses.each { PointCommand pc ->
			panelService.setupPointDataFromCalc(pc, calc)
		}
	}
	
	def prepareAllPosCommands(def allPoses, def calc) {
		allPoses.each { AllPosCommand apc ->
			panelService.setupAllPosDataFromCalc(apc, calc)
		}
	}
	
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

                log.debug("invokin ${panelFunctionName} on panelService")
                populateMethod.call(cmd, calc, panelFunctionName)
            }
        }

        cmd
    }

    def filterExcludedPanels(Process process, List<Panel> panelsList){
        def activePanels = []

        panelsList?.each { it ->
            // add future checks here
            def shouldBeExcluded = excludePoziomOplatiWarunkiPlatnosciKarty( process, it.name)

            if (!shouldBeExcluded){
                activePanels.add(it)
            }

        }

        activePanels
    }

    /**
     * eUmowy_ext-411 Rozszerzenie bez Zmiana prowizji - usuniecie panelu Poziom opłat i warunki płatnosci karty
     */
    boolean excludePoziomOplatiWarunkiPlatnosciKarty(def process, def panelName){
        def hasRozszerzenieDodPunkt = containsActivity(process.activities,"dodatkowyPunkt")
        def hasZmianaProwizji = containsActivity(process.activities,"zmianaProwizji")
        if (hasRozszerzenieDodPunkt && ! hasZmianaProwizji && "poziomOplatiWarunkiPlatnosciKarty" == panelName){
            log.info "excludePoziomOplatiWarunkiPlatnosciKarty - excluding panel [${panelName} from active panel list]"
            return true
        }
        return false
    }

    /**
     *  create data
     * */
    def loadProcessData(def process, def cmd) {
        process.processData?.each {ProcessData data ->
            if (!cmd.hasProperty(data.name)){
                log.warn('NoSuchField in ProcessComand for : ' + data.name)
                return
            } else if (["errors", "class"].contains(data.name) || (hasAnnotation(cmd, data.name, Omit) && getAnnotation(cmd, data.name, Omit).inPopulate())){
                return
            } else if (hasAnnotation(cmd, data.name, DateField)){
                cmd[data.name] = data.value?.trim()? DateUtils.getFormattedDate(DateUtils.parseWithTimezone(data.value), DateUtils.YYYY_MM_DD) : "";
                return;
            } else {
                cmd[data.name] = data.value ?: ""
            }
        }

        cmd
    }

    def getLocalPointsToPointCommandList(def process) {
        def localPoints = []
        process.points.each { PointData point ->

            /* Don't load points from CBD or points that were from CBD, but were removed and left in our DB - they have nulled CbdId but they lack point details */
            if (point.cbdId != null || (point.cbdId == null && point.pointDetails == null)) {
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
                        "constraints",
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
                if (["point","constraints", "pointId"]
                        .contains(key) || value == null){
                    return
                }

                if (PointCommand.metaClass.respondsTo(PointCommand, "set"+key.capitalize())) {
                    pc."set${key.capitalize()}"(value)
                }
            }

            //def posData = point.posDatas != null && point.posDatas.size() > 0 ? point.posDatas[0] : null
            def posData = point.posDatas?.find { PosData pd -> pd.tpsId == null }

            posData?.properties.each { key, value ->
                log.debug "PosData Key: " + key
                if (["tpsId","constraints",
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
			
			/*if (point.cbdId == null && point.pointDetails != null) {
				return
			}*/
			
            point.posDatas?.each { PosData posData ->

                /* Don't load POSes from CBD */
                if (!posData || posData.tpsId != null) {
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
                        "constraints",
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
                DataBindingUtils.bindObjectToInstance(apc, posData.properties
                        , ["id","tpsId", "numerZestawuPos", "dataOd", "dataDo", "wysokoscOplaty", "czyWybrany"], [], null)
                apc.setCzyCbd(false)
                apc.id = posData.id;
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
			apc.setNip(row.get("nip"))

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

    private def getHirePaymentByPointCommandList(def cmd, def calc) {
        def hpcResult = []

        def result = cbdService.getHirePaymentByPoint(cmd.nip)
        result.each { GroovyRowResult row ->
            HirePaymentCommand hpc = new HirePaymentCommand()
            hpc.setCbdId(Integer.valueOf(row.get("point_id").toString()))
            hpc.setName(row.get("nazwa_punktu").toString())
            hpc.setAddress(row.get("adres_posadowienia").toString())
            hpc.setType(row.get("typ"))
            def count = row.get("ile");
            if (count != null && count?.toString().isNumber()){
                hpc.setTermCount(Integer.valueOf(count.toString()))
            }
            def payment = row.get("oplata_za_pos");
            if (payment != null && payment?.toString().isNumber()){
                hpc.setCurrentTermPayment(payment.toString().toBigDecimal())
            }
            def hirePayment = calculatorService.getCalcProperty(calc,"CENA_NAJMU")
            if (hirePayment && hirePayment.toString()?.isNumber()){
                hpc.setNewTermPayment(hirePayment.toString().toBigDecimal())
            }
            hpc.setIsChoosen(false)

            //gdy dojdzie PP trzeba zapisac te dane tutaj
//            hpc.setPpCount()
//            hpc.setCurrentPpPayment()
            hpcResult.add(hpc)
        }
        return hpcResult
    }

    private def getHirePaymentByPosCommandList(def cmd, def calc) {
        def hpcResult = []

        def result = cbdService.getHirePaymentByPos(cmd.nip)
        result.each { GroovyRowResult row ->
            HirePaymentCommand hpc = new HirePaymentCommand()
            hpc.setTpsId(Integer.valueOf(row.get("pos_id").toString()))
            hpc.setPosNumber(row.get("tid").toString())
            hpc.setCbdId(Integer.valueOf(row.get("point_id").toString()))
            hpc.setName(row.get("nazwa_punktu").toString())
            hpc.setAddress(row.get("adres_posadowienia").toString())
            hpc.setType(row.get("rodzaj_terminala"))
            def count = row.get("terminal_count")
            if (count != null && count?.toString().isNumber()){
                hpc.setTermCount(Integer.valueOf(count.toString()))
            }
            def payment = row.get("oplata_za_pos");
            if (payment != null && payment?.toString().isNumber()){
                hpc.setCurrentTermPayment(payment.toString().toBigDecimal())
            }
            def hirePayment = calculatorService.getCalcProperty(calc,"CENA_NAJMU")
            if (hirePayment && hirePayment.toString()?.isNumber()){
                hpc.setNewTermPayment(hirePayment.toString().toBigDecimal())
            }
            hpc.setIsChoosen(false)

            //gdy dojdzie PP trzeba zapisac te dane tutaj
//            hpc.setPpCount()
//            hpc.setCurrentPpPayment()

            hpcResult.add(hpc)
        }
        return hpcResult
    }

    private def getHirePaymentCommandFromHirePayments(def dataSet){
        def result = []
        dataSet.each{ HirePayment hp ->
            HirePaymentCommand hpc = new HirePaymentCommand()

            hpc.setTpsId(hp.tpsId)
            hpc.setPosNumber(hp.posNumber)
            hpc.setCbdId(hp.cbdId)
            hpc.setName(hp.name)
            hpc.setAddress(hp.address)
            hpc.setType(hp.type)
            hpc.setTermCount(hp.termCount)
            hpc.setPpCount(hp.ppCount)
            hpc.setCurrentTermPayment(hp.currentTermPayment)
            hpc.setCurrentPpPayment(hp.currentPpPayment)
            hpc.setNewTermPayment(hp.newTermPayment)
            hpc.setNewPpPayment(hp.newPpPayment)
            hpc.setIsChoosen(hp.isChoosen)

            result.add(hpc)
        }
        return result
    }

    def getPointsToAllPointsCommandList(def process, def cmd) {
        def localPoints = [], cbdPoints = [], cbdIdsToRemove = [], result = []
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
					apc.nip = point.nip
					cbdIdsToRemove.add(apc.cbdId)
					//cbdPoints.remove(cbdPoints.findIndexOf { AllPointsCommand i -> i.cbdId == apc.cbdId })
                }
                else {
                    apc.cbdId = -1 // Mark the point for deletion from local (eumowy) db
                }
            }

            result.add(apc)
        }
		cbdIdsToRemove.each { Integer cbdId ->
			cbdPoints.removeAll { AllPointsCommand i -> i.cbdId == cbdId}
		}
        result.addAll(cbdPoints)
        result
    }

    def getPosesToAllPosCommandList(def process, def cmd, def calc) {
        def localPoses = [], cbdPoses = [], tpsIdsToRemove = [], result = []
        getLocalPosesToAllPosesCommandList(process, localPoses)
        getCbdPosesToAllPosesCommandList(cmd, cbdPoses)

		
        /* Merge them with possibly new data from CBD */
        localPoses.each { AllPosCommand apc ->
            if (apc.tpsId != null) {
                AllPosCommand pos = cbdPoses.find { AllPosCommand i -> i.tpsId == apc.tpsId	}

                /* Update field data */
                if (pos) {
                    apc.dataDo = pos.dataDo ?: apc.dataDo
                    apc.dataOd = pos.dataOd ?: apc.dataOd
                    apc.numerZestawuPos = pos.numerZestawuPos ?: apc.numerZestawuPos
                    //apc.wysokoscOplaty = pos.wysokoscOplaty ?: (apc.wysokoscOplaty ?: calculatorService.getCalcProperty(calc,"OPLATA_POS_PROM_CENA_NAJMU"))
                    apc.czyCbd = true // Mark for update in local (eumowy) db
					tpsIdsToRemove.add(apc.tpsId)
                }
                else {
                    apc.tpsId = -1 // Mark POS for deletion from local (eumowy) db
                }
            }
			
            result.add(apc)
        }
		tpsIdsToRemove.each { Integer tpsId ->
			cbdPoses.removeAll { AllPosCommand i -> i.tpsId == tpsId}
		}
        result.addAll(cbdPoses)
        result.sort {it.id}

//		Na chwile obecna przeniesione do odrebnej metody i wywolywane w hierarchii wyzej		
//		BigDecimal price = panelService.toBigDecimal(calculatorService.getCalcProperty(calc, "OPLATA_POS_PROM_CENA_NAJMU"))
//		result.each { AllPosCommand apc ->
//			apc.wysokoscOplaty = panelService.setAtLeastAs(apc.wysokoscOplaty, price)
//		}
		
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
                //log.debug("process data: ${data.processId?.class} ${data.version?.class} ${data.name?.class} ${data.id?.class}")
            }else if(data.value != foundData.value){
                foundData.value = data.value
            }
        }

        def pointDataList = getPointCommandsToPointDataList(cmd)

        pointDataList?.each { PointData point ->
            point.save(flush: true)
            process.addToPoints(point)
        }

		def posDataList = getPointCommandsToPosDataList(cmd, process)

		posDataList?.each { PointData point ->
			point.save(flush: true)
			process.addToPoints(point);
		}

        fillPaymentUsage(cmd, process);

        process.notesToCoa = cmd.notes //notesToCOA

        process
    }

    private def fillPaymentUsage(def cmd, def process){
        if ('one_for_all_terminals'.equals(cmd.odplatneUzywanie)){
            // dane dla tej opcji trzymamy w procesie
            println 'Zapisujemy dla one_for_all_terminals'
            clearHirePayments(process)
            saveHirePaymets(cmd.hirePaymentsByPoint, process, false)
            saveHirePaymets(cmd.hirePaymentsByPos, process, false)
        } else if ('one_for_all_terminals_in_point'.equals(cmd.odplatneUzywanie)){
            clearHirePayments(process)
            saveHirePaymets(cmd.hirePaymentsByPoint, process, true)
            saveHirePaymets(cmd.hirePaymentsByPos, process, false)
        } else if ('other_for_selected_terminals'.equals(cmd.odplatneUzywanie)){
            clearHirePayments(process)
            saveHirePaymets(cmd.hirePaymentsByPoint, process, false)
            saveHirePaymets(cmd.hirePaymentsByPos, process, true)
        }
    };

    private def saveHirePaymets(def hirePayments, def process, def choosen){
        hirePayments.each{ HirePaymentCommand hpc ->
            HirePayment hp = new HirePayment()
            hp.setTpsId(hpc.tpsId)
            hp.setPosNumber(hpc.posNumber)
            hp.setCbdId(hpc.cbdId)
            hp.setName(hpc.name)
            hp.setAddress(hpc.address)
            hp.setType(hpc.type)
            hp.setTermCount(hpc.termCount)
            hp.setPpCount(hpc.ppCount)
            hp.setCurrentTermPayment(hpc.currentTermPayment)
            hp.setCurrentPpPayment(hpc.currentPpPayment)
            hp.setNewTermPayment(hpc.newTermPayment)
            hp.setNewPpPayment(hpc.newPpPayment)
            hp.setIsChoosen(hpc.isChoosen)
            hp.setIsVisible(choosen)

            hp.setProcess(process)
            hp.save(flush: true)
            process.addToHirePayments(hp)
        }
        process
    }

    private def clearHirePayments(def process){
        def idsToDelete = []
        process.hirePayments?.each { HirePayment hp ->
            idsToDelete.add(hp.id);
        }

        idsToDelete.each {
            HirePayment hp = HirePayment.findById(it);
            if (hp){
                process.removeFromHirePayments(hp)
                hp.delete(flush: true);
            }
        }
        process
    }

    private def addCurrentDate(def processDataList){
        processDataList.add(new ProcessData([name: 'dataUmowy', value: DateUtils.formatWithTimezone(DateUtils.getCurrentDate())]))
    }

// Then, we will write a method to take an object and an annotation class
// And we will return all properties of the object that define that annotation
    private def findAllPropertiesToSave( def obj, def annotClass ) {
        obj.properties.findAll { prop ->
			obj.getClass().declaredFields.find {
                it.name == prop.key && (!it.isAnnotationPresent(annotClass) || !it.getAnnotation(annotClass).inSave())
            }
        }
    }

    private def hasAnnotation(def obj, def property, def annotClass){
        obj.getClass().declaredFields.find { it -> it.name == property }.isAnnotationPresent(annotClass);
    }

    private def getAnnotation(def obj, def property, def annotClass){
        obj.getClass().declaredFields.find { it -> it.name == property }.getAnnotation(annotClass);
    }

    def getDataFromPanels(def cmd) {
        def processDataList = [];
        def dataToSave = findAllPropertiesToSave(cmd, Omit);
        dataToSave.findAll { it.value != ProcessCommand.DEFAULT_VALUE && !["class", "errors"].contains(it.key) }.each { key, value ->
            if (hasAnnotation(cmd, key, DateField) || "dataUmowy".equals(key)){
                processDataList.add(new ProcessData(name: "${key}", value:"${DateUtils.formatWithTimezoneFromStr(value)}"));
                return;
            }

            log.debug("key: [${key}] , value [${value}]")
            processDataList.add(new ProcessData(name: "${key}", value:"${value ?: ''}"));
        }
        processDataList
    }

    def getPointCommandsToPointDataList(def cmd) {
        def pointsList = []

        cmd.points?.each { PointCommand pc ->
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
            }
            else {
				log.info "EXISTING POINT!"
                pointData = PointData.get(pc.id)

                if (pointData != null) {
                    log.debug "getPointCommandsToPointDataList - Znaleziono punkt o id: " + pc.id
                    pointDataDetails = pointData.pointDetails

                    posData = pointData.posDatas?.getAt(0)
                    posDataDetails = posData?.posDetails

                    if (posData == null) {
                        log.debug "getPointCommandsToPointDataList - Brakujacy POS dla Danych punktu o id: " + pc.id + " - Tworzę nowy!"
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
                log.debug "PCProperties " + key + ": " + value
                if (PointData.metaClass.respondsTo(PointData, "set" + key.capitalize())) {
                    pointData."set${key.capitalize()}"(value)
                }

                if (PointDataDetails.metaClass.respondsTo(PointDataDetails, "set" + key.capitalize()) && key != 'id') {
                    pointDataDetails."set${key.capitalize()}"(value)
                }

                if (PosData.metaClass.respondsTo(PosData, "set" + key.capitalize())  && key != 'id') {
                    // FIXME krytyczne obejscie, nigdy nie powinno byc null. Dodalem logi gdy sytuacja z NULL wystepuje, sprawdzimy czy nadal sa takie przypadki - mkniec
                    if (posData != null) {
						posData?."set${key.capitalize()}"(value)
                    }
					else {
						log.warn "FIXMEII - PosData = NULL"
					}
                }

                if (PosDataDetails.metaClass.respondsTo(PosDataDetails, "set" + key.capitalize()) && key != 'id') {
                    posDataDetails."set${key.capitalize()}"(value)
                }
            }
			
			updateChildPosData(posData, pointData.posDatas)

			// Create POSes with same values
			def terminalCount = 0
			terminalCount += posDataDetails?.gprsIlosc != null ? posDataDetails?.gprsIlosc : 0
			terminalCount += posDataDetails?.dialupIlosc != null ? posDataDetails?.dialupIlosc : 0
			terminalCount += posDataDetails?.vpnIlosc != null ? posDataDetails?.vpnIlosc : 0
			terminalCount += posDataDetails?.sslIlosc != null ? posDataDetails?.sslIlosc : 0
			terminalCount += posDataDetails?.wifiIlosc != null ? posDataDetails?.wifiIlosc : 0

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
				for (int i = 0; i < pc.terminalIlosc && i < pointData.posDatas.size(); i++) {
					pointData.posDatas?.get(i).posDetails?.telePompka = posData.posDetails?.telePompka
					pointData.posDatas?.get(i).posDetails?.teleKodzik = posData.posDetails?.teleKodzik
				}
			}

            pointData.nip = pointDataDetails.nipPunktu
            pointData.nazwa = pointDataDetails.nazwaDoWydrukuZTerminalaPos
            pointData.ulica = pointDataDetails.wydrukUlica
            pointData.nrLokalu = pointDataDetails.wydrukNrLokalu
            pointData.nrBudynku = pointDataDetails.wydrukNrDomu
            pointData.miejscowosc = pointDataDetails.wydrukMiasto
            pointData.kodPocztowy = pointDataDetails.wydrukKodPocztowy
            pointData.poczta = pointDataDetails.wydrukPoczta
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

			pdList.each { PosData pd ->
				if (pd != posData) {
					pd.parentPosId = posData.id
                    pd.save(flush: true)
				}
			}
			
            pointsList.add(pointData)
			
			
        }

        /* Save points from AllPointsCommand */
        cmd.allPoints?.each { AllPointsCommand apc ->
            //boolean isNew = false
            if (apc == null) {
                log.debug "AllPointCommand is NULL - skipping!"
                return
            }
			
			PointData point
			
			if (apc.id != null) {
				log.debug "ZNALAZLEM PUNKT - ALLPOINTS"
				point = PointData.get(apc.id)
			}
			else {
				log.debug "NIE ZNALAZLEM PUNKTU - ALLPOINTS"
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
					point.nip = apc.nip
                }

                point.czyWybranyAkceptacjaKart = apc.czyWybranyAkceptacjaKart
                point.czyWybranyZakresUruchomienia = apc.czyWybranyZakresUruchomienia
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

    def getPointCommandsToPosDataList(def cmd, def process) {
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
                log.info "NOWY POS"
                //pointData = new PointData()
                //pointDataDetails = new PointDataDetails()

                if (pc.cbdId != null) {
					if (pointsList.find { PointData pd -> pd.cbdId == pc.cbdId } != null) {
						log.info "Istnieje juz punkt dla pos o danym cbdId - tworzymy nowy"
						pointData = new PointData()
						pointDataDetails = new PointDataDetails()
					}
					else {
						pointData = PointData.findByCbdIdAndProcess(pc.cbdId, process)
						
						if (pointData == null) {
							log.info "NOWY PUNKT DLA POS"
							pointData = new PointData()
							pointDataDetails = new PointDataDetails()
						}
						else {
							pointDataDetails = pointData.pointDetails
							if (pointDataDetails == null) {
								pointDataDetails = new PointDataDetails()
							}
						}
					}
					
                    log.info "Ustawiam dane z CBD"
                    def cbdPoint = cbdService.getCbdPointById(cmd.nip,
                            pc.cbdId)
                    if (cbdPoint != null) {

                        pointData.setCbdId(Integer.valueOf(cbdPoint.get("id").toString()))
                        pointData.setKodPocztowy(cbdPoint.get("kod_pocztowy"))
                        //pointData.setLiczbaPos(Integer.valueOf(cbdPoint.get("liczba_pos").toString()))
                        pointData.setMiejscowosc(cbdPoint.get("miejscowosc"))
                        pointData.setNazwa(cbdPoint.get("nazwa"))
                        pointData.setNrBudynku(cbdPoint.get("nr_budynku"))
                        pointData.setUlica(cbdPoint.get("ulica"))
                        pointData.setNip(cbdPoint.get("nip"))
                    }
                    else {
                        log.info "DIDN'T FIND POINT INFORMATION IN CBD!!! CBDID: " + pc.cbdId + " NIP: " + cmd.nip
                    }
                }
                else {
                    log.info "Punkt nie pochodzi z CBD"
					pointData = new PointData()
					pointDataDetails = new PointDataDetails()
                }

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

                        if (pc.cbdId != null) {
                            def cbdPoint =
                                cbdService.getCbdPointById(cmd.nip, pc.cbdId)
                            if (cbdPoint != null) {
                                pointData.setCbdId(Integer.valueOf(cbdPoint.get("id").toString()))
                                pointData.setKodPocztowy(cbdPoint.get("kod_pocztowy"))
								//pointData.setLiczbaPos(Integer.valueOf(cbdPoint.get("liczba_pos").toString()))
                                pointData.setMiejscowosc(cbdPoint.get("miejscowosc"))
                                pointData.setNazwa(cbdPoint.get("nazwa"))
                                pointData.setNrBudynku(cbdPoint.get("nr_budynku"))
                                pointData.setUlica(cbdPoint.get("ulica"))
                                pointData.setNip(cbdPoint.get("nip"))
                            }
                            else {
                                log.info "DIDN'T FIND POINT INFORMATION IN CBD!!! CBDID: " + pc.cbdId + " NIP: " + cmd.nip
                            }
                        }
                    }
                }
                else {
                    log.info "getPointCommandsToPosDataList - Nie znaleziono POS o id: " + pc.id
                    return
                }
            }
			
            pdList.add(posData)

            pc.properties.each { key, value ->
                log.debug "PCPOSProperties " + key + ": " + value
                if (PointData.metaClass.respondsTo(PointData, "set" +
                        key.capitalize()) && key != 'id') {
                    pointData."set${key.capitalize()}"(value)
                }

                if(PointDataDetails.metaClass.respondsTo(PointDataDetails, "set" + key.capitalize()) && key != 'id') {
                    pointDataDetails."set${key.capitalize()}"(value)
                }

                if (PosData.metaClass.respondsTo(PosData, "set" +
                        key.capitalize())) {
                    // FIXME krytyczne obejscie, nigdy nie powinno byc null. Dodalem logi gdy sytuacja z NULL wystepuje, sprawdzimy czy nadal
                    // sa takie przypadki - mkniec
                    if (posData != null) {
                        posData?."set${key.capitalize()}"(value)
                    }
                    else {
                        log.warn "FIXME - PosData = NULL"
                    }
                }

                if (PosDataDetails.metaClass.respondsTo(PosDataDetails,
                        "set" + key.capitalize()) && key != 'id') {
                    posDataDetails."set${key.capitalize()}"(value)
                }
            }
			updateChildPosData(posData, pointData.posDatas)

            // Create POSes with same values
            def terminalCount = 0
            terminalCount += posDataDetails?.gprsIlosc != null ?
                posDataDetails?.gprsIlosc : 0
            terminalCount += posDataDetails?.dialupIlosc != null ?
                posDataDetails?.dialupIlosc : 0
            terminalCount += posDataDetails?.vpnIlosc != null ?
                posDataDetails?.vpnIlosc : 0
            terminalCount += posDataDetails?.sslIlosc != null ?
                posDataDetails?.sslIlosc : 0
            terminalCount += posDataDetails?.wifiIlosc != null ?
                posDataDetails?.wifiIlosc : 0

            // Create cloned poses only when they are not already cloned
            if (terminalCount > 1 && terminalCount > pointData.liczbaPos) {
                for (int i = 0; i < terminalCount; i++) {
                    PosData posDataNew
                    PosDataDetails posDataDetailsNew
                    Serializable posDataSer = posData
                    posDataNew = SerializationUtils.clone(posDataSer)
// as PosData
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
            if (pc.terminalIlosc != null && pc.terminalIlosc > 0 &&
                    terminalCount > pointData.liczbaPos) {
                for (int i = 0; i < pc.terminalIlosc && i <
                        pdList.size; i++) {
                    pdList.get(i).posDetails?.telePompka =
                        posData.posDetails?.telePompka
                    pdList.get(i).posDetails?.teleKodzik =
                        posData.posDetails?.teleKodzik
                }
            }
            else if (pc.terminalIlosc != null && pc.terminalIlosc > 0
                    && terminalCount == pointData.liczbaPos) {
                for (int i = 0; i < pc.terminalIlosc && i <
                        pointData.posDatas.size(); i++) {
                    pointData.posDatas?.get(i).posDetails?.telePompka =
                        posData.posDetails?.telePompka
                    pointData.posDatas?.get(i).posDetails?.teleKodzik =
                        posData.posDetails?.teleKodzik
                }
            }

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
			pdList.each { PosData pd ->
				if (pd != posData) {
					pd.parentPosId = posData.id
                    pd.save(flush: true)
				}
			}
			

            pointsList.add(pointData)
        }

        //TODO - save poses!!!

        /* Save poses from AllPosCommand */
        cmd.allPoses?.each { AllPosCommand apc ->

            if (apc == null) {
                log.info "AllPosCommand is NULL - skipping!"
                return
            }
            //ArrayList<PosData> pdList = new ArrayList<PosData>()
            PosData pos;
            PointData point;

            if (apc.id != null) {
                //poieramy point i pos z naszej bazy
                pos = PosData.get(apc.id)
                log.debug "Got POS Data!"
                if (pos != null) {
                    point = pos.point
                }
            }
            else if (apc.cbdId != null) {
                //pobieramy point z bazy CBD
                //point = PointData.findByCbdId(apc.cbdId)
				point = PointData.findByCbdIdAndProcess(apc.cbdId, process)

                if (point != null) {
                    pos = point.posDatas.find{pd -> pd.tpsId == apc.tpsId}
                    if (pos == null){
                        pos = new PosData();
                    }
                    log.info "FOUND POINT FOR CBD POS"
                }
                else {
                    log.info "DIDN'T FIND POINT FOR CBD POS"
                    point = new PointData()
                    pos = new PosData();

                    // Nie znalezlismy punktu z CBD u nas w bazie,
                    //musimy dossac dane z CBD dla tego punktu
                    def cbdPoint = cbdService.getCbdPointById(cmd.nip, apc.cbdId)
                    if (cbdPoint != null) {
                        point.setCbdId(Integer.valueOf(cbdPoint.get("id").toString()))
                        point.setKodPocztowy(cbdPoint.get("kod_pocztowy"))
                        point.setLiczbaPos(Integer.valueOf(cbdPoint.get("liczba_pos").toString()))
                        point.setMiejscowosc(cbdPoint.get("miejscowosc"))
                        point.setNazwa(cbdPoint.get("nazwa"))
                        point.setNrBudynku(cbdPoint.get("nr_budynku"))
                        point.setUlica(cbdPoint.get("ulica"))
                        point.setNip(cbdPoint.get("nip"))
                    }
                    else {
                        log.info "DIDN'T FIND POINT INFORMATION IN CBD!!! CBDID: " + apc.cbdId + " NIP: " + cmd.nip
                    }
                }
            }
            else {
                //zupelnie nowe point i pos
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
                pos.czyWybrany = apc.czyWybrany
                pos.tpsId = apc.tpsId
                //pdList.add(pos)


                point.cbdId = apc.cbdId
                point.save()
				
				if (point.posDatas){
					if (point.posDatas.find { PosData pd -> pd.id == pos.id } == null)
						point.addToPosDatas(pos)
				} else {
					point.setPosDatas([pos])
				}
                //point.setPosDatas(pdList)

                pos.setPoint(point)
                pos.save()
            }

            pointsList.add(point)
        }

        return pointsList
    }
	
	def updateChildPosData(PosData parent, List<PosData> children) {
		children?.each { PosData child ->
			if (child != null && child != parent) {
				parent?.properties.each { key, value ->
					if (["id", "version", "posDetails"].contains(key)) {
						return
					}
					if (PosData.metaClass.respondsTo(PosData, "set" + key.capitalize())) {
							child."set${key.capitalize()}"(value)
					}
	
					if (PosDataDetails.metaClass.respondsTo(PosDataDetails, "set" + key.capitalize()) && key != 'id') {
						child.posDetails?."set${key.capitalize()}"(value)
					}
				}
			}
		}
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