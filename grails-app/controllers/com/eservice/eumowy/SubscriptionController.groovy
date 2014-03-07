package com.eservice.eumowy

import grails.converters.JSON

class SubscriptionController {

    def refreshSubscription(){
        def uniqueKey = params.processId + params.role
        def subscription  = Subscription.find("from Subscription as s where s.uniqueKey=? order by s.signDate desc", [uniqueKey])

        def result = [:]

        if (subscription) {
            log.info "Saving subscription with id " + subscription?.id + " for processId: " + params.processId + " and role: " + params.role
            result.success = true
            result.subscriptionId = subscription.id
            render result as JSON
        } else {
            log.error "Error during saving subscription with id " + subscription?.id + " for processId: " + params.processId + " and role: " + params.role
            result.success = false
            result.text = "Nie udało się zapisać podpisu do bazy!"
            render result as JSON
        }
    }
	
	def preview() {
		Subscription sign = Subscription.last()
		render(view: "preview", model: [signature: sign])
	}
}
