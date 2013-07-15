class BootStrap {

    def dataSourceExt

    def init = { servletContext ->

       /* def sql = new Sql(dataSourceExt)
        File directory = new File(this.class.getResource("/sql/").getFile())

        directory.eachFileRecurse { sqlFile ->
            if(sqlFile.isFile() && sqlFile.name.toUpperCase().endsWith(".SQL")) {
                sqlFile.eachLine {
                    sql.execute(it)
                }
            }
        }*/

/*
        def session = sessionFactory.openSession()
        session.beginTransaction()
        session.save(user)
        session.getTransaction().commit();
        session.close()*/

        //user.save();

        /*    HashMap params = new HashMap();
            params.put("flush", true);
            params.put("failOnError", true);
            InvokerHelper.invokeMethod(user, "save", params);*/

        /*  String sqlString = new File("createUserTables.sql").eachLine {
              sql.execute(it)
              def sql = new Sql()
          }*/

        /*    def sqlFile = new SqlFile(new File())
            sqlFile.
           def sql = new Sql(dataSourceExt)
             sql.executeUpdate(
                     'insert into CBD_ADM.ADM_UZYTKOWNICY_WEB(AUW_ID,AUW_LOGIN,AUW_HASLO ) values(?, ?, ?)',
                     [1,'admin', 'iEmqhY8YxduPVOg5Tgs9aca8M5coCILEFRmk8Q=='])*/
    }
}
