package com.n11.conference.scheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.n11.conference.scheduler.document.User;
import com.n11.conference.scheduler.error.ConferenceSchedulerException;
import com.n11.conference.scheduler.mapper.UserMapper;
import com.n11.conference.scheduler.model.SignUpRequest;
import com.n11.conference.scheduler.model.SignUpResponse;
import com.n11.conference.scheduler.repository.UserRepository;
import com.n11.conference.scheduler.validator.UserValidator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserRegistryService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserValidator userValidator;
	

	public SignUpResponse signUp(SignUpRequest signUpRequest) throws ConferenceSchedulerException {
		
		SignUpResponse signUpResponse = null;

		userValidator.validateSignUpRequest(signUpRequest);
		User user = userMapper.toUser(signUpRequest);
		User savedUser = userRepository.save(user);
		signUpResponse = userMapper.toSignUpResponse(savedUser);
		
		log.info("User saved. SignUpResponse: " + signUpResponse.toString());

		return signUpResponse;
		
	}
}
