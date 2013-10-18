import com.eservice.eumowy.*
import com.eservice.eumowy.util.EumowyCustomEnvironment
import com.eservice.eumowy.util.ProjectPathHelper
import grails.util.Environment
import groovy.sql.Sql

import java.sql.SQLException

class BootStrap {

    def dataSource

    def init = { servletContext ->

        switch (Environment.getCurrent().getName()) {
            case EumowyCustomEnvironment.MOCK.getName():
                // Wykonywanie inicjacyjnych zapytan sql
                def projectPath = ProjectPathHelper.getProjectPath(this.class.getResource("BootStrap.class"))
                executeSqlScript(projectPath+"grails-app/conf/sql/createData.sql")
                executeSqlScript(projectPath+"otherResources/sql/1.0.0/02_100_dictionaries.sql")
                executeSqlScript(projectPath+"otherResources/sql/1.0.0/03_100_params.sql")
                createCBDDataForDevProfile();
                createTestDomains()
                break;
            default:
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
                saleSection: 'segment1', phNumber: "12345", observed: true,
                phSurname: 'Kowalski', calcNumber: '44444' , status: Process.ProcessStatus.ACCEPTED,
                client: Client.findByName("clientName1"))
                .addToDocuments(new DocumentFile(name: "pedef.pdf", pagesCount: 1, content: new DocumentContent(content:[95, 96] as byte[]))).save(flush: true);

/*
        Process.get(2) ?: new Process(id: 2, phFirstName: "Wanda",
                saleSection: 'segment2', phNumber: 321, observed: true,
                phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.ACCEPTED,
                client:  Client.findByName("clientName2"))
                .addToDocuments(new DocumentFile(name: "pedef.pdf"))
                .addToDocuments(new DocumentFile(name: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(name: "pedef3.pdf")).save(flush:true);
*/

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
        File sqlFile = new File(scriptPath)

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
