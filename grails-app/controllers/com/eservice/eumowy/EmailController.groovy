package com.eservice.eumowy

class EmailController {
	
	def emailService

    def index() { }
	
	def send() {
		def email = [
			from: 'apreel.eUmowy@gmail.com',
			to: 'apreel.eUmowy@gmail.com',
			subject: '[eUmowy]',
			text: params.content
		]
		emailService.sendEmail(email)
		redirect(action: "index")
	}
}
