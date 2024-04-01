package com.n11.conference.scheduler.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

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

import com.n11.conference.scheduler.document.User;
import com.n11.conference.scheduler.error.ConferenceSchedulerException;
import com.n11.conference.scheduler.model.SignUpRequest;
import com.n11.conference.scheduler.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserValidatorTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserValidator userValidator;
	
	
	@Test
	public void testSignUpRequestNull() {
		
		SignUpRequest signUpRequest = null;

		Exception exception = assertThrows(ConferenceSchedulerException.class, () -> {
			userValidator.validateSignUpRequest(signUpRequest);
		});

		String expectedMessage = "Request cannot be null.";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	public void testEmailAlreadyExist() {
		Optional<User> user = Optional.ofNullable(prepareUser());
		SignUpRequest signUpRequest = new SignUpRequest();
		signUpRequest.setEmail("xxx@gmail.com");
		
		given(userRepository.findByEmail("xxx@gmail.com")).willReturn(user);
		Exception exception = assertThrows(ConferenceSchedulerException.class, () -> {
			userValidator.validateSignUpRequest(signUpRequest);
		});

		String expectedMessage = "User already registed.";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));

	}
	
	@Test
	public void testEmailInvalid() {

		SignUpRequest signUpRequest = new SignUpRequest();
		signUpRequest.setEmail("aas");
		signUpRequest.setPassword("123");

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();

		Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(signUpRequest);

		assertEquals(1, violations.size());

		ConstraintViolation<SignUpRequest> violation = violations.iterator().next();
		assertEquals("email", violation.getPropertyPath().toString());
		assertEquals("düzgün biçimli bir e-posta adresi değil!", violation.getMessage());

	}
	
	@Test
	public void testEmailNullOrEmptyOrBlank() {

		SignUpRequest signUpRequest = new SignUpRequest();
		signUpRequest.setPassword("123");

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();

		Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(signUpRequest);

		assertEquals(3, violations.size());

		for(ConstraintViolation<SignUpRequest> violation : violations) {
			assertEquals("email", violation.getPropertyPath().toString());
			assertEquals("boş değer olamaz", violation.getMessage());
		}
	
	}
	
	@Test
	public void testPasswordNullOrEmptyOrBlank() {

		SignUpRequest signUpRequest = new SignUpRequest();
		signUpRequest.setEmail("xxx@gmail.com");

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();

		Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(signUpRequest);

		assertEquals(3, violations.size());

		for(ConstraintViolation<SignUpRequest> violation : violations) {
			assertEquals("password", violation.getPropertyPath().toString());
			assertEquals("boş değer olamaz", violation.getMessage());
		}
	
	}

	private User prepareUser() {

		return User.builder().userId(new ObjectId("62d322ddf9f5e01864bed242")).email("xxx@gmail.com").build();
		
	}

}