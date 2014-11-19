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

    public List<ActivitySignatures> resolve() {
        List<ActivitySignatures> activitySignaturesFromList = Lists.newArrayList(activity.activitySignatures.findAll {
            it.numberOfList == listNumber && it.signature.active
        }).sort {it.requiredActivities?.split(",")?.length}.reverse(true)

        return activitySignaturesFromList.findAll {it.requiredActivities == getRequiredActivities(activitySignaturesFromList)}
    }

    protected String getRequiredActivities(List<ActivitySignatures> signatures) {
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
