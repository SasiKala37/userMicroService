 package com.bridgelabz.usermicroservice.controllers;

import javax.mail.MessagingException;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.usermicroservice.exceptions.RegistrationException;
import com.bridgelabz.usermicroservice.exceptions.TokenNotFoundException;
import com.bridgelabz.usermicroservice.exceptions.UserActivationException;
import com.bridgelabz.usermicroservice.exceptions.UserNotFoundException;
import com.bridgelabz.usermicroservice.model.LoginDTO;
import com.bridgelabz.usermicroservice.model.RegistrationDTO;
import com.bridgelabz.usermicroservice.model.ResetPasswordDTO;
import com.bridgelabz.usermicroservice.model.ResponseDTO;
import com.bridgelabz.usermicroservice.services.SocialServices;
import com.bridgelabz.usermicroservice.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	public static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;
	
	@Autowired 
	private SocialServices socialServices;

	@PostMapping("/register")
	public ResponseEntity<ResponseDTO> registerUser(@RequestBody RegistrationDTO registerDTO,
			HttpServletRequest request) throws RegistrationException, UserActivationException, MessagingException {

		logger.info("Creating User ");

		userService.registerUser(registerDTO, request.getRequestURI());

		ResponseDTO response = new ResponseDTO();
		response.setMessage("Registered successfully");
		response.setStatus(2);

		logger.info("Reponse message ", response);

		return new ResponseEntity<>(response, HttpStatus.CREATED);

	}

	@PostMapping("/login")
	public ResponseEntity<ResponseDTO> loginUser(@RequestBody LoginDTO loginDTO, HttpServletResponse response)
			throws LoginException, MessagingException, UserActivationException, RegistrationException {
		logger.info("login  User");

		String token = userService.loginUser(loginDTO);

		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setMessage("Login successfully");
		responseDTO.setStatus(3);
		response.setHeader("token", token);
		logger.info("Response message:", responseDTO);

		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@GetMapping("/activation")
	public ResponseEntity<ResponseDTO> activationUser(@RequestParam("token") String token)
			throws UserActivationException {
		logger.info("check the user activation");

		userService.setActivationStatus(token);

		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setMessage("User activated successfully");

		return new ResponseEntity<>(responseDTO, HttpStatus.OK);

	}

	@PostMapping("/forgotPassword")
	public ResponseEntity<ResponseDTO> forgotPassword(@RequestBody String emailId, HttpServletRequest request)
			throws RegistrationException, MessagingException {
		logger.info("Reset the password");

		userService.forgotPassword(emailId, request.getRequestURI());

		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setMessage("sent mail to your registered mailId for reset password");

		return new ResponseEntity<>(responseDTO, HttpStatus.OK);

	}

	@PostMapping("/resetPassword")
	public ResponseEntity<ResponseDTO> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO,
			@RequestParam("token") String token)
			throws UserActivationException, RegistrationException, TokenNotFoundException, UserNotFoundException {

		userService.resetPassword(resetPasswordDTO, token);

		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setMessage("reset the password successfully");
		logger.info("Reset password done successfully");

		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}
	
	@GetMapping("/createFacebookAuthorization")
    public String createFacebookAuthorization() {
        return socialServices.createFacebookAuthorizationURL();
    }

    @GetMapping("/getName")
    public String getNameResponse() {
        return socialServices.getName();
    }

    @GetMapping("/facebook")
    public void createFacebookAccessToken(@RequestParam("code") String code) {
        socialServices.createFacebookAccessToken(code);
    }

}