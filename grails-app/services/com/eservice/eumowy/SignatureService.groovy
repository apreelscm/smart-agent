package com.eservice.eumowy

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap

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

    Set<Signature> getProcessSignatures(Process process, GrailsParameterMap params) {
        Set<Signature> signatures = []

        process.activities.each() { activity ->
            String[] activitySignatureParam = params["activitySignature_${activity.id}"];

            if (activitySignatureParam) {
                List activitySignaturesIds = (activitySignatureParam.flatten().findAll { it != "null" && it != "" })

                List activitySignaturesIdsList = []
                activitySignaturesIds.each { activityId ->
                    def evalItem = Eval.me(activityId)
                    if (evalItem instanceof ArrayList) {
                        activitySignaturesIdsList.addAll(evalItem)
                    } else {
                        activitySignaturesIdsList.add(evalItem)
                    }
                }

                def activitySignatures = ActivitySignatures.findAllByIdInList(activitySignaturesIdsList.findResults { new Long(it) });

                if (!signatures.contains(activitySignatures*.signature)) {
                    signatures.addAll(activitySignatures.signature)
                }

                activity.selectedActivitySignatures = activitySignatures;
            }
        }

        return signatures
    }
}
