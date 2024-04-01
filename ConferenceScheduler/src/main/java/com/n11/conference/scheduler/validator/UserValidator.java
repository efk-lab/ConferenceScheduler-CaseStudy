package com.n11.conference.scheduler.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.n11.conference.scheduler.error.ConferenceSchedulerException;
import com.n11.conference.scheduler.model.SignUpRequest;
import com.n11.conference.scheduler.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserValidator extends BaseValidator{
	
	@Autowired
	private UserRepository userRepository;

	public void validateSignUpRequest(SignUpRequest signUpRequest) throws ConferenceSchedulerException {

		validateRequest(signUpRequest);
		
		if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
			throw new ConferenceSchedulerException("User already registed.");
		}

		log.info("SignUpRequest validated. SignUpRequest: " + signUpRequest.toString());

	}
	
}
