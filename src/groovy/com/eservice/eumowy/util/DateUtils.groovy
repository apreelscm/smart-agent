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

    static DATE_FORMAT = "yyyy-MM-dd"


    def static Date getCurrentDate(){
        new Date()
    }

    def static String getCurrentFormattedDate(){
        getFormattedDate(getCurrentDate())
    }

    def static String getFormattedDate(def date){
        new SimpleDateFormat(DATE_FORMAT).format(date)
    }

}
