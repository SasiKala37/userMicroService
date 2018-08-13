package com.bridgelabz.usermicroservice.rabbitmq;

import javax.mail.MessagingException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bridgelabz.usermicroservice.model.MailDTO;
import com.bridgelabz.usermicroservice.security.UserEmailSecurity;
@Component
public class Consumer{
	@Autowired
	private UserEmailSecurity userEmailSecurity;
	@RabbitListener(queues = "${jsa.rabbitmq.queue}", containerFactory = "containerFactory")
	public void recievedMessage(MailDTO mailDTO ) throws MessagingException {
		System.out.println(mailDTO);
		userEmailSecurity.sendEmail(mailDTO);
	}
}
