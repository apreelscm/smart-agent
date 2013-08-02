import com.eservice.eumowy.*
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

        new Activity(numerPozycji:1, code:"nowaUmowa").save(flush: true);
        new Activity(numerPozycji:2, code:"dodatkowyPunkt").save();
        new Activity(numerPozycji:3, code:"dodatkowyPos").save()
        new Activity(numerPozycji:4, code:"zmianaProwizji").save()
        new Activity(numerPozycji:5, code:"dodanieAneksuKoszty").save()
        new Activity(numerPozycji:6, code:"wymianaUmowyNajmu").save()
        new Activity(numerPozycji:7, code:"aneks").save()
        new Activity(numerPozycji:8, code:"zmianaTabeliOplatDodatkowych").save()
        new Activity(numerPozycji:9, code:"zmianaWarunkowPrepaid").save()
        new Activity(numerPozycji:10, code:"zmianaOkresuLojalnosciowego").save()
        new Activity(numerPozycji:11, code:"promocyjneObnizenieNajmu").save()
        new Activity(numerPozycji:12, code:"dodaniePrepaid").save()
        new Activity(numerPozycji:13, code:"dodanieDcc").save()
        new Activity(numerPozycji:14, code:"dodanieCashBack").save()
        new Activity(numerPozycji:15, code:"dodanieIko").save()
        new Activity(numerPozycji:16, code:"zmianaWarunkowDcc").save()
        new Activity(numerPozycji:17, code:"ekonomiczny").save()
        new Activity(numerPozycji:18, code:"komfort").save()
        new Activity(numerPozycji:19, code:"prestiz").save()
        new Activity(numerPozycji:20, code:"poprawDane").save()
        new Activity(numerPozycji:21, code:"odrzucDokumenty").save()


        new Signature(name:"AP-AG/F/DF/2.003/12-02-06").save()
        new Signature(name:"AP-AG/F/DP/2.003/13-05-10").save()
        new Signature(name:"AP/F/DS/2.000/09-04-22").save()
        new Signature(name:"AP/UNTZ/2.003/12-01-16").save()
        new Signature(name:"AP/UPZBS/2.000/13-01-25").save()
        new Signature(name:"AP/UPZ/2.000/13-01-03").save()
        new Signature(name:"AP/UPZIF/2.000/13-03-26").save()
        new Signature(name:"AP-AG/F/DP/2.003/13-05-10").save()
        new Signature(name:"AP/UNTSS/1.003/12-01-16").save()
        new Signature(name:"AP/UNTZ/2.003/12-01-16").save()
        new Signature(name:"AP/UNTSZ/APOU/3.002/12-01-16").save()
        new Signature(name:"AP/UPZ/AWNZBS/1.000/13-01-25").save()
        new Signature(name:"AP/UPZ/AWNZS/1.000/13-01-25").save()
        new Signature(name:"AP/UPZ/IF/2.001/13-04-05").save()
        new Signature(name:"AP-AG/F/DP/2.003/13-05-10").save()
        new Signature(name:"AP/UNTSZ/APOU/3.002/12-01-16").save()
        new Signature(name:"AP/UNTSS/1.003/12-01-16").save()
        new Signature(name:"AP/UNTZ/2.003/12-01-16").save()
        new Signature(name:"AP/UPZ/AWNZBS/1.000/13-01-25").save()
        new Signature(name:"AP/UPZ/AWNZS/1.000/13-01-25").save()
        new Signature(name:"AP/UPZ/IF/2.001/13-04-05").save()
        new Signature(name:"AP/UNTSS/1.003/12-01-16").save()
        new Signature(name:"AP/UNTZ/2.003/12-01-16").save()
        new Signature(name:"AP/UNTSZ/APOU/3.002/12-01-16").save()
        new Signature(name:"AP/UNTSZ/AWNZ/3.002/12-01-16").save()
        new Signature(name:"AT/USU/FDU/4.004/13-05-22").save()
        new Signature(name:"AT/USU/5.004/13-05-22").save()
        new Signature(name:"AP/UNTSZ/OKOD/2.003/12-01-16").save()
        new Signature(name:"AP/UNTSZ/APOO/3.002/12-01-16").save()
        new Signature(name:"AP/UNTSZ/DCCZ/1.001/12-10-05").save()
        new Signature(name:"AP/UPZ/DCCZ/1.002/13-02-15").save()
        new Signature(name:"AT/USU/5.004/13-05-22").save()
        new Signature(name:"AT/USU/FDU/4.004/13-05-22").save()
        new Signature(name:"AP-AG/F/DP/2.003/13-05-10").save()
        new Signature(name:"AP/UPZ2/DCC/1.000/13-02-15").save()
        new Signature(name:"AP/UPZ/DCC/2.003/13-02-15").save()
        new Signature(name:"AP/UNTSS/1.003/12-01-16").save()
        new Signature(name:"AP/UNTZ/2.003/12-01-16").save()
        new Signature(name:"AP/UNTSZ/DCC/2.002/12-01-16").save()
        new Signature(name:"AP/UPZ2/ACB/1.000/13-02-15").save()
        new Signature(name:"AP/UPZ/ACB/2.003/13-02-15").save()
        new Signature(name:"AP/UPZBS/AIKO/1.000/13-03-25").save()
        new Signature(name:"AP/UNTW/AGON/1.002/12-01-16").save()
        new Signature(name:"AP/UNTW/ANOD/1.003/12-01-16").save()
        new Signature(name:"AP/UNTW/AGOK/1.002/12-01-16").save()
        new Signature(name:"AP/UNTW/ANOD/1.003/12-01-16").save()
        new Signature(name:"AP/UNTW/AGOP/1.002/12-01-16").save(flush: true)

        new ActivitySignatures(activity: Activity.findByNumerPozycji(1), signature: Signature.findByName("AP/UNTSS/1.003/12-01-16"), numberOfList:1).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(1), signature: Signature.findByName("AP-AG/F/DF/2.003/12-02-06"), mandatory: true).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(1), signature: Signature.findByName("AP-AG/F/DP/2.003/13-05-10"), mandatory: true).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(1), signature: Signature.findByName("AP/F/DS/2.000/09-04-22"), mandatory: true).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(1), signature: Signature.findByName("AP/UNTZ/2.003/12-01-16"), numberOfList:1).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(1), signature: Signature.findByName("AP/UPZBS/2.000/13-01-25"), numberOfList:2).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(1), signature: Signature.findByName("AP/UPZ/2.000/13-01-03"), numberOfList:2).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(1), signature: Signature.findByName("AP/UPZIF/2.000/13-03-26"), numberOfList:2).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(2), signature: Signature.findByName("AP-AG/F/DP/2.003/13-05-10"), mandatory: true).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(2), signature: Signature.findByName("AP/UNTSS/1.003/12-01-16"), numberOfList:1).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(2), signature: Signature.findByName("AP/UNTZ/2.003/12-01-16"), numberOfList:1).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(2), signature: Signature.findByName("AP/UNTSZ/APOU/3.002/12-01-16"), numberOfList:1).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(2), signature: Signature.findByName("AP/UPZ/AWNZBS/1.000/13-01-25"), numberOfList:2).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(2), signature: Signature.findByName("AP/UPZ/AWNZS/1.000/13-01-25"), numberOfList:2).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(2), signature: Signature.findByName("AP/UPZ/IF/2.001/13-04-05"), numberOfList:2).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(3), signature: Signature.findByName("AP-AG/F/DP/2.003/13-05-10"), mandatory: true).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(3), signature: Signature.findByName("AP/UNTSZ/APOU/3.002/12-01-16"), numberOfList:1).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(3), signature: Signature.findByName("AP/UNTSS/1.003/12-01-16"), numberOfList:1).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(3), signature: Signature.findByName("AP/UNTZ/2.003/12-01-16"), numberOfList:1).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(4), signature: Signature.findByName("AP/UPZ/AWNZBS/1.000/13-01-25"), numberOfList:1).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(4), signature: Signature.findByName("AP/UPZ/AWNZS/1.000/13-01-25"), numberOfList:1).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(5), signature: Signature.findByName("AP/UPZ/IF/2.001/13-04-05"), numberOfList:1).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(6), signature: Signature.findByName("AP/UNTSS/1.003/12-01-16"), numberOfList:1).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(6), signature: Signature.findByName("AP/UNTZ/2.003/12-01-16"), numberOfList:1).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(7), signature: Signature.findByName("AP/UNTSZ/APOU/3.002/12-01-16"), numberOfList:1).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(8), signature: Signature.findByName("AP/UNTSZ/AWNZ/3.002/12-01-16"), numberOfList:1).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(9), signature: Signature.findByName("AT/USU/FDU/4.004/13-05-22"), numberOfList:1).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(9), signature: Signature.findByName("AT/USU/5.004/13-05-22"), numberOfList:1).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(10), signature: Signature.findByName("AP/UNTSZ/OKOD/2.003/12-01-16"), numberOfList:1).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(11), signature: Signature.findByName("AP/UNTSZ/APOO/3.002/12-01-16"), numberOfList:1).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(16), signature: Signature.findByName("AP/UNTSZ/DCCZ/1.001/12-10-05"), numberOfList:1).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(16), signature: Signature.findByName("AP/UPZ/DCCZ/1.002/13-02-15"), numberOfList:2).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(12), signature: Signature.findByName("AT/USU/5.004/13-05-22"), numberOfList:1).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(12), signature: Signature.findByName("AT/USU/FDU/4.004/13-05-22"), numberOfList:1).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(12), signature: Signature.findByName("AP-AG/F/DP/2.003/13-05-10"), mandatory: true).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(13), signature: Signature.findByName("AP/UPZ2/DCC/1.000/13-02-15"), numberOfList:1).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(13), signature: Signature.findByName("AP/UPZ/DCC/2.003/13-02-15"), numberOfList:1).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(13), signature: Signature.findByName("AP/UNTSS/1.003/12-01-16"), numberOfList:2).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(13), signature: Signature.findByName("AP/UNTZ/2.003/12-01-16"), numberOfList:2).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(13), signature: Signature.findByName("AP/UNTSZ/DCC/2.002/12-01-16"), numberOfList:2).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(14), signature: Signature.findByName("AP/UPZ2/ACB/1.000/13-02-15"), numberOfList:1).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(14), signature: Signature.findByName("AP/UPZ/ACB/2.003/13-02-15"), numberOfList:1).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(15), signature: Signature.findByName("AP/UPZBS/AIKO/1.000/13-03-25"), numberOfList:1).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(17), signature: Signature.findByName("AP/UNTW/AGON/1.002/12-01-16"), numberOfList:1).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(17), signature: Signature.findByName("AP/UNTW/ANOD/1.003/12-01-16"), numberOfList:2).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(18), signature: Signature.findByName("AP/UNTW/AGOK/1.002/12-01-16"), numberOfList:1).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(18), signature: Signature.findByName("AP/UNTW/ANOD/1.003/12-01-16"), numberOfList:2).save()
        new ActivitySignatures(activity: Activity.findByNumerPozycji(19), signature: Signature.findByName("AP/UNTW/AGOP/1.002/12-01-16"),  numberOfList:1).save()

        /*  new Process( phFirstName: "Wanda", clientNip: "11241412",
                  clientName: 'nazwa2', saleSection: 'segment2', phNumber: '321',
                  phSurname: 'Iksińska',calcNumber: '33333', status: Process.ProcessStatus.REJECTED).save();*/

    }
}
