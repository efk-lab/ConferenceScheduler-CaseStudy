package com.n11.conference.scheduler.validator;

import com.n11.conference.scheduler.error.ConferenceSchedulerException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
abstract class BaseValidator {
	
	protected void validateRequest(Object request) {

		if (request == null) {
			log.error("Error during validation of request. Details: Request cannot be null.");
			throw new ConferenceSchedulerException("Request cannot be null.");
		}

	}

}
