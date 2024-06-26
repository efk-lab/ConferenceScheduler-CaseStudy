package com.n11.conference.scheduler.model;

import java.io.Serializable;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class SignUpResponse implements Serializable {
	
	private static final long serialVersionUID = -5374626595790331708L;

	@JsonSerialize(using = ToStringSerializer.class)
	private ObjectId userId;
	
	private String email;
	
}
