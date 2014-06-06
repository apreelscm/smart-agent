package com.eservice.eumowy.validator

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
        if(price == null){
            price = BigDecimal.ZERO
        }
        return price
    }

    public static def getMessage(def cmd, def propertyName){
        return cmd.metaClass.respondsTo(cmd, MESSAGE_PROPERTY_NAME)? cmd."${MESSAGE_PROPERTY_NAME}"(propertyName) : propertyName;
    }

}
