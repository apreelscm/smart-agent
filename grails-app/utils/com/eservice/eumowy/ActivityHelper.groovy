package com.eservice.eumowy

class ActivityHelper {
    static boolean isNewAgreement(Process process) {
        return contains(process, "nowaUmowa")
    }

    static boolean hasOnlyConcreteActivity(Process process, String activityName) {
        return contains(process, activityName) && process.activities.size() == 1
    }

    static boolean contains(Process process, String activityCode) {
        return process.activities?.any{it.code.equals(activityCode)}
    }

    static boolean contains(Process process, List<String> activities) {
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

    static boolean isCalculatorRedundant(Process process) {
        List<String> activitiesWithoutCalculator = ["wymianaTerminala", "pakietStart", "pakietStartPlus",
                "pakietMobilny", "dodatkowyPunkt", "dodatkowyPos"]

        return activitiesWithoutCalculator.any{hasOnlyConcreteActivity(process, it)}
    }

    static boolean isClientRedundant(Process process) {
        return isBundleActivity(process)
    }

    static boolean isBundleActivity(Process process) {
        List<String> bundleActivities = ["pakietStart", "pakietStartPlus", "pakietMobilny"]

        return bundleActivities.any{hasOnlyConcreteActivity(process, it)}
    }
}
