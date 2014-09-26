package com.eservice.eumowy

import com.google.common.collect.Lists

class SignaturesResolver {
    protected Process process
    protected Activity activity
    protected int listNumber

    public SignaturesResolver(Process process, Activity activity) {
        this.process = process
        this.activity = activity
        this.listNumber = 0
    }

    public SignaturesResolver(Process process, Activity activity, int listNumber) {
        this(process, activity)
        this.listNumber = listNumber
    }

    public Set<ActivitySignatures> resolve() {
        Set<ActivitySignatures> activitySignaturesFromList = activity.activitySignatures.findAll {
            it.numberOfList == listNumber && it.signature.active
        }

        return activitySignaturesFromList.findAll {it.requiredActivities == getRequiredActivities(activitySignaturesFromList)}
    }

    protected String getRequiredActivities(Set<ActivitySignatures> signatures) {
        for (ActivitySignatures signature : signatures) {
            if(!signature.requiredActivities) continue

            List<String> activities = Lists.newArrayList(activity.code)
            activities.addAll(signature.requiredActivities.split(","))

            if(ActivityHelper.containsAll(process, activities)) {
                return signature.requiredActivities
            }
        }

        return null
    }
}
