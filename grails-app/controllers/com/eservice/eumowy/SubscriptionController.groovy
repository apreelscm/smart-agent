package com.eservice.eumowy

import signaturepad.SignatureToImage

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

class SubscriptionController {
	
	def subscriptionService

    def index() {
		
	}
	
	def save() {
		Subscription signature = new Subscription(signature: params.signature)
		subscriptionService.save(signature)
		BufferedImage img = SignatureToImage.convertJsonToImage(signature.signature)
		File outputfile = new File("web-app/images/sign.png");
		ImageIO.write(img, "png", outputfile)
		redirect(action: "preview")
	}
	
	def preview() {
		Subscription sign = subscriptionService.getLast()
		render(view: "preview", model: [signature: sign])
	}
}
