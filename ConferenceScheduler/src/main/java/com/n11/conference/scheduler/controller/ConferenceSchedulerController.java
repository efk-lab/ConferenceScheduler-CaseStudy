package com.n11.conference.scheduler.controller;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.n11.conference.scheduler.error.ConferenceSchedulerException;
import com.n11.conference.scheduler.model.GetConferenceRequest;
import com.n11.conference.scheduler.model.GetConferenceResponse;
import com.n11.conference.scheduler.model.ScheduleConferenceRequest;
import com.n11.conference.scheduler.model.ScheduleConferenceResponse;

@RequestMapping("/conference-scheduler")
@CrossOrigin("*")
@RestController
public interface ConferenceSchedulerController {

	@RequestMapping(
			value = "/conference", 
			method = RequestMethod.POST,
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
	public ResponseEntity<ScheduleConferenceResponse> scheduleConference(@Valid @RequestBody ScheduleConferenceRequest scheduleConferenceRequest) throws ConferenceSchedulerException;
	
	
	@RequestMapping(
			value = "/conference-details", 
			method = RequestMethod.POST,
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
	public ResponseEntity<GetConferenceResponse> getConference(@Valid @RequestBody GetConferenceRequest getConferenceRequest) throws ConferenceSchedulerException;
	
	
	
}
