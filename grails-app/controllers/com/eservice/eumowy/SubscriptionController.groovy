package com.eservice.eumowy

import signaturepad.SignatureToImage

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

class SubscriptionController {
	
    def index() {
		
	}
	
	def inlineview() {
		
	}
	
	def saveSubscription() {
		def subscription = new Subscription(params).save();
		
        BufferedImage img = SignatureToImage.convertJsonToImage(subscription.content)

		File outputfile = new File("web-app/images/sign.png");
		ImageIO.write(img, "png", outputfile)
		//redirect(action: "preview")
		render(text: "OK")
	}
	
	def preview() {
		Subscription sign = Subscription.last()
		render(view: "preview", model: [signature: sign])
	}
}
