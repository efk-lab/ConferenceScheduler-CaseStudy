package com.n11.conference.scheduler.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.n11.conference.scheduler.document.Conference;
import com.n11.conference.scheduler.mapper.ConferenceMapper;
import com.n11.conference.scheduler.model.ConferenceEventRequest;
import com.n11.conference.scheduler.model.GetConferenceRequest;
import com.n11.conference.scheduler.model.GetConferenceResponse;
import com.n11.conference.scheduler.model.ScheduleConferenceRequest;
import com.n11.conference.scheduler.model.ScheduleConferenceResponse;
import com.n11.conference.scheduler.repository.ConferenceRepository;
import com.n11.conference.scheduler.validator.ConferenceValidator;


@ExtendWith(MockitoExtension.class)
public class ConferenceSchedulerServiceTest {

	@Mock
	private ConferenceRepository conferenceRepository;

	@Mock
	private ConferenceMapper conferenceMapper;

	@Mock
	private ConferenceValidator conferenceValidator;

	@InjectMocks
	private ConferenceSchedulerService conferenceSchedulerService;
	
	@Test
	public void testGetConference() throws Exception {

		GetConferenceRequest getConferenceRequest = prepareGetConferenceRequest();
		GetConferenceResponse getConferenceResponseExpected = prepareGetConferenceResponse();
		Optional<Conference> conference = Optional.of(Conference.builder().conferenceId(new ObjectId("62d322ddf9f5e01864bed242")).build()); 

		doNothing().when(conferenceValidator).validateGetConference(getConferenceRequest);
		given(conferenceRepository.findById(any(ObjectId.class))).willReturn(conference);
		given(conferenceMapper.toGetConferenceResponse(any(Conference.class))).willReturn(getConferenceResponseExpected);

		GetConferenceResponse getConferenceResponseActual = conferenceSchedulerService.getConference(getConferenceRequest);

		assertThat(getConferenceResponseActual.getConferenceId()).isEqualTo(getConferenceResponseExpected.getConferenceId());
		assertThat(getConferenceResponseActual.getSchedule().get(0)).isEqualTo(getConferenceResponseExpected.getSchedule().get(0));

	}
	
	private GetConferenceRequest prepareGetConferenceRequest() {
		GetConferenceRequest getConferenceRequest = new GetConferenceRequest(new ObjectId("62d322ddf9f5e01864bed242"));
		
		return getConferenceRequest;
	}
	
	private GetConferenceResponse prepareGetConferenceResponse() {

		List<List<String>> schedule = new ArrayList<>();
		List<String> tracks = new ArrayList<String>();
		tracks.add("09:00 AM  Architecting Your Codebase  60min \n12:00 PM  Lunch\n04:00 PM  Networking Event");
		schedule.add(tracks);

		return new GetConferenceResponse(new ObjectId("62d322ddf9f5e01864bed242"), schedule);
	}

	@Test
	public void testScheduleConference() {

		ScheduleConferenceRequest scheduleConferenceRequest = prepareScheduleConferenceRequest();
		ScheduleConferenceResponse scheduleConferenceResponseExpected = prepareScheduleConferenceResponse();
		List<ConferenceEventRequest> conferenceEventRequests = prepareConferenceEventRequests();
		List<List<ConferenceEventRequest>> scheduledConferenceEvents = prepareScheduledConferenceEvents(); 
		Conference conference = prepareConference();
		
		doNothing().when(conferenceValidator).validateScheduleConferenceRequest(scheduleConferenceRequest);
		given(conferenceMapper.toConferenceEventRequests(scheduleConferenceRequest.getConferenceEventRequests())).willReturn(conferenceEventRequests);
		given(conferenceMapper.toConference(scheduledConferenceEvents)).willReturn(conference);
		given(conferenceRepository.save(conference)).willReturn(conference);
		given(conferenceMapper.toScheduleConferenceResponse(conference)).willReturn(scheduleConferenceResponseExpected);

		ScheduleConferenceResponse scheduleConferenceResponseActual = conferenceSchedulerService.scheduleConference(scheduleConferenceRequest);

		assertThat(scheduleConferenceResponseActual.getConferenceId()).isEqualTo(scheduleConferenceResponseExpected.getConferenceId());
		assertThat(scheduleConferenceResponseActual.getSchedule().get(0)).isEqualTo(scheduleConferenceResponseExpected.getSchedule().get(0));

	}

	private ScheduleConferenceRequest prepareScheduleConferenceRequest() {

		ScheduleConferenceRequest sheduleConferenceRequest = new ScheduleConferenceRequest();

		List<ConferenceEventRequest> conferenceEventRequests = new ArrayList<ConferenceEventRequest>();

		conferenceEventRequests.add(new ConferenceEventRequest("Architecting Your Codebase", 60));
		
		return sheduleConferenceRequest;
	}

	private ScheduleConferenceResponse prepareScheduleConferenceResponse() {

		List<List<String>> schedule = new ArrayList<>();
		List<String> tracks = new ArrayList<String>();
		tracks.add("09:00 AM  Architecting Your Codebase  60min \n12:00 PM  Lunch\n04:00 PM  Networking Event");
		schedule.add(tracks);

		return new ScheduleConferenceResponse(new ObjectId("62d322ddf9f5e01864bed242"), schedule);

	}

	private Conference prepareConference() {

	 List<String> schedule = new ArrayList<String>();
	 String track1 = "09:00 AM  Architecting Your Codebase  60min \n12:00 PM  Lunch\n04:00 PM  Networking Event";
	 schedule.add(track1);
	 
	 return new Conference(new ObjectId("62d322ddf9f5e01864bed242"), schedule);

	}
	
	private List<ConferenceEventRequest> prepareConferenceEventRequests() {
		
		List<ConferenceEventRequest> conferenceEventRequests = new ArrayList<ConferenceEventRequest>();
		conferenceEventRequests.add(new ConferenceEventRequest("Architecting Your Codebase", 60));
		
		return conferenceEventRequests;
		
	}
	
	
	private List<List<ConferenceEventRequest>> prepareScheduledConferenceEvents(){
		
		List<List<ConferenceEventRequest>> schedule = new ArrayList<>();
		List<ConferenceEventRequest> conferenceEventRequests1 = new ArrayList<ConferenceEventRequest>();
		
		conferenceEventRequests1.add(new ConferenceEventRequest("Architecting Your Codebase", 60));
		conferenceEventRequests1.add(new ConferenceEventRequest("Gap", 120));
		conferenceEventRequests1.add(new ConferenceEventRequest("Lunch", 60));
		conferenceEventRequests1.add(new ConferenceEventRequest("Networking Event", 240));
	
		schedule.add(conferenceEventRequests1);
		
		return schedule;
		
	}
}
