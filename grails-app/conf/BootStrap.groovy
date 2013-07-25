import com.eservice.eumowy.AttachmentFile
import com.eservice.eumowy.DocumentFile
import com.eservice.eumowy.Process
import groovy.sql.Sql

class BootStrap {

    def dataSourceExt

    def init = { servletContext ->

        def sql = new Sql(dataSourceExt)
   /*     File directory = new File(this.class.getResource("/sql/").getFile())

        //wykonywanie polecen ze znalezionych  plikow sql w katalogu conf/sql
        directory.eachFileRecurse { sqlFile ->
            if(sqlFile.isFile() && sqlFile.name.toUpperCase().endsWith(".SQL")) {
                sqlFile.eachLine {
                    sql.execute(it)
                }
            }
        }*/


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


        new Process(id: "000001", phFirstName: "Jerzy", clientNip: "555344435",
                clientName: 'nazwa1', saleSection: 'segment1', phNumber: '12345',
                phSurname: 'Kowalski', calcNumber: '44444' , status: Process.ProcessStatus.REJECTED)
                .addToDocuments(new DocumentFile(filename: "pedef.pdf"))
                .addToDocuments(new DocumentFile(filename: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(filename: "pedef.pdf"))
                .addToAttachments(new AttachmentFile(filename: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(filename: "pedef3.pdf")).save();


        new Process(id: "000002", phFirstName: "Wanda", clientNip: "11241412",
                clientName: 'nazwa2', saleSection: 'segment2', phNumber: '321',
                phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.REJECTED)
                .addToDocuments(new DocumentFile(filename: "pedef.pdf"))
                .addToDocuments(new DocumentFile(filename: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(filename: "pedef3.pdf")).save();

       /* new Process( phFirstName: "Wanda", clientNip: "11241412",
                clientName: 'nazwa2', saleSection: 'segment2', phNumber: '321',
                phSurname: 'Iksińska',calcNumber: '33333')
                .addToDocuments(new DocumentFile(filename: "pedef.pdf"))
                .addToDocuments(new DocumentFile(filename: "pedef2.pdf"))
                .addToAttachments(new AttachmentFile(filename: "pedef3.pdf")).save();
*/

        new Process( phFirstName: "Wanda", clientNip: "11241412",
                clientName: 'nazwa2', saleSection: 'segment2', phNumber: '321',
               phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.REJECTED).save();

        new Process( phFirstName: "Wanda", clientNip: "11241412",
                clientName: 'nazwa2', saleSection: 'segment2', phNumber: '321',
                phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.REJECTED).save();

        new Process( phFirstName: "Wanda", clientNip: "11241412",
                clientName: 'nazwa2', saleSection: 'segment2', phNumber: '321',
                phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.REJECTED).save();

        new Process( phFirstName: "Wanda", clientNip: "11241412",
                clientName: 'nazwa2', saleSection: 'segment2', phNumber: '321',
                phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.REJECTED).save();

        new Process( phFirstName: "Wanda", clientNip: "11241412",
                clientName: 'nazwa2', saleSection: 'segment2', phNumber: '321',
                phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.REJECTED).save();

        new Process( phFirstName: "Wanda", clientNip: "11241412",
                clientName: 'nazwa2', saleSection: 'segment2', phNumber: '321',
                phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.REJECTED).save();

        new Process( phFirstName: "Wanda", clientNip: "11241412",
                clientName: 'nazwa2', saleSection: 'segment2', phNumber: '321',
                phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.REJECTED).save();

        new Process( phFirstName: "Wanda", clientNip: "11241412",
                clientName: 'nazwa2', saleSection: 'segment2', phNumber: '321',
                phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.REJECTED).save();

        new Process( phFirstName: "Wanda", clientNip: "11241412",
                clientName: 'nazwa2', saleSection: 'segment2', phNumber: '321',
                phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.REJECTED).save();

        new Process( phFirstName: "Wanda", clientNip: "11241412",
                clientName: 'nazwa2', saleSection: 'segment2', phNumber: '321',
                phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.REJECTED).save();

        new Process( phFirstName: "Wanda", clientNip: "11241412",
                clientName: 'nazwa2', saleSection: 'segment2', phNumber: '321',
                phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.REJECTED).save();

        new Process( phFirstName: "Wanda", clientNip: "11241412",
                clientName: 'nazwa2', saleSection: 'segment2', phNumber: '321',
                phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.REJECTED).save();

        new Process( phFirstName: "Wanda", clientNip: "11241412",
                clientName: 'nazwa2', saleSection: 'segment2', phNumber: '321',
                phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.REJECTED).save();

        new Process( phFirstName: "Wanda", clientNip: "11241412",
                clientName: 'nazwa2', saleSection: 'segment2', phNumber: '321',
                phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.REJECTED).save();

        new Process( phFirstName: "Wanda", clientNip: "11241412",
                clientName: 'nazwa2', saleSection: 'segment2', phNumber: '321',
                phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.REJECTED).save();
        new Process( phFirstName: "Wanda", clientNip: "11241412",
                clientName: 'nazwa2', saleSection: 'segment2', phNumber: '321',
                phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.REJECTED).save();


        new Process( phFirstName: "Wanda", clientNip: "11241412",
                clientName: 'nazwa2', saleSection: 'segment2', phNumber: '321',
                phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.REJECTED).save();


        new Process( phFirstName: "Wanda", clientNip: "11241412",
                clientName: 'nazwa2', saleSection: 'segment2', phNumber: '321',
                phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.REJECTED).save();

        new Process( phFirstName: "Wanda", clientNip: "11241412",
                clientName: 'nazwa2', saleSection: 'segment2', phNumber: '321',
                phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.REJECTED).save();

        new Process( phFirstName: "Wanda", clientNip: "11241412",
                clientName: 'nazwa2', saleSection: 'segment2', phNumber: '321',
                phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.REJECTED).save();


        new Process( phFirstName: "Wanda", clientNip: "11241412",
                clientName: 'nazwa2', saleSection: 'segment2', phNumber: '321',
                phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.REJECTED).save();

        new Process( phFirstName: "Wanda", clientNip: "11241412",
                clientName: 'nazwa2', saleSection: 'segment2', phNumber: '321',
                phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.REJECTED).save();

        new Process( phFirstName: "Wanda", clientNip: "11241412",
                clientName: 'nazwa2', saleSection: 'segment2', phNumber: '321',
                phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.REJECTED).save();

        new Process( phFirstName: "Wanda", clientNip: "11241412",
                clientName: 'nazwa2', saleSection: 'segment2', phNumber: '321',
                phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.REJECTED).save();

    }
}
