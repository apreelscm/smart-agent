import com.eservice.eumowy.*
import groovy.sql.Sql

class BootStrap {

    def dataSourceExt

    def init = { servletContext ->

        def sql = new Sql(dataSourceExt)

        // Wykonywanie inicjacyjnych zapytan sql z pliku insertData.sql
        File sqlFile = new File(this.class.getResource("/sql/insertData.sql").getFile())
        if(sqlFile.isFile()) {
            sqlFile.eachLine {
                sql.execute(it)
            }
        }

        //dodawanie pustego admina i ph
        sql.executeUpdate('insert into CBD_ADM.adm_uzytkownicy (uzy_id) values(?)',[1]);
        sql.executeUpdate('insert into CBD_ADM.cbt_przedstawicieleh (prz_id) values(?)',[1]);

        //uzytkownik "user" bez ról
        sql.executeUpdate(
                'insert into CBD_ADM.ADM_UZYTKOWNICY_WEB(AUW_ID,AUW_LOGIN,AUW_HASLO ) values(?, ?, ?)',
                [123451,'user', 'iEmqhY8YxduPVOg5Tgs9aca8M5coCILEFRmk8Q=='])

        //uzytkownik "admin" z rola ADM_ROLE
        sql.executeUpdate(
                'insert into CBD_ADM.ADM_UZYTKOWNICY_WEB(AUW_ID,AUW_LOGIN,AUW_HASLO,AUW_UZY_ID, AUW_IMIE, AUW_NAZWISKO  ) values(?, ?, ?, ?, ?, ?)',
                [223415,'admin', 'iEmqhY8YxduPVOg5Tgs9aca8M5coCILEFRmk8Q==',1, 'Jan', 'Kowalski'])

        //uzytkownik "ph" z rola PH_ROLE
        sql.executeUpdate(
                'insert into CBD_ADM.ADM_UZYTKOWNICY_WEB(AUW_ID,AUW_LOGIN,AUW_HASLO,AUW_PRZ_ID, AUW_IMIE, AUW_NAZWISKO  ) values(?, ?, ?, ?, ?, ?)',
                [312354,'ph', 'iEmqhY8YxduPVOg5Tgs9aca8M5coCILEFRmk8Q==',1, 'Jerzy',' Poniedziałek'])

        sql.close();


        new Client(name: 'clientName1', nip: "555344435").save();
        new Client(name: 'clientName2', nip: "11241412").save(flush:true);

        //przykladowe procesy
        new Process(id: "000001", phFirstName: "Jerzy",
                saleSection: 'segment1', phNumber: '12345',
                phSurname: 'Kowalski', calcNumber: '44444' , status: Process.ProcessStatus.REJECTED,
                client: Client.findByName("clientName1"))
                .addToDocuments(new DocumentFile(filename: "pedef.pdf"))
                .addToDocuments(new DocumentFile(filename: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(filename: "pedef.pdf"))
                .addToAttachments(new AttachmentFile(filename: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(filename: "pedef3.pdf")).save();

        new Process(id: "000002", phFirstName: "Wanda",
                saleSection: 'segment2', phNumber: '321',
                phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.REJECTED,
                client:  Client.findByName("clientName2"))
                .addToDocuments(new DocumentFile(filename: "pedef.pdf"))
                .addToDocuments(new DocumentFile(filename: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(filename: "pedef3.pdf")).save();



        new EmailTemplates(name: EmailTemplates.EmailTemplateType.NOTES_TO_COA,
                sender: "apreel.eUmowy@gmail.com",
                recipent: "apreel.eUmowy@gmail.com").save();
    }
}
