package com.eservice.eumowy

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap

class SignatureService {

    Set<ActivitySignatures> getActivitySignatures(Process process, Activity activity, int listNumber) {
        return new SignaturesResolver(process, activity, listNumber).resolve()
    }

    Set<ActivitySignatures> getMandatoryActivitySignatures(Process process, Activity activity) {
        return new MandatorySignaturesResolver(process, activity).resolve()
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
