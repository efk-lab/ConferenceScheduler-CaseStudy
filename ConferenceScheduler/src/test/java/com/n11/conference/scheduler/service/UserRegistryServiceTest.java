package com.n11.conference.scheduler.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.n11.conference.scheduler.document.User;
import com.n11.conference.scheduler.mapper.UserMapper;
import com.n11.conference.scheduler.model.SignUpRequest;
import com.n11.conference.scheduler.model.SignUpResponse;
import com.n11.conference.scheduler.repository.UserRepository;
import com.n11.conference.scheduler.validator.UserValidator;


@ExtendWith(MockitoExtension.class)
public class UserRegistryServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private UserMapper userMapper;

	@Mock
	private UserValidator userValidator;

	@InjectMocks
	private UserRegistryService userRegistryService;

	@Test
	public void testSignUp() {

		SignUpRequest signUpRequest = prepareSignUpRequest();
		SignUpResponse signUpResponseExpected = prepareSignUpResponse();
		User user = prepareUser();

		doNothing().when(userValidator).validateSignUpRequest(signUpRequest);
		given(userMapper.toUser(signUpRequest)).willReturn(user);
		given(userRepository.save(user)).willReturn(user);
		given(userMapper.toSignUpResponse(user)).willReturn(signUpResponseExpected);

		SignUpResponse signUpResponseActual = userRegistryService.signUp(signUpRequest);

		assertThat(signUpResponseActual.getEmail()).isEqualTo(signUpResponseExpected.getEmail());

	}

	private SignUpRequest prepareSignUpRequest() {

		SignUpRequest signUpRequest = new SignUpRequest();

		signUpRequest.setEmail("xxx@gmail.com");
		signUpRequest.setPassword("xxx123");

		return signUpRequest;

	}

	private SignUpResponse prepareSignUpResponse() {

		return SignUpResponse.builder().userId(new ObjectId("62d322ddf9f5e01864bed242")).email("xxx@gmail.com").build();
		
	}

	private User prepareUser() {

		return User.builder().userId(new ObjectId("62d322ddf9f5e01864bed242")).email("xxx@gmail.com").build();
		
	}

}