package com.bridgelabz.usermicroservice.services;

public interface SocialServices {

	String getName();

	String createFacebookAuthorizationURL();

	void createFacebookAccessToken(String code);
}
