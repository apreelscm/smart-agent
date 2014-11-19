package com.eservice.eumowy

import com.google.common.collect.Lists


class MandatorySignaturesResolver extends SignaturesResolver {
    public MandatorySignaturesResolver(Process process, Activity activity) {
        super(process, activity)
    }

    @Override
    List<ActivitySignatures> resolve() {
        List<ActivitySignatures> activitySignaturesFromList = Lists.newArrayList(activity.activitySignatures.findAll {
            it.mandatory && it.signature.active
        })

        return activitySignaturesFromList.findAll {it.requiredActivities == getRequiredActivities(activitySignaturesFromList)}
    }
}
