package com.eservice.eumowy

import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage

class EmailService {

	boolean transactional = false
	MailSender mailSender

	def sendEmail(email) {
		SimpleMailMessage smm = new SimpleMailMessage()
		smm.setFrom(email.from)
		smm.setTo(email.to)
		smm.setSubject(email.subject)
		smm.setText(email.text)
		log.info("Sending email from " + smm.getFrom() + " to " + smm.getTo() + " with subject " + smm.getSubject())
		mailSender.send(smm)
	}
}
