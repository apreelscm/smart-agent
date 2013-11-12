package com.eservice.eumowy

class SubscriptionController {

    private static int MIN_SIGNATURE_LENGHT = 700

	def saveSubscription() {
		
		if (params.nativeContent.length() < MIN_SIGNATURE_LENGHT) {
			render(text: "{\"status\": \"FAIL\", \"text\": \"Niewyraźny podpis. Proszę spróbować jeszcze raz.\"}")
			return
		}
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

    def refreshSubscription(){
        log.info "refreshSubscription params: " + params.content
        def uniqueKey = params.processId + params.role
        def subscription  = Subscription.findByUniqueKey(uniqueKey)
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
