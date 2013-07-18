package com.eservice.eumowy

import java.awt.image.BufferedImage

import javax.imageio.ImageIO

import signaturepad.SignatureToImage


class SignatureController {
	
	def signatureService

    def index() {
		
	}
	
	def save() {
		Signature signature = new Signature(signature: params.signature)
		signatureService.save(signature)
		BufferedImage img = SignatureToImage.convertJsonToImage(signature.signature)
		File outputfile = new File("web-app/images/sign.png");
		ImageIO.write(img, "png", outputfile)
		redirect(action: "preview")
	}
	
	def preview() {
		Signature sign = signatureService.getLast()
		render(view: "preview", model: [signature: sign])
	}
}
