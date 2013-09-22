package com.eservice.eumowy.util

/**
 * User: Dominik Walczak
 * Date: 22.09.13 Time: 01:46
 *
 */
public class ProjectPathHelper {

    public static String getProjectPath(URL projectResourceURL) {
        String urlString = projectResourceURL.toString();
        if (urlString.contains("target")){
            return urlString.substring(0,urlString.indexOf("target")).replace("file:/", "");
        } else {
            return urlString.substring(0,urlString.indexOf("out")).replace("file:/", "");
        }
    }
}