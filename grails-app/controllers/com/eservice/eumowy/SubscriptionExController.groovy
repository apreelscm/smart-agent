package com.eservice.eumowy

class SubscriptionExController {

    def index() { }
	
	def saveSubscription() {

		def subscription = new Subscription(params)
		subscription.signDate = new Date()

        log.info "Saving subscription from ANDROID APPLICATION for role " + subscription.personRole.toString()
		subscription.save(flush: true)

		if (subscription?.id != null) {
            log.info "Subscription with id ${subscription.id} saved"
			render(text: "{\"status\": \"OK\", \"subscriptionId\": " + subscription.id + "}")
		} else {
            log.error "Error during saving subscription with id ${subscription?.id}"
            render(status: 503, text: "{\"status\": \"FAIL\", \"text\": \"Nie udało się zapisać podpisu do bazy!\"}")
		}
	}
}
