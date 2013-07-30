package com.eservice.eumowy

import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage

class EmailService {

	boolean transactional = false

    final def DEFAULT_FROM_ADDRESS = 'apreel.eUmowy@gmail.com';
    final def DEFAULT_TO_ADDRESS = 'apreel.eUmowy@gmail.com';

	MailSender mailSender

	def sendEmail(email) {
        SimpleMailMessage smm = new SimpleMailMessage()
        smm.setFrom(email.from)
        smm.setTo(email.to)
        smm.setSubject(email.subject)
        smm.setText(email.text)

        log.info("Sending email from " + smm.getFrom() +
                " to " + smm.getTo() +
                " with subject " + smm.getSubject())

        mailSender.send(smm);
    }


    def sendSimpleMail(emailProperties) {
        assert emailProperties != null

        def message = new SimpleMailMessage();

        message.setFrom(emailProperties.from?:DEFAULT_FROM_ADDRESS)
        message.setTo(emailProperties.to?:DEFAULT_TO_ADDRESS)
        message.setSubject(emailProperties.subject)

        message.setText(emailProperties.text)

        log.info("Sending email from " + message.getFrom() +
                " to " + message.getTo() +
                " with subject " + message.getSubject())

        mailSender.send(message);
    }
}
