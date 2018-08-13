package com.bridgelabz.usermicroservice.services;

import java.util.Optional;

import javax.mail.MessagingException;
import javax.security.auth.login.LoginException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bridgelabz.usermicroservice.exceptions.RegistrationException;
import com.bridgelabz.usermicroservice.exceptions.TokenNotFoundException;
import com.bridgelabz.usermicroservice.exceptions.UserActivationException;
import com.bridgelabz.usermicroservice.exceptions.UserNotFoundException;
import com.bridgelabz.usermicroservice.model.LoginDTO;
import com.bridgelabz.usermicroservice.model.MailDTO;
import com.bridgelabz.usermicroservice.model.RegistrationDTO;
import com.bridgelabz.usermicroservice.model.ResetPasswordDTO;
import com.bridgelabz.usermicroservice.model.User;
import com.bridgelabz.usermicroservice.rabbitmq.IProducer;
import com.bridgelabz.usermicroservice.repository.RedisRepository;
import com.bridgelabz.usermicroservice.repository.UserRepository;
import com.bridgelabz.usermicroservice.security.JWTokenGenerator;
import com.bridgelabz.usermicroservice.util.Utility;

import io.jsonwebtoken.Claims;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserServiceImplementation implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private IProducer producer;

	@Autowired
	private Environment environment;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private RedisRepository redisRepository;

	@Override
	public void registerUser(RegistrationDTO registerDTO, String uri)
			throws RegistrationException, MessagingException {

		Utility.isValidateAllFields(registerDTO);

		Optional<User> optionalUser = userRepository.findByEmailId(registerDTO.getEmailId());

		if (optionalUser.isPresent()) {
			throw new RegistrationException("User already registerwith this email id ");
		}

		registerDTO.setPassword(encoder.encode(registerDTO.getPassword()));

		User user = modelMapper.map(registerDTO, User.class);

		userRepository.save(user);

		Optional<User> optionalUser1 = userRepository.findByEmailId(registerDTO.getEmailId());
		if (!optionalUser1.isPresent()) {
			throw new RegistrationException("user doesnot save in the database");
		}
		sendEmailMessage(uri, optionalUser1);

	}

	@Override
	public String loginUser(LoginDTO loginDTO)
			throws LoginException, MessagingException, RegistrationException {

		if (!Utility.validateEmailAddress(loginDTO.getEmailId())) {
			throw new LoginException("Email not valid");
		}

		if (!Utility.validatePassword(loginDTO.getPassword())) {
			throw new LoginException("Password not valid");
		}

		Optional<User> optionalUser = userRepository.findByEmailId(loginDTO.getEmailId());

		if (!optionalUser.isPresent()) {
			throw new LoginException("User not found Exception");
		}

		if (!encoder.matches(loginDTO.getPassword(), optionalUser.get().getPassword())) {
			throw new LoginException("Incorrect password exception");
		}

		if (!optionalUser.get().isActivate()) {
			throw new LoginException("User not in activation");
		}
		return JWTokenGenerator.createToken(optionalUser.get().getId());

	}

	@Override
	public void forgotPassword(String emailId, String uri) throws RegistrationException, MessagingException {

		Optional<User> optionalUser = userRepository.findByEmailId(emailId);
		if (!optionalUser.isPresent()) {
			throw new RegistrationException("User doesnot exist Exception");
		}

		String token = Utility.generateUUID();

		redisRepository.save(token, optionalUser.get().getId());

		MailDTO mailDTO = new MailDTO();

		mailDTO.setToMailAddress(optionalUser.get().getEmailId());
		mailDTO.setSubject(" Verification mail");
		mailDTO.setBody(environment.getProperty("link") + uri + "?token=" + token);

		producer.produceMsg(mailDTO);
	}

	private void sendEmailMessage(String uri, Optional<User> optionalUser) {

		String token = JWTokenGenerator.createToken(optionalUser.get().getId());

		MailDTO mailDTO = new MailDTO();

		mailDTO.setToMailAddress(optionalUser.get().getEmailId());
		mailDTO.setSubject(" Verification mail");
		mailDTO.setBody(environment.getProperty("link") + uri + "?token=" + token);

		producer.produceMsg(mailDTO);
	}

	@Override
	public void setActivationStatus(String token) throws UserActivationException {

		Claims claim = JWTokenGenerator.parseJwt(token);

		Optional<User> optionalUser = userRepository.findById(claim.getId());

		if (!optionalUser.isPresent()) {
			throw new UserActivationException("User not found Exception");
		}

		User user = optionalUser.get();
		user.setActivate(true);
		userRepository.save(user);
	}

	@Override
	public void resetPassword(ResetPasswordDTO resetPasswordDTO, String token)
			throws UserActivationException, RegistrationException, TokenNotFoundException, UserNotFoundException {

		if (!Utility.validatePassword(resetPasswordDTO.getNewPassword())) {
			throw new RegistrationException("password not valid Exception");
		}

		if (!Utility.isPasswordMatch(resetPasswordDTO.getNewPassword(), resetPasswordDTO.getConfirmPassword())) {
			throw new RegistrationException("confirm password mismatch with new password Exception");
		}

		String userId = redisRepository.find(token);
		if (userId.isEmpty()) {
			throw new NullPointerException("token is null");
		}
		Optional<User> optionalUser = userRepository.findById(userId);

		if (!optionalUser.isPresent()) {
			throw new UserNotFoundException("user not found");
		}

		User user = optionalUser.get();
		user.setPassword(encoder.encode(resetPasswordDTO.getNewPassword()));
		userRepository.save(user);

		redisRepository.delete(token);
		
	}
}
