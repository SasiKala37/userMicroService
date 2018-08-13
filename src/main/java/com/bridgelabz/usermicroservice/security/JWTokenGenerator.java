package com.bridgelabz.usermicroservice.security;

import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTokenGenerator {

	private JWTokenGenerator() {
		
	}
	private static final String KEY = "sasi";

	public static String createToken(String id) {

		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		Date startTime = new Date();
		Date expireTime = new Date(startTime.getTime() + (1000 * 60 * 60 * 24));

		JwtBuilder builder = Jwts.builder().setId(id).setIssuedAt(startTime).setExpiration(expireTime)
				.signWith(signatureAlgorithm, KEY);
		return builder.compact();
	}

	public static Claims parseJwt(String jwt) {

		return Jwts.parser().setSigningKey(KEY).parseClaimsJws(jwt).getBody();
	}
}
