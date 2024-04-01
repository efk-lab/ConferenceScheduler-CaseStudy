package com.n11.conference.scheduler.mapper;

import com.n11.conference.scheduler.document.BaseDocument;
import com.n11.conference.scheduler.model.BaseResponse;

public abstract class BaseMapper {

	protected BaseResponse toBaseResponse(BaseResponse baseResponse, BaseDocument baseDocument) {

		baseResponse.setCreatedBy(baseDocument.getCreatedBy());
		baseResponse.setCreationDate(baseDocument.getCreationDate());
		baseResponse.setModifiedBy(baseDocument.getModifiedBy());
		baseResponse.setModificationDate(baseDocument.getModificationDate());

		return baseResponse;
		
	}

}
