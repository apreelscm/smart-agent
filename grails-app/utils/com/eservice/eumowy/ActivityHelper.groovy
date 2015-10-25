package com.eservice.eumowy

import com.google.common.collect.Lists

class ActivityHelper {
    //TODO: enum
    public static final String NOWA_UMOWA = "nowaUmowa"
    public static final String DODATKOWY_POS = "dodatkowyPos"
    public static final String DODATKOWY_PUNKT = "dodatkowyPunkt"
    public static final String WYMIANA_TERMINALA = "wymianaTerminala"
    public static final String PAKIET_START = "pakietStart"
    public static final String PAKIET_START_PLUS = "pakietStartPlus"
    public static final String PAKIET_MOBILNY = "pakietMobilny"
    public static final String WYMIANA_UMOWY_NAJMU_NA_UMOWE_WSPOLPRACY = "wymianaUmowyNajmu"
    public static final String WYMIANA_UMOWY_PLATNICZEJ = "wymianaUmowyZaplaty"

    static boolean isNewAgreement(Process process) {
        return contains(process, NOWA_UMOWA)
    }

    static boolean isOnlyActivity(Process process, String activityName) {
        return contains(process, activityName) && process.activities.size() == 1
    }

    static boolean contains(Process process, String activityCode) {
        return process.activities?.any{it.code.equals(activityCode)}
    }

    static boolean containsOnly(Process process, List<String> activities) {
        return containsAll(process, activities) && process.activities.size() == activities.size()
    }

    static boolean containsAll(Process process, List<String> activities) {
        boolean hasAllActivities = true

        for(String activityCode : activities) {
            if(!contains(process, activityCode)) {
                hasAllActivities = false
                break
            }
        }

        return hasAllActivities
    }

    static boolean hasAtLeastOne(Process process, List<String> activities) {
        boolean hasAtLeastOneActivity = false

        for(String activity : activities) {
            if(contains(process, activity)) {
                hasAtLeastOneActivity = true
                break
            }
        }

        return hasAtLeastOneActivity
    }


    static boolean hasCombination(Process process, List<String> requiredActivities, List<String> optionalActivities) {
        return hasAtLeastOne(process, requiredActivities) && hasAtLeastOne(process, optionalActivities)
    }

    static boolean hasNoCombination(Process process, List<String> activites, List<String> excludingActivities) {
        return hasAtLeastOne(process, activites) && !hasAtLeastOne(process, excludingActivities)
    }

    static boolean isCalculatorRedundant(Process process) {
        List<String> activitiesWithoutCalculator = [WYMIANA_TERMINALA, PAKIET_START, PAKIET_START_PLUS,
                PAKIET_MOBILNY, DODATKOWY_PUNKT, DODATKOWY_POS]
        List<String> pointAndPosCombo = Lists.newArrayList(DODATKOWY_PUNKT, DODATKOWY_POS)

        return activitiesWithoutCalculator.any{isOnlyActivity(process, it)} || containsOnly(process, pointAndPosCombo)
    }

    static boolean isClientRedundant(Process process) {
        return isBundleActivity(process)
    }

    static boolean isBundleActivity(Process process) {
        List<String> bundleActivities = ["pakietStart", "pakietStartPlus", "pakietMobilny"]

        return bundleActivities.any{isOnlyActivity(process, it)}
    }
}
