package com.bridgelabz.usermicroservice.util;

import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bridgelabz.usermicroservice.exceptions.RegistrationException;
import com.bridgelabz.usermicroservice.model.LoginDTO;
import com.bridgelabz.usermicroservice.model.RegistrationDTO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class Utility {

	private Utility() {

	}

	public static void isValidateAllFields(RegistrationDTO registerDTO) throws RegistrationException {
		if (!validateEmailAddress(registerDTO.getEmailId())) {
			throw new RegistrationException("emailid not valid  Exception");
		} else if (!isValidUserName(registerDTO.getUserName())) {
			throw new RegistrationException("UserName Not valid  Exception");
		} else if (!validatePassword(registerDTO.getPassword())) {
			throw new RegistrationException("password not valid Exception");
		} else if (!isValidMobileNumber(registerDTO.getMobileNumber())) {
			throw new RegistrationException("mobilenumber not valid  Exception");
		} else if (!isPasswordMatch(registerDTO.getPassword(), registerDTO.getConfirmPassword())) {
			throw new RegistrationException("password mismatch exception");
		}
	}

	public static boolean validateEmailAddress(String emailId) throws RegistrationException {
		Pattern emailNamePtrn = Pattern
				.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		if (emailId.equals("") && emailId.equals("null")) {
			throw new RegistrationException("Email id should not be null");
		}
		Matcher mtch = emailNamePtrn.matcher(emailId);
		return mtch.matches();
		
	}

	public static boolean validatePassword(String password) throws RegistrationException {
		Pattern pattern = Pattern.compile("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}");
		if (password.equals("") && password.equals("null")) {
			throw new RegistrationException("Password should not be null");
		}
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	
	}

	public static boolean isValidUserName(String userName) throws RegistrationException {
		Pattern userNamePattern = Pattern.compile("^[a-z0-9_-]{6,14}$");
		if (userName.equals("") && userName.equals("null")) {
			throw new RegistrationException("username should not be null");
		}
		Matcher matcher = userNamePattern.matcher(userName);
		return matcher.matches();
		

	}

	public static boolean isValidMobileNumber(String mobileNumber) throws RegistrationException {
		Pattern mobileNumberPattern = Pattern.compile("\\d{10}");
		if (mobileNumber.equals("") && mobileNumber.equals("null")) {
			throw new RegistrationException("Mobile number should not be null");
		}
		Matcher matcher = mobileNumberPattern.matcher(mobileNumber);
		return matcher.matches();
		
	}

	public static boolean isPasswordMatch(String password, String confirmPassword) throws RegistrationException {
		if (confirmPassword != null && confirmPassword != "") {
			return password.equals(confirmPassword);
		}
		throw new RegistrationException("confirm password should not be null");
	}

	public String createToken(LoginDTO loginDTO) {
		
		final String key = "sasi";
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		String subject = loginDTO.getEmailId();
		Date date = new Date();

		JwtBuilder builder = Jwts.builder().setSubject(subject).setIssuedAt(date).signWith(signatureAlgorithm, key);
		return builder.compact();
	}

	/*public static String createToken(String id) {
		
		final String KEY = "sasi";
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		Date startTime = new Date();
		Date expireTime = new Date(startTime.getTime() + (1000 * 60 * 60 * 24));

		JwtBuilder builder = Jwts.builder().setId(id).setIssuedAt(startTime).setExpiration(expireTime)
				.signWith(signatureAlgorithm, KEY);
		return builder.compact();
	}

	public static Claims parseJwt(String jwt) {
		
		final String KEY = "sasi";
		return Jwts.parser().setSigningKey(KEY).parseClaimsJws(jwt).getBody();
	}*/
	
	public static String generateUUID() {
	       
        UUID gfg = UUID.randomUUID();
        return gfg.toString();
    }

}