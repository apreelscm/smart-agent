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
                [1,'user', 'iEmqhY8YxduPVOg5Tgs9aca8M5coCILEFRmk8Q=='])

        //uzytkownik "admin" z rola ADM_ROLE
        sql.executeUpdate(
                'insert into CBD_ADM.ADM_UZYTKOWNICY_WEB(AUW_ID,AUW_LOGIN,AUW_HASLO,AUW_UZY_ID ) values(?, ?, ?, ?)',
                [2,'admin', 'iEmqhY8YxduPVOg5Tgs9aca8M5coCILEFRmk8Q==',1])

        //uzytkownik "ph" z rola PH_ROLE
        sql.executeUpdate(
                'insert into CBD_ADM.ADM_UZYTKOWNICY_WEB(AUW_ID,AUW_LOGIN,AUW_HASLO,AUW_PRZ_ID ) values(?, ?, ?, ?)',
                [3,'ph', 'iEmqhY8YxduPVOg5Tgs9aca8M5coCILEFRmk8Q==',1])

        sql.close();

        new Process(id: 12,  phFirstName: "Jerzy", clientNip: "555344435",
                clientName: 'nazwa1', saleSection: 'segment1', phNumber: '12345',
                phSurname: 'Kowalski', calcNumber: '44444').save();

        new Process(id: 13,  phFirstName: "Wanda", clientNip: "11241412",
                clientName: 'nazwa2', saleSection: 'segment2', phNumber: '321',
                phSurname: 'Iksińska',calcNumber: '33333').save();
    }
}
