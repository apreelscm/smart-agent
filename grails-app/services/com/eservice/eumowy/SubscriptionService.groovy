package com.eservice.eumowy

class SubscriptionService {

    public Subscription getRepresentativeSubscription(Process process, Representative representative) {
        Subscription subscription = process.subscriptions.find{it.name.equals(representative?.imie) && it.surname.equals(representative?.nazwisko)}

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
}
