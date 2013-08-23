package com.eservice.eumowy

import signaturepad.SignatureToImage

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

class SubscriptionController {
	
	def appParametersService
	
    def index() {
		
	}
	
	def inlineview() {
		
	}
	
	def saveSubscription() {

        //TODO
        params.name = "testName";
        params.surname =  "testSurname";

		def subscription = new Subscription(params).save();
		
        BufferedImage img = SignatureToImage.convertJsonToImage(subscription.content)

		File outputfile = new File(appParametersService.getSubscriptionsPath()+"sign-"+subscription.name+"-"+subscription.surname+"-"+subscription.id+".png")
		ImageIO.write(img, "png", outputfile)
		//redirect(action: "preview")
		render(text: "{\"status\": \"OK\", \"subscriptionId\": " + subscription.id + "}")
	}
	
	def preview() {
		Subscription sign = Subscription.last()
		render(view: "preview", model: [signature: sign])
	}
}
