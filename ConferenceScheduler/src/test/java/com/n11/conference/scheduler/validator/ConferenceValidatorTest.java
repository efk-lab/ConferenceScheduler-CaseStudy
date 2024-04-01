package com.n11.conference.scheduler.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.n11.conference.scheduler.error.ConferenceSchedulerException;
import com.n11.conference.scheduler.model.ConferenceEventRequest;
import com.n11.conference.scheduler.model.GetConferenceRequest;
import com.n11.conference.scheduler.model.ScheduleConferenceRequest;
import com.n11.conference.scheduler.repository.ConferenceRepository;

@ExtendWith(MockitoExtension.class)
public class ConferenceValidatorTest {

	@Mock
	private ConferenceRepository conferenceRepository;

	@InjectMocks
	private ConferenceValidator conferenceValidator;
	

	@Test
	public void testValidateScheduleConferenceRequestNull() {
		
		ScheduleConferenceRequest scheduleConferenceRequest = null;

		Exception exception = assertThrows(ConferenceSchedulerException.class, () -> {
			conferenceValidator.validateScheduleConferenceRequest(scheduleConferenceRequest);
		});

		String expectedMessage = "Request cannot be null.";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	public void testValidateConferenceEventDurationNull() {
		ScheduleConferenceRequest scheduleConferenceRequest = new ScheduleConferenceRequest();
		List<ConferenceEventRequest> conferenceEventRequests = new ArrayList<ConferenceEventRequest>();
		ConferenceEventRequest conferenceEventRequest = new ConferenceEventRequest();
		conferenceEventRequest.setEventName("Cloud Native Java");   
		conferenceEventRequests.add(conferenceEventRequest);
		scheduleConferenceRequest.setConferenceEventRequests(conferenceEventRequests);
		

		Exception exception = assertThrows(ConferenceSchedulerException.class, () -> {
			conferenceValidator.validateScheduleConferenceRequest(scheduleConferenceRequest);
		});

		String expectedMessage = "EventDuration cannot be enmpty. ConferenceEventRequest's Index:";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	
	}
	
	@Test
	public void testValidateGetConferenceRequestNull() {
		
		GetConferenceRequest getConferenceRequest = null;

		Exception exception = assertThrows(ConferenceSchedulerException.class, () -> {
			conferenceValidator.validateGetConference(getConferenceRequest);
		});

		String expectedMessage = "Request cannot be null.";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	public void testValidateConferenceIdNull() {
		GetConferenceRequest getConferenceRequest = new GetConferenceRequest();

		Exception exception = assertThrows(ConferenceSchedulerException.class, () -> {
			conferenceValidator.validateGetConference(getConferenceRequest);
		});

		String expectedMessage = "ConferenceId cannot be empty.";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	
	}
	
	@Test
	public void testValidateConferenceNotFoundNull() {
		
		GetConferenceRequest getConferenceRequest = new GetConferenceRequest();
		getConferenceRequest.setConferenceId(new ObjectId("62d322ddf9f5e01864bed242"));

		given(conferenceRepository.findById(new ObjectId("62d322ddf9f5e01864bed242"))).willReturn(Optional.empty());
		Exception exception = assertThrows(ConferenceSchedulerException.class, () -> {
			conferenceValidator.validateGetConference(getConferenceRequest);
		});

		String expectedMessage = "Conference cannot be found.";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	
	}
	
	@Test
	public void testScheduleConferenceRequestConferenceEventRequestsNullOrEmpty() {

		ScheduleConferenceRequest sheduleConferenceRequest = new ScheduleConferenceRequest();
		sheduleConferenceRequest.setConferenceEventRequests(null);

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();

		Set<ConstraintViolation<ScheduleConferenceRequest>> violations = validator.validate(sheduleConferenceRequest);

		assertEquals(2, violations.size());

		for(ConstraintViolation<ScheduleConferenceRequest> violation : violations) {
			assertEquals("conferenceEventRequests", violation.getPropertyPath().toString());
			assertEquals("boş değer olamaz", violation.getMessage());
		}
	
	}
	
	@Test
	public void testScheduleConferenceRequestConferenceEventRequestsSizeLessThenOne() {

		ScheduleConferenceRequest sheduleConferenceRequest = new ScheduleConferenceRequest();
		List<ConferenceEventRequest> conferenceEventRequests = new ArrayList<ConferenceEventRequest>();
		sheduleConferenceRequest.setConferenceEventRequests(conferenceEventRequests);

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();

		Set<ConstraintViolation<ScheduleConferenceRequest>> violations = validator.validate(sheduleConferenceRequest);

		assertEquals(2, violations.size());

		for(ConstraintViolation<ScheduleConferenceRequest> violation : violations) {
			assertEquals("conferenceEventRequests", violation.getPropertyPath().toString());
			
			if(violation.getMessageTemplate().equals("{javax.validation.constraints.NotEmpty.message}")){
				assertEquals("boş değer olamaz", violation.getMessage());
			}
			
			if(violation.getMessageTemplate().equals("{javax.validation.constraints.Size.message}")){
				assertEquals("boyut '1' ile '2147483647' arasında olmalı", violation.getMessage());
			}
		}
	
	}
	
	@Test
	public void testScheduleConferenceRequestConferenceEventRequestEventNameNullOrEmptyOrBlank() {

		ScheduleConferenceRequest sheduleConferenceRequest = new ScheduleConferenceRequest();
		List<ConferenceEventRequest> conferenceEventRequests = new ArrayList<ConferenceEventRequest>();
		conferenceEventRequests.add(new ConferenceEventRequest("", 5));
		sheduleConferenceRequest.setConferenceEventRequests(conferenceEventRequests);
		

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();

		Set<ConstraintViolation<ScheduleConferenceRequest>> violations = validator.validate(sheduleConferenceRequest);

		assertEquals(2, violations.size());

		for(ConstraintViolation<ScheduleConferenceRequest> violation : violations) {
		    assertEquals("boş değer olamaz", violation.getMessage());
		}
	
	}
	
	@Test
	public void testScheduleConferenceRequestConferenceEventRequestEventDurationLessThenFive() {

		ScheduleConferenceRequest sheduleConferenceRequest = new ScheduleConferenceRequest();
		List<ConferenceEventRequest> conferenceEventRequests = new ArrayList<ConferenceEventRequest>();
		conferenceEventRequests.add(new ConferenceEventRequest("Architecting Your Codebase", 2));
		sheduleConferenceRequest.setConferenceEventRequests(conferenceEventRequests);
		

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();

		Set<ConstraintViolation<ScheduleConferenceRequest>> violations = validator.validate(sheduleConferenceRequest);

		assertEquals(1, violations.size());

		for(ConstraintViolation<ScheduleConferenceRequest> violation : violations) {
		    assertEquals("'5' değerinden büyük yada eşit olmalı", violation.getMessage());
		}
	
	}

}
