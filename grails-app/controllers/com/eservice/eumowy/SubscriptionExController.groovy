package com.eservice.eumowy

class SubscriptionExController {

    def index() { }
	
	def saveSubscription() {
        log.info "saving subscription"
		def subscription = new Subscription(params)
		subscription.signDate = new Date()
		subscription.save(flush: true)

		if (subscription?.id != null) { log.info "subscription with id ${subscription.id} saved"
			render(text: "{\"status\": \"OK\", \"subscriptionId\": " + subscription.id + "}")
		} else { log.error "error saving subscription with id ${subscription.id} "
            render(status: 503, text: "{\"status\": \"FAIL\", \"text\": \"Nie udało się zapisać podpisu do bazy!\"}")
		}
	}
}
