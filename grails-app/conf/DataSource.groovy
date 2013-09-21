dataSource {
    pooled = true
    loggingSql = true
    logSql= true
    formatSql = false
   // hibernate.default_schema = "CBD_UMOWY"

}

hibernate {
    cache.use_second_level_cache = false
    cache.use_query_cache = false
   /* cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'*/
}

// environment specific settings
environments {
    development {
        dataSource {
            driverClassName = "org.h2.Driver"
            dialect = "org.hibernate.dialect.H2Dialect"
            username = "sa"
            password = ""
            dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
            url = "jdbc:h2:mem:CbdDb;MODE=Oracle;MVCC=TRUE;LOCK_TIMEOUT=10000;INIT=CREATE SCHEMA IF NOT EXISTS CBD_ADM\\;CREATE SCHEMA IF NOT EXISTS EUMOWY"
        }
    }
    test {
        dataSource {
            driverClassName = "oracle.jdbc.driver.OracleDriver"
            dialect = "org.hibernate.dialect.Oracle10gDialect"
            username = "eumowy_app"
            password = "eumowy_app"
            dbCreate = "" // TODO change later for validate
           /*username = "eumowy"
             password = "V7S1947nK89O"
             dbCreate="update"*/
            url = "jdbc:oracle:thin:@db-eservice.apreel.lan:1521:cbd01out"
        }
    }
    uat {
        dataSource {
            driverClassName = "oracle.jdbc.driver.OracleDriver"
            dialect = "org.hibernate.dialect.Oracle10gDialect"
            username = "eumowy_app"
            password = "eumowy_app"
            dbCreate = "" // TODO change later for validate
            //url = "jdbc:oracle:thin:@192.168.3.221:1523:tstcbd"
            url = "jdbc:oracle:thin:@db-eservice.apreel.lan:1521:cbd01out"
        }
    }
    production {
        dataSource {
            dbCreate = "validate"
            url = "jdbc:h2:CbdDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
            pooled = true
            properties {
               maxActive = -1
               minEvictableIdleTimeMillis=1800000
               timeBetweenEvictionRunsMillis=1800000
               numTestsPerEvictionRun=3
               testOnBorrow=true
               testWhileIdle=true
               testOnReturn=true
               validationQuery="SELECT 1 from dual"
            }
        }
    }
}
