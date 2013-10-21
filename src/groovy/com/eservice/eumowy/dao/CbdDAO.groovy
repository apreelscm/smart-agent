package com.eservice.eumowy.dao

import groovy.sql.Sql

import java.sql.SQLException

class CbdDAO {

    def dataSource

    def selectOne(String sqlName ,def paramers = []){
        def row
        try {
            def sql = new Sql(dataSource)
            row = sql.firstRow(getSqlText(sqlName),paramers)
        }
        catch (SQLException ex) {
            log.error ex.message, ex
            throw ex
        }
        row
    }

    def selectMany(String sqlName ,def paramers = []){

        def rows = []

        try {
            def sql = new Sql(dataSource)
            sql.eachRow(getSqlText(sqlName),paramers) {
                rows.add(it.toRowResult())
            }
        }
        catch (SQLException ex) {
            log.error ex.message, ex
            throw ex
        }
        finally{
            return rows;
        }
    }

    def getSqlText(String sqlName){
        File sqlFile
        try {
            sqlFile = new File(this.class.getResource("/sql/${sqlName}.sql").getFile())
        } catch (IOException ex) {
            println("getSqlText error:"+ex.message)
            throw ex
        }

        StringBuilder buf = new StringBuilder();
        sqlFile.eachLine {
            buf.append(it).append(" ")
        }

        buf.toString()
    }
}