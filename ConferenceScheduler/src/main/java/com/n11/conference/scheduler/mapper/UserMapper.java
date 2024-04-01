package com.n11.conference.scheduler.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.n11.conference.scheduler.constant.Role;
import com.n11.conference.scheduler.document.User;
import com.n11.conference.scheduler.model.SignUpRequest;
import com.n11.conference.scheduler.model.SignUpResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserMapper {

	@Autowired
	private PasswordEncoder passwordEncoder;
	

	public User toUser(SignUpRequest signUpRequest) {

		log.info("Mapping SignUpRequest to User. SignUpRequest: " + signUpRequest.toString());

		return User.builder()
				.email(signUpRequest.getEmail())
				.password(passwordEncoder.encode(signUpRequest.getPassword()))
				.role(Role.USER)
				.build();

	}

	public SignUpResponse toSignUpResponse(User user) {

		log.info("Mapping User to SignUpResponse. User:" + user.toString());

		return  SignUpResponse.builder()
								.userId(user.getUserId())
								.email(user.getEmail())
								.build();
	}
}
