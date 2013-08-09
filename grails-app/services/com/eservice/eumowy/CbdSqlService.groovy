package com.eservice.eumowy

import groovy.sql.Sql

import java.sql.SQLException;

class CbdSqlService {

    def dataSourceExt

    def selectOne(String sqlName ,def paramers){
        def row
        try {
            def sql = Sql(dataSourceExt)
            row = sql.firstRow(getSqlText(sqlName), paramers)
        }
        catch (SQLException ex) {
            log.error ex.message, ex
            throw ex
        }
        row
    }

    def selectMany(String sqlName ,def paramers){

        def rows = []
        try {
            def sql = Sql(dataSourceExt)
            sql.eachRow(getSqlText(sqlName),paramers) {
                rows.add(it.toRowResult())
            }
        }
        catch (SQLException ex) {
            log.error ex.message, ex
            throw ex
        }
        rows
    }

    def getSqlText(String sqlName){
        try {
            File sqlFile = new File(this.class.getResource("/sql/${sqlName}.sql").getFile())
            return sqlFile.text;
        } catch (IOException ex) {
            log.error ex.message, ex
            throw ex
        }
    }

}
