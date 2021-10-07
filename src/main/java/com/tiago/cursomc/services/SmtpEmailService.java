package com.tiago.cursomc.services;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class SmtpEmailService extends AbstractEmailService{

	@Autowired
	private MailSender emailSender;
	
	@Autowired
	private JavaMailSender jaSender;
	
	private static final Logger LOG = LoggerFactory.getLogger(SmtpEmailService.class);
	
	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOG.info("Envio de email...");
		emailSender.send(msg);
		LOG.info("Email enviado..");
		
	}


	@Override
	public void sendHtmlEmail(MimeMessage msg) {
		LOG.info("Envio de email...");
		jaSender.send(msg);
		LOG.info("Email enviado..");
		
	}

}
