package com.eservice.eumowy

import com.eservice.eumowy.Process

class ActivityHelper {
    static boolean isNewAgreement(Process process) {
        return containsActivity(process, "nowaUmowa")
    }

    static boolean hasOnlyConcreteActivity(Process process, String activityName) {
        return containsActivity(process, activityName) && process.activities.size() == 1
    }

    static boolean containsActivity(Process process, String activityCode) {
        return process.activities?.any{it.code.equals(activityCode)}
    }

    static boolean containsActivities(Process process, List<String> activities) {
        boolean hasAllActivities = true

        for(String activityCode : activities) {
            if(!containsActivity(process, activityCode)) {
                hasAllActivities = false
                break
            }
        }

        return hasAllActivities
    }

    static boolean isCalculatorRedundant(Process process) {
        List<String> activitiesWithoutCalculator = ["wymianaTerminala", "pakietStart", "pakietStartPlus", "pakietMobilny"]

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
