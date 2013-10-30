package com.apache.log4j.jdbc

import org.apache.log4j.Priority
import org.apache.log4j.jdbc.JDBCAppender

/**
 * Created with IntelliJ IDEA.
 * User: mariusz.kaczkowski
 * Date: 30.10.13
 * Time: 13:43
 * To change this template use File | Settings | File Templates.
 */
class EumowyJDBCAppender extends JDBCAppender {

        protected Object dataSource = null;

        public EumowyJDBCAppender(Object dataSource, String name, String sql, Priority threshold) {
            this.dataSource = dataSource;
            this.setName(name);
            this.setSql(sql);
            this.setThreshold(threshold);
            this.setUser(((ConfigObject)dataSource).getProperty("username").toString());
            this.setPassword(((ConfigObject)dataSource).getProperty("password").toString());
            this.setDriver(((ConfigObject)dataSource).getProperty("driverClassName").toString());
            this.setURL(((ConfigObject)dataSource).getProperty("url").toString());
        }
}