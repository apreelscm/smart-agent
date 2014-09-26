package com.eservice.eumowy


class MandatorySignaturesResolver extends SignaturesResolver {
    public MandatorySignaturesResolver(Process process, Activity activity) {
        super(process, activity)
    }

    @Override
    Set<ActivitySignatures> resolve() {
        Set<ActivitySignatures> activitySignaturesFromList = activity.activitySignatures.findAll {
            it.mandatory && it.signature.active
        }

        return activitySignaturesFromList.findAll {it.requiredActivities == getRequiredActivities(activitySignaturesFromList)}
    }
}
