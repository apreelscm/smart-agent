package com.apache.log4j.jdbc

import org.apache.log4j.Priority
import org.apache.log4j.jdbc.JDBCAppender

import javax.naming.Context
import javax.naming.InitialContext
import javax.naming.NamingException
import javax.sql.DataSource
import java.sql.Connection

/**
 * Log4j appender for audit log
 *
 * User: mariusz.kaczkowski
 * Date: 30.10.13
 * Time: 13:43
 */
class EumowyJDBCAppender extends JDBCAppender {

        String jndi;

        public EumowyJDBCAppender(Object dataSourceConfig, String name, String sql, Priority threshold) {
            String jndiName = ((ConfigObject) dataSourceConfig).getProperty("jndiName")?.toString();
            if (! "[:]".equalsIgnoreCase(jndiName)){
                this.jndi = jndiName
            }
            else {
                setPropertiesFromConfig((ConfigObject) dataSourceConfig)
            }

            this.setName(name);
            this.setSql(sql);
            this.setThreshold(threshold);

        }

        @Override
        protected Connection getConnection() {
            if (connection == null && jndi){
                DataSource ds = lookupDataSource()
                this.connection(ds.getConnection())
            }
            super.getConnection()
        }

        private synchronized DataSource lookupDataSource() {
            try {
                Context initialContext = new InitialContext();
                Context envCtx = (Context) initialContext.lookup("java:comp/env");
                return (DataSource) envCtx.lookup(jndi);
            }
            catch (NamingException  e){
                throw new IllegalArgumentException(e)
            }
        }

        private void setPropertiesFromConfig(ConfigObject dataSourceConfig){
            this.setUser(dataSourceConfig.getProperty("username").toString());
            this.setPassword(dataSourceConfig.getProperty("password").toString());
            this.setDriver(dataSourceConfig.getProperty("driverClassName").toString());
            this.setURL(dataSourceConfig.getProperty("url").toString());
        }

}