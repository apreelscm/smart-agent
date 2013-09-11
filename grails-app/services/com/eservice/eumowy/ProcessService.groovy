package com.eservice.eumowy

import grails.util.Environment
import groovy.sql.GroovyRowResult

import org.apache.commons.collections.FactoryUtils
import org.apache.commons.collections.ListUtils
import org.apache.commons.lang.SerializationUtils
import org.apache.commons.lang.WordUtils

import com.eservice.eumowy.command.AllPointsCommand
import com.eservice.eumowy.command.AllPosCommand
import com.eservice.eumowy.command.PointCommand
import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.util.DateUtils

class ProcessService {

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

    def getLastProcessWithStatus(Client client, def statusList) {
        def result = Process.findByClient(Client.findByNip(client.nip),[sort: "lastUpdated", order: "desc"])
        log.info("getLastProcessWithStatus - client.nip = ${client.nip} , id = ${result?.id} status = ${result?.status}, statusList = ${statusList}")
        return (result && client.id && (result.status in statusList)) ? result : null
    }

    def getLastProcessNotStatus(Client client, def statusList) {
        def result = Process.findByClient(Client.findByNip(client.nip),[sort: "lastUpdated", order: "desc"])
        log.info("getLastProcessNotStatus - client.nip = ${client.nip} , id = ${result?.id} status = ${result?.status}, statusList = ${statusList}")
        return (result && client.id && !(result.status in statusList)) ? result : null
    }

    def containsActivity(def activities, def activityCode) {
        return activities?.any{it.code.equals(activityCode)};
    }

    def getNewProcessCommand(def process, def calc){
        log.info("getNewProcessCommand processId = ${process.id}")
        def cmd = initProcessCommand(process)
        loadAllPoints(process, cmd)
        loadAllPoses(process, cmd)
        prepareProcessCommand(cmd, calc)
    }

    def getSavedProcessCommand(def process, def calc){
        log.info("getSavedProcessCommand processId = ${process.id}")
        def cmd = initProcessCommand(process)
        loadProcessData(process,cmd)
		loadPoints(process, cmd)
		loadPoses(process, cmd)
		loadAllPoints(process, cmd)
		loadAllPoses(process, cmd)
       // loadPoses()
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
        process.processData?.each {ProcessData data ->

            if(data.name in ["dataUmowy","punktyTytulPlatnosci","punktySystemKasowy","punktyUta","punktyWybrane"]){
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
	
	def loadPoints(def process, def cmd) {
		log.info "loadPoints"
		process.points.each { PointData point ->
			
			if (point.cbdId != null) {
				return
			}
			
			PointCommand pc = new PointCommand()
			
			point.properties.each { key, value ->
				log.info "PointData Key: " + key
				if (["class", "posDatas", "errors", "constraints", "processId", "cbdId", "pointDetailsId", "empty"].contains(key) || value == null){
					return
				}
				
				if (PointCommand.metaClass.respondsTo(PointCommand, "set"+key.capitalize())) {
					pc."set${key.capitalize()}"(value)
				}
			}
			
			point.pointDetails?.properties.each { key, value ->
				log.info "PointDataDetails Key: " + key
				if (["class", "posDatas", "errors", "constraints", "processId", "cbdId", "pointDetailsId", "empty"].contains(key) || value == null){
					return
				}
				
				if (PointCommand.metaClass.respondsTo(PointCommand, "set"+key.capitalize())) {
					pc."set${key.capitalize()}"(value)
				}
			}
			
			def posData = point.posDatas != null && point.posDatas.size() > 0 ? point.posDatas[0] : null 
			
			posData?.properties.each { key, value ->
				log.info "PosData Key: " + key
				if (["class", "cbdId", "process", "point", "errors", "constraints", "empty", "", ""].contains(key) || value == null){
					return
				}
				
				if (PointCommand.metaClass.respondsTo(PointCommand, "set"+key.capitalize())) {
					pc."set${key.capitalize()}"(value)
				}
			}
			
			posData?.posDetails?.properties.each { key, value ->
				log.info "PosDataDetails Key: " + key
				if (["class", "cbdId", "process", "point", "errors", "constraints", "empty", "", ""].contains(key) || value == null){
					return
				}
				
				if (PointCommand.metaClass.respondsTo(PointCommand, "set"+key.capitalize())) {
					pc."set${key.capitalize()}"(value)
				}
			}
			
			cmd.points.add(pc)
		}
	}
	
	def loadPoses(def process, def cmd) {
		log.info "loadPoses"
		process.points.each { PointData point ->
			point.posDatas?.each { PosData posData ->
				
				if (posData.tpsId != null) {
					return
				}
				
				PointCommand pc = new PointCommand()
				
				point.properties.each { key, value ->
					log.info "PointData Key: " + key
					if (["class", "posDatas", "errors", "constraints", "processId", "cbdId", "pointDetailsId", "empty"].contains(key) || value == null){
						return
					}
					
					if (PointCommand.metaClass.respondsTo(PointCommand, "set"+key.capitalize())) {
						pc."set${key.capitalize()}"(value)
					}
				}
				
				point.pointDetails?.properties.each { key, value ->
					log.info "PointDataDetails Key: " + key
					if (["class", "posDatas", "errors", "constraints", "processId", "cbdId", "pointDetailsId", "empty"].contains(key) || value == null){
						return
					}
					
					if (PointCommand.metaClass.respondsTo(PointCommand, "set"+key.capitalize())) {
						pc."set${key.capitalize()}"(value)
					}
				}
				
				posData?.properties.each { key, value ->
					log.info "PosData Key: " + key
					if (["class", "cbdId", "process", "point", "errors", "constraints", "empty", "", ""].contains(key) || value == null){
						return
					}
					
					if (PointCommand.metaClass.respondsTo(PointCommand, "set"+key.capitalize())) {
						pc."set${key.capitalize()}"(value)
					}
				}
				
				posData?.posDetails?.properties.each { key, value ->
					log.info "PosDataDetails Key: " + key
					if (["class", "cbdId", "process", "point", "errors", "constraints", "empty", "", ""].contains(key) || value == null){
						return
					}
					
					if (PointCommand.metaClass.respondsTo(PointCommand, "set"+key.capitalize())) {
						pc."set${key.capitalize()}"(value)
					}
				}
				
				cmd.poses.add(pc)
			}
		}
	}
	
	def loadAllPoints(def process, def cmd) {
		log.info "loadAllPoints"
		process.points.each { PointData point ->
			AllPointsCommand apc = new AllPointsCommand()
			
			point.properties.each { key, value ->
				log.info "PointData Key: " + key
				if (["class", "posDatas", "errors", "constraints", "processId", "pointDetailsId", "empty"].contains(key) || value == null){
					return
				}
				
				if (AllPointsCommand.metaClass.respondsTo(AllPointsCommand, "set"+key.capitalize())) {
					apc."set${key.capitalize()}"(value)
				}
			}
			apc.id = point.id
			cmd.allPoints.add(apc)
		}
		
		def cbdPoints = cbdService.getZakresUruchomieniaPunktyGrid(cmd.nip)
		cbdPoints.each { GroovyRowResult row ->
			AllPointsCommand apc = new AllPointsCommand()
			
			apc.setCzyCbd(true)
			apc.setCbdId(Integer.valueOf(row.get("id").toString()))
			apc.setKodPocztowy(row.get("kod_pocztowy"))
			apc.setLiczbaPos(Integer.valueOf(row.get("liczba_pos").toString()))
			apc.setMiejscowosc(row.get("miejscowosc"))
			apc.setNazwa(row.get("nazwa"))
			apc.setNrBudynku(row.get("nr_budynku"))
			apc.setUlica(row.get("ulica"))
			
			/* Don't add a point from CBD if we have a local copy with saved data */
			if (cmd.allPoints.find { obj -> obj.cbdId == apc.cbdId } == null) {
				log.info "Nowy punkt z CBD: " + apc.cbdId
				cmd.allPoints.add(apc)
			}
			else {
				log.info "Znaleziony punkt z CBD: " + apc.cbdId
				AllPointsCommand foundApc = cmd.allPoints.find { obj -> obj.cbdId == apc.cbdId }
				foundApc.setCzyCbd(apc.czyCbd)
				foundApc.setCbdId(apc.cbdId)
				foundApc.setKodPocztowy(apc.kodPocztowy)
				foundApc.setLiczbaPos(apc.liczbaPos)
				foundApc.setMiejscowosc(apc.miejscowosc)
				foundApc.setNazwa(apc.nazwa)
				foundApc.setNrBudynku(apc.nrBudynku)
				foundApc.setUlica(apc.ulica)
			}
		}
	}

	def loadAllPoses(def process, def cmd) {
		log.info "loadAllPoses"
		process.points.each { PointData point ->
			point.posDatas?.each { PosData posData ->
				AllPosCommand apc = new AllPosCommand()
				apc.setCzyCbd(false)
				
				posData.properties.each { key, value ->
					log.info "PosData Key: " + key
					if (["class", "process", "point", "errors", "constraints", "empty", "", ""].contains(key) || value == null){
						return
					}
					
					if (AllPosCommand.metaClass.respondsTo(AllPosCommand, "set"+key.capitalize())) {
						apc."set${key.capitalize()}"(value)
					}
				}
				
				cmd.allPoses.add(apc)
			}
		}
		
		def cbdPoses = cbdService.getPromocyjneObinzenieOplatGrid(cmd.nip)
		log.info cbdPoses
		cbdPoses.each { GroovyRowResult row ->
			log.info row
			AllPosCommand apc = new AllPosCommand()
			
			apc.setCzyCbd(true)
			apc.setTpsId(Integer.valueOf(row.get("tps_id").toString()))
			apc.setNumerZestawuPos(Integer.valueOf(row.get("numer_logiczny").toString()))
			
			/* Don't add a POS from CBD if we have a local copy with saved data */
			if (cmd.allPoses.find { obj -> obj.tpsId == apc.tpsId } == null) {
				cmd.allPoses.add(apc)
			}
			else {
				AllPosCommand foundApc = cmd.allPoses.find { obj -> obj.tpsId == apc.tpsId }
				
				foundApc.setCzyCbd(apc.czyCbd)
				foundApc.setTpsId(apc.tpsId)
				foundApc.setNumerZestawuPos(apc.numerZestawuPos)
			}
		}
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
            }else if(data.value != foundData.value){
                foundData.value = data.value
            }
        }
		
        def pointsDataList = getPointData(cmd, process)
		//process.points?.clear()
        pointsDataList.each { data ->
			def foundData = process.points.find { p -> p.id != null }
			if (foundData != null) {
				log.info "Removing old point data: " + foundData.id
				process.points.remove(foundData)
			}
            process.addToPoints(data)
            process.discard()
        }
		
		def posDataList = getPosData(cmd)
		posDataList.each { data ->
			process.addToPoints(data)
			process.discard()
		}
		
        process
    }

    def addCurrentDate(def processDataList){
        processDataList.add(new ProcessData([name: 'dataUmowy', value: DateUtils.formatWithTimezone(DateUtils.getCurrentDate())]))
    }

    List<PointCommand> points = ListUtils.lazyList([], FactoryUtils.instantiateFactory(PointCommand))
    List<AllPointsCommand> allPoints = ListUtils.lazyList([], FactoryUtils.instantiateFactory(AllPointsCommand))
    List<AllPosCommand> allPoses = ListUtils.lazyList([], FactoryUtils.instantiateFactory(AllPosCommand))
	def getDataFromPanels(def cmd) {
        def processDataList = [];
        cmd.properties.each { key, value ->
            if (["class","process", "cbdService", "errors", "constraints", "notes"].contains(key) || value == null){
                return
            }

            if(["allPoses", "allPoints", "points"].contains(key)){
                //TODO implementacja logiki dla punktow
                return;
            }

            if(["umowaOznOd", "umowaOznDo", "dataAneksowanejUmowyPos", "dataAneksowanejUmowyPrepaid", "dataUmowy"].contains(key)){
                  processDataList.add(new ProcessData(name: "${key}", value:"${value? DateUtils.formatWithTimezone(DateUtils.parseDate(value)): ''}"));
                return;
            }

            processDataList.add(new ProcessData(name: "${key}", value:"${value ?: ''}"));
        }
        processDataList
    }

	def getPointData(def cmd, def process) {
		def pointsList = []
		cmd.points.each { PointCommand pc ->
			
			if (pc == null) {
				log.info "PointCommand is NULL - skipping!"
				return
			}
			
			ArrayList<PosData> pdList = new ArrayList<PosData>()
			PointData pointData = new PointData()
			PointDataDetails pointDataDetails = new PointDataDetails()
			PosData posData = new PosData()
			PosDataDetails posDataDetails = new PosDataDetails()
			
			pc.properties.each { key, value ->
				if (PointData.metaClass.respondsTo(PointData, "set" + key.capitalize())) {
					pointData."set${key.capitalize()}"(value)
				}
				
				if (PointDataDetails.metaClass.respondsTo(PointDataDetails, "set" + key.capitalize())) {
					pointDataDetails."set${key.capitalize()}"(value)
				}
				
				if (PosData.metaClass.respondsTo(PosData, "set" + key.capitalize())) {
					posData."set${key.capitalize()}"(value)
				}
				
				if (PosDataDetails.metaClass.respondsTo(PosDataDetails, "set" + key.capitalize())) {
					posDataDetails."set${key.capitalize()}"(value)
				}
			}
			
			pdList.add(posData)
			
			// Create POSes with same values
			if (pc.terminalIlosc != null && pc.terminalIlosc > 1) {
				for (int i = 0; i < pc.terminalIlosc; i++) {
					PosData posDataNew
					PosDataDetails posDataDetailsNew
					
					posDataNew = SerializationUtils.clone(posData)
					posDataDetailsNew = SerializationUtils.clone(posDataDetails)
					
					posDataNew.setPosDetails(posDataDetailsNew)
					posDataNew.setPoint(pointData)
					posDataDetailsNew.setPos(posDataNew)
					
					pdList.add(posDataNew)
				}
			}
			
			pointData.cbdId = null
			pointData.nip = pointDataDetails.nipPunktu
			pointData.nazwa = pointDataDetails.nazwaDoWyszukiwarki
			pointData.ulica = pointDataDetails.korespondencjaUlica
			pointData.nrLokalu = pointDataDetails.korespondencjaNrLokalu
			pointData.nrBudynku = pointDataDetails.korespondencjaNrDomu
			pointData.miejscowosc = pointDataDetails.korespondencjaMiasto
			pointData.kodPocztowy = pointDataDetails.korespondencjaKodPocztowy
			pointData.poczta = pointDataDetails.korespondencjaPoczta
			pointData.liczbaPos = pdList.size()
			
			pointsList.add(pointData)
			
			posData.setPosDetails(posDataDetails)
			posData.setPoint(pointData)
			
			pointData.setPointDetails(pointDataDetails)
			pointData.setPosDatas(pdList)
			
			posDataDetails.setPos(posData)
			pointDataDetails.setPoint(pointData)
		}
		
		/* Save points from AllPointsCommand */
		cmd.allPoints?.each { AllPointsCommand apc ->
			
			if (apc == null) {
				log.info "AllPointCommand is NULL - skipping!"
				return
			}
			
			/* 
			 * Check if the point already exists in our DB, if yes, just update it.
			 * If not, create one, and mark him as "from CBD"
			 */
			if (apc.id != null) {
				//PointData.withNewSession {
					log.info "Szukam punktu: " + apc.id
					//PointData pointData = process.points?.find { p -> p.id == apc.id && p.cbdId == null}
					PointData pointData = PointData.find {id == apc.id}
					if (pointData != null) {
						log.info "Znaleziono punkt: " + pointData.id
					
						/*apc.properties.each { key, value ->
							if (PointData.metaClass.respondsTo(PointData, "set" + key.capitalize())) {
								pointData?."set${key.capitalize()}"(value)
							}
						}*/
			
					    pointData.czyWybranyZakresUruchomienia = apc.czyWybranyZakresUruchomienia
						pointData.tytulPlatnosci = apc.tytulPlatnosci
						pointData.systemKasowy = apc.systemKasowy
						pointData.uta = apc.uta
						pointData.czyWybranyAkceptacjaKart = apc.czyWybranyAkceptacjaKart
						
						pointData.save()
					}
					else {
						log.info "Nie znaleziono punktu: " + apc.id
					}
				//}
			}
			else {
				PointData pointData = new PointData()
				apc.properties.each { key, value ->
					if (PointData.metaClass.respondsTo(PointData, "set" + key.capitalize())) {
						pointData?."set${key.capitalize()}"(value)
					}
				}
				pointData.cbdId = apc.cbdId
				pointsList.add(pointData)
			}
		}
			
		return pointsList
	}
	
	def getPosData(def cmd) {
		return []
		//TODO
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