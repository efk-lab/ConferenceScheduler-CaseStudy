package com.n11.conference.scheduler.service.parameters;

import java.util.List;
import java.util.Map;

import com.n11.conference.scheduler.model.ConferenceEventRequest;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@RequiredArgsConstructor
public class ScheduleParameters {
	
	private Integer duration;
	
	@NonNull
	private List<ConferenceEventRequest> scheduledRequests;
	
	@NonNull
	private List<ConferenceEventRequest> scheduleConferenceRequests;
	
	@NonNull
	private Map<Integer, Integer> eventDurationCounts;
}
