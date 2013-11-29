package com.eservice.eumowy.dao

import groovy.sql.Sql
import org.perf4j.StopWatch
import org.perf4j.log4j.Log4JStopWatch

import java.sql.SQLException

class CbdDAO {

    def dataSource

    def selectOne(String sqlName ,def paramers = []){
        StopWatch stopWatch = new Log4JStopWatch();
        def row
        def sql
        try {
            sql = new Sql(dataSource)
            row = sql.firstRow(getSqlText(sqlName),paramers)
        } catch (SQLException ex) {
            log.error ex.message, ex
            throw ex
        } finally {
            sql?.close()
        }
        stopWatch.stop('sql-'+sqlName)
        row
    }

    def selectMany(String sqlName ,def paramers = []){
        StopWatch stopWatch = new Log4JStopWatch();

        def rows = []
        def sql

        try {
            sql = new Sql(dataSource)
            sql.eachRow(getSqlText(sqlName),paramers) {
                rows.add(it.toRowResult())
            }
        }
        catch (SQLException ex) {
            log.error ex.message, ex
            throw ex
        }
        finally{
            sql?.close()
            stopWatch.stop('sql-'+sqlName)
            return rows;
        }
    }

    def getSqlText(String sqlName){
        StopWatch stopWatch = new Log4JStopWatch();
        File sqlFile
        try {
            sqlFile = new File(this.class.getResource("/sql/${sqlName}.sql").getFile())
        } catch (IOException ex) {
            log.error ex.message, ex
            throw ex
        }

        StringBuilder buf = new StringBuilder();
        sqlFile.eachLine {
//            //hack dla randomowego dodawania przez jave srednikow na koncu query
//            if(it.length() > 0 && it.charAt(it.length() - 1) == ';'){
//                it = it.substring(0, it.length() - 1)
//            }
            buf.append(it).append(" ")
        }
        stopWatch.stop('sql-'+sqlName)
        buf.toString()
    }
}