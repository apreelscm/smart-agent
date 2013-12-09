package com.eservice.eumowy.validator

class ValidatorUtils {

    static def MESSAGE_PROPERTY_NAME = 'getMessageForProperty'

    private static boolean hasMorePriceGroups(def maxSize, def pointCommands){
        Set<BigDecimal> normalPriceGroups = new HashSet<BigDecimal>()
        Set<BigDecimal> prefPriceGroups = new HashSet<BigDecimal>()

        pointCommands.each { pos ->

            if (pos != null) {
                normalPriceGroups.add(getGroupValue(pos.dialupCena, pos.dialupPPCena))
                normalPriceGroups.add(getGroupValue(pos.vpnCena, pos.vpnPPCena))
                normalPriceGroups.add(getGroupValue(pos.sslCena, pos.sslPPCena))
                normalPriceGroups.add(getGroupValue(pos.gprsCena, pos.gprsPPCena))
                normalPriceGroups.add(getGroupValue(pos.pinPadCena, BigDecimal.ZERO))
                normalPriceGroups.add(getGroupValue(pos.wifiCena, BigDecimal.ZERO))

                prefPriceGroups.add(getGroupValue(pos.dialupCenaPreferencyjna, pos.dialupPPCenaPreferencyjna))
                prefPriceGroups.add(getGroupValue(pos.vpnCenaPreferencyjna, pos.vpnPPCenaPreferencyjna))
                prefPriceGroups.add(getGroupValue(pos.sslCenaPreferencyjna, pos.sslPPCenaPreferencyjna))
                prefPriceGroups.add(getGroupValue(pos.gprsCenaPreferencyjna, pos.gprsPPCenaPreferencyjna))
                prefPriceGroups.add(getGroupValue(pos.pinPadCenaPreferencyjna, BigDecimal.ZERO))
                prefPriceGroups.add(getGroupValue(pos.wifiCenaPreferencyjna, BigDecimal.ZERO))
            }
        }

        normalPriceGroups.removeAll(Collections.singleton(BigDecimal.ZERO)) //jesli obie ceny sa nullem to dostajemy 0
        prefPriceGroups.removeAll(Collections.singleton(BigDecimal.ZERO))
        if(normalPriceGroups.size() > maxSize || prefPriceGroups.size() > maxSize){
            return true
        }
        return false
    }

    private static def getGroupValue(def normalPrice, def ppPrice){
        if(normalPrice == null){
            normalPrice = BigDecimal.ZERO
        }
        if (ppPrice == null){
            ppPrice = BigDecimal.ZERO
        }
        return normalPrice + ppPrice
    }

    public static def getMessage(def cmd, def propertyName){

        println 'Trying to invoke method ' + MESSAGE_PROPERTY_NAME + ' from class: ' + cmd.class + ' for property: ' + propertyName

        def result
        if (cmd.metaClass.respondsTo(cmd, MESSAGE_PROPERTY_NAME)){
            println 'Object has ' + MESSAGE_PROPERTY_NAME
            result = cmd."${MESSAGE_PROPERTY_NAME}"(propertyName)
        } else {
            println "Object hasn't " + MESSAGE_PROPERTY_NAME
            result = propertyName
        }

        println 'Result: ' + result

        //def aaa = cmd.metaClass.respondsTo(cmd, MESSAGE_PROPERTY_NAME)? cmd."${MESSAGE_PROPERTY_NAME}"(propertyName) : propertyName;
        return result;
    }

}
