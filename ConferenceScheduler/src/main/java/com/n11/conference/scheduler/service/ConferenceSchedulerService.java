package com.n11.conference.scheduler.service;

import static com.n11.conference.scheduler.constant.ConferenceSchedulerConstants.DURATION_AFTER_LUNCH_IN_MINUTE;
import static com.n11.conference.scheduler.constant.ConferenceSchedulerConstants.DURATION_BEFORE_LUNCH_IN_MINUTE;
import static com.n11.conference.scheduler.constant.ConferenceSchedulerConstants.DURATION_LUNCH_IN_MINUTE;
import static com.n11.conference.scheduler.constant.ConferenceSchedulerConstants.EVENT_LUNCH;
import static com.n11.conference.scheduler.constant.ConferenceSchedulerConstants.EVENT_NETWORKING;
import static com.n11.conference.scheduler.constant.ConferenceSchedulerConstants.GAP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.n11.conference.scheduler.document.Conference;
import com.n11.conference.scheduler.error.ConferenceSchedulerException;
import com.n11.conference.scheduler.mapper.ConferenceMapper;
import com.n11.conference.scheduler.model.ConferenceEventRequest;
import com.n11.conference.scheduler.model.GetConferenceRequest;
import com.n11.conference.scheduler.model.GetConferenceResponse;
import com.n11.conference.scheduler.model.ScheduleConferenceRequest;
import com.n11.conference.scheduler.model.ScheduleConferenceResponse;
import com.n11.conference.scheduler.repository.ConferenceRepository;
import com.n11.conference.scheduler.service.parameters.ScheduleParameters;
import com.n11.conference.scheduler.validator.ConferenceValidator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConferenceSchedulerService {

	@Autowired
	private ConferenceValidator conferenceValidator;
	
	@Autowired
	private ConferenceRepository conferenceRepository;

	@Autowired
	private ConferenceMapper conferenceMapper;
	
	
	public ScheduleConferenceResponse scheduleConference(ScheduleConferenceRequest scheduleConferenceRequest) throws ConferenceSchedulerException {
		
		conferenceValidator.validateScheduleConferenceRequest(scheduleConferenceRequest);
		
		List<ConferenceEventRequest> conferenceEventRequests = conferenceMapper.toConferenceEventRequests(scheduleConferenceRequest.getConferenceEventRequests());
		
		List<List<ConferenceEventRequest>> scheduledConferenceEvents = scheduleConferenceEventRequests(conferenceEventRequests);
		
		Conference conference = conferenceMapper.toConference(scheduledConferenceEvents);
		Conference scheduledConference = conferenceRepository.save(conference);
		ScheduleConferenceResponse scheduledConferenceResponse = conferenceMapper.toScheduleConferenceResponse(scheduledConference);
		
		log.info("Conference saved. ScheduleConferenceResponse: " + scheduledConferenceResponse.toString());

		
		return scheduledConferenceResponse;
		
	}
	
	
	private List<List<ConferenceEventRequest>> scheduleConferenceEventRequests(List<ConferenceEventRequest> conferenceEventRequests) {

		List<List<ConferenceEventRequest>> schedule = new ArrayList<>();

		while (conferenceEventRequests.size() > 0) {
			Map<Integer, Integer> eventDurationCounts = new HashMap<>();

			conferenceEventRequests.stream().forEach(scheduleConferenceRequest -> eventDurationCounts.put(scheduleConferenceRequest.getEventDuration(),
					eventDurationCounts.getOrDefault(scheduleConferenceRequest.getEventDuration(), 0) + 1));

			Map<Integer, Integer> sortedEventDurationCounts = eventDurationCounts.entrySet().stream().sorted(Map.Entry.<Integer, Integer>comparingByKey().reversed())
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

			List<ConferenceEventRequest> scheduledRequests = new ArrayList<ConferenceEventRequest>();

			ScheduleParameters scheduleParameters = new ScheduleParameters(scheduledRequests,conferenceEventRequests,sortedEventDurationCounts);
			
			scheduleBeforeLunch(scheduleParameters);
			scheduleAfterLunch(scheduleParameters);

			schedule.add(scheduledRequests);

		}

		return schedule;
	}
	
	private static void scheduleBeforeLunch(ScheduleParameters scheduleParameters) {
		
		List<ConferenceEventRequest> scheduledRequests = scheduleParameters.getScheduledRequests();
		scheduleParameters.setDuration(DURATION_BEFORE_LUNCH_IN_MINUTE);
		int remainingDurationBeforeLunch = scheduleConferenceEvents(scheduleParameters);

		if (remainingDurationBeforeLunch > 0) {
			scheduledRequests.add(new ConferenceEventRequest(GAP, remainingDurationBeforeLunch));
		}
		scheduledRequests.add(ConferenceEventRequest.builder().eventName(EVENT_LUNCH).eventDuration(DURATION_LUNCH_IN_MINUTE).build());

	}
	
	private static void scheduleAfterLunch(ScheduleParameters scheduleParameters) {
		List<ConferenceEventRequest> scheduledRequests = scheduleParameters.getScheduledRequests();
		scheduleParameters.setDuration(DURATION_AFTER_LUNCH_IN_MINUTE);
		int remainingDurationBeforeNetworking = scheduleConferenceEvents(scheduleParameters);

		scheduledRequests.add(ConferenceEventRequest.builder().eventName(EVENT_NETWORKING).eventDuration(remainingDurationBeforeNetworking).build());
	}

	private static int scheduleConferenceEvents(ScheduleParameters scheduleParameters) {

		int remainingDuration = scheduleParameters.getDuration();
		List<ConferenceEventRequest> scheduledRequests = scheduleParameters.getScheduledRequests();
		List<ConferenceEventRequest> conferenceEventRequests = scheduleParameters.getScheduleConferenceRequests();
		Map<Integer, Integer> eventDurationCounts = scheduleParameters.getEventDurationCounts();
		
		for (Map.Entry<Integer, Integer> eventDurationCount : eventDurationCounts.entrySet()) {
			int eventDuration = eventDurationCount.getKey();
			int eventCount = eventDurationCount.getValue();
			
			while (remainingDuration >= eventDuration && eventCount > 0) {
				conferenceEventRequests.stream()
										.filter(conferenceEventRequest -> conferenceEventRequest.getEventDuration() == eventDuration)
										.findFirst().ifPresent(conferenceEventRequest -> scheduledRequests.add(conferenceEventRequest));

				conferenceEventRequests.removeAll(scheduledRequests);

				remainingDuration -= eventDuration;
				eventCount--;
				eventDurationCount.setValue(eventCount);		
			}
		}

		return remainingDuration;
	}
	
	public GetConferenceResponse getConference(GetConferenceRequest getConferenceRequest) throws ConferenceSchedulerException {
		
		conferenceValidator.validateGetConference(getConferenceRequest);
		
		Optional<Conference>  retrievedConference = conferenceRepository.findById(getConferenceRequest.getConferenceId());
		GetConferenceResponse getConferenceResponse = conferenceMapper.toGetConferenceResponse(retrievedConference.orElseThrow());
		
		log.info("Conference retrieved. GetConferenceResponse: " + getConferenceResponse.toString());

		return getConferenceResponse;
	
	}
	
}
