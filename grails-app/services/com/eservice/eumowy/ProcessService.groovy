package com.eservice.eumowy

import com.eservice.eumowy.annotation.DateField
import com.eservice.eumowy.annotation.Omit
import com.eservice.eumowy.command.*
import com.eservice.eumowy.dto.MerchantDetailsDTO
import com.eservice.eumowy.util.DateUtils
import com.eservice.eumowy.util.EumowyCustomEnvironment
import grails.util.Environment
import groovy.sql.GroovyRowResult
import org.apache.commons.lang.WordUtils
import org.codehaus.groovy.grails.web.binding.DataBindingUtils
import serializationutils.SerializationUtils

class ProcessService {
    def messageSource
    def sessionFactory
    def panelService
    def cbdService
    def panelMockService
    def calculatorService
    def bisnodeService

    void invalidateCaches() {
        cbdService.invalidateCaches()
    }

    def searchProcessByFilters(def params) {
        def filterObserved = params.filterObserved
        def filterStatus = params.filterStatus
        def filterNip = params.filterNip

        def filterPhNo = params.filterPhNo
        def filterDateFrom = params.filterDateFrom
        def filterDateTo = params.filterDateTo

        def clientCriteria = Process.createCriteria()
        def searchResults = clientCriteria.list(
                max: params.max,
                offset: params.offset,
                sort: params.sort,
                order: params.order){

            if (filterStatus){
                eq("status", Process.ProcessStatus.valueOf(filterStatus))
            }

            if(isNumber(filterNip)) {
                client {
                    eq("nip", filterNip)
                }
            }

            if(filterPhNo) {
                eq("phNumber", filterPhNo);
            }

            if(DateUtils.isDate(filterDateFrom, DateUtils.DD_MM_YYYY) && DateUtils.isDate(filterDateTo, DateUtils.DD_MM_YYYY)) {
                ge("lastUpdated", DateUtils.parseDate(filterDateFrom, DateUtils.DD_MM_YYYY))
                le("lastUpdated", DateUtils.addDays(DateUtils.parseDate(filterDateTo, DateUtils.DD_MM_YYYY), 1))
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

    Boolean isProcessHasActivity(Process process, String activityCode) {
        return containsActivity(process.activities, activityCode)
    }

    Boolean containsActivity(def activities, def activityCode) {
        return activities?.any{it.code.equals(activityCode)};
    }

    def getNewProcessCommand(def process, def calcId, def calc){
        log.info("getNewProcessCommand processId = ${process.id}")
        def cmd = initProcessCommand(process, calcId)
        cmd.allPoints?.addAll(getPointsToAllPointsCommandList(process, cmd))
        cmd.allPoses?.addAll(getPosesToAllPosCommandList(process, cmd, calc))
		
		cmd.allPoints?.each { AllPointsCommand apc ->
			if (apc.cbdId == -1) {
				PointData point = PointData.findById(apc.id)
				if (point != null) {
					boolean atLeastOneLocalPos = point.posDatas?.findAll { PosData pos -> pos != null && pos.isLocal() == true } != null
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
				}
			}
		}
		cmd.allPoints?.removeAll { it.cbdId == -1 }

        cmd.hirePaymentsCurrent?.addAll(getHirePaymentCurrentCommandList(cmd))
        cmd.hirePaymentsByPoint?.addAll(getHirePaymentByPointCommandList(cmd, calc))
        cmd.hirePaymentsByPos?.addAll(getHirePaymentByPosCommandList(cmd, calc))
        cmd.posExchanges?.addAll(getPosExchangeCommandList(cmd))

        prepareProcessCommand(cmd, calc,)
		preparePointCommands(cmd.points, cmd.poses, calc)
		prepareAllPosCommands(cmd.allPoses, calc)
		
		cmd
	}

    def getSavedProcessCommand(Process process, def calcId, def calc, def newProcess){
        log.info("getSavedProcessCommand processId = ${process.id}")
        ProcessCommand cmd = initProcessCommand(process, calcId)
        loadProcessData(process,cmd)
        cmd.points?.clear()
        cmd.poses?.clear()
        cmd.allPoints?.clear()
        cmd.allPoses?.clear()
        cmd.representatives?.clear()
        cmd.beneficiaries?.clear()
        cmd.points?.addAll(getLocalPointsToPointCommandList(process))
        cmd.poses?.addAll(getLocalPosesToPointCommandList(process))
        cmd.allPoints?.addAll(getPointsToAllPointsCommandList(process, cmd))
        cmd.allPoses?.addAll(getPosesToAllPosCommandList(process, cmd, calc))
        cmd.representatives?.addAll(getRepresentativesCommand(process, Representative.Type.REPRESENTATIVE))
        cmd.beneficiaries?.addAll(getRepresentativesCommand(process, Representative.Type.BENEFICIARY))
        cmd.hirePaymentsByPoint?.clear()
        cmd.hirePaymentsByPos?.clear()
        cmd.hirePaymentsCurrent.clear()

        cmd.hirePaymentsCurrent?.addAll(getHirePaymentCurrentCommandList(cmd))
        if (newProcess){
            cmd.hirePaymentsByPoint?.addAll(getHirePaymentByPointCommandList(cmd, calc))
            cmd.hirePaymentsByPos?.addAll(getHirePaymentByPosCommandList(cmd, calc))
        } else {
            cmd.hirePaymentsByPoint?.addAll(getHirePaymentCommandFromHirePayments(process.hirePayments.findAll{ it.tpsId == null}))
            cmd.hirePaymentsByPos?.addAll(getHirePaymentCommandFromHirePayments(process.hirePayments.findAll{ it.tpsId != null}))
        }

        cmd.posExchanges = mergePosExchanges(process, getPosExchangeCommandList(cmd), getPosExchangeCommandFromPosExchanges(process.posExchanges))

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
					boolean atLeastOneLocalPos = point.posDatas?.findAll { PosData pos -> pos != null && pos.isLocal() == true } != null
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

    List<RepresentativeCommand> getRepresentativesCommand(Process process, Representative.Type type) {
        List<Representative> representatives = process.representatives?.findAll {type.equals(it.typ)}.sort {it.id}
        Map representativeProperties

        List<RepresentativeCommand> result = []
        representatives.each {
            representativeProperties = [:]
            representativeProperties.putAll(it.properties)
            representativeProperties.put("id", it.id)

            representativeProperties.remove("typ")

            if(Representative.Type.REPRESENTATIVE.equals(type)) {
                representativeProperties.remove("posiadaAkceptanta")
                representativeProperties.remove("kontrolujeAkceptanta")
                representativeProperties.remove("znaczaceUdzialy")
                representativeProperties.remove("procentUdzialow")

                result.add(new RepresentativeCommand(representativeProperties))
            } else {
                result.add(new BeneficiaryCommand(representativeProperties))
            }

            representativeProperties = null
        }

        return result
    }

    List<PosExchangeCommand> mergePosExchanges(Process processInstance, List<PosExchangeCommand> fromCbd, List<PosExchangeCommand> fromEumowy) {
        def toAdd = [];
        fromCbd.each { peCbd ->

            if (fromEumowy.any { peEumowy -> peEumowy.tpsId == peCbd.tpsId }){
                //update
                PosExchangeCommand foundEumowy = fromEumowy.find { peEumowy -> peEumowy.tpsId == peCbd.tpsId }

                foundEumowy.setTpsId(peCbd.tpsId)
                foundEumowy.setPosNumber(peCbd.posNumber)
                foundEumowy.setName(peCbd.name)
                foundEumowy.setAddress(peCbd.address)
                foundEumowy.setModel(peCbd.model)
                foundEumowy.setCurrentPrice(peCbd.currentPrice)
            } else {
                //add
                log.info "Adding PosExchange with tpsId: " + peCbd.tpsId + ' from Process: ' + processInstance.id
                toAdd.add(peCbd)
            }
        }

        fromEumowy.addAll(toAdd)

        def toRemove = []
        fromEumowy.each { peEumowy ->
            if (!fromCbd.any{it.tpsId == peEumowy.tpsId}){
                 toRemove.add(peEumowy)
            }
        }

        toRemove.each{
            log.info "Removing PosExchange with id: " + it.id + ", tpsId: " + it.tpsId + ' from Process: ' + processInstance.id

            fromEumowy.remove(it)

            PosExchange peRem = PosExchange.findById(it.id)
            processInstance.removeFromPosExchanges(peRem)
        }

        log.info "fromEumowy size = " + fromEumowy.size()

        fromEumowy
    }

    def getRepresentative1(Process process) {
        Representative representative = process.representatives[0]

        if(representative?.imie && representative?.nazwisko) {
            return [name: representative?.imie, surname: representative?.nazwisko]
        } else {
            return null
        }
    }

    def getRepresentative2(Process process) {
        Representative representative = process.representatives[1]

        if(representative.imie && representative.nazwisko) {
            return [name: representative?.imie, surname: representative?.nazwisko]
        } else {
            return null
        }
    }

    /**
     *  init data
     * */

    def initProcessCommand(def process, def calcId) {
        def cmd = new ProcessCommand();
        cmd.process = process
        cmd.calcId = String.valueOf(calcId)
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
     * eUmowy_ext-558 Rozszerzenie warunku o wymiane umowy zaplaty.
     */
    boolean excludePoziomOplatiWarunkiPlatnosciKarty(def process, def panelName){
        def hasRozszerzenieDodPunkt = containsActivity(process.activities,"dodatkowyPunkt")
        def hasZmianaProwizji = containsActivity(process.activities,"zmianaProwizji")
        def hasWymianaUmowyZaplaty = containsActivity(process.activities,"wymianaUmowyZaplaty")
        if (!hasWymianaUmowyZaplaty && hasRozszerzenieDodPunkt && ! hasZmianaProwizji && "poziomOplatiWarunkiPlatnosciKarty" == panelName){
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
			if (!point || point.isLocal() == false)
				return

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
            def posData = point.posDatas?.find { PosData pd -> pd.isLocal() == true }

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
			// Don't load POSes that are automatically created for points - this causes panel duplication
			if (!point || point.isLocal() == true)
				return
			
            point.posDatas?.each { PosData posData ->

                /* Don't load POSes from CBD */
                if (posData == null || posData?.isLocal() == false) {
                    return
                }

                PointCommand pc = new PointCommand()
                pc.id = posData.id

                point.properties.each { key, value ->
                   // log.info "PointData Key: " + key
                    if (["class", "posDatas", "errors", "constraints", "processId", "pointDetailsId", "empty"].contains(key) || value == null){
                        return
                    }

                    if (PointCommand.metaClass.respondsTo(PointCommand, "set"+key.capitalize())) {
                        pc."set${key.capitalize()}"(value)
                    }
                }

                point.pointDetails?.properties.each { key, value ->
//                    log.info "PointDataDetails Key: " + key
                    if (["class", "posDatas", "errors", "constraints", "processId", "pointDetailsId", "empty"].contains(key) || value == null){
                        return
                    }

                    if (PointCommand.metaClass.respondsTo(PointCommand, "set"+key.capitalize())) {
                        pc."set${key.capitalize()}"(value)
                    }
                }

                posData?.properties.each { key, value ->
//                    log.info "PosData Key: " + key
                    if (["class", "process", "point", "errors", "constraints", "empty", "", ""].contains(key) || value == null){
                        return
                    }

                    if (PointCommand.metaClass.respondsTo(PointCommand, "set"+key.capitalize())) {
                        pc."set${key.capitalize()}"(value)
                    }
                }

                posData?.posDetails?.properties.each { key, value ->
//                    log.info "PosDataDetails Key: " + key
                    if (["class", "process", "point", "errors", "constraints", "empty", "", ""].contains(key) || value == null){
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
			if (point == null)
				return
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
			if (point == null)
				return
            point.posDatas?.each { PosData posData ->
				if (posData == null)
					return
                AllPosCommand apc = new AllPosCommand()
                DataBindingUtils.bindObjectToInstance(apc, posData.properties
                        , ["id","tpsId", "numerZestawuPos", "dataOd", "dataDo", "wysokoscOplaty", "czyWybrany"], [], null)
                apc.setCzyCbd(false)
                apc.id = posData.id
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

    //metoda do pobierana i przechowywania obecnych wartosci z cbd
    private def getHirePaymentCurrentCommandList(def cmd) {
        def hpcResult = []

        def result = cbdService.getTerminalPricesAndCounts(cmd.nip)
        result.each { GroovyRowResult row ->
            HirePaymentCommand hpc = new HirePaymentCommand()

            def count = row.get("ile");
            if (count != null && count?.toString().isNumber()){
                hpc.setTermCount(Integer.valueOf(count.toString()))
            }
            def payment = row.get("top");
            if (payment != null && payment?.toString().isNumber()){
                hpc.setCurrentTermPayment(payment.toString().toBigDecimal())
            }
            hpcResult.add(hpc)
        }
        return hpcResult
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

    private def getPosExchangeCommandList(def cmd) {
        def peResult = []

        def result = cbdService.getHirePaymentByPos(cmd.nip)
        result.each { GroovyRowResult row ->
            PosExchangeCommand pe = new PosExchangeCommand()
            pe.setTpsId(Integer.valueOf(row.get("pos_id").toString()))
            pe.setPosNumber(Integer.valueOf(row.get("tid").toString()))
            pe.setCbdId(Integer.valueOf(row.get("point_id").toString()))
            pe.setName(row.get("nazwa_punktu").toString())
            pe.setAddress(row.get("adres_posadowienia").toString())
            pe.setModel(row.get("rodzaj_terminala"))
            pe.setCurrentPrice(row.get("oplata_za_pos").toString().toBigDecimal())
            pe.setNewType("")
            pe.setNewModel("")
            pe.setSimType("")
            pe.setIsChoosen(false)
            peResult.add(pe)
        }
        return peResult
    }

    private def getPosExchangeCommandFromPosExchanges(def dataSet){
        def result = []
        dataSet.each{ PosExchange pe ->
            PosExchangeCommand pec = new PosExchangeCommand()

            pec.setId(pe.id)
            pec.setTpsId(pe.tpsId)
            pec.setPosNumber(pe.posNumber)
            pec.setCbdId(pe.cbdId)
            pec.setName(pe.name)
            pec.setAddress(pe.address)
            pec.setModel(pe.model)
            pec.setNewType(pe.newType)
            pec.setNewModel(pe.newModel)
            pec.setSimType(pe.simType)
            pec.setCurrentPrice(pe.currentPrice)
            pec.setIsChoosen(pe.isChoosen)

            result.add(pec)
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
		
        result
    }

    /** save data */
    def populateProcessWithData(Process process, def cmd, def calc){
        def processDataList = getDataFromPanels(cmd)

        //zapis obecnej daty na potrzeby dokumentow
        addCurrentDate(processDataList);
        addFromCalc(processDataList, calc);

        processDataList.each { ProcessData data ->
            def foundData = process.processData.find { it.name == data.name }
            if(!foundData){
                process.addToProcessData(data)
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

        fillPaymentUsage(cmd, process)

        fillPosExchange(cmd, process)

        fillRepresentatives(cmd, process)

        process.notesToCoa = cmd.notes //notesToCOA

        process
    }

    private def fillPaymentUsage(def cmd, def process){
        if ('one_for_all_terminals'.equals(cmd.odplatneUzywanie)){
            // dane dla tej opcji trzymamy w procesie
            log.info('Zapisujemy dla one_for_all_terminals')
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

    private def fillPosExchange(def cmd, def process){
        cmd.posExchanges.each { PosExchangeCommand pec ->
            log.info "Saving PosExchange with id: " + pec.id + " and tpsId: " + pec.tpsId

            PosExchange pe = PosExchange.findById(pec.id)
            if (!pe){
                log.info "Not found!! Creating new PosExchange!!"
                pe = new PosExchange()
                pe.setId(pec.id)
            }

            pe.setTpsId(pec.tpsId)
            pe.setPosNumber(pec.posNumber)
            pe.setCbdId(pec.cbdId)
            pe.setName(pec.name)
            pe.setAddress(pec.address)
            pe.setModel(pec.model)
            pe.setNewType(pec.newType)
            pe.setNewModel(pec.newModel)
            pe.setSimType(pec.simType)
            pe.setCurrentPrice(pec.currentPrice)
            pe.setIsChoosen(pec.isChoosen)

            //pe.setProcess(process)

            process.addToPosExchanges(pe)
            pe.save(flush: true)
        }
        process
    };

    private void fillRepresentatives(ProcessCommand processCommand, Process process) {
        processCommand.representatives.each { representativeCmd ->
            Representative representative = saveRepresentative(Representative.Type.REPRESENTATIVE, representativeCmd)

            if(representative) {
                log.info(String.format("Representative %s %s saved/updated (process: %s)", representative.imie, representative.nazwisko, process.id))
                process.addToRepresentatives(representative)
            }
        }

        processCommand.beneficiaries.each { beneficiaryCmd ->
            Representative beneficiary = saveRepresentative(Representative.Type.BENEFICIARY, beneficiaryCmd)

            if(beneficiary) {
                log.info(String.format("Beneficiary %s %s saved/updated (process: %s)", beneficiary.imie, beneficiary.nazwisko, process.id))
                process.addToRepresentatives(beneficiary)
            }
        }
    }

    private Representative saveRepresentative(Representative.Type type, RepresentativeCommand representativeCmd) {
        Representative representative = Representative.findById(representativeCmd.properties.id)

        if(!representative && !representativeCmd.imie && !representativeCmd.nazwisko) {
            log.info("Not saving representative - empty name and surname")
            return null
        }

        if(!representative) {
            representative = new Representative()
            representative.typ = type

            log.info(String.format("Saving new %s %s %s", representative.typ, representativeCmd.imie, representativeCmd.nazwisko))
        }

        representativeCmd.properties.remove("id")
        representative.properties = representativeCmd.properties

        return representative.save(flush: true)
    }

    private def addCurrentDate(def processDataList){
        processDataList.add(new ProcessData([name: 'dataUmowy', value: DateUtils.formatWithTimezone(DateUtils.getCurrentDate())]))
    }

    private def addFromCalc(def processDataList, def calc){
        processDataList.add(new ProcessData([name: 'liczbaMiesZwolNaj1', value: calculatorService.getCalcProperty(calc,"E_LICZBA_MIES_ZWOL_NAJ_1")]))
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

            PointData pointData = null
            PointDataDetails pointDataDetails = null
            PosData posData = null
            PosDataDetails posDataDetails = null

            ArrayList<PosData> pdList = new ArrayList<PosData>()

            if (pc.id == null) {
				log.info "NEW POINT!"
                pointData = new PointData()
                pointDataDetails = new PointDataDetails()

                posData = new PosData()
                posDataDetails = new PosDataDetails()
				
				pointData.czyLokalny = true
            }
            else {
				log.info "EXISTING POINT!"
                pointData = PointData.findById(pc.id)

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
				// Skip auto-mapping of isLocal value
				if (key == "czyLokalny")
					return
					
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
			updateChildPosData(posData, posDataDetails, pointData.posDatas)

			// Create POSes with same values
			def terminalCount = 0
			terminalCount += posDataDetails?.gprsIlosc != null ? posDataDetails?.gprsIlosc : 0
			terminalCount += posDataDetails?.dialupIlosc != null ? posDataDetails?.dialupIlosc : 0
			terminalCount += posDataDetails?.vpnIlosc != null ? posDataDetails?.vpnIlosc : 0
			terminalCount += posDataDetails?.sslIlosc != null ? posDataDetails?.sslIlosc : 0
			terminalCount += posDataDetails?.wifiIlosc != null ? posDataDetails?.wifiIlosc : 0

            // Create cloned poses only when they are not already cloned
			if (terminalCount > 1 && terminalCount > pointData.posDatas.findAll { it != null && it.tpsId == null }?.size()) {
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

            pointData.nip = pointDataDetails.nipPunktu
            pointData.nazwa = pointDataDetails.nazwaDoWydrukuZTerminalaPos
            pointData.ulica = pointDataDetails.wydrukUlica
            pointData.nrLokalu = pointDataDetails.wydrukNrLokalu
            pointData.nrBudynku = pointDataDetails.wydrukNrDomu
            pointData.miejscowosc = pointDataDetails.wydrukMiasto
            pointData.kodPocztowy = pointDataDetails.wydrukKodPocztowy
            pointData.poczta = pointDataDetails.wydrukPoczta
            if (pointData.posDatas?.size() < pdList?.size()) {
				pointData.liczbaPos = pdList.size()
			}
            pointData.save()

            posData.setPosDetails(posDataDetails)
            posData.setPoint(pointData)

            pointData.setPointDetails(pointDataDetails)
            pointData.setPosDatas(pdList)

            posDataDetails.setPos(posData)
            pointDataDetails.setPoint(pointData)
            pointDataDetails.save()
            posData.save()

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
            if (apc == null) {
                log.debug "AllPointCommand is NULL - skipping!"
                return
            }
			
			PointData point
			
			if (apc.id != null) {
				log.debug "ZNALAZLEM PUNKT - ALLPOINTS"
				point = PointData.findById(apc.id)
			}
			else {
				log.debug "NIE ZNALAZLEM PUNKTU - ALLPOINTS"
				point = new PointData()
				point.czyLokalny = false
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
                point.czyWybranyWymianaUmowy = apc.czyWybranyWymianaUmowy
                point.systemKasowy = apc.systemKasowy
                point.uta = apc.uta

                pointsList.add(point)
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

            PointData pointData = null
            PointDataDetails pointDataDetails = null
            PosData posData = null
            PosDataDetails posDataDetails = null

            ArrayList<PosData> pdList = new ArrayList<PosData>()

            if (pc.id == null) {
                log.info "NOWY POS"

                if (pc.cbdId != null) {
					if (pointsList.find { PointData pd -> pd.cbdId == pc.cbdId } != null) {
						log.info "Istnieje juz punkt dla pos o danym cbdId - tworzymy nowy"
						pointData = new PointData()
						pointDataDetails = new PointDataDetails()
						
						pointData.czyLokalny = false
					}
					else {
						pointData = PointData.findByCbdIdAndProcess(pc.cbdId, process)
						
						if (pointData == null) {
							log.info "NOWY PUNKT DLA POS"
							pointData = new PointData()
							pointDataDetails = new PointDataDetails()
							
							pointData.czyLokalny = false
						}
						else {
							pointDataDetails = pointData.pointDetails
							if (pointDataDetails == null) {
								pointDataDetails = new PointDataDetails()
							}
						}
					}
					
                    log.info "Ustawiam dane z CBD"
                    def cbdPoint = cbdService.getCbdPointById(cmd.nip, pc.cbdId)
                    if (cbdPoint != null) {

                        pointData.setCbdId(Integer.valueOf(cbdPoint.get("id").toString()))
                        pointData.setKodPocztowy(cbdPoint.get("kod_pocztowy"))
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
					
					pointData.czyLokalny = false
                }

                posData = new PosData()
                posDataDetails = new PosDataDetails()
                isNew = true
            }
            else {
                posData = PosData.findById(pc.id)

                if (posData != null) {
					def proc = process
					pointData = PointData.find {
						process == proc &&
						posDatas { id == posData.id }
					}
					if (pointData.liczbaPos == null) {
						pointData.liczbaPos = pointData.posDatas?.size()
					}
                    posDataDetails = posData.posDetails
					
					if (posDataDetails == null) {
						posDataDetails = new PosDataDetails()
					}

                    if (pointData != null) {
                        log.info "getPointCommandsToPosDataList - Znaleziono punkt o id: " + pointData.id + " dla POS o id: " + pc.id
                        pointDataDetails = pointData.pointDetails
                    }
                    else {
                        log.info "getPointCommandsToPosDataList - Brakujacy punkt dla POS o id: " + pc.id
                        pointData = new PointData()
                        pointDataDetails = new PointDataDetails()
						
						pointData.czyLokalny = false

                        if (pc.cbdId != null) {
                            def cbdPoint =
                                cbdService.getCbdPointById(cmd.nip, pc.cbdId)
                            if (cbdPoint != null) {
                                pointData.setCbdId(Integer.valueOf(cbdPoint.get("id").toString()))
                                pointData.setKodPocztowy(cbdPoint.get("kod_pocztowy"))
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
				
				// Skip auto-mapping for this property
				if (key == "czyLokalny")
					return
				
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
			updateChildPosData(posData, posDataDetails, pointData.posDatas)

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
            if (terminalCount > 1 && terminalCount > pointData.posDatas.findAll { it != null && it.tpsId == null }?.size()) {
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

			if (pointData.posDatas?.size() < pdList?.size()) {
				pointData.liczbaPos = pdList.size()
			}
            pointData.save()
            posData.setPosDetails(posDataDetails)
            posData.setPoint(pointData)

            pointData.setPointDetails(pointDataDetails)
            //pointData.setPosDatas(pdList)
			if (pointData.posDatas != null && pointData.posDatas.size() > 0){
				pdList.each { PosData pos ->
					if (pointData.posDatas.find { it?.id == pos.id } == null) {
						pointData.addToPosDatas(pos)
					}
				}
			} else {
				pointData.setPosDatas(pdList)
			}

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

            PosData pos = null
            PointData point = null

            if (apc.id != null) {
                //poieramy point i pos z naszej bazy
                pos = PosData.findById(apc.id)
                log.debug "Got POS Data!"
                if (pos != null) {
                    //point = pos.point
					def proc = process
					point = PointData.find {
						process == proc &&
						posDatas { id == pos.id }
					}
                }
            }
            else if (apc.cbdId != null) {
                //pobieramy point z bazy CBD
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
                    pos = new PosData()
					
					point.czyLokalny = false

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
				
				point.czyLokalny = false
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

                point.cbdId = apc.cbdId
                point.save()
				
				if (point.posDatas){
					if (point.posDatas.find { PosData pd -> pd.id == pos.id } == null)
						point.addToPosDatas(pos)
				} else {
					point.setPosDatas([pos])
				}

                pos.setPoint(point)
                pos.save()
            }

            pointsList.add(point)
        }

        return pointsList
    }
	
	def updateChildPosData(PosData parent, PosDataDetails parentDetails, List<PosData> children) {
		if (parent.tpsId != null) {
			return
		}
		children?.findAll { it != null && it.tpsId == null }?.each { PosData child ->
			if (child != null && child != parent) {
				def childDetails = child.posDetails
				parent?.properties.each { key, value ->
					if (["id", "version", "posDetails", "parentPosId", "errors"].contains(key)) {
						return
					}
					if (PosData.metaClass.respondsTo(PosData, "set" + key.capitalize())) {
							child."set${key.capitalize()}"(value)
					}
	
					if (PosDataDetails.metaClass.respondsTo(PosDataDetails, "set" + key.capitalize()) && key != 'id') {
						child.posDetails?."set${key.capitalize()}"(value)
					}
				}
				
				parentDetails?.properties.each { key, value ->
					if (["id", "pos", "posId", "version", "parentPosId", "errors"].contains(key)) {
						return
					}
					if (PosData.metaClass.respondsTo(PosData, "set" + key.capitalize())) {
							childDetails?."set${key.capitalize()}"(value)
					}
	
					if (PosDataDetails.metaClass.respondsTo(PosDataDetails, "set" + key.capitalize()) && key != 'id') {
						childDetails?."set${key.capitalize()}"(value)
					}
				}
			}
		}
	}

    Map createMailParametersForPaperVersion(Process process) {
        String merchantName = getFromProcessData(process, 'akceptantNazwaOficjalna')
        String merchantNip = getFromProcessData(process, 'nip')
        List<String> activities = getActivities(process)

        return [merchantName: merchantName, merchantNip: merchantNip, phNumber: process?.phNumber,
                phFirstName: process?.phFirstName, phSurname: process?.phSurname, activities: activities]
    }

    Map createMailParametersForElectronicalVersion(Process process) {
        String recipient = getFromProcessData(process, 'kontaktEmail') ?: getFromProcessData(process, 'emailDoWysylkiDokumentu')
        String merchantName = getFromProcessData(process, 'akceptantNazwaOficjalna')
        String merchantNip = getFromProcessData(process, 'nip')
        List<String> activities = getActivities(process)

        return [merchantName: merchantName, merchantNip: merchantNip, activities: activities, recipient: recipient]
    }

    List<String> getActivities(Process process){
        return process.activities.collect {messageSource.getMessage('activity.' + it.code + '.name', [] as Object[], Locale.getDefault())}
    }

    Boolean hasWymianaTermianalaOnly(Process process) {
        return containsActivity(process.activities, "wymianaTerminala") && process.activities.size() == 1
    }

    String getFromProcessData(Process process, String key){
        def result = process.processData.find{ processData -> processData.name.equals(key)}
        return (result && result?.value) ? result?.value : ""
    }

    Integer requiredNumberOfSubscriptions(Process process) {
        Integer requiredSubscriptionsCount = 1 //PH

        if(!hasWymianaTermianalaOnly(process)) {
            if(getRepresentative1(process)) {
                requiredSubscriptionsCount++
            }

            if(getRepresentative2(process)) {
                requiredSubscriptionsCount++
            }
        }

        return requiredSubscriptionsCount
    }

    Integer savedSubscriptionsCount(Process process) {
        List<Subscription> subscriptions = Subscription.findAllByProcess(process)
        return subscriptions.size()
    }

    def calculatorForProcess(Process process) {
        return cbdService.findCalculatorByNip(process.client.nip)
    }

    public void fillCommandWithBisnodeData(ProcessCommand command, MerchantDetailsDTO merchantDetailsDTO) {
        command.isFromBisnode = true

        command.akceptantNazwaOficjalna = merchantDetailsDTO.akceptantNazwaOficjalna
        command.akceptantRegon = merchantDetailsDTO.akceptantRegon
        command.akceptantUlicaTytul = merchantDetailsDTO.akceptantUlicaTytul
        command.akceptantUlica = merchantDetailsDTO.akceptantUlica
        command.akceptantNrDomu = merchantDetailsDTO.akceptantNrDomu
        command.akceptantNrMieszkania = merchantDetailsDTO.akceptantNrMieszkania
        command.akceptantKodPocztowy = merchantDetailsDTO.akceptantKodPocztowy
        command.akceptantMiasto = merchantDetailsDTO.akceptantMiasto
        command.akceptantTelStacjonarny = merchantDetailsDTO.akceptantTelStacjonarny
        command.akceptantTelKomorkowy = merchantDetailsDTO.akceptantTelKomorkowy
        command.akceptantFax = merchantDetailsDTO.akceptantFax
    }
}