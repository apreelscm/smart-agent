package com.eservice.eumowy

class SignatureService {

    Set<ActivitySignatures> getActivitySignatures(Process process, Activity activity, int listNumber) {
        Set<ActivitySignatures> activeSignaturesFromList = activity.activitySignatures.findAll {
            it.numberOfList == listNumber && it.signature.active
        }

        Set<ActivitySignatures> activitySignaturesWithRequiredActivity = activeSignaturesFromList.findAll{
            isSignatureFulfillRequirements(it, process)
        }

        if(activitySignaturesWithRequiredActivity.size() > 0) {
            return activitySignaturesWithRequiredActivity
        }

        return activeSignaturesFromList.findAll {!it.required}
    }

    private boolean isSignatureFulfillRequirements(ActivitySignatures activitySignature, Process process) {
        if(!activitySignature.required) return false

        return process.activities.contains(activitySignature.required)
    }

    Set<ActivitySignatures> getMandatoryActivitySignatures(Activity activity) {
        return activity.activitySignatures.findAll {it.mandatory && it.signature.active}
    }
}
