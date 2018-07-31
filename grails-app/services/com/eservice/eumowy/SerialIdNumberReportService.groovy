package com.eservice.eumowy

import com.eservice.eumowy.serialidreport.RepresentativeInfo
import com.eservice.eumowy.serialidreport.SerialIdReportCreator
import com.eservice.eumowy.serialidreport.SerialIdRow
import grails.transaction.Transactional
import org.apache.poi.ss.usermodel.Workbook
import org.joda.time.LocalDateTime

class SerialIdNumberReportService {

    def messageSource

    public Workbook getReport() {
        Date start = LocalDateTime.now().withTime(0, 0, 0, 0).toDate()
        Date end = LocalDateTime.now().withTime(23, 59, 59, 999).toDate()
        List<Process> processes = Process.createCriteria().list {
            createAlias("client", "c")
            between("acceptanceDate", start, end)
            like("phNumber", "98%")
            eq("status", Process.ProcessStatus.ACCEPTED)
            activities {
                idEq(Activity.findByCode("nowaUmowa").id)
            }
            order('dateCreated', 'asc')
        }
        List<SerialIdRow> rows = getRows(processes)

        return new SerialIdReportCreator(messageSource, rows).getWorkbook()
    }

    private List<SerialIdRow> getRows(List<Process> processes) {
        List<SerialIdRow> rows = []

        processes.each {
            SerialIdRow row = new SerialIdRow(it.client.nip, it.getData("akceptantNazwaOficjalna"), it.getPhNumber(), getRepresentatives(it))
            if (row.getRepresentatives().size() > 0) {
                rows.add(row)
            }
        }

        return rows
    }

    private List<RepresentativeInfo> getRepresentatives(Process process) {
        List<RepresentativeInfo> representatives = []

        process.representatives.each {
            representatives.add(new RepresentativeInfo(it.fullName, it.getDocumentNumber(), it.documentIssueDate, it.documentExpirationDate))
        }

        return representatives
    }
}
