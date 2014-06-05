package com.eservice.eumowy.validator

class ValidatorUtils {

    static def MESSAGE_PROPERTY_NAME = 'getMessageForProperty'

    public static boolean hasMorePriceGroups(def maxSize, def pointCommands){
        Set<BigDecimal> normalPriceGroups = new HashSet<BigDecimal>()

        pointCommands.each { pos ->

            if (pos != null) {
                normalPriceGroups.add(getGroupValue(pos.dialupCena, pos.dialupPPCena))
                normalPriceGroups.add(getGroupValue(pos.vpnCena, pos.vpnPPCena))
                normalPriceGroups.add(getGroupValue(pos.sslCena, pos.sslPPCena))
                normalPriceGroups.add(getGroupValue(pos.gprsCena, pos.gprsPPCena))
                normalPriceGroups.add(getGroupValue(pos.pinPadCena, BigDecimal.ZERO))
            }
        }

        normalPriceGroups.removeAll(Collections.singleton(BigDecimal.ZERO)) //jesli obie ceny sa nullem to dostajemy 0
        if(normalPriceGroups.size() > maxSize){
            return true
        }
        return false
    }

    public static def getGroupValue(def normalPrice, def ppPrice){
        if(normalPrice == null){
            normalPrice = BigDecimal.ZERO
        }
        if (ppPrice == null){
            ppPrice = BigDecimal.ZERO
        }
        return normalPrice + ppPrice
    }

    public static def getMessage(def cmd, def propertyName){
        return cmd.metaClass.respondsTo(cmd, MESSAGE_PROPERTY_NAME)? cmd."${MESSAGE_PROPERTY_NAME}"(propertyName) : propertyName;
    }

}
