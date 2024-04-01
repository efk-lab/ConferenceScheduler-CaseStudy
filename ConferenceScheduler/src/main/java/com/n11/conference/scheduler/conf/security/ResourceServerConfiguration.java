package com.n11.conference.scheduler.conf.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import com.n11.conference.scheduler.constant.Role;
import com.n11.conference.scheduler.error.ConferenceSchedulerAccessDeniedHandler;
import com.n11.conference.scheduler.error.ConferenceSchedulerAuthenticationEntryPoint;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
	
	private final ConferenceSchedulerAuthenticationEntryPoint conferenceSchedulerAuthenticationEntryPoint;
	
	private final String RESOURCE_SERVER_RESOURCE_ID = "ConferenceSchedulerApi";

	public ResourceServerConfiguration(ConferenceSchedulerAuthenticationEntryPoint conferenceSchedulerAuthenticationEntryPoint) {
		this.conferenceSchedulerAuthenticationEntryPoint = conferenceSchedulerAuthenticationEntryPoint;
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(RESOURCE_SERVER_RESOURCE_ID);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.antMatcher("/conference-scheduler/**").authorizeRequests()
				.antMatchers("/conference-scheduler/sign-up/**").permitAll()
				.antMatchers("/conference-scheduler/conference/**").hasAnyAuthority(Role.ADMIN.name(), Role.USER.name())
				.antMatchers("/conference-scheduler/conference-details/**").hasAnyAuthority(Role.ADMIN.name(), Role.USER.name())
				.antMatchers("/conference-scheduler/**").authenticated().anyRequest().authenticated().and().exceptionHandling()
				.authenticationEntryPoint(conferenceSchedulerAuthenticationEntryPoint)
				.accessDeniedHandler(new ConferenceSchedulerAccessDeniedHandler());

	}
}
