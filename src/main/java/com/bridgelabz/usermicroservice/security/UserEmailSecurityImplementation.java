package com.bridgelabz.usermicroservice.security;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.bridgelabz.usermicroservice.model.MailDTO;

@Component
public class UserEmailSecurityImplementation implements UserEmailSecurity {
	 @Autowired
	    private JavaMailSender emailSender;
	 	@Override
	    public void sendEmail(MailDTO mailDTO) throws MessagingException {
	    
	    MimeMessage mimeMessage=emailSender.createMimeMessage();
	    MimeMessageHelper message=new MimeMessageHelper(mimeMessage);
	    
	        message.setTo(mailDTO.getToMailAddress());
	        message.setSubject(mailDTO.getSubject());
	        message.setText(mailDTO.getBody()+"\n");
	        emailSender.send(mimeMessage);      
	    }
}
