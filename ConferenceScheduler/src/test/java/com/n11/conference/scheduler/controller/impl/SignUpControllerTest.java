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
import com.n11.conference.scheduler.model.SignUpRequest;
import com.n11.conference.scheduler.model.SignUpResponse;
import com.n11.conference.scheduler.service.UserRegistryService;

@ContextConfiguration(classes = ConferenceSchedulerApplicationTest.class)
@WebMvcTest(UserRegistryControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(UserRegistryControllerImpl.class)
public class SignUpControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserRegistryService userRegistryService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean 
	private LocalValidatorFactoryBean validator;


	@Test
	public void testSignUp() throws Exception {

		SignUpRequest signUpRequest = new SignUpRequest();
		SignUpResponse signUpResponse = prepareSignUpResponse();

		given(userRegistryService.signUp(any(SignUpRequest.class))).willReturn(signUpResponse);

		this.mockMvc.perform(post("/conference-scheduler/sign-up")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(signUpRequest)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("email").value("xxx@gmail.com"));
		
	}
	
	@Test
	public void testSignUpReturnsNoContent() throws Exception {

		SignUpRequest signUpRequest = new SignUpRequest();
		SignUpResponse signUpResponse = null;

		given(userRegistryService.signUp(any(SignUpRequest.class))).willReturn(signUpResponse);
		this.mockMvc.perform(post("/conference-scheduler/sign-up")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(signUpRequest)))
		.andExpect(status().isNoContent());
		
	}
	
	private SignUpResponse prepareSignUpResponse() {	
		return SignUpResponse.builder().userId(new ObjectId("62d322ddf9f5e01864bed242")).email("xxx@gmail.com").build();
	}

}
