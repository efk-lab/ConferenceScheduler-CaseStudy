package com.n11.conference.scheduler.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class ScheduleConferenceRequest implements Serializable {
	

	private static final long serialVersionUID = -1783507976578262987L;
	
	@NotNull
	@NotEmpty
	@Size(min=1)
	@Valid
	private List<ConferenceEventRequest> conferenceEventRequests;

}