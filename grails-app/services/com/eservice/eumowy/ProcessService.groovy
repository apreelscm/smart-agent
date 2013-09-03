package com.eservice.eumowy

import com.eservice.eumowy.command.AllPointsCommand
import com.eservice.eumowy.command.AllPosCommand
import com.eservice.eumowy.command.PointCommand
import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.util.DateUtils
import grails.util.Environment
import org.apache.commons.collections.FactoryUtils
import org.apache.commons.collections.ListUtils
import org.apache.commons.lang.SerializationUtils
import org.apache.commons.lang.WordUtils

class ProcessService {

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

        /*return result?.id;*/

        return result?.status in [
                Process.ProcessStatus.NEW,
                Process.ProcessStatus.WAITING,
                Process.ProcessStatus.WAIT_FOR_SUBSRIPTION,
                Process.ProcessStatus.EDIT] ? result.id : null
    }

    def containsActivity(def activities, def activityCode) {
        return activities?.any{it.code.equals(activityCode)};
    }

    def getNewProcessCommand(def process, def calc){
        log.info("getNewProcessCommand processId = ${process.id}")
        def cmd = initProcessCommand(process)
        prepareProcessCommand(cmd, calc)
    }

    def getSavedProcessCommand(def process, def calc){
        log.info("getSavedProcessCommand processId = ${process.id}")
        def cmd = initProcessCommand(process)
        loadProcessData(process,cmd)
		loadPoints(process, cmd)
       // loadPoses()
        prepareProcessCommand(cmd, calc, cbdMethods)
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

            if(data.name in ["dataUmowy"]){
                return
            }

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
	
	def loadPoints(def process, def cmd) {
		log.info "loadPoints"
		process.points.each { PointData point ->
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
	
    /**
     *  save data
     * */

    def populateProcessWithData(def process, def cmd){
        def processDataList = getDataFromPanels(cmd)

        //zapis obecnej daty na potrzeby dokumentow
        addCurrentDate(processDataList);

        process.processData?.clear()
        processDataList.each { data ->
            process.addToProcessData(data)
            process.discard();
        }

        def pointsDataList = getPointAndPosData(cmd)
        process.points?.clear()
        pointsDataList.each { data ->
            process.addToPoints(data)
            process.discard()
        }
        process
    }

    def addCurrentDate(def processDataList){
        processDataList.add(new ProcessData([name: 'dataUmowy', value: DateUtils.getCurrentFormattedDate()]))
    }

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

            println(key+"()")

            processDataList.add(new ProcessData(name: "${key}", value:"${value ?: ''}"));
        }




        processDataList
    }

	def getPointAndPosData(def cmd) {
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
			
			pointsList.add(pointData)
			
			posData.setPosDetails(posDataDetails)
			posData.setPoint(pointData)
			
			pointData.setPointDetails(pointDataDetails)
			pointData.setPosDatas(pdList)
			
			posDataDetails.setPos(posData)
			pointDataDetails.setPoint(pointData)
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