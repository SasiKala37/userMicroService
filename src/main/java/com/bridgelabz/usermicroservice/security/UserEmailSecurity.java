package com.bridgelabz.usermicroservice.security;

import javax.mail.MessagingException;

import com.bridgelabz.usermicroservice.model.MailDTO;

public interface UserEmailSecurity {
	public void sendEmail(MailDTO mailDTO) throws MessagingException;
}
