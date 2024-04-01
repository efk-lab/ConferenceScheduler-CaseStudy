package com.n11.conference.scheduler.document;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.n11.conference.scheduler.constant.Role;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Document(collection = "user")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Builder
public class User extends BaseDocument implements Serializable {


	private static final long serialVersionUID = -8767566925391693716L;

	@Id
	private ObjectId userId;

	@Field
	private String email;

	@Field
	private String password;

	@Field
	private Role role;
	
}
