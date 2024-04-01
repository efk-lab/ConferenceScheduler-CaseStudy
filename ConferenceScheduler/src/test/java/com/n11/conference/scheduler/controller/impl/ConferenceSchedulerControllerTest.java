package com.n11.conference.scheduler.controller.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n11.conference.scheduler.ConferenceSchedulerApplicationTest;
import com.n11.conference.scheduler.model.GetConferenceRequest;
import com.n11.conference.scheduler.model.GetConferenceResponse;
import com.n11.conference.scheduler.model.ScheduleConferenceRequest;
import com.n11.conference.scheduler.model.ScheduleConferenceResponse;
import com.n11.conference.scheduler.service.ConferenceSchedulerService;

@ContextConfiguration(classes = ConferenceSchedulerApplicationTest.class)
@WebMvcTest(ConferenceSchedulerControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ConferenceSchedulerControllerImpl.class)
public class ConferenceSchedulerControllerTest {


	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ConferenceSchedulerService conferenceSchedulerService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean 
	private LocalValidatorFactoryBean validator;
	

	@Test
	public void testScheduleConference() throws Exception {

		ScheduleConferenceRequest scheduleConferenceRequest = new ScheduleConferenceRequest();
		ScheduleConferenceResponse scheduleConferenceResponse = prepareScheduleConferenceResponse();

		given(conferenceSchedulerService.scheduleConference(any(ScheduleConferenceRequest.class))).willReturn(scheduleConferenceResponse);

		this.mockMvc.perform(post("/conference-scheduler/conference")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(scheduleConferenceRequest)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("conferenceId").value("62d322ddf9f5e01864bed242"));

	}
	
	@Test
	public void testSaveDeliveryPointReturnsNoContent() throws Exception {

		ScheduleConferenceRequest scheduleConferenceRequest = new ScheduleConferenceRequest();
		ScheduleConferenceResponse scheduleConferenceResponse = null;

		given(conferenceSchedulerService.scheduleConference(any(ScheduleConferenceRequest.class))).willReturn(scheduleConferenceResponse);

		this.mockMvc.perform(post("/conference-scheduler/conference")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(scheduleConferenceRequest)))
		        .andExpect(status().isNoContent());

	}
	
	@Test
	public void testGetConference() throws Exception {

		GetConferenceRequest getConferenceRequest = new GetConferenceRequest();
		GetConferenceResponse getConferenceResponse = prepareGetConferenceResponse();

		given(conferenceSchedulerService.getConference(any(GetConferenceRequest.class))).willReturn(getConferenceResponse);

		this.mockMvc.perform(post("/conference-scheduler/conference-details")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(getConferenceRequest)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("conferenceId").value("62d322ddf9f5e01864bed242"));

	}
	
	@Test
	public void testGetConferenceReturnsNoContent() throws Exception {

		GetConferenceRequest getConferenceRequest = new GetConferenceRequest();
		GetConferenceResponse getConferenceResponse = null;

		given(conferenceSchedulerService.getConference(any(GetConferenceRequest.class))).willReturn(getConferenceResponse);

		this.mockMvc.perform(post("/conference-scheduler/conference-details")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(getConferenceRequest)))
		        .andExpect(status().isNoContent());

	}
	
	private ScheduleConferenceResponse prepareScheduleConferenceResponse() {
		
		return ScheduleConferenceResponse.builder().conferenceId(new ObjectId("62d322ddf9f5e01864bed242")).build();
		
	}
	
	private GetConferenceResponse prepareGetConferenceResponse() {
		
		return GetConferenceResponse.builder().conferenceId(new ObjectId("62d322ddf9f5e01864bed242")).build();
		
	}

}
