package com.n11.conference.scheduler.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.n11.conference.scheduler.controller.UserRegistryController;
import com.n11.conference.scheduler.error.ConferenceSchedulerException;
import com.n11.conference.scheduler.model.SignUpRequest;
import com.n11.conference.scheduler.model.SignUpResponse;
import com.n11.conference.scheduler.service.UserRegistryService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class UserRegistryControllerImpl implements UserRegistryController {

	@Autowired
	private UserRegistryService userRegistryService;
	

	@Override
	public ResponseEntity<SignUpResponse> signUp(SignUpRequest signUpRequest) throws ConferenceSchedulerException {
		
		log.info("SignUpRequest received: " + signUpRequest.toString());

		SignUpResponse signUpResponse = userRegistryService.signUp(signUpRequest);

		if (signUpResponse != null) {
			return new ResponseEntity<>(signUpResponse, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(signUpResponse, HttpStatus.NO_CONTENT);
		}
		
	}
}
