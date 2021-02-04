package com.trecapps.userservice.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class TrecEmailService
{
	@Autowired
	JavaMailSender mailSender;
	
	void sendValidationEmail(String to,
            String subject,
            String code) throws MessagingException
	{
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
		
		String url = code;
		
		String htmlEmailContent = "<h1>Please enter the code to Verify your account</h1><br><h2>"+ url +"</h2>";
		
		helper.setSubject(subject);
		helper.setText(htmlEmailContent, true);
		helper.setTo(to);
		mailSender.send(mimeMessage);
	}
}
