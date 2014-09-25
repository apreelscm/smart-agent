package com.eservice.eumowy

import com.google.common.collect.Lists

class ActivitySignaturesResolver {
    private Process process
    private Activity activity
    private int listNumber

    public ActivitySignaturesResolver(Process process, Activity activity, int listNumber) {
        this.process = process
        this.activity = activity
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

            if(ActivityHelper.containsOnly(process, activities)) {
                return signature.requiredActivities
            }
        }

        return null
    }
}
