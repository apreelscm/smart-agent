package com.eservice.eumowy

import com.eservice.eumowy.Representative
import com.eservice.eumowy.Subscription
import com.eservice.eumowy.Process

class SubscriptionService {

    Subscription getRepresentativeSubscription(Process process, Representative representative) {
        Subscription subscription = process.subscriptions.find{it.name.equals(representative?.imie) && it.surname.equals(representative?.nazwisko)}

        if(!subscription) {
            log.error(String.format("Cannot find subscription for representative with id: %s", representative.id))
            return null
        }

        return subscription
    }
}
