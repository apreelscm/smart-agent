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

    static boolean isCalculatorRedundant(Process process) {
        List<String> activitiesWithoutCalculator = ["wymianaTerminala", "pakietStart", "pakietStartPlus", "pakietMobilny"]

        return activitiesWithoutCalculator.any{hasOnlyConcreteActivity(process, it)}
    }

    static boolean isClientRedundant(Process process) {
        List<String> activitiesWithoutClient = ["pakietStart", "pakietStartPlus", "pakietMobilny"]

        return activitiesWithoutClient.any{hasOnlyConcreteActivity(process, it)}
    }
}
