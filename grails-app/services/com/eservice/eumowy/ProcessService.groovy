package com.eservice.eumowy

class ProcessService {

    def searchProcessByFilters(def params) {
        def filterObserved = params.filterObserved
        def filterStatus = params.filterStatus
        def filterNip = params.filterNip

        def filterPhNo = params.filterPhNo
        def filterDateFromDF = params.filterDateFromDF
        def filterDateToDF = params.filterDateToDF


        log.info(filterObserved + " --- " + filterStatus);

        def clientCriteria = Process.createCriteria()
        def searchResults = clientCriteria.list(max: params.max,
                offset: params.offset,
                sort: params.sort,
                order: params.order){
            /*eq("status", "${Process.ProcessStatus.valueOf(filterStatus)}")*/
            eq("status", Process.ProcessStatus.valueOf(filterStatus))

            if(filterNip && filterNip != "") {
                client {
                    eq("nip", filterNip)
                }
            }

            if(filterPhNo && filterPhNo != "") {
                eq("phNumber", filterPhNo)
            }

            if(filterDateFromDF != null && filterDateToDF != null) {
                between("dateCreated",filterDateFromDF,filterDateToDF)
            }

        }
        [searchResults: searchResults, searchResultSize: searchResults.getTotalCount()]
    }
}
