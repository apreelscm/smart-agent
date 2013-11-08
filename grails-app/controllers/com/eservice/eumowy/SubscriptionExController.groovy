package com.eservice.eumowy

class SubscriptionExController {

    def index() { }
	
	def saveSubscription() {
		log.info "CONTENT: " + params.content
		def subscription = new Subscription(params)
		subscription.signDate = new Date()
		subscription.save(flush: true)

		if (subscription?.id != null) {
			render(text: "{\"status\": \"OK\", \"subscriptionId\": " + subscription.id + "}")
		} else {
			render(text: "{\"status\": \"FAIL\", \"text\": \"Nie udało się zapisać podpisu do bazy!\"}")
		}
	}
}
