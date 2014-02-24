package com.eservice.eumowy

class SubscriptionController {

    def refreshSubscription(){
        def uniqueKey = params.processId + params.role
        def subscription  = Subscription.find("from Subscription as s where s.uniqueKey=? order by s.signDate desc", [uniqueKey])

        log.info "Saving subscription with id " + subscription?.id + " for processId: " + params.processId + " and role: " + params.role

		if (subscription != null) {
	        if (subscription.id != null) {
	            render(text: "{\"status\": \"OK\", \"subscriptionId\": " + subscription.id + "}")
	        } else {
	            render(text: "{\"status\": \"FAIL\", \"text\": \"Nie udało się zapisać podpisu do bazy!\"}")
	        }
		}
    }
	
	def preview() {
		Subscription sign = Subscription.last()
		render(view: "preview", model: [signature: sign])
	}
}
