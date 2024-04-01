package com.n11.conference.scheduler.model;

import java.io.Serializable;
import java.util.List;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Builder
public class ScheduleConferenceResponse extends BaseResponse implements Serializable {

	private static final long serialVersionUID = 4762331121751319718L;
	
	@JsonSerialize(using = ToStringSerializer.class)
	private ObjectId conferenceId;

	private List<List<String>> schedule;
}
