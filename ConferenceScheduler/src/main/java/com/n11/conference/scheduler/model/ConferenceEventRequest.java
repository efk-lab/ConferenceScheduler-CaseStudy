package com.n11.conference.scheduler.model;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Builder
public class ConferenceEventRequest implements Serializable {

	private static final long serialVersionUID = 6091092322090795912L;

	@NotNull
	@NotBlank
	@NotEmpty
	private String eventName;
	
	@Min(value = 5)
	private Integer eventDuration;
}
