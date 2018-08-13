package com.bridgelabz.usermicroservice.rabbitmq;

import com.bridgelabz.usermicroservice.model.MailDTO;

public interface IProducer {
	public void produceMsg(MailDTO mailDTO);
}
