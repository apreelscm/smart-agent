dataSource {
    pooled = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
    loggingSql = true
    logSql= true
    formatSql = false
   // hibernate.default_schema = "CBD_UMOWY"
    //dialect = org.hibernate.dialect.Oracle10gDialect
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
            dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
            url = "jdbc:h2:mem:CbdDb;MVCC=TRUE;LOCK_TIMEOUT=10000;INIT=CREATE SCHEMA IF NOT EXISTS CBD_ADM\\;CREATE SCHEMA IF NOT EXISTS CBD_UMOWY"
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:mem:CbdDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
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
               validationQuery="SELECT 1"
            }
        }
    }
}
