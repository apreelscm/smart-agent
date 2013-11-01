package com.apache.log4j.jdbc

import org.apache.log4j.Priority
import org.apache.log4j.jdbc.JDBCAppender

import javax.naming.Context
import javax.naming.InitialContext
import javax.naming.NamingException
import javax.sql.DataSource
import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement

/**
 * Log4j appender for audit log
 *
 * User: mariusz.kaczkowski
 * Date: 30.10.13
 * Time: 13:43
 */
class EumowyJDBCAppender extends JDBCAppender {

        String jndiValue
        DataSource jndiDS
        boolean jndiSearched

        public EumowyJDBCAppender(Object dataSourceConfig, String name, String sql, Priority threshold) {
            String jndiName = ((ConfigObject) dataSourceConfig).getProperty("jndiName")?.toString();
            if (! "[:]".equalsIgnoreCase(jndiName)){
                this.jndiValue = jndiName
            }
            else {
                setPropertiesFromConfig((ConfigObject) dataSourceConfig)
            }

            this.setName(name);
            this.setSql(sql);
            this.setThreshold(threshold);

        }

    /**
     *
     * Jndi lookup placed in this method because on constructor JNDI value can be not initialized yet by server
     * */
     @Override
     protected void execute(String sql) throws SQLException {

        Connection con = null;
        Statement stmt = null;

        try {
            if (jndiValue && ! jndiSearched){
                jndiDS = lookupDataSource()
            }

            if (jndiValue) {
                con = jndiDS.getConnection();
            }
            else {
                con = getConnection();
            }

            stmt = con.createStatement();
            stmt.executeUpdate(sql);
        } finally {
            if(stmt != null) {
                stmt.close();
            }
            closeConnection(con);
        }

    }

    private synchronized DataSource lookupDataSource() {
            try {
                Context initialContext = new InitialContext();
                Context envCtx = (Context) initialContext.lookup("java:comp/env");
                return (DataSource) envCtx.lookup(jndiValue);
            }
            catch (NamingException  e){
                throw new IllegalArgumentException(e)
            }
            finally {
                jndiSearched = true
            }
    }

    private void setPropertiesFromConfig(ConfigObject dataSourceConfig){
            this.setUser(dataSourceConfig.getProperty("username").toString());
            this.setPassword(dataSourceConfig.getProperty("password").toString());
            this.setDriver(dataSourceConfig.getProperty("driverClassName").toString());
            this.setURL(dataSourceConfig.getProperty("url").toString());
    }

}