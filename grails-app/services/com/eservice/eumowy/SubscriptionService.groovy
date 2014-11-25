package com.eservice.eumowy

class SubscriptionService {

    public Subscription getRepresentativeSubscription(Process process, Representative representative) {
        Subscription subscription = process.subscriptions.find{it.name.equals(representative?.name) && it.surname.equals(representative?.surname)}

        if(!subscription) {
            log.error(String.format("Cannot find subscription for representative with id: %s", representative.id))
            return null
        }

        return subscription
    }

    public void deleteSubscriptionsFromProcess(Process process) {
        List<Long> subscriptionIds = Subscription.executeQuery("SELECT id FROM Subscription WHERE process.id = :processId " +
                "OR uniqueKey LIKE upper(:uniqueKey)", [processId: process.id, uniqueKey: process.id + "%"]
        )

        if (subscriptionIds.size() > 0) {
            Subscription.executeUpdate("delete Subscription where id in (:list)",[list: subscriptionIds])
        }

        process.subscriptions?.clear()
    }

    public int getRequiredSubscriptionsCount(Process process) {
        int count = 1 //one subscription is always required for PH
        int representativesCount = process.representatives?.size()

        if(!representativesCount) return count

        boolean isFirstAcceptorSubscriptionDefinitionPresent = process.signatures.any {
            it.subscriptionDefinitions.any {Subscription.PersonRole.ACCEPTANT1.equals(it.role)}
        }
        boolean isSecondAcceptorSubscriptionDefinitionPresent = process.signatures.any {
            it.subscriptionDefinitions.any {Subscription.PersonRole.ACCEPTANT2.equals(it.role)}
        }

        if(isFirstAcceptorSubscriptionDefinitionPresent && representativesCount >= 1) {
            count++
        }

        if(isSecondAcceptorSubscriptionDefinitionPresent && representativesCount == 2) {
            count++
        }

        return count
    }
}
