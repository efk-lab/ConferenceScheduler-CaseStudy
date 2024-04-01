package com.n11.conference.scheduler.mapper;

import static com.n11.conference.scheduler.constant.ConferenceSchedulerConstants.CONFERENCE_START_TIME_NINE_AM;
import static com.n11.conference.scheduler.constant.ConferenceSchedulerConstants.DURATION_LIGHTNING_IN_MINUTE;
import static com.n11.conference.scheduler.constant.ConferenceSchedulerConstants.EVENT_LIGHTNING;
import static com.n11.conference.scheduler.constant.ConferenceSchedulerConstants.EVENT_LUNCH;
import static com.n11.conference.scheduler.constant.ConferenceSchedulerConstants.EVENT_NETWORKING;
import static com.n11.conference.scheduler.constant.ConferenceSchedulerConstants.GAP;
import static com.n11.conference.scheduler.constant.ConferenceSchedulerConstants.MINUTE_SIXTY;
import static com.n11.conference.scheduler.constant.ConferenceSchedulerConstants.NETWORKING_START_TIME_FOUR_PM;
import static com.n11.conference.scheduler.constant.ConferenceSchedulerConstants.ABBR_MINUTE;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.n11.conference.scheduler.document.Conference;
import com.n11.conference.scheduler.model.ConferenceEventRequest;
import com.n11.conference.scheduler.model.GetConferenceResponse;
import com.n11.conference.scheduler.model.ScheduleConferenceResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ConferenceMapper extends BaseMapper {
	
	public List<ConferenceEventRequest> toConferenceEventRequests(List<ConferenceEventRequest> conferenceEventRequests) {
		log.info("Mapping ConferenceEventRequest list to Conference. ConferenceEventRequests:" + conferenceEventRequests.toString());


		return conferenceEventRequests.stream()
				.map(conferenceEventRequest -> conferenceEventRequest.getEventName().toLowerCase().contains(EVENT_LIGHTNING)
						? new ConferenceEventRequest(conferenceEventRequest.getEventName(), DURATION_LIGHTNING_IN_MINUTE)
						: conferenceEventRequest)
				.collect(Collectors.toList());

	}

	public Conference toConference(List<List<ConferenceEventRequest>> conferenceEventRequests) {
		log.info("Mapping ConferenceEventRequest list to Conference. ConferenceEventRequests:" + conferenceEventRequests.toString());

		List<String> schedule = new ArrayList<String>();

		conferenceEventRequests.stream().forEach(request -> schedule.add(toScheduleTrack(request)));

		return Conference.builder().schedule(schedule).build();

	}

	private String toScheduleTrack(List<ConferenceEventRequest> conferenceEventRequests) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);

		StringBuffer track = new StringBuffer();
		LocalTime eventStartTime = null;
		LocalTime eventFinishTime = null;

		for (ConferenceEventRequest eventRequest : conferenceEventRequests) {
			if (GAP.equals(eventRequest.getEventName())) {
				eventFinishTime = eventFinishTime.plusMinutes(eventRequest.getEventDuration());
				continue;
			}

			if (conferenceEventRequests.indexOf(eventRequest) == 0) {
				eventStartTime = CONFERENCE_START_TIME_NINE_AM;
				eventFinishTime = eventStartTime.plusMinutes(eventRequest.getEventDuration());

			} else {
				eventStartTime = eventFinishTime;
				if (EVENT_NETWORKING.equals(eventRequest.getEventName())) {
					if(MINUTE_SIXTY < eventRequest.getEventDuration()) {
						eventStartTime = NETWORKING_START_TIME_FOUR_PM;
					}
				} else {
					eventFinishTime = eventStartTime.plusMinutes(eventRequest.getEventDuration());
				}

			}

			if (EVENT_NETWORKING.equals(eventRequest.getEventName()) || EVENT_LUNCH.equals(eventRequest.getEventName()) || eventRequest.getEventName().toLowerCase().contains(EVENT_LIGHTNING)) {
				track.append(eventStartTime.format(formatter) + "  " + eventRequest.getEventName() + "\n");
			} else {
				track.append(eventStartTime.format(formatter) + "  " + eventRequest.getEventName() + "  " + eventRequest.getEventDuration() + ABBR_MINUTE + "\n");
			}
		}
		
		return track.toString();
				
				
	}
	
	
	public ScheduleConferenceResponse toScheduleConferenceResponse(Conference conference) {
		
		log.info("Mapping Conference to ScheduleConferenceResponse. Conference:" + conference.toString());
		
		ScheduleConferenceResponse scheduleConferenceResponse =  ScheduleConferenceResponse.builder()
				.conferenceId(conference.getConferenceId())
				.schedule(toScheduleResponse(conference.getSchedule()))
				.build();
		
		return (ScheduleConferenceResponse)toBaseResponse(scheduleConferenceResponse, conference);
		
	}
	
	public GetConferenceResponse toGetConferenceResponse(Conference conference) {
		
		log.info("Mapping Conference to GetConferenceResponse. Conference:" + conference.toString());
		
		GetConferenceResponse getConferenceResponse = GetConferenceResponse.builder()
				.conferenceId(conference.getConferenceId())
				.schedule(toScheduleResponse(conference.getSchedule()))
				.build();
		
		return (GetConferenceResponse)toBaseResponse(getConferenceResponse, conference);
	}
	
	private List<List<String>> toScheduleResponse(List<String> schedule) {
		
		List<List<String>> scheduleResponse =  new ArrayList<>();

		schedule.stream()
				.forEach(conferenceEvents -> scheduleResponse.add(Arrays.asList(conferenceEvents.split("\n"))));
		
		return scheduleResponse;
		
	}

}
