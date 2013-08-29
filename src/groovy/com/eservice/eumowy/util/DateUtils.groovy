package com.eservice.eumowy.util

import java.text.SimpleDateFormat

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 26.08.13
 * Time: 16:19
 * To change this template use File | Settings | File Templates.
 */
public class DateUtils {

    static YYYY_MM_DD = "yyyy-MM-dd"
    static DD_MM_YYYY = "dd-MM-yyyy"
    static DEFAULT_DATE_FORMAT = YYYY_MM_DD


    def static Date getCurrentDate(){
        new Date()
    }

    def static String getCurrentFormattedDate(){
        getFormattedDate(getCurrentDate(), DEFAULT_DATE_FORMAT)
    }

    def static String getCurrentFormattedDate(def dateFormat){
        getFormattedDate(getCurrentDate(), dateFormat)
    }

    def static String getFormattedDate(def date, def dateFormat){
        new SimpleDateFormat(dateFormat).format(date)
    }

    static def Date parseDate(def dateStr, def dateFormat){
        new SimpleDateFormat(dateFormat).parse(dateStr)
    }

    static def Date parseDate(def dateStr){
        parseDate(dateStr, DEFAULT_DATE_FORMAT)
    }

    static def Date addDays(date, days){
        new Date(date.getTime()+days*86400000L)
    }

    static def boolean isDate(def date, def dateFormat){
        try {
            date != null && !"".equals(date) && parseDate(date, dateFormat)
        } catch (Exception e){
            false
        }
    }
}
