package com.n11.conference.scheduler.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.n11.conference.scheduler.controller.ConferenceSchedulerController;
import com.n11.conference.scheduler.error.ConferenceSchedulerException;
import com.n11.conference.scheduler.model.GetConferenceRequest;
import com.n11.conference.scheduler.model.GetConferenceResponse;
import com.n11.conference.scheduler.model.ScheduleConferenceRequest;
import com.n11.conference.scheduler.model.ScheduleConferenceResponse;
import com.n11.conference.scheduler.service.ConferenceSchedulerService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ConferenceSchedulerControllerImpl implements ConferenceSchedulerController {
	
	@Autowired
	private ConferenceSchedulerService conferenceSchedulerService;
	

	@Override
	public ResponseEntity<ScheduleConferenceResponse> scheduleConference(ScheduleConferenceRequest scheduleConferenceRequest) throws ConferenceSchedulerException {
	
		log.info("ScheduleConferenceRequest received: " + scheduleConferenceRequest.toString());

		ScheduleConferenceResponse scheduleConferenceResponse = conferenceSchedulerService.scheduleConference(scheduleConferenceRequest);

		if (scheduleConferenceResponse != null) {
			return new ResponseEntity<>(scheduleConferenceResponse, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(scheduleConferenceResponse, HttpStatus.NO_CONTENT);
		}
		
	}


	@Override
	public ResponseEntity<GetConferenceResponse> getConference(GetConferenceRequest getConferenceRequest) throws ConferenceSchedulerException {
		log.info("GetConferenceRequest received: " + getConferenceRequest.toString());

		GetConferenceResponse getConferenceResponse = conferenceSchedulerService.getConference(getConferenceRequest);

		if (getConferenceResponse != null) {
			return new ResponseEntity<>(getConferenceResponse, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(getConferenceResponse, HttpStatus.NO_CONTENT);
		}
		
	}

}
