package com.eservice.eumowy

import java.awt.image.BufferedImage

import signaturepad.SignatureToImage

class SubscriptionController {
	
	def appParametersService
	
    def index() {
		
	}
	
	def inlineview() {
		
	}
	
	def saveSubscription() {
		
		if (params.content.length() < 2000) {
			render(text: "{\"status\": \"FAIL\", \"text\": \"Niewyraźny podpis. Proszę spróbować jeszcze raz.\"}")
			return
		}
		
		def subscription = new Subscription(params)
		subscription.signDate = new Date()
		subscription.save(flush: true)
		
        BufferedImage img = SignatureToImage.convertJsonToImage(subscription.content)

		/* We don't need it for now
		
		File outputfile = new File(appParametersService.getSubscriptionsPath()+subscription.getFileName())
		ImageIO.write(img, "png", outputfile)
		
		*/

		if (subscription?.id != null) {
			render(text: "{\"status\": \"OK\", \"subscriptionId\": " + subscription.id + "}")
		}
		else {
			render(text: "{\"status\": \"FAIL\", \"text\": \"Nie udało się zapisać podpisu do bazy!\"}")
		}
	}
	
	def preview() {
		Subscription sign = Subscription.last()
		render(view: "preview", model: [signature: sign])
	}
	
}
