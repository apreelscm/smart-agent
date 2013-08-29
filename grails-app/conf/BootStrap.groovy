import com.eservice.eumowy.*
import grails.util.Environment
import groovy.sql.Sql

import java.sql.SQLException

class BootStrap {

    def dataSource

    def init = { servletContext ->

        switch (Environment.getCurrent()) {
            case Environment.DEVELOPMENT:
                // Wykonywanie inicjacyjnych zapytan sql z pliku insertData.sql
                executeSqlScript("/sql/createData.sql")
                executeSqlScript("/sql/insertData.sql")
                //createCBDDataForDevProfile();
                createTestDomains()
                break;
            case Environment.TEST:
                //createTestDomains()
                break;
        }

    }


    def createTestDomains(){
        // klienci
        Client.findByName('clientName1') ?: new Client(name: 'clientName1', nip: "555344435").save(flush:true);
        Client.findByName('clientName2') ?: new Client(name: 'clientName2', nip: "11241412").save(flush:true);

        //przykladowe procesy
        Process.get(1) ?: new Process(id: 1, phFirstName: "Jerzy",
                saleSection: 'segment1', phNumber: 12345, observed: true,
                phSurname: 'Kowalski', calcNumber: '44444' , status: Process.ProcessStatus.ACCEPTED,
                client: Client.findByName("clientName1"))
                .addToDocuments(new DocumentFile(name: "pedef.pdf", pagesCount: 1, content: new DocumentContent(content:[95, 96] as byte[]))).save(flush: true);

        Process.get(2) ?: new Process(id: 2, phFirstName: "Wanda",
                saleSection: 'segment2', phNumber: 321, observed: true,
                phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.ACCEPTED,
                client:  Client.findByName("clientName2"))
                .addToDocuments(new DocumentFile(name: "pedef.pdf"))
                .addToDocuments(new DocumentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef3.pdf")).save(flush:true);

        Process.get(3) ?: new Process(id: 3, phFirstName: "Henryk",
                saleSection: 'segment1', phNumber: 12345, observed: true,
                phSurname: 'Nowak', calcNumber: '44444' , status: Process.ProcessStatus.NEW,
                client: Client.findByName("clientName1"))
                .addToDocuments(new DocumentFile(name: "pedef.pdf"))
                .addToDocuments(new DocumentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef3.pdf")).save(flush:true);

        Process.get(4) ?: new Process(id: 4, phFirstName: "Zuzanna",
                saleSection: 'segment2', phNumber: 321, observed: true,
                phSurname: 'łapicka',calcNumber: '33333', status: Process.ProcessStatus.NEW,
                client:  Client.findByName("clientName2"))
                .addToDocuments(new DocumentFile(name: "pedef.pdf"))
                .addToDocuments(new DocumentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef3.pdf")).save(flush:true);

        Process.get(5) ?: new Process(id: 5, phFirstName: "Adam",
                saleSection: 'segment1', phNumber: 12345, observed: true,
                phSurname: 'Wiśniewski', calcNumber: '44444' , status: Process.ProcessStatus.WAIT_FOR_SUBSCRIPTION_PAPER_VERSION,
                client: Client.findByName("clientName1"))
                .addToDocuments(new DocumentFile(name: "pedef.pdf"))
                .addToDocuments(new DocumentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef3.pdf")).save(flush:true);

        Process.get(6) ?: new Process(id: 6, phFirstName: "Anna Maria",
                saleSection: 'segment2', phNumber: 321,
                phSurname: 'Olsen',calcNumber: '33333', status: Process.ProcessStatus.WAIT_FOR_SUBSRIPTION,
                client:  Client.findByName("clientName2"))
                .addToDocuments(new DocumentFile(name: "pedef.pdf"))
                .addToDocuments(new DocumentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef3.pdf")).save(flush:true);

        Process.get(7) ?: new Process(id: 7, phFirstName: "Jurek",
                saleSection: 'segment1', phNumber: 12345,
                phSurname: 'Dudek', calcNumber: '44444' , status: Process.ProcessStatus.WAITING,
                client: Client.findByName("clientName1"))
                .addToDocuments(new DocumentFile(name: "pedef.pdf"))
                .addToDocuments(new DocumentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef3.pdf")).save(flush:true);

        Process.get(8) ?: new Process(id: 8, phFirstName: "Wanda",
                saleSection: 'segment2', phNumber: 321,
                phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.WAITING,
                client:  Client.findByName("clientName2"))
                .addToDocuments(new DocumentFile(name: "pedef.pdf"))
                .addToDocuments(new DocumentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef3.pdf")).save(flush:true);

        Process.get(9) ?: new Process(id: 9, phFirstName: "Adam",
                saleSection: 'segment1', phNumber: 12345, observed: true,
                phSurname: 'Małysz', calcNumber: '44444' , status: Process.ProcessStatus.REJECTED,
                client: Client.findByName("clientName1"))
                .addToDocuments(new DocumentFile(name: "pedef.pdf"))
                .addToDocuments(new DocumentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef3.pdf")).save(flush:true);

        Process.get(10) ?: new Process(id: 10, phFirstName: "Lidia",
                saleSection: 'segment2', phNumber: 321, observed: true,
                phSurname: 'Chojecka',calcNumber: '33333', status: Process.ProcessStatus.REJECTED,
                client:  Client.findByName("clientName2"))
                .addToDocuments(new DocumentFile(name: "pedef.pdf"))
                .addToDocuments(new DocumentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef3.pdf")).save(flush:true);

        Process.get(11) ?: new Process(id: 11, phFirstName: "Wojciech",
                saleSection: 'segment1', phNumber: 12345,
                phSurname: 'Szczęsny', calcNumber: '44444' , status: Process.ProcessStatus.EDIT,
                client: Client.findByName("clientName1"))
                .addToDocuments(new DocumentFile(name: "pedef.pdf"))
                .addToDocuments(new DocumentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef3.pdf")).save(flush:true);

        Process.get(12) ?: new Process(id: 12, phFirstName: "Łukasz",
                saleSection: 'segment2', phNumber: 321,
                phSurname: 'Fabiański',calcNumber: '33333', status: Process.ProcessStatus.SUBSCRIPTIONS_DONE,
                client:  Client.findByName("clientName2"))
                .addToDocuments(new DocumentFile(name: "pedef.pdf"))
                .addToDocuments(new DocumentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef3.pdf")).save(flush:true);

        Process.get(13) ?: new Process(id: 13, phFirstName: "Wojciech",
                saleSection: 'segment1', phNumber: 12345,
                phSurname: 'Skaba', calcNumber: '44444' , status: Process.ProcessStatus.SUBSCRIPTIONS_DONE,
                client: Client.findByName("clientName1"))
                .addToDocuments(new DocumentFile(name: "pedef.pdf"))
                .addToDocuments(new DocumentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef3.pdf")).save(flush:true);

        Process.get(14) ?: new Process(id: 14, phFirstName: "Mieszko",
                saleSection: 'segment2', phNumber: 321,
                phSurname: 'Pierwszy',calcNumber: '33333', status: Process.ProcessStatus.REJECTED,
                client:  Client.findByName("clientName2"))
                .addToDocuments(new DocumentFile(name: "pedef.pdf"))
                .addToDocuments(new DocumentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef3.pdf")).save(flush:true);

        Process.get(15) ?: new Process(id: 15, phFirstName: "Adam",
                saleSection: 'segment1', phNumber: 12345,
                phSurname: 'Małysz', calcNumber: '44444' , status: Process.ProcessStatus.SUBSCRIPTIONS_DONE,
                client: Client.findByName("clientName1"))
                .addToDocuments(new DocumentFile(name: "pedef.pdf"))
                .addToDocuments(new DocumentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef3.pdf")).save(flush:true);

        Process.get(16) ?: new Process(id: 16, phFirstName: "Lidia",
                saleSection: 'segment2', phNumber: 321,
                phSurname: 'Chojecka',calcNumber: '33333', status: Process.ProcessStatus.REJECTED,
                client:  Client.findByName("clientName2"))
                .addToDocuments(new DocumentFile(name: "pedef.pdf"))
                .addToDocuments(new DocumentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef3.pdf")).save(flush:true);

        Process.get(17) ?: new Process(id: 17, phFirstName: "Wojciech",
                saleSection: 'segment1', phNumber: 12345,
                phSurname: 'Szczęsny', calcNumber: '44444' , status: Process.ProcessStatus.REJECTED,
                client: Client.findByName("clientName1"))
                .addToDocuments(new DocumentFile(name: "pedef.pdf"))
                .addToDocuments(new DocumentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef3.pdf")).save(flush:true);

        Process.get(18) ?: new Process(id: 18, phFirstName: "Łukasz",
                saleSection: 'segment2', phNumber: 321,
                phSurname: 'Fabiański',calcNumber: '33333', status: Process.ProcessStatus.SUBSCRIPTIONS_DONE,
                client:  Client.findByName("clientName2"))
                .addToDocuments(new DocumentFile(name: "pedef.pdf"))
                .addToDocuments(new DocumentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef3.pdf")).save(flush:true);

        Process.get(19) ?: new Process(id: 19, phFirstName: "Wojciech",
                saleSection: 'segment1', phNumber: 12345,
                phSurname: 'Skaba', calcNumber: '44444' , status: Process.ProcessStatus.REJECTED,
                client: Client.findByName("clientName1"))
                .addToDocuments(new DocumentFile(name: "pedef.pdf"))
                .addToDocuments(new DocumentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef3.pdf")).save(flush:true);

        Process.get(20) ?: new Process(id: 20, phFirstName: "Mieszko",
                saleSection: 'segment2', phNumber: 321,
                phSurname: 'Pierwszy',calcNumber: '33333', status: Process.ProcessStatus.REJECTED,
                client:  Client.findByName("clientName2"))
                .addToDocuments(new DocumentFile(name: "pedef.pdf"))
                .addToDocuments(new DocumentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef3.pdf")).save(flush:true);

        Process.get(21) ?: new Process(id: 21, phFirstName: "Wojciech",
                saleSection: 'segment1', phNumber: 12345,
                phSurname: 'Skaba', calcNumber: '44444' , status: Process.ProcessStatus.REJECTED,
                client: Client.findByName("clientName1"))
                .addToDocuments(new DocumentFile(name: "pedef.pdf"))
                .addToDocuments(new DocumentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef3.pdf")).save(flush:true);

        Process.get(22) ?: new Process(id: 22, phFirstName: "Mieszko",
                saleSection: 'segment2', phNumber: 321,
                phSurname: 'Pierwszy',calcNumber: '33333', status: Process.ProcessStatus.REJECTED,
                client:  Client.findByName("clientName2"))
                .addToDocuments(new DocumentFile(name: "pedef.pdf"))
                .addToDocuments(new DocumentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef3.pdf")).save(flush:true);

        // szablony maili
        EmailTemplates.findByName(EmailTemplates.EmailTemplateType.NOTES_TO_COA) ?: new EmailTemplates(name: EmailTemplates.EmailTemplateType.NOTES_TO_COA,
                sender: "apreel.eUmowy@gmail.com",
                recipent: "apreel.eUmowy@gmail.com").save(flush:true);
    }

    def createCBDDataForDevProfile(){
        def sql = new Sql(dataSource)

        //dodawanie pustego admina i ph
        sql.executeUpdate('insert into CBD_ADM.adm_uzytkownicy (uzy_id) values(?)',[1]);
        sql.executeUpdate('insert into CBD_ADM.cbt_przedstawicieleh (prz_id) values(?)',[1]);

        //uzytkownik "user" bez ról
        sql.executeUpdate(
                'insert into CBD_ADM.ADM_UZYTKOWNICY_WEB(AUW_ID,AUW_LOGIN,AUW_HASLO ) values(?, ?, ?)',
                [123451,'user', 'iEmqhY8YxduPVOg5Tgs9aca8M5coCILEFRmk8Q=='])

        //uzytkownik "admin" z rola EUM_ZRD
        sql.executeUpdate(
                'insert into CBD_ADM.ADM_UZYTKOWNICY_WEB(AUW_ID,AUW_LOGIN,AUW_HASLO,AUW_UZY_ID, AUW_IMIE, AUW_NAZWISKO  ) values(?, ?, ?, ?, ?, ?)',
                [223415,'admin', 'iEmqhY8YxduPVOg5Tgs9aca8M5coCILEFRmk8Q==',1, 'Jan', 'Kowalski'])

        //uzytkownik "ph" z rola EUM_PH_BZOS
        sql.executeUpdate(
                'insert into CBD_ADM.ADM_UZYTKOWNICY_WEB(AUW_ID,AUW_LOGIN,AUW_HASLO,AUW_PRZ_ID, AUW_IMIE, AUW_NAZWISKO  ) values(?, ?, ?, ?, ?, ?)',
                [312354,'ph', 'iEmqhY8YxduPVOg5Tgs9aca8M5coCILEFRmk8Q==',1, 'Jerzy',' Poniedziałek'])

        sql.close()
    }

    def executeSqlScript(String scriptPath){
        def sql = new Sql(dataSource)
        File sqlFile = new File(this.class.getResource(scriptPath).getFile())

        try{
            sqlFile.eachLine {
                sql.executeInsert(it)
            }
        }
        catch (SQLException e) {
            println e.getMessage();
        }
        finally {
            sql.close()
        }
    }
}
