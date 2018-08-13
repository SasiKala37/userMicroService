package com.bridgelabz.usermicroservice.exceptions;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bridgelabz.usermicroservice.model.ResponseDTO;

@ControllerAdvice
public class GlobalUserExceptionHandler {

	private final Logger logger = LoggerFactory.getLogger(GlobalUserExceptionHandler.class);

	@ExceptionHandler(RegistrationException.class)
	public ResponseEntity<ResponseDTO> handleRegistrationException(RegistrationException exception) {
		logger.info("Error occured for: " + exception.getMessage(), exception);
		ResponseDTO response = new ResponseDTO();
		response.setMessage(exception.getMessage());
		response.setStatus(-2);
		// System.out.println("global");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(LoginException.class)
	public ResponseEntity<ResponseDTO> handleLoginActivationException(LoginException exception) {
		logger.info("Error occured: " + exception.getMessage(), exception);
		ResponseDTO response = new ResponseDTO();
		response.setMessage(exception.getMessage());
		response.setStatus(-3);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * 
	 * @param exception
	 * @param request
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseDTO> handleException(Exception exception, HttpServletRequest request) {
		logger.error("Error occured for: " + exception.getMessage(), exception);
		ResponseDTO response = new ResponseDTO();
		response.setMessage("Something went wrong");
		response.setStatus(-1);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
