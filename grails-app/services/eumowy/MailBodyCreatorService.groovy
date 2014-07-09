package eumowy

import com.eservice.eumowy.auth.EServiceUserDetails
import com.eservice.eumowy.Process
import grails.transaction.Transactional

@Transactional
class MailBodyCreatorService {
    def processService

    Map notesToCoa(Process process, EServiceUserDetails user) {
        return [notes: process.notesToCoa, phNumber: user.nr, phName: user.fullName, merchantNip: process.getData("nip"),
                merchantName: process.getData("akceptantNazwaOficjalna"), activities: processService.getActivities(process)]
    }
}
