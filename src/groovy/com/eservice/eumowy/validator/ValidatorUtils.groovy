package com.eservice.eumowy.validator

import com.eservice.eumowy.command.PointCommand
import grails.validation.ValidationErrors

class ValidatorUtils {

    private static final String MESSAGE_PROPERTY_NAME = 'getMessageForProperty'

    public static boolean hasMorePriceGroups(def maxSize, def pointCommands){
        Set<BigDecimal> normalPriceGroups = new HashSet<BigDecimal>()

        pointCommands.each { pos ->
            if (pos != null) {
                normalPriceGroups.add(getGroupValue(pos.dialupCena))
                normalPriceGroups.add(getGroupValue(pos.dialupPPCena))

                normalPriceGroups.add(getGroupValue(pos.vpnCena))
                normalPriceGroups.add(getGroupValue(pos.vpnPPCena))

                normalPriceGroups.add(getGroupValue(pos.sslCena))
                normalPriceGroups.add(getGroupValue(pos.sslPPCena))

                normalPriceGroups.add(getGroupValue(pos.gprsCena))
                normalPriceGroups.add(getGroupValue(pos.gprsPPCena))
                normalPriceGroups.add(getGroupValue(pos.gprsCenaPortable))

                normalPriceGroups.add(getGroupValue(pos.wifiCenaPortable))

                normalPriceGroups.add(getGroupValue(pos.pinPadCena))
            }
        }

        normalPriceGroups.removeAll(Collections.singleton(BigDecimal.ZERO))

        if(normalPriceGroups.size() > maxSize){
            return true
        }

        return false
    }

    public static def getGroupValue(BigDecimal price) {
        return price ?: BigDecimal.ZERO
    }

    public static def getGroupValue(BigDecimal normalPrice, BigDecimal ppPrice){
        normalPrice = normalPrice ?: BigDecimal.ZERO
        ppPrice = ppPrice ?: BigDecimal.ZERO

        return normalPrice + ppPrice
    }

    public static def getGroupValue(Integer normalPrice, Integer ppPrice){
        normalPrice = normalPrice ?: 0
        ppPrice = ppPrice ?: 0

        return normalPrice + ppPrice
    }

    public static def getMessage(def cmd, def propertyName){
        return cmd.metaClass.respondsTo(cmd, MESSAGE_PROPERTY_NAME)? cmd."${MESSAGE_PROPERTY_NAME}"(propertyName) : propertyName;
    }

    public static int pointsWithoutFormaDoladowania(List<PointCommand> points) {
        int count = 0;

        points.each { point ->
            point.errors?.each { ValidationErrors error ->
                if (error.getAt("hasDodaniePrepaid")) {
                    count++;
                }
            }
        }

        return count
    }

    private static Boolean hasNotFilledField(List fieldsToCheck) {
        Boolean hasNotFilledField = true

        fieldsToCheck.each { field ->
            if(field) {
                hasNotFilledField = false
                return
            }
        }

        return hasNotFilledField
    }

}
