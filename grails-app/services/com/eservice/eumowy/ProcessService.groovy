package com.eservice.eumowy

import java.text.SimpleDateFormat

class ProcessService {

    static final def DATE_FORMAT = "dd-MM-yyyy";

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

}
