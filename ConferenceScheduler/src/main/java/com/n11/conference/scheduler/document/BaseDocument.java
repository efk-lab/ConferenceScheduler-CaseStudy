package com.n11.conference.scheduler.document;

import java.io.Serializable;
import java.time.ZonedDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
public class BaseDocument implements Serializable {
	
	private static final long serialVersionUID = -6578242662303142578L;

	@Field
	@CreatedBy
	protected String createdBy;

	@Field
	@CreatedDate
	protected ZonedDateTime creationDate;

	@Field
	@LastModifiedBy
	protected String modifiedBy;

	@Field
	@LastModifiedDate
	protected ZonedDateTime modificationDate;
	
	@Field
	@Version
	private Long version;

}
