dataSource {
    pooled = true
    formatSql = false
    dialect = "org.hibernate.dialect.Oracle10gDialect"
   // hibernate.default_schema = "CBD_UMOWY"

}

hibernate {
	generate_statistics=true
    cache.use_second_level_cache = true
    cache.use_query_cache = true
	cache.provider_class='org.hibernate.cache.EhCacheProvider'
}

// environment specific settings
environments {
    mock {
        dataSource {
            driverClassName = "org.h2.Driver"
            dialect = "org.hibernate.dialect.H2Dialect"
            username = "sa"
            password = ""
            dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
            url = "jdbc:h2:mem:CbdDb;MODE=Oracle;MVCC=TRUE;LOCK_TIMEOUT=10000;INIT=CREATE SCHEMA IF NOT EXISTS CBD_ADM\\;CREATE SCHEMA IF NOT EXISTS EUMOWY"
            loggingSql = true
            logSql= true
        }
    }
    development {
        dataSource {
            driverClassName = "oracle.jdbc.driver.OracleDriver"
            dialect = "org.hibernate.dialect.Oracle10gDialect"
            username = "EUMOWY_APP"
            password = 'WsMQ8h2$'
            dbCreate = ""
            url = "jdbc:oracle:thin:@192.168.9.22:1521:cbd"
            properties {
                maxActive = -1
                initialSize = 1
                maxWait = 10000
                numTestsPerEvictionRun = 3
                testOnBorrow = true
                testWhileIdle = true
                testOnReturn = true
                validationQuery = "SELECT 1 from dual"
                minEvictableIdleTimeMillis = 1000 * 60 * 5
                timeBetweenEvictionRunsMillis = 1000 * 60 * 5
            }
            logSql= false
            loggingSql = false
        }
    }
    test {
        dataSource {
            jndiName ="java:comp/env/jdbc/eumowyDS"
            loggingSql = false
            logSql= false
        }
    }
    uat {
        dataSource {
            jndiName ="java:comp/env/jdbc/eumowyDS"
            loggingSql = false
            logSql= false
        }
    }
    production {
        dataSource {
            jndiName ="java:comp/env/jdbc/eumowyDS"
            pooled = true
            loggingSql = false
            logSql= false
        }
    }
}
