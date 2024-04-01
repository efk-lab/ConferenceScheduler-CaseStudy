package com.n11.conference.scheduler.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.n11.conference.scheduler.error.ConferenceSchedulerException;
import com.n11.conference.scheduler.model.ConferenceEventRequest;
import com.n11.conference.scheduler.model.GetConferenceRequest;
import com.n11.conference.scheduler.model.ScheduleConferenceRequest;
import com.n11.conference.scheduler.repository.ConferenceRepository;

import lombok.extern.slf4j.Slf4j;

import static com.n11.conference.scheduler.constant.ConferenceSchedulerConstants.EVENT_LIGHTNING;

@Component
@Slf4j
public class ConferenceValidator extends BaseValidator {
	
	@Autowired
	private ConferenceRepository conferenceRepository;

	
	public void validateScheduleConferenceRequest(ScheduleConferenceRequest scheduleConferenceRequest) throws ConferenceSchedulerException {
		
		validateRequest(scheduleConferenceRequest);
			
		List<ConferenceEventRequest> conferenceEventRequests = scheduleConferenceRequest.getConferenceEventRequests();
		for(ConferenceEventRequest conferenceEventRequest : conferenceEventRequests) {
			if(!conferenceEventRequest.getEventName().toLowerCase().contains(EVENT_LIGHTNING) && conferenceEventRequest.getEventDuration() == null) {
				throw new ConferenceSchedulerException("EventDuration cannot be enmpty. ConferenceEventRequest's Index:" + conferenceEventRequests.indexOf(conferenceEventRequest));
			}
		}
		
		log.info("ScheduleConferenceRequest validated. ScheduleConferenceRequest: " + scheduleConferenceRequest.toString());
		
	}
	
	public void validateGetConference(GetConferenceRequest getConferenceRequest) throws ConferenceSchedulerException {
		
		validateRequest(getConferenceRequest);
		
		if(getConferenceRequest.getConferenceId() == null) {
			throw new ConferenceSchedulerException("ConferenceId cannot be empty.");
		}
			
		if(conferenceRepository.findById(getConferenceRequest.getConferenceId()).isEmpty()) {
			throw new ConferenceSchedulerException("Conference cannot be found.");
		}

		log.info("GetConferenceRequest validated. GetConferenceRequest: " + getConferenceRequest.toString());
		
	}
	
}
