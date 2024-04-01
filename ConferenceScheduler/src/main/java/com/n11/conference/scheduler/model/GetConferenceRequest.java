package com.n11.conference.scheduler.model;

import java.io.Serializable;

import org.bson.types.ObjectId;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class GetConferenceRequest implements Serializable {
	
	private static final long serialVersionUID = -6596971873003660219L;
	
	private ObjectId conferenceId;

}
