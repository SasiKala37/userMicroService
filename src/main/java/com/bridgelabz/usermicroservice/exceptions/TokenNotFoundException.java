package com.bridgelabz.usermicroservice.exceptions;

public class TokenNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public TokenNotFoundException(String message) {
		super(message);
	}
}
